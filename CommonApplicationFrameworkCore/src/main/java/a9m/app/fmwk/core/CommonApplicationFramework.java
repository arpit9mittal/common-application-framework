/*******************************************************************************
 * Copyright  2017-2018 Arpit Mittal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package a9m.app.fmwk.core;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.DirectFieldAccessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanTextMap;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.messaging.MessagingSpanTextMapExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListenerAnnotationBeanPostProcessor;
import org.springframework.jms.config.JmsListenerConfigUtils;
import org.springframework.jms.config.MethodJmsListenerEndpoint;
import org.springframework.jms.listener.MessageListenerContainer;
import org.springframework.jms.listener.adapter.MessagingMessageListenerAdapter;
import org.springframework.messaging.handler.invocation.InvocableHandlerMethod;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import a9m.app.fmwk.annotation.Schema;
import a9m.app.fmwk.config.DefaultFlywayConfiguration;
import a9m.app.fmwk.config.DefaultJmsConfiguration;
import a9m.app.fmwk.core.jms.DefaultJsonSchemaValidator;
import a9m.app.fmwk.core.jms.MessagingTextMap;
import a9m.app.fmwk.core.support.Processor;
import a9m.app.fmwk.core.support.TerminateProcessException;

/**
 * This class is the both the configuration class and spring boot application
 * class. This class applied all the annotation, imports all the configurations
 * and define any beans need for the common application framework.
 * 
 * @author arpmitta
 */
@EnableJms
@EnableTransactionManagement
@SpringBootApplication
@Import({ DefaultFlywayConfiguration.class, DefaultJmsConfiguration.class })
public class CommonApplicationFramework {
    
    public static void main(String[] args) {
        SpringApplication.run(CommonApplicationFramework.class, args);
    }
    
    /**
     * This must be override by application implementing the common application
     * framework if they want to provide different validation processor for
     * schema annotation or define and add more annotation
     * 
     * @param processor
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean
    public Map<Class, Processor> annotationProcessorMap(DefaultJsonSchemaValidator processor) {
        Map<Class, Processor> map = new HashMap<>();
        map.put(Schema.class, processor);
        return map;
    }
    
    /**
     * @param isSleuthJmsInstrumentationEnabled
     * @param tracer
     * @param spanExtractor
     * @return
     */
    @SuppressWarnings("rawtypes")
    @Bean(JmsListenerConfigUtils.JMS_LISTENER_ANNOTATION_PROCESSOR_BEAN_NAME)
    public static JmsListenerAnnotationBeanPostProcessor bpp(Map<Class, Processor> annotationProcessorMap, @Value("${spring.jms.sleuth.instrumentation.enable}") boolean isSleuthJmsInstrumentationEnabled, Tracer tracer, MessagingSpanTextMapExtractor spanExtractor) {
        return new JmsListenerAnnotationBeanPostProcessor() {
            
            @Override
            protected MethodJmsListenerEndpoint createMethodJmsListenerEndpoint() {
                
                return new MethodJmsListenerEndpoint() {
                    
                    @SuppressWarnings("unchecked")
                    @Override
                    protected MessagingMessageListenerAdapter createMessageListener(MessageListenerContainer container) {
                        final MessagingMessageListenerAdapter listener = super.createMessageListener(container);
                        
                        InvocableHandlerMethod handlerMethod = (InvocableHandlerMethod) new DirectFieldAccessor(listener).getPropertyValue("handlerMethod");
                        
                        // get all the valid annotation applied to the method
                        Map<Class, Annotation> annotationMap = new HashMap<>();
                        for (Entry<Class, Processor> entry : annotationProcessorMap.entrySet()) {
                            final Annotation annotation = AnnotationUtils.getAnnotation(handlerMethod.getMethod(), entry.getKey());
                            if (null != annotation) {
                                annotationMap.put(Schema.class, annotation);
                            }
                        }
                        
                        return new MessagingMessageListenerAdapter() {
                            
                            @Override
                            public void onMessage(Message jmsMessage, Session session) throws JMSException {
                                Span span = null;
                                if (isSleuthJmsInstrumentationEnabled) {
                                    SpanTextMap textMap = new MessagingTextMap(MessageBuilder.fromMessage(toMessagingMessage(jmsMessage)));
                                    span = spanExtractor.joinTrace(textMap);
                                    tracer.continueSpan(span);
                                }
                                
                                
                                try {
                                 // execute schema processor if any    
                                    for (Entry<Class, Annotation> entry : annotationMap.entrySet()) {
                                        annotationProcessorMap.get(entry.getKey()).process(jmsMessage, entry.getValue());
                                    }
                                    
                                    // start the @JMSListener annotated method
                                    // processing.
                                    listener.onMessage(jmsMessage, session);
                                    
                                } catch (TerminateProcessException ex) {
                                    logger.error(ex);
                                }
                                
                                if (isSleuthJmsInstrumentationEnabled) {
                                    tracer.detach(span);
                                }
                            }
                            
                        };
                    }
                    
                };
            }
            
        };
    }
    
}
