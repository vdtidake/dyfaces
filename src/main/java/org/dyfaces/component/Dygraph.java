package org.dyfaces.component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.component.FacesComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.HighlightRegion;

@FacesComponent(value=Dygraph.COMPONENT_TYPE)
public class Dygraph extends UIOutput implements ClientBehaviorHolder {
	public static final String COMPONENT_TYPE = "org.dyfaces.component.graph";
	public static final String COMPONENT_FAMILY = "org.dyfaces.component";
	private static final String  DEFAULT_EVENT ="click";
	private static final Collection<String> EVENTS = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_EVENT));

	@Override
	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	
    @Override
    public Collection<String> getEventNames() {
        return EVENTS;
    }

    @Override
    public String getDefaultEventName() {
        return DEFAULT_EVENT;
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
	
	 @Override
	 public void encodeBegin(FacesContext context) throws IOException {
		 	ResponseWriter writer = context.getResponseWriter();
		 	String graphJSVar = this.getClientId(context).replace(":", "_dy");
		 	/*
			 * create Dygraph div element
			 */
			writer.startElement("div", this);
			writer.writeAttribute("id", graphJSVar, null);
			Map<String,List<ClientBehavior>> behaviors = this.getClientBehaviors();
			for (String eventName : behaviors.keySet()) {
			    if (this.getEventNames().contains(eventName)) {
			    	ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context, this, "click", graphJSVar, null);
			    	String click = behaviors.get("click").get(0).getScript(behaviorContext);
			    	writer.writeAttribute("onclick", click, null);
			    }
			}
			writer.endElement("div");
	 }
	 @Override
	 public void decode(FacesContext context) {
		 
		Map<String, List<ClientBehavior>> behaviors = this.getClientBehaviors();
		if (behaviors.isEmpty()) {
			return;
		}

		ExternalContext external = context.getExternalContext();
		Map<String, String> params = external.getRequestParameterMap();
		String behaviorEvent = params.get("javax.faces.behavior.event");
		if (behaviorEvent != null) {
			List<ClientBehavior> behaviorsForEvent = behaviors
					.get(behaviorEvent);

			if (behaviors.size() > 0) {
				String behaviorSource = params.get("javax.faces.source");
				String clientId = this.getClientId(context).replace(":", "_dy");
				if (behaviorSource != null && behaviorSource.equals(clientId)) {
					for (ClientBehavior behavior : behaviorsForEvent) {
						behavior.decode(context, this);
					}
				}
			}
		}
	 }


	@Override
	public void encodeChildren(FacesContext context) throws IOException {
		super.encodeChildren(context);
	}
	 
	 
}
