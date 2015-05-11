package org.dyfaces.data.api;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ConfigOptions implements Serializable{
	
	private Integer rollPeriod;
	private Boolean showRoller;
	private Boolean customBars;
	private String legend;
	private Boolean labelsUTC;
	private Boolean showRangeSelector;
	private Integer rangeSelectorHeight;
	private String rangeSelectorPlotStrokeColor;
	private String rangeSelectorPlotFillColor;
	private String labelsDivStyles;
	private Integer labelDivWidth;
	private Integer titleHeight;
	private Double strokeWidth;
	private Boolean includeZero;
	private Boolean avoidMinZero;
	private Integer xRangePad;
	private Integer yRangePad;
	private Boolean drawAxesAtZero;
	private Number[] dateWindow;
	private Number[] valueRange;
	private Boolean drawPoints;
	private Boolean errorBars;
	private Boolean logscale;
	private Boolean animatedZooms;
	private Boolean stackedGraph;
	private HighlightSeriesOpts highlightSeriesOpts = new HighlightSeriesOpts();
	
	public Integer getRollPeriod() {
		return rollPeriod;
	}
	public void setRollPeriod(Integer rollPeriod) {
		this.rollPeriod = rollPeriod;
	}
	public Boolean getShowRoller() {
		return showRoller;
	}
	public void setShowRoller(Boolean showRoller) {
		this.showRoller = showRoller;
	}
	public Boolean getCustomBars() {
		return customBars;
	}
	public void setCustomBars(Boolean customBars) {
		this.customBars = customBars;
	}
	public String getLegend() {
		return legend;
	}
	public void setLegend(String legend) {
		this.legend = legend;
	}
	public Boolean getLabelsUTC() {
		return labelsUTC;
	}
	public void setLabelsUTC(Boolean labelsUTC) {
		this.labelsUTC = labelsUTC;
	}
	public Boolean getShowRangeSelector() {
		return showRangeSelector;
	}
	public void setShowRangeSelector(Boolean showRangeSelector) {
		this.showRangeSelector = showRangeSelector;
	}
	public Integer getRangeSelectorHeight() {
		return rangeSelectorHeight;
	}
	public void setRangeSelectorHeight(Integer rangeSelectorHeight) {
		this.rangeSelectorHeight = rangeSelectorHeight;
	}
	public String getRangeSelectorPlotStrokeColor() {
		return rangeSelectorPlotStrokeColor;
	}
	public void setRangeSelectorPlotStrokeColor(String rangeSelectorPlotStrokeColor) {
		this.rangeSelectorPlotStrokeColor = rangeSelectorPlotStrokeColor;
	}
	public String getRangeSelectorPlotFillColor() {
		return rangeSelectorPlotFillColor;
	}
	public void setRangeSelectorPlotFillColor(String rangeSelectorPlotFillColor) {
		this.rangeSelectorPlotFillColor = rangeSelectorPlotFillColor;
	}
	public String getLabelsDivStyles() {
		return labelsDivStyles;
	}
	public void setLabelsDivStyles(String labelsDivStyles) {
		this.labelsDivStyles = labelsDivStyles;
	}
	public Integer getLabelDivWidth() {
		return labelDivWidth;
	}
	public void setLabelDivWidth(Integer labelDivWidth) {
		this.labelDivWidth = labelDivWidth;
	}
	public Integer getTitleHeight() {
		return titleHeight;
	}
	public void setTitleHeight(Integer titleHeight) {
		this.titleHeight = titleHeight;
	}
	public Double getStrokeWidth() {
		return strokeWidth;
	}
	public void setStrokeWidth(Double strokeWidth) {
		this.strokeWidth = strokeWidth;
	}
	public Boolean getIncludeZero() {
		return includeZero;
	}
	public void setIncludeZero(Boolean includeZero) {
		this.includeZero = includeZero;
	}
	public Boolean getAvoidMinZero() {
		return avoidMinZero;
	}
	public void setAvoidMinZero(Boolean avoidMinZero) {
		this.avoidMinZero = avoidMinZero;
	}
	public Integer getxRangePad() {
		return xRangePad;
	}
	public void setxRangePad(Integer xRangePad) {
		this.xRangePad = xRangePad;
	}
	public Integer getyRangePad() {
		return yRangePad;
	}
	public void setyRangePad(Integer yRangePad) {
		this.yRangePad = yRangePad;
	}
	public Boolean getDrawAxesAtZero() {
		return drawAxesAtZero;
	}
	public void setDrawAxesAtZero(Boolean drawAxesAtZero) {
		this.drawAxesAtZero = drawAxesAtZero;
	}
	public Number[] getDateWindow() {
		return dateWindow;
	}
	public void setDateWindow(Number[] dateWindow) {
		this.dateWindow = dateWindow;
	}
	public Number[] getValueRange() {
		return valueRange;
	}
	public void setValueRange(Number[] valueRange) {
		this.valueRange = valueRange;
	}
	public Boolean getDrawPoints() {
		return drawPoints;
	}
	public void setDrawPoints(Boolean drawPoints) {
		this.drawPoints = drawPoints;
	}
	public Boolean getErrorBars() {
		return errorBars;
	}
	public void setErrorBars(Boolean errorBars) {
		this.errorBars = errorBars;
	}
	public Boolean getLogscale() {
		return logscale;
	}
	public void setLogscale(Boolean logscale) {
		this.logscale = logscale;
	}
	public Boolean getAnimatedZooms() {
		return animatedZooms;
	}
	public void setAnimatedZooms(Boolean animatedZooms) {
		this.animatedZooms = animatedZooms;
	}
	
	public Boolean getStackedGraph() {
		return stackedGraph;
	}
	public void setStackedGraph(Boolean stackedGraph) {
		this.stackedGraph = stackedGraph;
	}
	public HighlightSeriesOpts getHighlightSeriesOpts() {
		return highlightSeriesOpts;
	}
	
}
