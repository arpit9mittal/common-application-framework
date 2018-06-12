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
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import a9m.app.fmwk.annotation.EnableAppFramework;
import a9m.app.fmwk.demo.repo.dao.UserDao;
import a9m.app.fmwk.demo.repo.entity.User;
import a9m.app.fmwk.demo.repo.operations.UserRepository;

/**
 * This is common application framework demo application, which shows how to use
 * application framework with data access features.
 * 
 * Features show case- 
 * <ul>
 * <li> data access with JPA c
 * <li> data access with jdbc templates </li>
 * </ul>
 * 
 * @author arpmitta
 *
 */
@EnableAppFramework
@EnableTransactionManagement
public class DataAccessDemoApplication {
    
    private static final Logger logger = LoggerFactory.getLogger(DataAccessDemoApplication.class);
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DataAccessDemoApplication.class, args);
        
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        int exit = SpringApplication.exit(context);
        System.exit(exit);
    }
    
    @Bean(name = "sqlQueries")
    public PropertiesFactoryBean mapper() {
        PropertiesFactoryBean bean = new PropertiesFactoryBean();
        bean.setLocation(new ClassPathResource("sql/queries.xml"));
        return bean;
    }
    
    /**
     * @param repository
     * @return
     */
    @Bean
    @Order(1)
    public CommandLineRunner demoJpa(UserRepository repository) {
        return (args) -> {
            logger.info("\n-------------------------------\n Data Access through JPA \n-------------------------------\n");
            
            // fetch all users
            logger.info("Users found with findAll():");
            for (User user : repository.findAll()) {
                logger.info("{}", user);
            }
            
            // fetch users by last name
            logger.info("User found with findByLastName('Mittal'):");
            for (User user : repository.findByLastName("Mittal")) {
                logger.info("{}", user);
            }
            
            // fetch an individual user by ID
            User user = repository.findOne(2L);
            logger.info("User found with findOne(2L):");
            logger.info("{}", user);
        };
    }
    
    /**
     * @param repository
     * @return
     */
    @Bean
    @Order(2)
    public CommandLineRunner demoJdbc(UserDao dao) {
        return (args) -> {
            logger.info("\n---------------------------------------\n Data Access through JDBC Template \n---------------------------------------\n");
            
            // fetch all users
            logger.info("Users found with findAll():");
            for (User user : dao.findAll()) {
                logger.info("{}", user);
            }
            
            // fetch users by last name
            logger.info("User found with findByLastName('Mittal'):");
            for (User user : dao.findByLastName("Mittal")) {
                logger.info("{}", user);
            }
            
            // fetch an individual user by ID
            User user = dao.findById(2L);
            logger.info("User found with findOne(2L):");
            logger.info("{}", user);
        };
    }
    
}
