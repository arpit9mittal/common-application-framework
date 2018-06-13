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
 * This interface help in constructing a pipeline of processes.
 * 
 * @author arpmitta
 *
 */
public interface Processor<T, V> {
    
    /**
     * THis method process with input T and V and throw either
     * {@link RuntimeException} or {@link Exception}
     * 
     * @param t
     *            an input object of type T
     * @param v
     *            an input object of type V
     * @throws ProcessingException
     *             this exception is thrown when the need is for rolling back
     *             transactions when Spring transaction management is used.
     * @throws TerminateProcessException
     *             this exception is thrown when the need is to end the process
     *             and all the other processes in the pipeline.
     */
    void process(T t, V v) throws ProcessingException, TerminateProcessException;
    
}
