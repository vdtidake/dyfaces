package org.dyfaces.data.api.impl;

import java.io.Serializable;

import org.dyfaces.data.api.Point;

public class DyPoint implements Serializable,Point{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2488662326846611211L;
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
	
}
