package org.dyfaces.event;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.AjaxBehaviorEvent;

import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.SelectedPointDetails;

@SuppressWarnings("serial")
public class AnnotationClicked extends AjaxBehaviorEvent{

	private AnnotationPoint annotationPoint;
	public SelectedPointDetails pointDetails;
	
	public AnnotationClicked(UIComponent component, Behavior behavior,AnnotationPoint annotationPoint,SelectedPointDetails pointDetails) {
		super(component, behavior);
		this.annotationPoint=annotationPoint;
		this.pointDetails=pointDetails;
	}

	public AnnotationPoint getAnnotationPoint() {
		return annotationPoint;
	}

	public SelectedPointDetails getPointDetails() {
		return pointDetails;
	}

	
}
