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

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import a9m.app.fmwk.annotation.EnableAppFramework;
import a9m.app.fmwk.config.ActiveMqJmsConfiguration;

/**
 * This is common application framework demo application, which shows how to use
 * application framework with all its features in full glory.
 * 
 * Features showcased-
 * <ul>
 * <li>Flyway with clean</li>
 * <li>Restful calls</li>
 * <li>JPA</li>
 * <li>JMS</li>
 * <li>Schema validation</li>
 * <li>Spring Sleuth instrumentation with @JmsListener</li>
 * </ul>
 * 
 * @author arpmitta
 *
 */
@EnableAppFramework
@Import({ ActiveMqJmsConfiguration.class })
@ComponentScan(excludeFilters = { @Filter(type = FilterType.REGEX, pattern = "a9m.app.fmwk.demo.repo.dao.*") })
public class DemoApplication {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
}
