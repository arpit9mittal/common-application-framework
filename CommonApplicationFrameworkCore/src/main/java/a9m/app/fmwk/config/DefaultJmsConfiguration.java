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
package a9m.app.fmwk.config;

import java.text.SimpleDateFormat;

import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This configuration class defines the default jms listener factory needed for
 * jms application. It is based on spring boot configuration and adds on jackson
 * converter to jms incoming and outgoing messages.
 * 
 * Additionally it provides {@link org.springframework.jms.core.JmsTemplate} and
 * {@link org.springframework.jms.core.JmsMessagingTemplate} to be used for
 * queue and topics
 * 
 * @author arpmitta
 */
@Configuration
public class DefaultJmsConfiguration {
    
    // private static final Logger logger =
    // LoggerFactory.getLogger(DefaultJmsConfiguration.class);
    
    /**
     * @param connectionFactory
     *            bean of
     *            {@link org.springframework.jms.config.JmsListenerContainerFactory}
     * @param configurer
     *            bean of
     *            {@link org.springframework.jms.config.DefaultJmsListenerContainerFactory}
     * @return instance of
     *         {@link org.springframework.jms.config.DefaultJmsListenerContainerFactory}
     */
    @Bean
    public JmsListenerContainerFactory<?> myFactory(final ConnectionFactory connectionFactory, final DefaultJmsListenerContainerFactoryConfigurer configurer) {
        
        final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        
        factory.setErrorHandler(t -> System.err.println("An error has occurred in the transaction"));
        
        configurer.configure(factory, connectionFactory);
        // You could still override some of Boot's default if necessary.
        return factory;
    }
    
    /**
     * @return instance of
     *         {@link org.springframework.jms.support.converter.MessageConverter}
     */
    @Bean
    static MessageConverter jacksonJmsMessageConverter() {
        
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSSZ"));
        
        converter.setObjectMapper(objectMapper);
        
        return converter;
    }
    
    /**
     * @param connectionFactory
     *            bean of {@link javax.jms.ConnectionFactory}
     * @param jacksonJmsMessageConverter
     *            bean of
     *            {@link org.springframework.jms.support.converter.MessageConverter}
     * @return instance of {@link org.springframework.jms.core.JmsTemplate}
     */
    @Bean
    @Qualifier("jmsQueueTemplate")
    public JmsTemplate jmsQueueTemplate(final ConnectionFactory connectionFactory, final MessageConverter jacksonJmsMessageConverter) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setPubSubDomain(false);
        template.setMessageConverter(jacksonJmsMessageConverter);
        
        return template;
    }
    
    /**
     * @param connectionFactory
     *            bean of
     *            {@link org.springframework.jms.config.JmsListenerContainerFactory}
     * @param jacksonJmsMessageConverter
     *            bean of
     *            {@link org.springframework.jms.support.converter.MessageConverter}
     * @return instance of {@link org.springframework.jms.core.JmsTemplate}
     */
    @Bean
    @Qualifier("jmsTopicTemplate")
    public JmsTemplate jmsTopicTemplate(final ConnectionFactory connectionFactory, final MessageConverter jacksonJmsMessageConverter) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setPubSubDomain(true);
        template.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        template.setMessageConverter(jacksonJmsMessageConverter);
        
        return template;
    }
    
    /**
     * @param jmsTopicTemplate
     *            bean of {@link org.springframework.jms.core.JmsTemplate}
     * @param jacksonJmsMessageConverter
     *            bean of
     *            {@link org.springframework.jms.support.converter.MessageConverter}
     * @return instance of
     *         {@link org.springframework.jms.core.JmsMessagingTemplate}
     */
    @Bean
    @Qualifier("jmsMessagingTopicTemplate")
    public JmsMessagingTemplate jmsMessagingTopicTemplate(final JmsTemplate jmsTopicTemplate, final MessageConverter jacksonJmsMessageConverter) {
        return new JmsMessagingTemplate(jmsTopicTemplate);
    }
    
    /**
     * @param jmsQueueTemplate
     *            bean of {@link org.springframework.jms.core.JmsTemplate}
     * @param jacksonJmsMessageConverter
     *            bean of
     *            {@link org.springframework.jms.support.converter.MessageConverter}
     * @return instance of
     *         {@link org.springframework.jms.core.JmsMessagingTemplate}
     */
    @Bean
    @Qualifier("jmsMessagingQueueTemplate")
    public JmsMessagingTemplate jmsMessagingQueueTemplate(final JmsTemplate jmsQueueTemplate, final MessageConverter jacksonJmsMessageConverter) {
        return new JmsMessagingTemplate(jmsQueueTemplate);
    }
    
}
