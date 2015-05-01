package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.dyfaces.data.api.AnnotationConfigurations;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.GridOptions;
import org.dyfaces.data.api.SeriesColorOptions;

public class DyDataModel implements Serializable, DataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6959678274477033126L;
	private List<DataSeries> dyDataSeries;
	private String graphTitle;
	private String xAxisLable;
	private String yAxisLable;
	/**
	 * Annotations
	 */
	private AnnotationConfigurations annotationConfigurations;
	/**
	 * Data Series Colors
	 */
	private SeriesColorOptions seriesOptions;
	/**
	 * Grid
	 */
	private GridOptions gridOptions;
	
	public DyDataModel(){
		this.dyDataSeries=new LinkedList<DataSeries>();
	}
	
	public DyDataModel(String graphTitle){
		this();
		this.graphTitle=graphTitle;
	}
	
	public List<DataSeries> getDataSeries() {
		return dyDataSeries;
	}
	public String getGraphTitle() {
		return graphTitle;
	}
	
	public void setGraphTitle(String graphTitle) {
		this.graphTitle = graphTitle;
	}

	public String getxAxisLable() {
		return xAxisLable;
	}

	public void setxAxislable(String xAxisLable) {
		this.xAxisLable = xAxisLable;
	}

	public String getyAxisLable() {
		return yAxisLable;
	}

	public void setyAxislable(String yAxisLable) {
		this.yAxisLable = yAxisLable;
	}

	public AnnotationConfigurations getAnnotationConfigurations() {
		return annotationConfigurations;
	}

	public void setAnnotationConfigurations(
			AnnotationConfigurations annotationConfigurations) {
		this.annotationConfigurations = annotationConfigurations;
	}
	
	public SeriesColorOptions getSeriesColorOptions() {
		return seriesOptions;
	}

	public void setSeriesColorOptions(SeriesColorOptions seriesOptions) {
		this.seriesOptions = seriesOptions;
	}

	public GridOptions getGridOptions() {
		return gridOptions;
	}

	public void setGridOptions(GridOptions gridOptions) {
		this.gridOptions = gridOptions;
	}
	
	
}
