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
package a9m.app.fmwk.demo.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * @author amitt8
 *
 */
@Aspect
@Component
public class ApplicationLogger {
    
    private static final Logger logger = LoggerFactory.getLogger(ApplicationLogger.class);
    
    @Before("@annotation(logInfo)")
    public void logInfoBefore(JoinPoint jointPoint, LogInfo logInfo) throws Throwable {
        logger.info(" Entered inside the method: {}.{}(..)", jointPoint.getSignature().getDeclaringTypeName(), jointPoint.getSignature().getName());
    }
    
    @AfterReturning(pointcut = "@annotation(logInfo)", returning = "result")
    public void logInfoAfterReturning(JoinPoint joinPoint, LogInfo logInfo, Object result) {
        logger.info(" Exited the method: {}.{}(..)", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    }
    
    @AfterThrowing(pointcut = "@annotation(logInfo)", throwing = "ex")
    public void logInfoAfterThrowing(JoinPoint joinPoint, LogInfo logInfo, Throwable ex) {
        logger.error(" Exception thrown inside the method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName() + "(..)", ex);
    }
    
    @Around("@annotation(logInfo)")
    public Object logInfoAround(ProceedingJoinPoint jointPoint, LogInfo logInfo) throws Throwable {
        Object ret;
        StopWatch watch = new StopWatch();
        watch.start();
        
        try {
            ret = jointPoint.proceed();
        } finally {
            watch.stop();
            logger.info(" {}.{} took [{}] milli seconds", jointPoint.getSignature().getDeclaringTypeName(), jointPoint.getSignature().getName(), watch.getTotalTimeMillis());
        }
        
        return ret;
    }
    
}
