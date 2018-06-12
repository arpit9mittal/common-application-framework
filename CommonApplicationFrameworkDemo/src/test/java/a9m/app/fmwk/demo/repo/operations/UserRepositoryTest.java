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
package a9m.app.fmwk.demo.repo.operations;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import a9m.app.fmwk.demo.DemoApplication;
import a9m.app.fmwk.demo.repo.entity.User;

/**
 * @author arpmitta
 *
 */
//@ActiveProfiles("local")
//@RunWith(SpringRunner.class)
//@DataJpaTest
//@ContextConfiguration(classes = { DemoApplication.class })
public class UserRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private UserRepository userRepo;
    
//    @Test
    public void testFindByLastName() {
        User user = User.builder().firstName("Arpit").lastName("Mittal").username("amitt").build();
        entityManager.persist(user);
        
        List<User> findByLastName = userRepo.findByLastName(user.getLastName());
        
        assertThat(findByLastName).extracting(User::getLastName).containsOnly(user.getLastName());
    }
    
}
