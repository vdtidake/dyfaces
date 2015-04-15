package org.dyfaces;

public class DyAttributes {
	private String legend=LegendOptions.always.name();
	private String title;
	private Boolean showRoller=false;
	private Integer rollPeriod=0;
	private Boolean customBars=false;
	private String ylabel;
	private String xlabel;
	
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
	
}
