package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.dyfaces.data.api.AnnotationConfigurations;
import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.HighlightRegion;
import org.dyfaces.data.api.Point;

public class DyDataSeries implements Serializable,DataSeries{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3363178368797531823L;
	private String series;
	private List<Point> dataPoints;
	private List<AnnotationPoint> annotations;
	private List<HighlightRegion> highlightRegions;
	private AnnotationConfigurations annotationConfigurations;
	
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

	public void addAnnotations(List<AnnotationPoint> annotations) {
		if (annotations != null) {
			for (AnnotationPoint annotationPoint : annotations) {
				annotationPoint.setSeries(series);
			}
		}
		this.annotations = annotations;
	}

	public List<HighlightRegion> getHighlightRegions() {
		return highlightRegions;
	}

	public void addHighlightRegions(List<HighlightRegion> highlightRegions) {
		if (highlightRegions != null) {
			for (HighlightRegion region : highlightRegions) {
				if(!region.validate()){
					//TODO Throw custom exception here
				}
			}
			Collections.sort(highlightRegions);
		}
		
		this.highlightRegions = highlightRegions;
	}

	public AnnotationConfigurations getAnnotationConfigurations() {
		return annotationConfigurations;
	}

	public void setAnnotationConfigurations(
			AnnotationConfigurations annotationConfigurations) {
		this.annotationConfigurations = annotationConfigurations;
	}

	
}
