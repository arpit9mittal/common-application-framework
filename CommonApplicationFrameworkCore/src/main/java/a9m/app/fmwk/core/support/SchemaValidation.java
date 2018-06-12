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

import java.io.IOException;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.ValidationMessage;

/**
 * @author arpmitta
 *
 */
public class SchemaValidation {
    
    /**
     * @param content
     * @return
     * @throws IOException
     */
    public static JsonNode getJsonNodeFromStringContent(String content) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(content);
        return node;
    }
    
    /**
     * @param schemaContent
     * @return
     * @throws Exception
     */
    public static JsonSchema getJsonSchemaFromStringContent(String schemaContent) throws Exception {
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance();
        JsonSchema schema = factory.getSchema(schemaContent);
        return schema;
    }
    
    /**
     * @param jsonSchemaFilePath
     * @param jsonContent
     * @return
     * @throws Exception
     */
    public static Set<ValidationMessage> withJsonSchema(String jsonSchemaFilePath, String jsonContent) throws Exception {
        String schemaContent = Utils.getFileContent(jsonSchemaFilePath);
        JsonSchema schema = getJsonSchemaFromStringContent(schemaContent);
        JsonNode node = getJsonNodeFromStringContent(jsonContent);
        Set<ValidationMessage> errors = schema.validate(node);
        
        return errors;
    }
    
}
