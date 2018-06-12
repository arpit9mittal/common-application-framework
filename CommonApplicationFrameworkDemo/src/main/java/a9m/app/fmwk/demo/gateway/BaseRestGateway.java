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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class BaseRestGateway {
    
    private static Logger logger = LoggerFactory.getLogger(BaseRestGateway.class);
    
    private RestTemplate restTemplate;
    
    /**
     * @param restTemplate
     */
    public BaseRestGateway(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    /**
     * @param object
     * @return
     * @throws JsonProcessingException
     */
    private static String toJson(final Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
    
    /**
     * @param url
     * @param request
     * @param responseType
     * @param uriVariables
     * @return
     * @throws GatewayException
     */
    protected <T> T postRestService(String url, Object request, Class<T> responseType, Object... uriVariables) throws GatewayException {
        try {
            logger.info("Sending post request to {} with payload {}", url, request);
            return restTemplate.postForObject(url, request, responseType, uriVariables);
        } catch (HttpStatusCodeException ex) {
            logger.error("Error while posting request to {} with request payload:{}", url, request);
            throw new GatewayException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            logger.error("Error while posting request to {} with request payload:{}", url, request);
            throw new GatewayException(ex);
        }
    }
    
    /**
     * @param url
     * @param request
     * @param responseType
     * @param uriVariables
     * @return
     * @throws GatewayException
     */
    protected <T> List<T> postRestServiceForResponseTypeList(String url, Object request, ParameterizedTypeReference<List<T>> responseType, Object... uriVariables) throws GatewayException {
        try {
            logger.info("Sending post request to {} with payload {}", url, request);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<?> entity = new HttpEntity<>(toJson(request), headers);
            ResponseEntity<List<T>> response = restTemplate.exchange(url, HttpMethod.POST, entity, responseType, uriVariables);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            logger.error("Error while posting request to {} with request payload:{}", url, request);
            throw new GatewayException(ex.getStatusCode(), ex.getResponseBodyAsString(), ex);
        } catch (Exception ex) {
            logger.error("Error while posting request to {} with request payload:{}", url, request);
            throw new GatewayException(ex);
        }
    }
    
}
