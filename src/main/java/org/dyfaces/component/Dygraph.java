package org.dyfaces.component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import org.dyfaces.data.api.ConfigOptions;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.HighlightRegion;
import org.dyfaces.data.api.Point;
import org.dyfaces.data.api.SelectedPointDetails;
import org.dyfaces.data.api.SeriesOptions;
import org.dyfaces.event.AnnotationClicked;
import org.dyfaces.event.GraphClicked;
import org.dyfaces.event.GraphZoomed;
import org.dyfaces.event.PointClicked;
import org.dyfaces.utils.DyUtils;

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
	
	public Integer getThreshold() {
		Integer threshold = (Integer) getValue("threshold");
		if(threshold == null){
			return 0;
		}
		return threshold;
	}

	public void setThreshold(Integer value) {
		setValue("threshold", value);
	}
	
	public Boolean isDateAxis() {
		return (Boolean) getValue("dateAxis");
	}
	public void setDateAxis(Boolean value) {
		setValue("dateAxis",value);
    }
	
	public Boolean isLabelsUTC() {
		return (Boolean) getValue("labelsUTC");
	}
	public void setLabelsUTC(Boolean value) {
		setValue("labelsUTC",value);
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
			
			 String style = getStyle();
			 if (style != null) {
				 writer.writeAttribute("style", style, "style");
		     }
			writer.endElement("div");
			
			/**
			 * add hidden input fields for ajax calls
			 */
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
			
			writer.startElement("input", null);
			writer.writeAttribute("type", "hidden", null);
			writer.writeAttribute("id", graphJSVar + "dataValue", null);
			writer.writeAttribute("name", graphJSVar + "dataValue", null);
			//writer.writeAttribute("value",prepareDygraphData(), null);
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
	 
	/**
	 * 
	 * @param dygraph
	 * @return parsed Dygraph data
	 */
	public StringBuilder prepareDygraphData() {
		DataModel dataset= (DataModel) getValue();
		Class datasetXAxisType = dataset.getxAxisType();
		StringBuilder data = new StringBuilder("[");
		int threshold = 0;
		List<DataSeries> dataserieses = dataset.getDataSerieses();
		boolean isDate = false;
		boolean isLong = false;
		
		Map<Number,Map<Integer,Number>> dataMapper = new TreeMap<Number, Map<Integer,Number>>();
		List<AnnotationPoint> annotationPoints = new ArrayList<AnnotationPoint>();
		List<HighlightRegion> highlightRegions = new ArrayList<HighlightRegion>();
		List<String> labels = new ArrayList<String>();
		
		if(dataserieses != null && !dataserieses.isEmpty()){
			for (int seriesCount = 0; seriesCount < dataserieses.size();seriesCount++) {
				DataSeries dataseries = dataserieses.get(seriesCount);
				List<Point> pointsTotal = dataseries.getDataPoints();
				List<Point> points = null;
				if(threshold > 0){
					points = DyUtils.desampleData(pointsTotal,threshold);
				}else{
					points = pointsTotal;
				}
				
				if(points != null){
					if(datasetXAxisType.getName().equals(Date.class.getName())){
						isDate = true;
						Object dataType = points.get(0).getxValue();
						if(dataType instanceof Number){
							isLong = true;
						}
					}
					
					for (Iterator<Point> iterator = points.iterator(); iterator.hasNext();) {
						Point point = iterator.next();
						if(isDate && !isLong){
							Long xval = ((Date)point.getxValue()).getTime();
							if(dataMapper.containsKey(xval)){
								Map<Integer,Number> seriesMap = dataMapper.get(xval);
								seriesMap.put(seriesCount, point.getyValue());
							}else{
								Map<Integer,Number> seriesMap = new HashMap<Integer, Number>();
								seriesMap.put(seriesCount,point.getyValue());
								dataMapper.put(xval, seriesMap);
							}
						}else{
							
							Long xval = (Long) point.getxValue();
							if(dataMapper.containsKey(xval)){
								Map<Integer,Number> seriesMap = dataMapper.get(xval);
								seriesMap.put(seriesCount, point.getyValue());
							}else{
								Map<Integer,Number> seriesMap = new HashMap<Integer, Number>();
								seriesMap.put(seriesCount,point.getyValue());
								dataMapper.put(xval, seriesMap);
							}
						}
					}
					
				}
				
				List<AnnotationPoint> seriesAnnotations= dataseries.getAnnotations();
				if(seriesAnnotations != null && !seriesAnnotations.isEmpty()){
					annotationPoints.addAll(seriesAnnotations);
				}
				List<HighlightRegion> seriesHighlightRegion= dataseries.getHighlightRegions();
				if(seriesHighlightRegion != null && !seriesHighlightRegion.isEmpty()){
					highlightRegions.addAll(seriesHighlightRegion);
				}
				labels.add(dataseries.getSeries());
			}
		}
		if(!annotationPoints.isEmpty()){
			setAnnotations(annotationPoints);
		}
		if(!highlightRegions.isEmpty()){
			setHighlightRegions(highlightRegions);
		}
		if(!labels.isEmpty()){
			labels.add(0,"X");
			setLabels(labels);
		}
		
		ConfigOptions configOptions= dataset.getConfigOptions();
		Boolean labelsUTC = configOptions.getLabelsUTC();
		if(labelsUTC != null){
			setLabelsUTC(labelsUTC);
		}
		
		int seriesCount = dataserieses.size();
		for(Map.Entry<Number, Map<Integer,Number>> entry:dataMapper.entrySet()){
			Number key = entry.getKey();
			Map<Integer,Number> value = entry.getValue();
			data.append("[").append(key).append(",");
			for (int i = 0; i < seriesCount; i++) {
				data.append(value.get(i));
				if(i < seriesCount-1){
					data.append(",");
				}
			}
			data.append("],");
		}
		data.append("]");
		setDateAxis(isDate);
		return data;
	}
	 
}
