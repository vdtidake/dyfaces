package org.dyfaces.data.api;

public class SeriesColorOptions {
	/**
	 * A per-series color definition. Used in conjunction with, and overrides, the colors option
	 */
	private String color;
	/**
	 * If colors is not specified, saturation of the automatically-generated data series colors.(0.0 - 1.0)
	 */
	private Float colorSaturation=1.0F;
	/**
	 * If colors is not specified, value of the data series colors, as in hue/saturation/value. (0.0-1.0, default 0.5)
	 */
	private Float colorValue = 0.5F;
	/**
	 * Error bars (or custom bars) for each series are drawn in the same color as the series, but with partial transparency (0.0-1.0, default 0.15)
	 */
	private Float fillAlpha = 0.15F;
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public Float getColorSaturation() {
		return colorSaturation;
	}
	public void setColorSaturation(Float colorSaturation) {
		this.colorSaturation = colorSaturation;
	}
	public Float getColorValue() {
		return colorValue;
	}
	public void setColorValue(Float colorValue) {
		this.colorValue = colorValue;
	}
	public Float getFillAlpha() {
		return fillAlpha;
	}
	public void setFillAlpha(Float fillAlpha) {
		this.fillAlpha = fillAlpha;
	}
	
	
}
