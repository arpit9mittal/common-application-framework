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
package a9m.app.fmwk.demo.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import a9m.app.fmwk.demo.aop.LogInfo;
import a9m.app.fmwk.demo.repo.entity.User;

/**
 * @author arpmitta
 *
 */
@Component
public class UserPublisherGatewayImpl extends BaseRestGateway implements UserPublisherGateway {
    
    private static final Logger logger = LoggerFactory.getLogger(UserPublisherGatewayImpl.class);
    
    private String baseUrl;
    
    /**
     * @param producer
     * @param repository
     */
    @Autowired
    public UserPublisherGatewayImpl(RestTemplate restTemplate, @Value("${demo.gateway.rest.user.publisher}") String baseUrl) {
        super(restTemplate);
        this.baseUrl = baseUrl;
    }
    
    /*
     * (non-Javadoc)
     * @see
     * a9m.app.fmwk.demo.gateway.UserPublisherGateway#publishUser(a9m.app.fmwk.
     * demo.repo.model.User)
     */
    @LogInfo
    @Override
    public String publishUser(User user) {
        String response;
        try {
            response = postRestService(baseUrl, user, String.class);
        } catch (GatewayException e) {
            response = e.getMessage();
            logger.error(e.getMessage(), e);
        }
        
        logger.info("Response from User publisher gateway: {}", response);
        return response;
    }
    
}
