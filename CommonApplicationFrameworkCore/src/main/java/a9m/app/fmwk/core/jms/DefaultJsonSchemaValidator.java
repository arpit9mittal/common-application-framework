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

import java.util.Set;

import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.networknt.schema.ValidationMessage;

import a9m.app.fmwk.annotation.Schema;
import a9m.app.fmwk.core.support.Processor;
import a9m.app.fmwk.core.support.SchemaValidation;
import a9m.app.fmwk.core.support.Utils;

/**
 * @author arpmitta
 *
 */
@Component
public class DefaultJsonSchemaValidator implements Processor<Message, Schema> {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultJsonSchemaValidator.class);
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.core.support.Processor#process(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public void process(Message jmsMessage, Schema annotation) {
        logger.info("Validation the jms message with schema '{}'", annotation.value());
        
        String schemaFilePath = String.valueOf(annotation.value());
        
        try {
            String payload = Utils.getJsonPayload(jmsMessage);
            Set<ValidationMessage> errors = SchemaValidation.withJsonSchema(schemaFilePath, payload);
            
            if (errors.size() > 0) {
                logger.error("Json Schema validation failed with following errors: \n {}", errors);
            } else {
                logger.info("Schema validation passed for payload: {}", payload);
            }
            
        } catch (Exception e) {
            logger.error("Error while validating JMS Message with Schema", e);
        }
    }
    
}
