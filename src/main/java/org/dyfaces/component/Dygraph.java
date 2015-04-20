package org.dyfaces.component;

import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehaviorHolder;

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
}
