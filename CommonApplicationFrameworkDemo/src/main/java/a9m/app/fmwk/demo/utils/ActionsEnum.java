/**
 * 
 */
package a9m.app.fmwk.demo.utils;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

/**
 * @author s2565087
 *
 */
public enum ActionsEnum {
	
	DECODE_PREFIX("decode:", ActionUtils::decode),
	DECODE_TO_FILE_PREFIX("decodeToFile:", ActionUtils::decodeToFile);
	
	private String action;
	private Function<String, Object> fn;
	
	/**
	 * @param actionCode
	 */
	ActionsEnum(String action, Function<String, Object> fn){
		this.fn = fn ;
		this.action = action;
	}

	/**
	 * @return
	 */
	public String action() {
		return action;
	}
	
    /**
     * @return
     */
    public static Stream<ActionsEnum> stream() {
        return Stream.of(ActionsEnum.values()); 
    }
    
    
    /**
     * @param data
     * @return
     */
    public static Optional<ActionsEnum> findAction(Function<String, Boolean> fn) {
    	return ActionsEnum.stream()
    				.filter(a -> fn.apply(a.action()))
    				.findFirst();
    }
    
	/**
	 * @param data
	 * @return
	 */
	public Object apply(String data) {
		// remove prefix 'action' from the data, before applying the function
		return fn.apply(StringUtils.removeStart(data, action));
	}
}
