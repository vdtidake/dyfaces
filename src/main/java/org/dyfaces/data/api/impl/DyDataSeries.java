package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.Point;

public class DyDataSeries implements Serializable,DataSeries{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3363178368797531823L;
	private String series;
	private List<Point> dataPoints;
	private List<AnnotationPoint> annotations;
	
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

	public List<AnnotationPoint> getAnnotations() {
		return annotations;
	}

	public void setAnnotations(List<AnnotationPoint> annotations) {
		this.annotations = annotations;
	}

}
