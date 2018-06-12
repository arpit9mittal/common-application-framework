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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import a9m.app.fmwk.core.jms.JmsSleuthPostProcessor;
import a9m.app.fmwk.demo.aop.LogInfo;
import a9m.app.fmwk.demo.repo.entity.User;

/**
 * @author arpmitta
 *
 */
@Component
public class Producer {
    
    private static final Logger logger = LoggerFactory.getLogger(Producer.class);
    
    private String qName;
    private JmsSleuthPostProcessor postProcessor;
    private JmsMessagingTemplate jmsMessagingTemplate;
    
    /**
     * @param processor
     * @param qName
     * @param jmsMessagingTemplate
     */
    @Autowired
    public Producer(JmsSleuthPostProcessor processor, @Value("${inbound.queue}") String qName, @Qualifier("jmsMessagingQueueTemplate") JmsMessagingTemplate jmsMessagingTemplate) {
        this.qName = qName;
        this.postProcessor = processor;
        this.jmsMessagingTemplate = jmsMessagingTemplate;
    }
    
    /**
     * @param user
     */
    @LogInfo
    public void send(User user) {
        logger.info(" Publishing single user: {}", user);
        this.jmsMessagingTemplate.convertAndSend(qName, user, postProcessor);
    }
    
    /**
     * @param users
     */
    @LogInfo
    public void send(String qName, String userName) {
        logger.info(" Publishing username: {}", userName);
        this.jmsMessagingTemplate.convertAndSend(qName, userName, postProcessor);
    }
    
}
