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
 * Marker interface
 * 
 * @author arpmitta
 *
 */
public interface Processor<T, V> {
    
    /**
     * @param t
     * @param v
     * @throws ProcessingException
     * @throws TerminateProcessException
     */
    void process(T t, V v) throws ProcessingException, TerminateProcessException;
    
}