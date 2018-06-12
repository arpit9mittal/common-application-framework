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
package a9m.app.fmwk.demo.gateway;

import org.springframework.http.HttpStatus;

public class GatewayException extends Exception {
    
    private static final long serialVersionUID = 1337390073185335943L;
    
    private HttpStatus statusCode;
    
    public GatewayException(HttpStatus statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }
    
    public GatewayException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
    
    public GatewayException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public GatewayException(String message) {
        super(message);
    }
    
    public GatewayException(Throwable cause) {
        super(cause);
    }
    
    public HttpStatus getStatusCode() {
        return statusCode;
    }
    
}
