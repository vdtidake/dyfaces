package org.dyfaces;

public class DyCallbacks {
	/*
	 * 
	 * Callbacks
	 */
	private String clickCallback;//function(e, x, points)
	private String drawCallback;//function(dygraph, is_initial)
	private String highlightCallback;//function(event, x, points, row, seriesName)
	private String pointClickCallback;//function(e, point)
	private String underlayCallback;//function(context, area, dygraph)
	private String unhighlightCallback;//function(event)
	private String zoomCallback;//function(minDate, maxDate, yRanges)
	
	public String getClickCallback() {
		return clickCallback;
	}
	public void setClickCallback(String clickCallback) {
		this.clickCallback = clickCallback;
	}
	public String getDrawCallback() {
		return drawCallback;
	}
	public void setDrawCallback(String drawCallback) {
		this.drawCallback = drawCallback;
	}
	public String getHighlightCallback() {
		return highlightCallback;
	}
	public void setHighlightCallback(String highlightCallback) {
		this.highlightCallback = highlightCallback;
	}
	public String getPointClickCallback() {
		return pointClickCallback;
	}
	public void setPointClickCallback(String pointClickCallback) {
		this.pointClickCallback = pointClickCallback;
	}
	public String getUnderlayCallback() {
		return underlayCallback;
	}
	public void setUnderlayCallback(String underlayCallback) {
		this.underlayCallback = underlayCallback;
	}
	public String getUnhighlightCallback() {
		return unhighlightCallback;
	}
	public void setUnhighlightCallback(String unhighlightCallback) {
		this.unhighlightCallback = unhighlightCallback;
	}
	public String getZoomCallback() {
		return zoomCallback;
	}
	public void setZoomCallback(String zoomCallback) {
		this.zoomCallback = zoomCallback;
	}

}
