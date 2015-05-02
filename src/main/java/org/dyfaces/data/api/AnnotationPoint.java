package org.dyfaces.data.api;

import java.io.Serializable;

public class AnnotationPoint implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4439349984729280824L;
	private String series;
	private Object x;
	private String shortText;
	private String text;
    private String icon;
    private Integer width;
    private Integer height;
    private Integer tickHeight;
    private String cssClass;
    private Boolean attachAtBottom=Boolean.FALSE;
    
	public AnnotationPoint(Object x, String shortText,String text) {
		super();
		this.x = x;
		this.shortText = shortText;
		this.text = text;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Object getX() {
		return x;
	}

	public void setX(Object x) {
		this.x = x;
	}

	public String getShortText() {
		return shortText;
	}

	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getTickHeight() {
		return tickHeight;
	}

	public void setTickHeight(Integer tickHeight) {
		this.tickHeight = tickHeight;
	}

	public String getCssClass() {
		return cssClass;
	}

	public void setCssClass(String cssClass) {
		this.cssClass = cssClass;
	}

	public Boolean getAttachAtBottom() {
		return attachAtBottom;
	}

	public void setAttachAtBottom(Boolean attachAtBottom) {
		this.attachAtBottom = attachAtBottom;
	}

	@Override
	public String toString() {
		return "[series=" + series + ", x=" + x
				+ ", shortText=" + shortText + ", text=" + text + ", icon="
				+ icon + ", width=" + width + ", height=" + height
				+ ", tickHeight=" + tickHeight + ", cssClass=" + cssClass
				+ ", attachAtBottom=" + attachAtBottom + "]";
	}

	
}
