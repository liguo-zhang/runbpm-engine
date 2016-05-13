package org.runbpm.utils;

import java.util.Date;

public class RunBPMUtils {

	
	public static boolean notNull(String string){
		return string!=null && string.trim().length()>0;
	}
	
	public static Date convertLongToDate(String longTime){
		if(notNull(longTime)){
			return new Date(Long.parseLong(longTime));
		}else{
			return new Date();
		}
	}
	

	public static long parseLong(String longString){
		if(notNull(longString)){
			return Long.parseLong(longString);
		}else{
			return 0;
		}
	}
	
	
	public static String getEmptyString(String string){
		if(string ==null){
			return "";
		}else{
			return string;
		}
	}

	public static boolean notNullLong(Long longValue) {
		if(longValue == null ||longValue == 0){
			return false;
		}else{
			return true;
		}
	}
	
}
