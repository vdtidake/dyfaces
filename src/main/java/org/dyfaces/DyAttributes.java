package org.dyfaces;

public class DyAttributes {
	private String legend=LegendOptions.always.name();
	private String title;
	private Boolean showRoller=Boolean.FALSE;
	private Integer rollPeriod=0;
	private Boolean customBars=Boolean.FALSE;
	private String ylabel;
	private String xlabel;
	/*
	 * Point settings
	 */
	private Boolean drawPoints;
	private Short pointSize;
	private Short highlightCircleSize;
	/*
	 * Grid settings
	 */
	private Boolean drawGrid=Boolean.TRUE;
	private Boolean drawXAxis;
	private Boolean drawYAxis;
	
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Boolean getShowRoller() {
		return showRoller;
	}
	public void setShowRoller(Boolean showRoller) {
		this.showRoller = showRoller;
	}
	public Integer getRollPeriod() {
		return rollPeriod;
	}
	public void setRollPeriod(Integer rollPeriod) {
		this.rollPeriod = rollPeriod;
	}
	public Boolean getCustomBars() {
		return customBars;
	}
	public void setCustomBars(Boolean customBars) {
		this.customBars = customBars;
	}
	public String getYlabel() {
		return ylabel;
	}
	public void setYlabel(String ylabel) {
		this.ylabel = ylabel;
	}
	public String getXlabel() {
		return xlabel;
	}
	public void setXlabel(String xlabel) {
		this.xlabel = xlabel;
	}
	public Boolean getDrawPoints() {
		return drawPoints;
	}
	public void setDrawPoints(Boolean drawPoints) {
		this.drawPoints = drawPoints;
	}
	public Short getPointSize() {
		return pointSize;
	}
	public void setPointSize(Short pointSize) {
		this.pointSize = pointSize;
	}
	public Short getHighlightCircleSize() {
		return highlightCircleSize;
	}
	public void setHighlightCircleSize(Short highlightCircleSize) {
		this.highlightCircleSize = highlightCircleSize;
	}
	public Boolean getDrawGrid() {
		return drawGrid;
	}
	public void setDrawGrid(Boolean drawGrid) {
		this.drawGrid = drawGrid;
	}
	public Boolean getDrawXAxis() {
		return drawXAxis;
	}
	public void setDrawXAxis(Boolean drawXAxis) {
		this.drawXAxis = drawXAxis;
	}
	public Boolean getDrawYAxis() {
		return drawYAxis;
	}
	public void setDrawYAxis(Boolean drawYAxis) {
		this.drawYAxis = drawYAxis;
	}
	
	
}
