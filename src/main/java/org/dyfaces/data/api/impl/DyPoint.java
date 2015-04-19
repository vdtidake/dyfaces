package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.Date;

import org.dyfaces.data.api.Point;

public class DyPoint implements Serializable,Point,Comparable<Point>{
	/*
	 * Date or Number type
	 */
	private Object xValue;
	private Number yValue;
	
	public DyPoint(){
		
	}
	
	public DyPoint(Object xValue, Number yValue) {
		this.xValue = xValue;
		this.yValue = yValue;
	}
	public Object getxValue() {
		return xValue;
	}
	public void setxValue(Object xValue) {
		this.xValue = xValue;
	}
	public Number getyValue() {
		return yValue;
	}
	public void setyValue(Number yValue) {
		this.yValue = yValue;
	}

	@Override
	public String toString() {
		return "["+xValue +","+yValue+"]";
	}

	@Override
	public int compareTo(Point o) {
		Object index1 = o.getxValue();
		Object index2 = this.xValue;
 
		if(index1 instanceof Number && index2 instanceof Number){
			Number no1 = (Number) index1;
			Number no2 = (Number) index2;
			
			if (no1.doubleValue() > no2.doubleValue()) {
				return 1;
			} else if (no1.doubleValue() < no2.doubleValue()) {
				return -1;
			} else {
				return 0;
			}
		}else if(index1 instanceof Date && index2 instanceof Date){
			Date no1 = (Date) index1;
			Date no2 = (Date) index2;
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
	
	
}
