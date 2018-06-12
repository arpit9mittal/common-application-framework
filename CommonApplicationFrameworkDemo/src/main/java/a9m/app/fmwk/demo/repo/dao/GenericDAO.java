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
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

/**
 * @author arpmitta
 *
 */
public interface GenericDAO {
    
    String getQuery(String qName);

    Map<String, Object> getMap( String sql, Object... args) throws DataAccessException;

    <T> T getData( String sql, Class<T> type, Object... args) throws DataAccessException;

    long getSequenceNumber( String sql) throws DataAccessException;

    <T> T getObject( String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException;

    <T> T getObject( String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException;

    <T> T getObject( String sql, Class<T> type, Object... args) throws DataAccessException;

    <T> List<T> list( String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException;

    <T> List<T> list( String sql, Class<T> type, Object... args) throws DataAccessException;

    <T> List<T> listData( String sql, Class<T> type, Object... args) throws DataAccessException;

    List<Map<String, Object>> listOfMaps( String sql, Object... args) throws DataAccessException;

    int update( String sql, Object... args) throws DataAccessException;

    <T> int updateObject( String sql, T obj) throws DataAccessException;

    <T> int[] batchSubmit( String sql, List<T> objList) throws DataAccessException;

}
