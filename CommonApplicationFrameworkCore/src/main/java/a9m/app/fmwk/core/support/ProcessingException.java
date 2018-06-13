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

/**
 * This class is an extension of RuntimeException
 * 
 * @author arpmitta
 *
 */
public class ProcessingException extends RuntimeException {
    
    private static final long serialVersionUID = -8043667937572664002L;
    
    /**
     * @param message
     *            a text message
     * @param cause
     *            an object of Throwable
     */
    public ProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * @param message
     *            a text message
     */
    public ProcessingException(String message) {
        super(message);
    }
    
    /**
     * @param cause
     *            an object of Throwable
     */
    public ProcessingException(Throwable cause) {
        super(cause);
    }
    
}
