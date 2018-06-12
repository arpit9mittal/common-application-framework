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

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.RedeliveryPolicy;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties;
import org.springframework.boot.autoconfigure.jms.activemq.ActiveMQProperties.Packages;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.util.StringUtils;

/**
 * @author arpmitta
 *
 */
@Configuration
@Import(DefaultJmsConfiguration.class)
@EnableConfigurationProperties({ActiveMQProperties.class, AppFrmwkActiveMQProperties.class })
public class ActiveMqJmsConfiguration {
    
    @Value("${spring.application.name}")
    private String appName;
    
    /**
     * @param properties
     * @param appFmwkProperties
     * @return
     */
    ActiveMQConnectionFactory myConnectionFactory(ActiveMQProperties properties, AppFrmwkActiveMQProperties appFmwkProperties) {
        
        ActiveMQConnectionFactory factory;
        
        String user = properties.getUser();
        String password = properties.getPassword();
        if (StringUtils.hasLength(user) && StringUtils.hasLength(password)) {
            factory = new ActiveMQConnectionFactory(user, password, properties.getBrokerUrl());
        } else {
            factory = new ActiveMQConnectionFactory(properties.getBrokerUrl());
        }
        
        factory.setCloseTimeout(properties.getCloseTimeout());
        factory.setNonBlockingRedelivery(properties.isNonBlockingRedelivery());
        factory.setSendTimeout(properties.getSendTimeout());
        Packages packages = properties.getPackages();
        if (packages.getTrustAll() != null) {
            factory.setTrustAllPackages(packages.getTrustAll());
        }
        if (!packages.getTrusted().isEmpty()) {
            factory.setTrustedPackages(packages.getTrusted());
        }
        
        // custom properties
        factory.setProducerWindowSize(1024000);
        factory.setClientIDPrefix(appName);
        
        // redelivery policy
        RedeliveryPolicy redeliveryPolicy = new RedeliveryPolicy();
        redeliveryPolicy.setInitialRedeliveryDelay(appFmwkProperties.getInitialRedeliveryDelay());
        redeliveryPolicy.setBackOffMultiplier(appFmwkProperties.getBackffMultiplier());
        redeliveryPolicy.setUseExponentialBackOff(appFmwkProperties.isUseExponentialBackoff());
        redeliveryPolicy.setMaximumRedeliveries(appFmwkProperties.getMaximumRedeliveries());
        
        factory.setRedeliveryPolicy(redeliveryPolicy);
        
        return factory;
    }
    
    /**
     * @param properties
     * @param appFmwkProperties
     * @return
     */
    @Primary
    @Bean(initMethod = "start", destroyMethod = "stop")
    PooledConnectionFactory pooledJmsConnectionFactory(ActiveMQProperties properties, AppFrmwkActiveMQProperties appFmwkProperties) {
        PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(myConnectionFactory(properties, appFmwkProperties));
        
        ActiveMQProperties.Pool pool = properties.getPool();
        pooledConnectionFactory.setBlockIfSessionPoolIsFull(pool.isBlockIfFull());
        pooledConnectionFactory.setBlockIfSessionPoolIsFullTimeout(pool.getBlockIfFullTimeout());
        pooledConnectionFactory.setCreateConnectionOnStartup(pool.isCreateConnectionOnStartup());
        pooledConnectionFactory.setExpiryTimeout(pool.getExpiryTimeout());
        pooledConnectionFactory.setIdleTimeout(pool.getIdleTimeout());
        pooledConnectionFactory.setMaxConnections(pool.getMaxConnections());
        pooledConnectionFactory.setMaximumActiveSessionPerConnection(pool.getMaximumActiveSessionPerConnection());
        pooledConnectionFactory.setReconnectOnException(pool.isReconnectOnException());
        pooledConnectionFactory.setTimeBetweenExpirationCheckMillis(pool.getTimeBetweenExpirationCheck());
        pooledConnectionFactory.setUseAnonymousProducers(pool.isUseAnonymousProducers());
        
        return pooledConnectionFactory;
    }
    
}
