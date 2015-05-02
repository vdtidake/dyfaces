package org.dyfaces.data.api;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import org.dyfaces.DyConstants;

public class HighlightRegion implements Comparable<HighlightRegion>,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4057949772485070364L;
	private Object minX;
	private Object maxX;
	private String hexColor="#CCEBD6";//FFE6E6
	
	
	public HighlightRegion(Object minX, Object maxX) {
		super();
		this.minX = minX instanceof Date?DyConstants.dateFormat.format(minX):minX;
		this.maxX = maxX instanceof Date?DyConstants.dateFormat.format(maxX):maxX;
	}
	
	public Object getMinX() {
		return minX;
	}
	public void setMinX(Object minX) {
		this.minX = minX instanceof Date?DyConstants.dateFormat.format(minX):minX;
	}
	public Object getMaxX() {
		return maxX;
	}
	public void setMaxX(Object maxX) {
		this.maxX = maxX instanceof Date?DyConstants.dateFormat.format(maxX):maxX;
	}

	@Override
	public int compareTo(HighlightRegion o) {
		Object index1 = o.getMinX();
		Object index2 = this.minX;
 
		if(index1 instanceof Number && index2 instanceof Number){
			Number no1 = (Number) index1;
			Number no2 = (Number) index2;
			
			if (no1.doubleValue() < no2.doubleValue()) {
				return 1;
			} else if (no1.doubleValue() > no2.doubleValue()) {
				return -1;
			} else {
				return 0;
			}
		}else if(index1 instanceof String && index2 instanceof String){
			Date no1 = null;
			Date no2 = null;
			try {
				no1 = DyConstants.dateFormat.parse((String) index1);
				no2 = DyConstants.dateFormat.parse((String) index2);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (no1.before(no2)) {
				return 1;
			} else if (no1.after(no2)) {
				return -1;
			} else {
				return 0;
			}
		}
		return 0;

	}

	public boolean validate() {
		if(minX instanceof Number && maxX instanceof Number){
			Number no1 = (Number) minX;
			Number no2 = (Number) maxX;
			
			if (no1.doubleValue() > no2.doubleValue()) {
				return false;
			}
			return true;
		}else if(minX instanceof String && maxX instanceof String){
			Date no1 = null;
			Date no2 = null;
			try {
				no1 = DyConstants.dateFormat.parse((String) minX);
				no2 = DyConstants.dateFormat.parse((String) maxX);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			if (no1.after(no2)) {
				return false;
			}

			return true;
		}
		return false;
			
	}

	public String getHexColor() {
		return hexColor;
	}

	public void setHexColor(String hexColor) {
		this.hexColor = hexColor;
	}
	
}
