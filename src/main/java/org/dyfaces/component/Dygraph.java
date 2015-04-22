package org.dyfaces.component;

import java.util.List;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;

import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.HighlightRegion;

@FacesComponent(value=Dygraph.COMPONENT_TYPE)
public class Dygraph extends UIOutput implements ClientBehaviorHolder {
	public static final String COMPONENT_TYPE = "org.dyfaces.component.graph";
	public static final String COMPONENT_FAMILY = "org.dyfaces.component";
	
	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}


	/**
	 * 
	 * @return dataset with either value or model
	 */
	public Object getDyDataModel() {
		try {
			Object value = getValue();
			if(value != null){
				return value;
			}
			Object series = this.getStateHelper().eval("series",null);
			if(series != null){
				return series;
			}
			return this.getStateHelper().eval("model",null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/*public void setDyDataModel(Object value) {
        this.getStateHelper().put("model", value);
        ValueExpression f = getValueExpression("model");

        if (f != null) {

            ELContext elContext = this.getFacesContext().getELContext();

            f.setValue(elContext, value);
        }
    }*/


	public List<AnnotationPoint> getAnnotations() {
		return (List<AnnotationPoint>) this.getStateHelper().eval("annotations",null);
	}
	
	public void setAnnotations(List<AnnotationPoint> value) {
        this.getStateHelper().put("annotations", value);
        ValueExpression valueExpression = getValueExpression("annotations");

        if (valueExpression != null) {
            ELContext elContext = this.getFacesContext().getELContext();
            valueExpression.setValue(elContext, value);
        }
    }
	

	public List<HighlightRegion> getHghlightRegions() {
		return (List<HighlightRegion>) this.getStateHelper().eval("highlightRegions",null);
	}
	
	public void setHighlightRegions(List<HighlightRegion> value) {
        this.getStateHelper().put("highlightRegions", value);
        ValueExpression valueExpression = getValueExpression("highlightRegions");

        if (valueExpression != null) {
            ELContext elContext = this.getFacesContext().getELContext();
            valueExpression.setValue(elContext, value);
        }
    }

	
}
