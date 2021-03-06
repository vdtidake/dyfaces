package org.dyfaces.data.api;

public class SelectedPointDetails {
	private Number x;
	private Number y;
	private Number xval;
	private Number yval;
	private String name;
	private Number idx;
	private Number canvasx;
	private Number canvasy;
	
	public SelectedPointDetails() {
		super();
	}

	public SelectedPointDetails(Number x, Number y, Number xval, Number yval,
			String name, Number idx, Number canvasx, Number canvasy) {
		super();
		this.x = x;
		this.y = y;
		this.xval = xval;
		this.yval = yval;
		this.name = name;
		this.idx = idx;
		this.canvasx = canvasx;
		this.canvasy = canvasy;
	}
	
	public Number getX() {
		return x;
	}
	public void setX(Number x) {
		this.x = x;
	}
	public Number getY() {
		return y;
	}
	public void setY(Number y) {
		this.y = y;
	}
	public Number getXval() {
		return xval;
	}
	public void setXval(Number xval) {
		this.xval = xval;
	}
	public Number getYval() {
		return yval;
	}
	public void setYval(Number yval) {
		this.yval = yval;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Number getIdx() {
		return idx;
	}
	public void setIdx(Number idx) {
		this.idx = idx;
	}
	public Number getCanvasx() {
		return canvasx;
	}
	public void setCanvasx(Number canvasx) {
		this.canvasx = canvasx;
	}
	public Number getCanvasy() {
		return canvasy;
	}
	public void setCanvasy(Number canvasy) {
		this.canvasy = canvasy;
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + ", xval=" + xval
				+ ", yval=" + yval + ", name=" + name + ", idx=" + idx
				+ ", canvasx=" + canvasx + ", canvasy=" + canvasy + "]";
	}
	
	
}
