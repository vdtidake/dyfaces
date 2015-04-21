package org.dyfaces;

public class DyAnnoAttributes {
	private String series;
	private Object x;
	private String shortText;
	private String text;
	

	public DyAnnoAttributes(String series, Object x, String shortText,
			String text) {
		super();
		this.series = series;
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

}
