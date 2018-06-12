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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Repository;

/**
 * @author arpmitta
 *
 */
@Repository("commonDAO")
public class GenericDAOImpl extends BaseDao implements GenericDAO {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericDAOImpl.class);
    
    // private methods
    
    /**
     * @param sql
     * @param parameters
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static final void logQueryInfo(String sql, Object... parameters) {
        if (LOGGER.isDebugEnabled()) {
            StringBuilder builder = new StringBuilder(100);
            
            builder.append("Executing query -> ").append(sql).append(System.lineSeparator());
            
            if (parameters == null || parameters.length == 0) {
                builder.append("Paramerters:  NONE");
            } else if (parameters.length == 1 && parameters[0] instanceof Map) {
                builder.append("Paramerters:  Map<String, ?>").append(System.lineSeparator());
                Map<String, ?> params = (Map<String, ?>) parameters[0];
                for (Entry<String, ?> entry : params.entrySet()) {
                    builder.append("\t -> Setting at ").append(entry.getKey()).append(": ").append(entry.getValue());
                    
                    if (null != entry.getValue()) {
                        builder.append("\t\t of type: ").append(entry.getValue().getClass());
                    }
                    builder.append(System.lineSeparator());
                }
            } else if (parameters.length == 1 && parameters[0] instanceof List) {
                List list = (List) parameters[0];
                builder.append("Paramerters: ").append(list.getClass()).append(" of size: ").append(list.size()).append(System.lineSeparator());
            } else {
                for (int i = 0; i < parameters.length; i++) {
                    builder.append("\t -> Setting at ").append((i + 1)).append(": ").append(parameters[i]).append(System.lineSeparator());
                }
            }
            
            LOGGER.debug(builder.toString());
        }
    }
    
    // public methods
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#getData(java.lang.String,
     * java.lang.Class, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T> T getData(String sql, Class<T> type, Object... args) throws DataAccessException {
        logQueryInfo(sql, args);
        
        if (args == null || args.length == 0) {
            return getJdbcTemplate().queryForObject(sql, type);
        } else if (args.length == 1 && args[0] instanceof Map) {
            Map<String, ?> params = (Map<String, ?>) args[0];
            return getNamedParameterJdbcTemplate().queryForObject(sql, params, type);
        } else {
            return getJdbcTemplate().queryForObject(sql, type, args);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see
     * a9m.app.fmwk.demo.repo.dao.GenericDAO#getSequenceNumber(java.lang.String)
     */
    @Override
    public final long getSequenceNumber(String sql) throws DataAccessException {
        return getData(sql, Long.class, new Object[] {});
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#getObject(java.lang.String,
     * org.springframework.jdbc.core.ResultSetExtractor, java.lang.Object[])
     */
    @Override
    public final <T> T getObject(String sql, ResultSetExtractor<T> rse, Object... args) throws DataAccessException {
        logQueryInfo(sql, args);
        
        if (args == null || args.length == 0) {
            return getJdbcTemplate().query(sql, rse);
        } else {
            return getJdbcTemplate().query(sql, args, rse);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#getObject(java.lang.String,
     * org.springframework.jdbc.core.RowMapper, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T> T getObject(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        T object;
        logQueryInfo(sql, args);
        
        try {
            if (args == null || args.length == 0) {
                object = getJdbcTemplate().queryForObject(sql, rowMapper);
            } else if (args.length == 1 && args[0] instanceof Map) {
                Map<String, ?> params = (Map<String, ?>) args[0];
                object = getNamedParameterJdbcTemplate().queryForObject(sql, params, rowMapper);
            } else {
                object = getJdbcTemplate().queryForObject(sql, args, rowMapper);
            }
        } catch (EmptyResultDataAccessException emptyDae) {
            LOGGER.warn(emptyDae.getMessage(), emptyDae);
            object = null;
        } catch (DataAccessException dae) {
            throw dae;
        }
        
        return object;
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#getObject(java.lang.String,
     * java.lang.Class, java.lang.Object[])
     */
    @Override
    public final <T> T getObject(String sql, Class<T> type, Object... args) throws DataAccessException {
        return getObject(sql, new BeanPropertyRowMapper<T>(type), args);
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#list(java.lang.String,
     * org.springframework.jdbc.core.RowMapper, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T> List<T> list(String sql, RowMapper<T> rowMapper, Object... args) throws DataAccessException {
        logQueryInfo(sql, args);
        
        if (args == null || args.length == 0) {
            return getJdbcTemplate().query(sql, rowMapper);
        } else if (args.length == 1 && args[0] instanceof Map) {
            Map<String, ?> params = (Map<String, ?>) args[0];
            return getNamedParameterJdbcTemplate().query(sql, params, rowMapper);
        } else {
            return getJdbcTemplate().query(sql, args, rowMapper);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#list(java.lang.String,
     * java.lang.Class, java.lang.Object[])
     */
    @Override
    public final <T> List<T> list(String sql, Class<T> type, Object... args) throws DataAccessException {
        return list(sql, new BeanPropertyRowMapper<T>(type), args);
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#listData(java.lang.String,
     * java.lang.Class, java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public final <T> List<T> listData(String sql, Class<T> type, Object... args) throws DataAccessException {
        logQueryInfo(sql, args);
        
        if (args == null || args.length == 0) {
            return getJdbcTemplate().queryForList(sql, type);
        } else if (args.length == 1 && args[0] instanceof Map) {
            Map<String, ?> params = (Map<String, ?>) args[0];
            return getNamedParameterJdbcTemplate().queryForList(sql, params, type);
        } else {
            return getJdbcTemplate().queryForList(sql, type, args);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#listOfMaps(java.lang.String,
     * java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public final List<Map<String, Object>> listOfMaps(String sql, Object... args) throws DataAccessException {
        logQueryInfo(sql, args);
        
        if (args == null || args.length == 0) {
            return getJdbcTemplate().queryForList(sql);
        } else if (args.length == 1 && args[0] instanceof Map) {
            Map<String, ?> params = (Map<String, ?>) args[0];
            return getNamedParameterJdbcTemplate().queryForList(sql, params);
        } else {
            return getJdbcTemplate().queryForList(sql, args);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#update(java.lang.String,
     * java.lang.Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public final int update(String sql, Object... args) throws DataAccessException {
        logQueryInfo(sql, args);
        
        if (args == null || args.length == 0) {
            return getJdbcTemplate().update(sql);
        } else if (args.length == 1 && args[0] instanceof Map) {
            Map<String, ?> params = (Map<String, ?>) args[0];
            return getNamedParameterJdbcTemplate().update(sql, params);
        } else {
            return getJdbcTemplate().update(sql, args);
        }
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#updateObject(java.lang.String,
     * java.lang.Object)
     */
    @Override
    public final <T> int updateObject(String sql, T obj) throws DataAccessException {
        if (obj == null) {
            throw new DataIntegrityViolationException("null object not allowed");
        }
        
        logQueryInfo(sql, obj);
        return getNamedParameterJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(obj));
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#batchSubmit(java.lang.String,
     * java.util.List)
     */
    @Override
    public final <T> int[] batchSubmit(String sql, List<T> objList) throws DataAccessException {
        logQueryInfo(sql, objList);
        return getNamedParameterJdbcTemplate().batchUpdate(sql, SqlParameterSourceUtils.createBatch(objList.toArray()));
    }
    
    /*
     * (non-Javadoc)
     * @see a9m.app.fmwk.demo.repo.dao.GenericDAO#getMap(java.lang.String,
     * java.lang.Object[])
     */
    @Override
    public Map<String, Object> getMap(String sql, Object... args) throws DataAccessException {
        return getObject(sql, new KeyValueMapRSE(), args);
    }
}

/**
 * @author arpmitta
 *
 */
class KeyValueMapRSE implements ResultSetExtractor<Map<String, Object>> {
    
    /*
     * (non-Javadoc)
     * @see
     * org.springframework.jdbc.core.ResultSetExtractor#extractData(java.sql.
     * ResultSet)
     */
    @Override
    public Map<String, Object> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<String, Object> results = new HashMap<String, Object>();
        while (resultSet.next()) {
            results.put(resultSet.getString("KEY"), resultSet.getString("VALUE"));
        }
        return results;
    }
}
