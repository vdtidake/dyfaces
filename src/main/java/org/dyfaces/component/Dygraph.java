package org.dyfaces.component;

import java.io.IOException;
import java.util.ArrayList;
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
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesEvent;

import org.dyfaces.FacesParam;
import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.HighlightRegion;
import org.dyfaces.data.api.SelectedPointDetails;
import org.dyfaces.data.api.SeriesOptions;
import org.dyfaces.data.api.impl.DyDataModel;
import org.dyfaces.event.AnnotationClicked;
import org.dyfaces.event.GraphClicked;
import org.dyfaces.event.GraphZoomed;
import org.dyfaces.event.PointClicked;
import org.dyfaces.exception.InvalidDataInputException;

import com.google.gson.Gson;

@FacesComponent(value=Dygraph.COMPONENT_TYPE)
public class Dygraph extends UIOutput implements ClientBehaviorHolder {
	public static final String COMPONENT_TYPE = "org.dyfaces.component.graph";
	public static final String COMPONENT_FAMILY = "org.dyfaces.component";
	public static final String  DEFAULT_EVENT ="graphClicked";
	public static final String EVENT_POINTCLICKED = "pointClicked";
	public static final String EVENT_ANNOCLICKED = "annotationClicked";
	public static final String EVENT_ANNODBLCLICKED = "annotationDblClicked";
	public static final String EVENT_GRAPHZOOMED = "graphZoomed";
	private static final Collection<String> EVENTS = Collections.unmodifiableCollection(Arrays.asList(DEFAULT_EVENT,EVENT_POINTCLICKED,EVENT_ANNOCLICKED,EVENT_ANNODBLCLICKED,EVENT_GRAPHZOOMED));
	private static final Gson gson = new Gson();

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
		byte dataInputs = 0;
		Object dyDataModel = null;
		Object value = getValue();
		if (value != null) {
			dyDataModel = value;
			dataInputs++;
		}
		Object series = getValue("series");
		if (series != null) {
			dyDataModel = series;
			dataInputs++;
		}
		Object model = getValue("model");
		if (model != null) {
			dyDataModel = model;
			dataInputs++;
		}

		if (dataInputs != 1 || dyDataModel == null) {
			throw new InvalidDataInputException("no or more than one data inputs found for graph");
		}
		return dyDataModel;
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
	/*public String getSelectedPoint() {
		return (String) getValue("selectedPoint");
	}
	public void setSelectedPoint(String value) {
		setValue("selectedPoint",value);
    }
	public String getClosestPoints() {
		return (String) getValue("closestPoints");
	}
	public void setClosestPoints(String value) {
		setValue("clickCoordinate",value);
    }*/
	public String getVar() {
		return (String) getValue("var");
	}
	public void setVar(String value) {
		setValue("var",value);
    }

	public String getTitle() {
		return (String) getValue("title");
	}
	public void setTitle(String value) {
		setValue("title",value);
    }
	
	public String getXlabel() {
		return (String) getValue("xlabel");
	}
	public void setXlabel(String value) {
		setValue("xlabel",value);
    }
	
	public String getYlabel() {
		return (String) getValue("ylabel");
	}
	public void setYlabel(String value) {
		setValue("ylabel",value);
    }
	
	public List<String> getLabels() {
		return (List<String>) getValue("labels");
	}
	
	public void setLabels(List<String> value) {
		setValue("labels",value);
    }
	
	public String getStyle() {
        return (String) getValue("style");
    }

    public void setStyle(String value) {
    	setValue("style",value);
    }
    
    public Integer getHeight() {
    	Integer h = (Integer) getValue("height");
        if (h == null) {
            return 320;
        }
        return h;
    }

    public void setHeight(Integer value) {
        setValue("height", value);
    }

    public Integer getWidth() {
    	Integer w = (Integer) getValue("width");
        if (w == null) {
            return 680;
        }
        return w;
    }

    public void setWidth(Integer value) {
    	setValue("width", value);
    }
    
    public SeriesOptions getSeriesOptions() {
        return (SeriesOptions) getValue("seriesOptions");
    }

    public void setSeriesOptions(SeriesOptions value) {
    	setValue("seriesOptions",value);
    }
    
	public DataModel getModel() {
		DataModel dataModel = (DataModel) getValue("model");
		
		if(dataModel.getGraphTitle() != null){
			setTitle(dataModel.getGraphTitle());
		}
		if(dataModel.getxAxisLable() != null){
			setXlabel(dataModel.getxAxisLable());
		}
		if(dataModel.getyAxisLable() != null){
			setYlabel(dataModel.getyAxisLable());
		}
		
		int seriesCount = dataModel.getDataSeries().size();
		
		List<AnnotationPoint> annotationPoints = new ArrayList<AnnotationPoint>();
		List<HighlightRegion> highlightRegions = new ArrayList<HighlightRegion>();
				
		List<String> seriesLabels = new ArrayList<String>(seriesCount);
		seriesLabels.add("");
		
		for (DataSeries tmps : dataModel.getDataSeries()) {
			String name = tmps.getSeries();
			if(name != null && !name.isEmpty()){
				seriesLabels.add(name);
			}
			
			List<AnnotationPoint> annotations = tmps.getAnnotations();
			if(annotations != null && !annotations.isEmpty()){
				annotationPoints.addAll(annotations);
			}
			List<HighlightRegion> highlight = tmps.getHighlightRegions();
			
			if(highlight != null && !highlight.isEmpty()){
				highlightRegions.addAll(highlight);
			}
			
			SeriesOptions seriesOptions = tmps.getSeriesOptions();
			if(seriesOptions != null){
				setSeriesOptions(seriesOptions);
			}
		}
		if(seriesLabels.size() > 1){
			setLabels(seriesLabels);
		}
		if(seriesCount != seriesLabels.size()-1){
			//TODO throw exception
		}
		
		/**
		 * annotations and highlight points
		 */
		if(annotationPoints != null && !annotationPoints.isEmpty()){
			setAnnotations(annotationPoints);
		}
		
		if(highlightRegions != null && !highlightRegions.isEmpty()){
			setHighlightRegions(highlightRegions);
		}
		
		return dataModel;
	}
	
	public void setModel(DataModel value) {
		setValue("model",value);
    }
	
	public DataSeries getSeries() {
		DataSeries dataseries = (DataSeries) getValue("series");
		
		/**
		 * series lables
		 */
		List<String> seriesLabels = new ArrayList<String>(2);
		seriesLabels.add("");
		String name = dataseries.getSeries();
		if (name != null && !name.isEmpty()) {
			seriesLabels.add(name);
		}
		if(seriesLabels.size() > 1){
			setLabels(seriesLabels);
		}
		/**
		 * annotations and highlight points
		 */
		List<AnnotationPoint> annotationPoints = dataseries.getAnnotations();
		if(annotationPoints != null && !annotationPoints.isEmpty()){
			setAnnotations(annotationPoints);
		}
		List<HighlightRegion> highlightRegions = dataseries.getHighlightRegions();
		if(highlightRegions != null && !highlightRegions.isEmpty()){
			setHighlightRegions(highlightRegions);
		}
		return dataseries;
	}
	
	public void setSeries(DataSeries value) {
		setValue("series",value);
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
		 	if(getVar() == null || getVar().isEmpty()){
		 		setVar(graphJSVar);
		 	}
		 	/*
			 * create Dygraph div element
			 */
			writer.startElement("div", this);
			writer.writeAttribute("id", graphJSVar, null);
			/*Map<String,List<ClientBehavior>> behaviors = this.getClientBehaviors();
			for (String eventName : behaviors.keySet()) {
			    if (this.getEventNames().contains(eventName)) {
			    	ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context, this, DEFAULT_EVENT, graphJSVar, null);
			    	String click = behaviors.get(DEFAULT_EVENT).get(0).getScript(behaviorContext);
			    	writer.writeAttribute("onclick", click, null);
			    }
			}*/
			 String style = getStyle();
			 if (style != null) {
				 writer.writeAttribute("style", style, "style");
		     }
			writer.endElement("div");
			
			writer.startElement("input", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", graphJSVar + "selectedPoint", null);
			writer.writeAttribute("name", graphJSVar + "selectedPoint", null);
			writer.endElement("input");
			
			writer.startElement("input", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", graphJSVar + "closestPoints", null);
			writer.writeAttribute("name", graphJSVar + "closestPoints", null);
			writer.endElement("input");
			
			writer.startElement("input", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", graphJSVar + "annotationPoint", null);
			writer.writeAttribute("name", graphJSVar + "annotationPoint", null);
			writer.endElement("input");
			
			writer.startElement("input", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", graphJSVar + "dateWindow", null);
			writer.writeAttribute("name", graphJSVar + "dateWindow", null);
			writer.endElement("input");
			
			writer.startElement("input", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", graphJSVar + "valueRange", null);
			writer.writeAttribute("name", graphJSVar + "valueRange", null);
			writer.endElement("input");
			
	 }
	 @Override
	 public void decode(FacesContext context) {
		 
		Map<String, List<ClientBehavior>> behaviors = this.getClientBehaviors();
		if (behaviors.isEmpty()) {
			return;
		}

		ExternalContext external = context.getExternalContext();
		Map<String, String> params = external.getRequestParameterMap();
		String behaviorEvent = params.get(FacesParam.EVENT.getName());
		if (behaviorEvent != null) {
			List<ClientBehavior> behaviorsForEvent = behaviors
					.get(behaviorEvent);

			if (behaviors.size() > 0) {
				String behaviorSource = params.get(FacesParam.SOURCE.getName());
				String clientId = getClientId(context);
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


	@Override
	public void queueEvent(FacesEvent event) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Map<String, String> params = context.getExternalContext().getRequestParameterMap();
		final String clientId = getClientId(context);
		final BehaviorEvent behaviorEvent = (BehaviorEvent) event;

		final String eventName = params.get(FacesParam.EVENT.getName());

		if (eventName.equals(Dygraph.DEFAULT_EVENT)){
			final String pointDetails = params.get(clientId + "closestPoints");
			SelectedPointDetails[] points= gson.fromJson(pointDetails, SelectedPointDetails[].class);
			List<SelectedPointDetails> closestPoints = Arrays.asList(points);
			GraphClicked graphClicked = new GraphClicked(this, behaviorEvent.getBehavior(),closestPoints);
			super.queueEvent(graphClicked);
		}else if(eventName.equals(Dygraph.EVENT_POINTCLICKED)){
			final String pointDetails = params.get(clientId + "selectedPoint");
			PointClicked pointClicked = new PointClicked(this, behaviorEvent.getBehavior(),gson.fromJson(pointDetails, SelectedPointDetails.class));
			super.queueEvent(pointClicked);
		}else if(eventName.equals(Dygraph.EVENT_ANNOCLICKED) || eventName.equals(Dygraph.EVENT_ANNODBLCLICKED)){
			final String annopointDetails = params.get(clientId + "annotationPoint");
			final String pointDetails = params.get(clientId + "selectedPoint");
			AnnotationClicked annotationClicked = new AnnotationClicked(this, behaviorEvent.getBehavior(), gson.fromJson(annopointDetails, AnnotationPoint.class),gson.fromJson(pointDetails, SelectedPointDetails.class));
			super.queueEvent(annotationClicked);
		}else if(eventName.equals(Dygraph.EVENT_GRAPHZOOMED)){
			final String dateWindow = params.get(clientId + "dateWindow");
			final String valueRange = params.get(clientId + "valueRange");
			Number[] dateWindowArr= gson.fromJson(dateWindow, Number[].class);
			Number[] valueRangeArr= gson.fromJson(valueRange, Number[].class);
			GraphZoomed graphZoomed = new GraphZoomed(this, behaviorEvent.getBehavior(), dateWindowArr,valueRangeArr);
			super.queueEvent(graphZoomed);
		}else{
			 super.queueEvent(event);
		}
	}
	 
	
	 
}
