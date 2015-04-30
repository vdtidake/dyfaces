package org.dyfaces.data.api;

public class AnnotationConfigurations {
	private Boolean showAnnotations=Boolean.TRUE;
	private String clickHandler;
	private String mouseOverHandler;
	private String mouseOutHandler;
	private String dblClickHandler;
	public Boolean getShowAnnotations() {
		return showAnnotations;
	}
	public void setShowAnnotations(Boolean showAnnotations) {
		this.showAnnotations = showAnnotations;
	}
	public String getClickHandler() {
		return clickHandler;
	}
	public void setClickHandler(String clickHandler) {
		this.clickHandler = clickHandler;
	}
	public String getMouseOverHandler() {
		return mouseOverHandler;
	}
	public void setMouseOverHandler(String mouseOverHandler) {
		this.mouseOverHandler = mouseOverHandler;
	}
	public String getMouseOutHandler() {
		return mouseOutHandler;
	}
	public void setMouseOutHandler(String mouseOutHandler) {
		this.mouseOutHandler = mouseOutHandler;
	}
	public String getDblClickHandler() {
		return dblClickHandler;
	}
	public void setDblClickHandler(String dblClickHandler) {
		this.dblClickHandler = dblClickHandler;
	}
	
	
}
