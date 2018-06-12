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
package a9m.app.fmwk.demo.config;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

import java.util.Collections;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.google.common.base.Predicate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author arpmitta
 *
 */
@Configuration
@EnableSwagger2
public class RestConfiguration {
    
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    
    @Bean
    public Docket portfolioApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("a9m.app.fmwk.demo.rest"))
                .paths(PathSelectors.any())
                .build()
                .pathMapping("/");
    }
    
    static Predicate<String> paths() {
        return or(regex("/*.*"));
    }
    
    /**
     * @return
     */
    private ApiInfo apiInfo() {
        return new ApiInfo(
                  "Common Application Framework - Demo"
                , "Demo application showcases various features with API calls."
                , "1.0.0-RC1"
                , "Terms Of Service"
                , new Contact("Arpit Mittal", "www.arpit9mittal.com", "m.arpit@gmail.com")
                , "Licensed under Apache License 2.0"
                , "https://www.apache.org/licenses/LICENSE-2.0.html"
                , Collections.emptyList());
    }
    
}
