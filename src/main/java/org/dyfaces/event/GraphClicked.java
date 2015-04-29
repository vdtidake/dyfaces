package org.dyfaces.event;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

import org.dyfaces.data.api.SelectedPointDetails;

@SuppressWarnings("serial")
public class GraphClicked  extends AjaxBehaviorEvent{

	public List<SelectedPointDetails> closestPoints;
	
	public GraphClicked(UIComponent component, Behavior behavior,List<SelectedPointDetails> closestPoints) {
		super(component, behavior);
		this.closestPoints = closestPoints;
	}

	public List<SelectedPointDetails> getClosestPoints() {
		return closestPoints;
	}

}
