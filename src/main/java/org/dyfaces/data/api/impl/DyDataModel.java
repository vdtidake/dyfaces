package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.dyfaces.data.api.AnnotationConfigurations;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.GridOptions;

@SuppressWarnings("serial")
public class DyDataModel implements DataModel,Serializable{
	private Class xAxisType = Number.class;
	private List<DataSeries> dataSerieses = new LinkedList<DataSeries>();
	/**
	 * Grid
	 */
	private GridOptions gridOptions;
	
	/**
	 * Annotations
	 */
	private AnnotationConfigurations annotationConfigurations;
	
	public DyDataModel(Class xAxisType) {
		this.xAxisType = xAxisType;
	}
	
	public Class getxAxisType() {
		return xAxisType;
	}
	public void setxAxisType(Class xAxisType) {
		this.xAxisType = xAxisType;
	}
	public List<DataSeries> getDataSerieses() {
		return dataSerieses;
	}
	
	public void addDataSeries(DataSeries dataSerieses) {
		getDataSerieses().add(dataSerieses);
	}
	
	public void addDataSerieses(List<DataSeries> dataSerieses) {
		getDataSerieses().addAll(dataSerieses);
	}

	public GridOptions getGridOptions() {
		return gridOptions;
	}

	public void setGridOptions(GridOptions gridOptions) {
		this.gridOptions = gridOptions;
	}
	

	public AnnotationConfigurations getAnnotationConfigurations() {
		return annotationConfigurations;
	}

	public void setAnnotationConfigurations(
			AnnotationConfigurations annotationConfigurations) {
		this.annotationConfigurations = annotationConfigurations;
	}

}
