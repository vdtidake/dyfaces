package org.dyfaces.data.api;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class GridOptions {
	/**
	 * Whether to display gridlines in the chart.
	 */
	private Boolean drawGrid = Boolean.TRUE;
	private Boolean drawXGrid = Boolean.TRUE;
	private Boolean drawYGrid = Boolean.TRUE;
	private String gridLineColor="'rgb(128,128,128)'";
	private List<PerAxis> axisGridOptions;
	
	public GridOptions(){
		axisGridOptions = new ArrayList<GridOptions.PerAxis>(3);
	}
	
	public Boolean getDrawGrid() {
		return drawGrid;
	}
	public void setDrawGrid(Boolean drawGrid) {
		this.drawGrid = drawGrid;
	}
	public Boolean getDrawXGrid() {
		return drawXGrid;
	}
	public void setDrawXGrid(Boolean drawXGrid) {
		this.drawXGrid = drawXGrid;
	}
	public Boolean getDrawYGrid() {
		return drawYGrid;
	}
	public void setDrawYGrid(Boolean drawYGrid) {
		this.drawYGrid = drawYGrid;
	}
	public String getGridLineColor() {
		return gridLineColor;
	}
	public void setGridLineColor(String gridLineColor) {
		this.gridLineColor = gridLineColor;
	}
	
	public List<PerAxis> getAxisGridOptions() {
		return axisGridOptions;
	}

	public class PerAxis{
		/**
		 * per axis options
		 */
		private transient Axes axis;
		private Boolean drawGrid = Boolean.TRUE;
		private String gridLineColor="'rgb(128,128,128)'";
		private Float gridLineWidth;
		private Boolean independentTicks;
		private Integer pixelsPerLabel;
		private Number[] gridLinePattern;
		
		public PerAxis(){
			
		}
		
		public PerAxis(Axes axis){
			this.axis=axis;
		}
		
		public Axes getAxis() {
			return axis;
		}
		public void setAxis(Axes axis) {
			this.axis = axis;
		}
		public Boolean getDrawGrid() {
			return drawGrid;
		}
		public void setDrawGrid(Boolean drawGrid) {
			this.drawGrid = drawGrid;
		}
		public String getGridLineColor() {
			return gridLineColor;
		}
		public void setGridLineColor(String gridLineColor) {
			this.gridLineColor = gridLineColor;
		}
		public Float getGridLineWidth() {
			return gridLineWidth;
		}
		public void setGridLineWidth(Float gridLineWidth) {
			this.gridLineWidth = gridLineWidth;
		}
		public Boolean getIndependentTicks() {
			return independentTicks;
		}
		public void setIndependentTicks(Boolean independentTicks) {
			this.independentTicks = independentTicks;
		}
		public Integer getPixelsPerLabel() {
			return pixelsPerLabel;
		}
		public void setPixelsPerLabel(Integer pixelsPerLabel) {
			this.pixelsPerLabel = pixelsPerLabel;
		}
		public Number[] getGridLinePattern() {
			return gridLinePattern;
		}
		public void setGridLinePattern(Number[] gridLinePattern) {
			this.gridLinePattern = gridLinePattern;
		}
		
	}
	
	public enum Axes{x,y,y2}
}
