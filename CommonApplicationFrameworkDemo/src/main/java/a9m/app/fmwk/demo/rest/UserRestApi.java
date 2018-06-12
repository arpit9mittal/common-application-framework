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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import a9m.app.fmwk.demo.aop.LogInfo;
import a9m.app.fmwk.demo.gateway.UserPublisherGateway;
import a9m.app.fmwk.demo.repo.entity.User;
import a9m.app.fmwk.demo.repo.operations.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author arpmitta
 *
 */
@Api(value = "User Rest service", description = "user creation, modifcation and delete features", tags = { "User" })
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/user")
public class UserRestApi {
    
    private static final Logger logger = LoggerFactory.getLogger(UserRestApi.class);
    
    public UserRepository repository;
    public UserPublisherGateway gateway;
    
    /**
     * @param repository
     * @param gateway
     */
    @Autowired
    public UserRestApi(UserRepository repository, UserPublisherGateway gateway) {
        this.gateway = gateway;
        this.repository = repository;
    }
    
    /**
     * @return
     */
    @LogInfo
    @ApiOperation(value = "Get users", notes = "Returns a list of user", nickname = "list", response = List.class)
    @GetMapping(path = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<User> list(@ApiIgnore @RequestHeader Map<String, String> header) {
        logger.info("\n ** Http Headers: {}", header);
        
        List<User> list = StreamSupport.stream(repository.findAll().spliterator(), false).collect(Collectors.toList());
        logger.info("Found {} users", list.size());
        
        // publish the list
        gateway.publishUser(list.get(0));
        
        return list;
    }
    
    /**
     * @param id
     * @return
     */
    @LogInfo
    @ApiOperation(value = "Get user by Id", notes = "Returns a user", nickname = "getbyId", response = User.class)
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User getbyId(@PathVariable("id") long id, @ApiIgnore @RequestHeader Map<String, String> header) {
        logger.info("\n ** Http Headers: {}", header);
        
        User user = repository.findOne(id);
        logger.info(" Found User:{}", user);
        
        gateway.publishUser(user);
        
        return user;
    }
    
    /**
     * @param user
     * @param header
     * @return
     */
    @LogInfo
    @ApiOperation(value = "Create user", notes = "Returns a status", nickname = "save", response = User.class)
    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public User save(@RequestBody User user, @ApiIgnore @RequestHeader Map<String, String> header) {
        logger.info("\n ** Saving User: {} \n ** Http Headers: {}", user, header);
        
        User persistedUser = repository.save(user);
        
        if (null != persistedUser) {
            gateway.publishUser(persistedUser);
        }
        
        return persistedUser;
    }
}
