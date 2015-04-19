package org.dyfaces.utils;

import java.util.Date;

import org.dyfaces.DyConstants;

public class DyUtils {

	public static String getDyDate(Object date){
		return DyConstants.dateFormat.format(Date.class.cast(date));
	}
	
	public static String getJSDyDate(Object date){
		return "new Date('"+DyConstants.dateFormat.format(Date.class.cast(date))+"')";
	}
}
