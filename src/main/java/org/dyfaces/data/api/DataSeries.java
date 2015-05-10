package org.dyfaces.data.api;

import java.util.List;

public interface DataSeries {
	public String getSeries();

	public List<Point> getDataPoints();

	public void addDataPoint(Point point);
	
	public List<AnnotationPoint> getAnnotations();

	public void addAnnotations(List<AnnotationPoint> annotations);
	
	public List<HighlightRegion> getHighlightRegions();

	public void addHighlightRegions(List<HighlightRegion> highlightRegions);
	
	public SeriesColorOptions getSeriesColorOptions();

	public void setSeriesColorOptions(SeriesColorOptions seriesOptions);
	
	public SeriesOptions getSeriesOptions();

	public void setSeriesOptions(SeriesOptions seriesOptions);
	
	public void addAllDataPoints(List<Point> dyPoints);
	
	public void addAllDataPoints(Number[][] dyPoint);
}
