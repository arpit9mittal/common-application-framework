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
package a9m.app.fmwk.demo.rest;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import a9m.app.fmwk.demo.aop.LogInfo;
import a9m.app.fmwk.demo.jms.Producer;
import a9m.app.fmwk.demo.repo.entity.User;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author arpmitta
 *
 */
@ApiIgnore
@RestController
@RequestMapping(value = "/pub")
public class PublishUserRestApi {
    
    private static final Logger logger = LoggerFactory.getLogger(PublishUserRestApi.class);
    
    public Producer producer;
    
    /**
     * @param producer
     */
    @Autowired
    public PublishUserRestApi(Producer producer) {
        this.producer = producer;
    }
    
    @LogInfo
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String publish(@RequestBody User user, @RequestHeader Map<String, String> header) {
        logger.info("\n ** Publishing User: {} \n ** Http Headers: {}", user, header);
        
        if (null != user) {
            producer.send(user);
            return "SUCCESS";
        }
        
        return "FAIL";
    }
}
