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
package a9m.app.fmwk.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.Order;

import a9m.app.fmwk.annotation.EnableAppFramework;
import a9m.app.fmwk.config.ActiveMqJmsConfiguration;
import a9m.app.fmwk.demo.jms.Producer;
import a9m.app.fmwk.demo.repo.entity.User;

/**
 * This is common application framework demo application, which shows how to use
 * application framework with all its jms features.
 * 
 * Features showcased - 
 * <ul>
 * <li>Flyway with clean</li>
 * <li>JMS listeners with schema validations</li>
 * <li>Spring Sleuth instrumentation with @JmsListener</li>
 * <li>Existing the spring boot application</li>
 * </ul>
 * 
 * @author arpmitta
 *
 */
@EnableAppFramework
@Import({ ActiveMqJmsConfiguration.class })
@ComponentScan(excludeFilters = { @Filter(type = FilterType.REGEX, pattern = "a9m.app.fmwk.demo.repo.dao.*") })
public class JmsDemoApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(JmsDemoApplication.class);
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(JmsDemoApplication.class, args);
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int exit = SpringApplication.exit(context);
        System.exit(exit);
    }
    
    /**
     * @param repository
     * @return
     */
    @Bean
    @Order
    public CommandLineRunner demoJms(Producer producer) {
        return (args) -> {
            logger.info("\n-------------------------------\n JMS Demo \n-------------------------------\n");
            User user = User.builder().firstName("Arpit").lastName("Mittal").username("amitt8").id(100839l).build();
            producer.send(user);
        };
    }
    
}
