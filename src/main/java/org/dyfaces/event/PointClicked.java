package org.dyfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

@SuppressWarnings("serial")
public class PointClicked  extends AjaxBehaviorEvent{

	public PointClicked(UIComponent component, Behavior behavior) {
		super(component, behavior);
	}

}
