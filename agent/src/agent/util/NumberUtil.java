package agent.util;

import java.util.regex.Pattern;

public class NumberUtil {

	public static boolean isInteger(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
		return pattern.matcher(str).matches();
	}

	public static boolean isDouble(String str) {
		Pattern pattern = Pattern.compile("^[-\\+]?[.\\d]*$");
		return pattern.matcher(str).matches();
	}
	
	public static boolean isNumber( String str ){
		if( !isInteger(str) && !isDouble(str) )
			return false;
		else
			return true;
	}
	
}
