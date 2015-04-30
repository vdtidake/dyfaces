package org.dyfaces.data.api;

import java.util.List;

public interface DataSeries {
	public String getSeries();

	public void setSeries(String series);

	public List<Point> getDataPoints();

	public void addDataPoint(Point point);
	
	public List<AnnotationPoint> getAnnotations();

	public void addAnnotations(List<AnnotationPoint> annotations);
	
	public List<HighlightRegion> getHighlightRegions();

	public void addHighlightRegions(List<HighlightRegion> highlightRegions);
	
	public AnnotationConfigurations getAnnotationConfigurations();
	
	public void setAnnotationConfigurations(AnnotationConfigurations annotationConfigurations);
}
