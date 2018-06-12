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

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Data;

/**
 * @author arpmitta
 *
 */
@Component
@Data
public abstract class BaseDao {
    
    @Resource(name = "sqlQueries")
    private Properties queryMap;
    
    @Autowired
    private JdbcOperations jdbcTemplate;
    
    @Autowired
    private NamedParameterJdbcOperations namedParameterJdbcTemplate;
    
    /**
     * @param qName
     * @return
     */
    public String getQuery(String qName) {
        if (StringUtils.isEmpty(qName)) {
            return null;
        }
        
        return queryMap.getProperty(qName);
    }
}
