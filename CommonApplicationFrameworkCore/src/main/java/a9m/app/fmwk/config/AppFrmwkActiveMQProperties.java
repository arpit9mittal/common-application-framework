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

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for ActiveMQ.
 *
 */
@ConfigurationProperties(prefix = "app.fmwk.activemq")
public class AppFrmwkActiveMQProperties {
    
    private int maximumRedeliveries = 5;
    private int backffMultiplier = 5;
    private long initialRedeliveryDelay = 1;
    private boolean useExponentialBackoff;
    
    public int getMaximumRedeliveries() {
        return maximumRedeliveries;
    }
    
    public void setMaximumRedeliveries(int maximumRedeliveries) {
        this.maximumRedeliveries = maximumRedeliveries;
    }
    
    public int getBackffMultiplier() {
        return backffMultiplier;
    }
    
    public void setBackffMultiplier(int backffMultiplier) {
        this.backffMultiplier = backffMultiplier;
    }
    
    public long getInitialRedeliveryDelay() {
        return initialRedeliveryDelay;
    }
    
    public void setInitialRedeliveryDelay(long initialRedeliveryDelay) {
        this.initialRedeliveryDelay = initialRedeliveryDelay;
    }
    
    public boolean isUseExponentialBackoff() {
        return useExponentialBackoff;
    }
    
    public void setUseExponentialBackoff(boolean useExponentialBackoff) {
        this.useExponentialBackoff = useExponentialBackoff;
    }
    
}
