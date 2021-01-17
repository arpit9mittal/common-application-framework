package a9m.app.fmwk.demo.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.origin.OriginTrackedValue;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * An {@link EnvironmentPostProcessor} which does custom actions on the properties prefixed with actions present in application*.yml.
 * Example 
 * - "decode" actions decodes Base64 encoded values
 * - "decodeToFile" actions decodes Base64 encoded value into a file, required when you need a file but can only pass a base64 encoded string. 
 * 
 * Note, to make this class effective it needs to registered in META-INF/spring.factories:
 * org.springframework.boot.env.EnvironmentPostProcessor=<package>.CustomEnvironmentPostProcessor
 * 
 * 
 * Reference - https://www.baeldung.com/spring-boot-environmentpostprocessor
 */
@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomEnvironmentPostProcessor implements EnvironmentPostProcessor {

    ConfigurableEnvironment environment = null;

    @Override
    public void postProcessEnvironment(final ConfigurableEnvironment environment, final SpringApplication application) {
    	
        this.environment = environment;
        environment.getPropertySources().stream()
            .filter(ps -> ps instanceof OriginTrackedMapPropertySource)
            .map(OriginTrackedMapPropertySource.class::cast)
            .map(this::findAndPerformAction)
            .collect(Collectors.toList())
            .forEach(ps -> environment.getPropertySources().replace(ps.getName(), ps));
    }

    /**
     * @param originTrackedMapPropertySource
     * @return
     */
    private OriginTrackedMapPropertySource findAndPerformAction(final OriginTrackedMapPropertySource originTrackedMapPropertySource) {
        
    	Map<String, Object> propertyMap = new HashMap<>(originTrackedMapPropertySource.getSource());
    	for (Map.Entry<String, Object> entry : propertyMap.entrySet()) {
			if( entry.getValue() instanceof OriginTrackedValue 
					&& OriginTrackedValue.class.cast(entry.getValue()).getValue() instanceof String) {
				
				String originalData = OriginTrackedValue.class.cast(entry.getValue()).getValue().toString() ;
				Optional<ActionsEnum> actionFound = ActionsEnum.findAction(originalData::startsWith);
				
				if(actionFound.isPresent())
					propertyMap.put(entry.getKey(), actionFound.get().apply(environment.getProperty(entry.getKey())));
			}
		}
    	
    	return new OriginTrackedMapPropertySource(originTrackedMapPropertySource.getName(), Collections.unmodifiableMap(propertyMap));
    }
}
