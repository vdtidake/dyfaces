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
			Object series = getValue("series");
			if(series != null){
				return series;
			}
			return  getValue("model");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<AnnotationPoint> getAnnotations() {
		return (List<AnnotationPoint>)getValue("annotations");
	}
	
	public void setAnnotations(List<AnnotationPoint> value) {
		setValue("annotations",value);
    }
	

	public List<HighlightRegion> getHghlightRegions() {
		return (List<HighlightRegion>) getValue("highlightRegions");
	}
	
	public void setHighlightRegions(List<HighlightRegion> value) {
		setValue("highlightRegions",value);
    }

	public String getSynchronize() {
		return (String) getValue("synchronize");
	}
	public void setSynchronize(String value) {
		setValue("synchronize",value);
    }
	
	public Boolean isTooltip() {
		return (Boolean) getValue("tooltip");
	}
	public void setTooltip(Boolean value) {
		setValue("tooltip",value);
    }

	/**
	 * 
	 * @param param
	 * @return value of param
	 */
	public Object getValue(String param) {
		return this.getStateHelper().eval(param,null);
	}
	/**
	 * 
	 * @param param
	 * @param value
	 */
	public void setValue(String param,Object value) {
        this.getStateHelper().put(param, value);
        ValueExpression valueExpression = getValueExpression(param);

        if (valueExpression != null) {
            ELContext elContext = this.getFacesContext().getELContext();
            valueExpression.setValue(elContext, value);
        }
    }
	
}
