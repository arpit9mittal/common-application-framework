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
package a9m.app.fmwk.core.support;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author arpmitta
 *
 */
public final class Utils {
    
    private static final Logger logger = LoggerFactory.getLogger(Utils.class);
    
    /**
     * @param json
     * @param type
     * @return
     */
    public static <T> T jsonToObject(String json, Class<T> type) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, type);
        } catch (IOException e) {
            logger.error("JSON Error:" + json, e);
            return null;
        }
    }
    
    /**
     * @param type
     * @return
     */
    public static String toJson(final Object type) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(type);
        } catch (JsonProcessingException e) {
            logger.error("Object cannot be converted to json. Object class: " + type.getClass().getCanonicalName(), e);
            return null;
        }
    }
    
    /**
     * @param path
     * @return
     * @throws IOException
     */
    public static String getFileContent(String path) throws IOException {
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        return FileUtils.readFileToString(file, Charset.defaultCharset());
    }
    
    /**
     * @param message
     * @return
     * @throws JMSException
     */
    public static String getJsonPayload(final Message message) throws JMSException {
        String json = "";
        if (message instanceof TextMessage) {
            json = ((TextMessage) message).getText();
        }
        
        return json;
    }
    
}
