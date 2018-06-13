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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import a9m.app.fmwk.demo.DemoApplication;

/**
 * @author arpmitta
 *
 */
@ActiveProfiles("local")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = { DemoApplication.class })
public class UserRestApiIntegrationTest {
    
    @LocalServerPort
    private int port;
    
    private URL base;
    
    @Autowired
    private TestRestTemplate template;
    
    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/user/1/");
    }
    
    @Test
    public void getUser() throws Exception {
        ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
        assertThat(response.getBody(), equalTo("{\"id\":1,\"username\":\"Flywaychamp\",\"firstName\":\"Arpit\",\"lastName\":\"Mittal\"}"));
    }
    
}
