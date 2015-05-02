package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.dyfaces.data.api.AnnotationConfigurations;
import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.GridOptions;
import org.dyfaces.data.api.HighlightRegion;
import org.dyfaces.data.api.Point;
import org.dyfaces.data.api.SeriesColorOptions;
import org.dyfaces.data.api.SeriesOptions;
import org.dyfaces.exception.EmptyDataSetException;
import org.dyfaces.exception.HighlightRegionException;

public class DyDataSeries implements Serializable,DataSeries{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3363178368797531823L;
	private String series;
	private List<Point> dataPoints;
	private List<AnnotationPoint> annotations;
	private List<HighlightRegion> highlightRegions;
	/**
	 * Annotations
	 */
	private AnnotationConfigurations annotationConfigurations;
	/**
	 * Data Series Colors
	 */
	private SeriesColorOptions seriesColorOptions;
	/**
	 * Grid
	 */
	private GridOptions gridOptions;
	/**
	 * 
	 */
	private SeriesOptions seriesOptions;
	
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

	public void addAllDataPoint(Number[][] dyPoint){
		if(dyPoint == null || dyPoint.length == 0){
			throw new EmptyDataSetException("Dataset cannot be null or empty");
		}
		for (Number[] numbers : dyPoint) {
			dataPoints.add(new DyPoint(numbers[0], numbers[1]));
		}
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
					throw new HighlightRegionException("highlight region start point cannot be less than end point");
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

	public SeriesColorOptions getSeriesColorOptions() {
		return seriesColorOptions;
	}

	public void setSeriesColorOptions(SeriesColorOptions seriesColorOptions) {
		this.seriesColorOptions = seriesColorOptions;
	}

	public GridOptions getGridOptions() {
		return gridOptions;
	}

	public void setGridOptions(GridOptions gridOptions) {
		this.gridOptions = gridOptions;
	}

	public SeriesOptions getSeriesOptions() {
		return seriesOptions;
	}

	public void setSeriesOptions(SeriesOptions seriesOptions) {
		this.seriesOptions = seriesOptions;
	}

	
}
