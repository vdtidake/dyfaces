package org.dyfaces.data.api;

import java.util.List;

public interface DataModel {
	public Class getxAxisType();
	public void setxAxisType(Class xAxisType);
	public List<DataSeries> getDataSerieses();
	public void addDataSeries(DataSeries dataSerieses);
	public void addDataSerieses(List<DataSeries> dataSerieses);
	public GridOptions getGridOptions();
	public void setGridOptions(GridOptions gridOptions);
	public AnnotationConfigurations getAnnotationConfigurations();
	public void setAnnotationConfigurations(AnnotationConfigurations annotationConfigurations);
	
}
