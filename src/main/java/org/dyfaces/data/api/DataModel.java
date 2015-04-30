package org.dyfaces.data.api;

import java.util.List;

public interface DataModel {
	public List<DataSeries> getDataSeries();
	public String getGraphTitle();
	public void setGraphTitle(String graphTitle);
	public String getxAxisLable();
	public void setxAxislable(String xAxisLable);
	public String getyAxisLable();
	public void setyAxislable(String yAxisLable);
	public AnnotationConfigurations getAnnotationConfigurations();
	public void setAnnotationConfigurations(AnnotationConfigurations annotationConfigurations);
}
