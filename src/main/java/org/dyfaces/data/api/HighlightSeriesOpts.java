package org.dyfaces.data.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HighlightSeriesOpts implements Serializable{
	private Integer strokeWidth;
	private Integer strokeBorderWidth;
	private Integer highlightCircleSize;
	
	public Integer getStrokeWidth() {
		return strokeWidth;
	}
	public void setStrokeWidth(Integer strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	public Integer getStrokeBorderWidth() {
		return strokeBorderWidth;
	}
	public void setStrokeBorderWidth(Integer strokeBorderWidth) {
		this.strokeBorderWidth = strokeBorderWidth;
	}
	public Integer getHighlightCircleSize() {
		return highlightCircleSize;
	}
	public void setHighlightCircleSize(Integer highlightCircleSize) {
		this.highlightCircleSize = highlightCircleSize;
	}
	
	
}
