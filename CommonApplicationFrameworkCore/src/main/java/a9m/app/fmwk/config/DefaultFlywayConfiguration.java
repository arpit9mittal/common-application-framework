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

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class defines all the beans required for flyway
 * implementation. It adds the feature enables clean functionality of the flyway
 * in spring boot application by the passing the runtime property
 * "flyway.command".
 * 
 * In order to protect the clean command to run on production the application
 * property "flyway.cleanDisabled" is defaulted to false. Its recommended to set
 * this property to true only in local or dev environment for safety.
 * 
 * 
 * @author arpmitta
 */
@Configuration
public class DefaultFlywayConfiguration {
    
    private static final Logger logger = LoggerFactory.getLogger(DefaultFlywayConfiguration.class);
    
    /**
     * @param flywayCleanDisabled
     * @return
     */
    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy(@Value("${flyway.cleanDisabled:true}")
    boolean flywayCleanDisabled) {
        FlywayMigrationStrategy strategy = new FlywayMigrationStrategy() {
            @Override
            public void migrate(Flyway flyway) {
                String command = System.getProperty("flyway.command", "migrate");
                
                logger.info("***************> flyway.command:{}", command);
                
                switch (command) {
                    case "clean":
                        // System.out.println("flywayCleanDisabled =>: " +
                        // flywayCleanDisabled);
                        if (!flywayCleanDisabled)
                            flyway.clean();
                        
                    case "migrate":
                        flyway.migrate();
                        break;
                    
                    case "repair":
                        flyway.repair();
                        break;
                    
                    default:
                        throw new IllegalArgumentException("Invalid command: " + command);
                }
            }
        };
        
        return strategy;
    }
    
}
