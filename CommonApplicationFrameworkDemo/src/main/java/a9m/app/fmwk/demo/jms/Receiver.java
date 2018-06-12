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
package a9m.app.fmwk.demo.jms;

import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import a9m.app.fmwk.annotation.Schema;
import a9m.app.fmwk.demo.aop.LogInfo;
import a9m.app.fmwk.demo.repo.entity.User;

/**
 * @author arpmitta
 *
 */
@Component
public class Receiver {
    
    @Autowired
    private Producer producer;
    
    @Value("${internal.queue}")
    private String internalQueue;
    
    private static final Logger logger = LoggerFactory.getLogger(Receiver.class);
    
    @LogInfo
    @SuppressWarnings("rawtypes")
    @Schema("schema/User.json")
    @JmsListener(destination = "${inbound.queue}", containerFactory = "myFactory")
    // @SendTo("${internal.queue}")
    public void receiveMessage(
            @Payload User user, 
            @Headers MessageHeaders headers, 
            Message message, 
            Session session) {
        
        logger.info("\n ** Receiving Queue 1 with \n ** Header: {} \n ** Payload: {}", headers, user);
        
        producer.send(internalQueue, user.getUsername());
        // return
        // MessageBuilder.withPayload(user.getUsername()).setHeader("code",
        // 1234).build();
    }
    
    @LogInfo
    @Schema("schema/User.json")
    @JmsListener(destination = "${internal.queue}", containerFactory = "myFactory")
    public void receiveMessage2(
            @Payload String userName, 
            @Headers MessageHeaders headers) {
        
        logger.info("\\n ** Receiving Queue 2 with \\n ** Header: {} \\n ** Payload: {}", headers, userName);
    }
    
}
