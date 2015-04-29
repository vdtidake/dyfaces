package org.dyfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

@SuppressWarnings("serial")
public class GraphClicked  extends AjaxBehaviorEvent{

	public GraphClicked(UIComponent component, Behavior behavior) {
		super(component, behavior);
	}

}
