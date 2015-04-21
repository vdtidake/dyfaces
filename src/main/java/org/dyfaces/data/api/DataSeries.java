package org.dyfaces.data.api;

import java.util.List;

public interface DataSeries {
	public String getSeries();

	public void setSeries(String series);

	public List<Point> getDataPoints();

	public void addDataPoint(Point point);
	
	public List<Object> getAnnotations();

	public void setAnnotations(List<Object> annotations);
	
}
