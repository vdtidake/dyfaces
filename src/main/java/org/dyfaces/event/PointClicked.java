package org.dyfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

import org.dyfaces.data.api.SelectedPointDetails;

@SuppressWarnings("serial")
public class PointClicked  extends AjaxBehaviorEvent{

	public SelectedPointDetails pointDetails;
	
	public PointClicked(UIComponent component, Behavior behavior,SelectedPointDetails pointDetails) {
		super(component, behavior);
		this.pointDetails = pointDetails;
	}

	public SelectedPointDetails getPointDetails() {
		return pointDetails;
	}
}
