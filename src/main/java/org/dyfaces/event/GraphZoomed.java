package org.dyfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

@SuppressWarnings("serial")
public class GraphZoomed extends AjaxBehaviorEvent{

	private Number[] dateWindow;
	private Number[] valueRange;
	
	public GraphZoomed(UIComponent component, Behavior behavior,Number[] dateWindow,Number[] valueRange) {
		super(component, behavior);
		this.dateWindow=dateWindow;
		this.valueRange=valueRange;
	}

	public Number[] getDateWindow() {
		return dateWindow;
	}

	public Number[] getValueRange() {
		return valueRange;
	}
	
	 

}
