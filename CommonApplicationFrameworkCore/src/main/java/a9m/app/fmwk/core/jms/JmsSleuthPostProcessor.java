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
package a9m.app.fmwk.core.jms;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.messaging.TraceMessageHeaders;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author arpmitta
 *
 */
@Component
public class JmsSleuthPostProcessor implements MessagePostProcessor {
    
    private static final Logger logger = LoggerFactory.getLogger(JmsSleuthPostProcessor.class);
    
    private Tracer tracer;
    
    @Autowired
    public JmsSleuthPostProcessor(Tracer tracer) {
        super();
        this.tracer = tracer;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.messaging.core.MessagePostProcessor#postProcessMessage(org.springframework.messaging.Message)
     */
    @Override
    public org.springframework.messaging.Message<?> postProcessMessage(org.springframework.messaging.Message<?> message) {
        if (tracer != null && tracer.isTracing()) {
            Span currentSpan = tracer.getCurrentSpan();
            
            Span newSpan = tracer.createSpan(currentSpan.getName());
            long newSpanid = newSpan.getSpanId();
            tracer.close(newSpan);
            
            Map<String, Object> headers = new HashMap<>();
            headers.put(TraceMessageHeaders.TRACE_ID_NAME, Span.idToHex(currentSpan.getTraceId()));
            headers.put(TraceMessageHeaders.SPAN_ID_NAME, Span.idToHex(newSpanid));
            headers.put(TraceMessageHeaders.PROCESS_ID_NAME, currentSpan.getProcessId());
            headers.put(TraceMessageHeaders.SPAN_NAME_NAME, currentSpan.getName());
            headers.put(TraceMessageHeaders.PARENT_ID_NAME, Span.idToHex(currentSpan.getSpanId()));
            
            logger.info("\n new header before publishing : \n {} \n", headers);
            
            return MessageBuilder.fromMessage(message).copyHeadersIfAbsent(headers).build();
        }
        
        return message;
    }
    
}
