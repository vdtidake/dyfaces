package org.dyfaces.data;

import java.util.List;

import org.dyfaces.data.api.impl.DyPoint;

public interface DataSeries {
	public String getSeries();

	public void setSeries(String series);

	public List<Point> getDataPoints();

	public void addDataPoint(Point dyPoint);
}
