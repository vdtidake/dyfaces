package org.dyfaces.data.api;

import java.io.Serializable;

public class AnnotationConfigurations implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7258389525697420358L;
	/**
	 * Only applies when graph uses GViz chart (Currently not supported)
	 */
	private Boolean showAnnotations=Boolean.TRUE;
	/**
	 * function is called whenever the user clicks on an annotation. signature  function(annotation, point, dygraph, event)
	 * For ajax behavior add ajax event annotationClicked
	 */
	private String clickHandler;
	/**
	 * function is called whenever the user double-clicks on an annotation. signature  function(annotation, point, dygraph, event)
	 * For ajax behavior add ajax event annotationDblClicked
	 */
	private String mouseOverHandler;
	/**
	 *  function is called whenever the user mouses out of an annotation. signature  function(annotation, point, dygraph, event)
	 */
	private String mouseOutHandler;
	/**
	 * function is called whenever the user mouses over an annotation. signature  function(annotation, point, dygraph, event)
	 */
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
