package org.dyfaces.data.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class HighlightSeriesOpts implements Serializable{
	private Double strokeWidth;
	private Integer strokeBorderWidth;
	private Integer highlightCircleSize;
	
	public void initDefault(){
		strokeWidth=3D;
		strokeBorderWidth=1;
		highlightCircleSize=5;
	}
	
	public Double getStrokeWidth() {
		return strokeWidth;
	}
	public void setStrokeWidth(Double strokeWidth) {
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
