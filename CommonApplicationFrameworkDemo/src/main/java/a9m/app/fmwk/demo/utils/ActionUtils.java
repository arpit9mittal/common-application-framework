/**
 * 
 */
package a9m.app.fmwk.demo.utils;

import java.io.File;
import java.util.Base64;

/**
 *
 */
public final class ActionUtils {
	
	/**
	 * @param encodedData
	 * @return
	 */
	public static String decode(String encodedData) {
		return new String(Base64.getDecoder().decode(encodedData));
	}

	/**
	 * @param encodedData
	 * @return
	 */
	public static File decodeToFile(String encodedData) {
		// TODO
		return null;
	}
}
