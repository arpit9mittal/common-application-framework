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
package a9m.app.fmwk.demo.repo.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import a9m.app.fmwk.demo.aop.LogInfo;
import a9m.app.fmwk.demo.repo.entity.User;

/**
 * @author arpmitta
 *
 */
@Repository
public class UserDaoImpl implements UserDao {
    
    // private static final Logger logger =
        // LoggerFactory.getLogger(UserDaoImpl.class);
    
    private GenericDAO dao;
    
    /**
     * @param dao
     */
    @Autowired
    public UserDaoImpl(@Qualifier("commonDAO") GenericDAO dao) {
        this.dao = dao;
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.UserDao#findAll()
     */
    @LogInfo
    @Override
    public List<User> findAll() {
        String sql = dao.getQuery("list.user");
        return dao.list(sql, User.class);
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.UserDao#findById(long)
     */
    @LogInfo
    @Override
    public User findById(long userId) {
        if (userId > 0) {
            String sql = dao.getQuery("find.user.id");
            return dao.getObject(sql, User.class, userId);
        }
        
        return null;
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.UserDao#findByLastName(java.lang.String)
     */
    @LogInfo
    @Override
    public List<User> findByLastName(String lastname) {
        if (StringUtils.isEmpty(lastname)) {
            return null;
        }
        
        String sql = dao.getQuery("find.user.last.name");
        return dao.list(sql, User.class, lastname);
    }
    
}
