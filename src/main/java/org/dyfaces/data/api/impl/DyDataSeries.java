package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.dyfaces.data.DataSeries;
import org.dyfaces.data.Point;

public class DyDataSeries implements Serializable,DataSeries{
	private String series;
	private List<Point> dataPoints;
	
	public DyDataSeries(){
		dataPoints = new LinkedList<Point>();
	}
	
	public String getSeries() {
		return series;
	}
	public void setSeries(String series) {
		this.series = series;
	}

	public List<Point> getDataPoints() {
		return dataPoints;
	}
	
	public void addDataPoint(Point dyPoint){
		dataPoints.add(dyPoint);
	}
	
	
}
