package org.dyfaces.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.dyfaces.DyAttributes;
import org.dyfaces.DyCallbacks;
import org.dyfaces.DyConstants.Callback;
import org.dyfaces.Version;
import org.dyfaces.component.Dygraph;
import org.dyfaces.data.api.AnnotationConfigurations;
import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.GridOptions;
import org.dyfaces.data.api.GridOptions.Axes;
import org.dyfaces.data.api.GridOptions.PerAxis;
import org.dyfaces.data.api.HighlightRegion;
import org.dyfaces.data.api.Point;
import org.dyfaces.data.api.SeriesColorOptions;
import org.dyfaces.data.api.impl.DyDataModel;
import org.dyfaces.utils.DyUtils;
import org.dyfaces.utils.DyfacesUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesRenderer(componentFamily = Dygraph.COMPONENT_FAMILY, rendererType = DygraphRenderer.RENDERER_TYPE)
@ResourceDependencies({
		@ResourceDependency(name = "jsf.js", target = "head", library = "javax.faces"),
		@ResourceDependency(library = "webjars", name = Version.DYGRAPH_RESOURCES
				+ "/dygraph-combined.js", target = "head"),
		@ResourceDependency(library = "webjars", name = Version.DYGRAPH_RESOURCES
				+ "/dygraph-interaction-model.js", target = "head"),
		@ResourceDependency(library = "webjars", name = Version.UNDERSCORE_RESOURCES
				+ "/underscore-min.js", target = "head"), 
		@ResourceDependency(library = "webjars", name = Version.MOMENT_RESOURCES
				+ "/moment.js", target = "head"),
		@ResourceDependency(library = "dyfaces", name = "js/dyfaces.js")})
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class DygraphRenderer extends Renderer implements ComponentSystemEventListener{
	public static final String RENDERER_TYPE = "org.dyfaces.component.graph.renderer";
	private static final Gson gson = new Gson();
	private static final GsonBuilder builder = new GsonBuilder();
	private static final Gson gsonExopse = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

	@Override
	public void decode(FacesContext context, UIComponent component) {
		super.decode(context, component);
	}

	@Override
	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		super.encodeBegin(context, component);
	}

	@Override
	public void encodeEnd(FacesContext context, UIComponent component)
			throws IOException {
		/* super.encodeEnd(context, component); */
		dygraphMarkup(context, component);
	}

	private void dygraphMarkup(FacesContext context, UIComponent component) throws IOException {
		Dygraph dygraph = (Dygraph) component;
		ResponseWriter writer = context.getResponseWriter();
		String graphJSVar = dygraph.getClientId(context).replace(":", "_dy");
				
		/*
		 * start Dygraph creation inside <script> tag
		 */
		writer.startElement("script", dygraph);
		writer.writeAttribute("type", "text/javascript", null);
		String dyvar = dygraph.getVar();
		String dyjsVar = "";
		if(dyvar != null && !dyvar.isEmpty()){
			dyjsVar = "var "+dyvar+"=";
		}
		/*
		 * assign Dygraph object to javascript variable defined with 'var' attribute
		 */
		StringBuilder graphBuilder = new StringBuilder(dyjsVar).append("(function () {var "+graphJSVar+"= new Dygraph(");
		graphBuilder.append("document.getElementById(\"").append(graphJSVar).append("\"),");
		/*
		 * get Dygraph data defined with either value or model attribute
		 */
		StringBuilder data = getDyGraphData(dygraph);
		graphBuilder.append(data);
		/*
		 * get Dygraph attributes
		 */
		String dygraphAttributes = getDygraphAttribures(dygraph);
		
		/*
		 * append graph attributes to Dygraph
		 */
		graphBuilder.append(dygraphAttributes);
		graphBuilder.append(");");
		/*
		 * write all Dygraph script to response
		 */
		writer.write(graphBuilder.toString());
		
		/*
		 *Add annotations if exists 
		 */
		List<AnnotationPoint> annotations =  dygraph.getAnnotations();
		if(annotations != null && !annotations.isEmpty()){
			addDyAnnotations(context,graphJSVar,dygraph);
		}
		/*
		 *Dygraph callback options 
		 */
		bindDyCallbacks(context,graphJSVar,dygraph);
		
		String  sync= dygraph.getSynchronize();
		if(sync != null && !sync.isEmpty()){
			syncDygraphs(sync,context,graphJSVar);
		}
		writer.write("return "+graphJSVar+"})();");
		writer.endElement("script");
		
		/**
		 * roll period textbox binding
		 */
		writer.startElement("script", dygraph);
		writer.writeAttribute("type", "text/javascript", null);
		
		writer.write("$('#"+graphJSVar+" :input').bind('keyup',function(){dyRollPeriodChangeFn("+graphJSVar+",this.value);});");
		
		writer.endElement("script");
		
	}

	private void bindDyCallbacks(FacesContext context,String graphJSVar,Dygraph dygraph) throws IOException {
		String callbacks = getDygraphCallbacks(dygraph);
		ResponseWriter writer = context.getResponseWriter();
		StringBuilder graphBuilder = new StringBuilder();

		Map<String,Object> callBackMap = gson.fromJson(callbacks, Map.class);
		
		List<HighlightRegion> highlightRegions = dygraph.getHghlightRegions();
		if(highlightRegions != null && !highlightRegions.isEmpty()){
			String hData = gson.toJson(highlightRegions).replaceAll("\"", "'");
			graphBuilder.append("var hd=").append(hData).append(";");
			 String dyunderlayCallback = "''";
			 if(callBackMap.containsKey(Callback.UnderlayCallback)){
				 dyunderlayCallback = "'"+callBackMap.get(Callback.UnderlayCallback)+"'";
			 }
			callBackMap.put(Callback.UnderlayCallback, "dyHighlightRegionFn(hd,"+dyunderlayCallback+")");
		}
		
		Boolean graphClickedBehavior = dygraph.getClientBehaviors().containsKey(Dygraph.DEFAULT_EVENT);
		if(graphClickedBehavior){
			String dyclickCallback = "''";
			 if(callBackMap.containsKey(Callback.ClickCallback)){
				 dyclickCallback = "'"+callBackMap.get(Callback.ClickCallback)+"'";
			 }
		    String click  = getScript(context, dygraph, Dygraph.DEFAULT_EVENT, graphJSVar);
		    callBackMap.put(Callback.ClickCallback, "dyClickCallbackFn("+dyclickCallback+",\""+click+"\",'"+graphJSVar+"')");
		}
		
		Boolean pointClickedBehavior = dygraph.getClientBehaviors().containsKey(Dygraph.EVENT_POINTCLICKED);
		if(pointClickedBehavior){
			String dyclickCallback = "''";
			 if(callBackMap.containsKey(Callback.PointClickCallback)){
				 dyclickCallback = "'"+callBackMap.get(Callback.PointClickCallback)+"'";
			 }
		    String pointclick = getScript(context, dygraph, Dygraph.EVENT_POINTCLICKED, graphJSVar);
				
		    callBackMap.put(Callback.PointClickCallback, "dyPointClickCallbackFn("+dyclickCallback+",\""+pointclick+"\",'"+graphJSVar+"')");
		}
		
		Boolean zoomBehavior = dygraph.getClientBehaviors().containsKey(Dygraph.EVENT_GRAPHZOOMED);
		if(zoomBehavior){
			String dyzoomCallback = "''";
			 if(callBackMap.containsKey(Callback.ZoomCallback)){
				 dyzoomCallback = "'"+callBackMap.get(Callback.ZoomCallback)+"'";
			 }
		    String zoom = getScript(context, dygraph, Dygraph.EVENT_GRAPHZOOMED, graphJSVar);
				
		    callBackMap.put(Callback.ZoomCallback, "dyZoomCallbackFn("+dyzoomCallback+",\""+zoom+"\",'"+graphJSVar+"')");
		}
		if(callBackMap == null){
			callBackMap = new HashMap<String, Object>();
		}
		
		Map<String,Object> annoConfig= bindAnnotationConfiurations(context, graphJSVar, dygraph); 
		
		if(annoConfig != null && !annoConfig.isEmpty()){
			callBackMap.putAll(annoConfig);
		}
		
		Map<String,Object> seriesColorOptions= bindSeriesColorOptions(context, graphJSVar, dygraph); 
		
		if(seriesColorOptions != null && !seriesColorOptions.isEmpty()){
			callBackMap.putAll(seriesColorOptions);
		}
		
		Map<String,Object> gridOptions= bindGridOptions(context, graphJSVar, dygraph); 
		
		if(gridOptions != null && !gridOptions.isEmpty()){
			callBackMap.putAll(gridOptions);
		}
		
		Map<String,Object> seriesOptions= bindSeriesOptions(context, graphJSVar, dygraph); 
		
		if(seriesOptions != null && !seriesOptions.isEmpty()){
			callBackMap.putAll(seriesOptions);
		}
		
		if(!callBackMap.isEmpty()){
			graphBuilder.append(graphJSVar).append(".updateOptions(").append(gson.toJsonTree(callBackMap)).append(");");
			String updateOptions = graphBuilder.toString().replaceAll("\"", "");
			writer.write(updateOptions.replaceAll("\\\\", "\""));
		}else if(annoConfig != null && !annoConfig.isEmpty()){
			graphBuilder.append(graphJSVar).append(".updateOptions(").append(gson.toJsonTree(annoConfig)).append(");");
			String updateOptions = graphBuilder.toString().replaceAll("\"", "");
			writer.write(updateOptions.replaceAll("\\\\", "\""));
		}
	}
	
	private Map<String, Object> bindSeriesOptions(FacesContext context,
			String graphJSVar, Dygraph dygraph) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Object dataModel= dygraph.getDyDataModel();
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DyDataModel) dataModel;
			if(dyDataModel != null){
				List<DataSeries> dataSerieses= dyDataModel.getDataSeries();
				if(dataSerieses != null){
					for (DataSeries ds : dataSerieses) {
						map.put("'"+ds.getSeries()+"'", gson.toJson(ds.getSeriesOptions()));
					}
				}
			}
		}
		return map;
	}

	private Map<String, Object> bindGridOptions(FacesContext context,
			String graphJSVar, Dygraph dygraph) {
		Map<String, Object> map = new HashMap<String, Object>();
		GridOptions gridOptions = null;
		Object dataModel= dygraph.getDyDataModel();
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DyDataModel) dataModel;
			if(dyDataModel != null){
				//TODO Handle for X, Y and Y2
				gridOptions = dyDataModel.getGridOptions();
				setGridOptions(gridOptions, map);
			}
		}else if(dataModel instanceof DataSeries){
			DataSeries dataseries = (DataSeries) dataModel;
			if(dataseries != null){
				gridOptions = dataseries.getGridOptions();
				setGridOptions(gridOptions, map);
			}
		}
		return map;
	}
	
	private void setGridOptions(GridOptions gridOptions, Map<String, Object> map){
		if(gridOptions == null){
			return;
		}
		map.put("drawGrid", gridOptions.getDrawGrid());
		if(gridOptions.getDrawGrid()){
			if(!gridOptions.getDrawXGrid()){
				map.put("drawXGrid", gridOptions.getDrawXGrid());
			}
			if(!gridOptions.getDrawYGrid()){
				map.put("drawYGrid", gridOptions.getDrawYGrid());
			}
		}
		map.put("gridLineColor", gridOptions.getGridLineColor());
		/*
		 * check per axes options
		 */
		List<PerAxis> perAxesList = gridOptions.getAxisGridOptions();
		if(perAxesList != null){
			Map<Axes,PerAxis> tmp = new HashMap<Axes, PerAxis>(3);
			for (PerAxis perAxis : perAxesList) {
				tmp.put(perAxis.getAxis(), perAxis);
			}
			if(!tmp.isEmpty()){
				map.put("axes",gson.toJson(tmp).replaceAll("\"", "'"));
			}
		}
		
	}

	private Map<String, Object> bindAnnotationConfiurations(FacesContext context,String graphJSVar,Dygraph dygraph) {
		Map<String, Object> map = new HashMap<String, Object>();
		AnnotationConfigurations configurations = null;
		Object dataModel= dygraph.getDyDataModel();
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DyDataModel) dataModel;
			if(dyDataModel != null){
				configurations = dyDataModel.getAnnotationConfigurations();
			}
		}else if(dataModel instanceof DataSeries){
			DataSeries dataseries = (DataSeries) dataModel;
			if(dataseries != null){
				configurations = dataseries.getAnnotationConfigurations();
			}
		}
		if(configurations != null){
			String clickHandler = configurations.getClickHandler();
			if(clickHandler != null && !clickHandler.isEmpty()){
			    String annoClick = getScript(context, dygraph, Dygraph.EVENT_ANNOCLICKED, graphJSVar);
				
				map.put("annotationClickHandler", "dyAnnotationClickHandlerFn('"+clickHandler+"',\""+annoClick+"\",'"+graphJSVar+"')");
			}
			String dblClickHandler = configurations.getDblClickHandler();
			if(dblClickHandler != null && !dblClickHandler.isEmpty()){
			    String dblclick = getScript(context, dygraph, Dygraph.EVENT_ANNODBLCLICKED, graphJSVar);
				
				map.put("annotationDblClickHandler", "dyAnnotationDblClickHandlerFn('"+clickHandler+"',\""+dblclick+"\",'"+graphJSVar+"')");
			}
			String mouseOutHandler = configurations.getMouseOutHandler();
			if(mouseOutHandler != null && !mouseOutHandler.isEmpty()){
				map.put("annotationMouseOutHandler", "dyAnnotationMouseOutHandlerFn('"+clickHandler+"')");
			}
			String mouseOverHandler = configurations.getMouseOverHandler();
			if(mouseOverHandler != null && !mouseOverHandler.isEmpty()){
				map.put("annotationMouseOverHandler", "dyannotationMouseOverHandlerFn('"+clickHandler+"')");
			}
			map.put("displayAnnotations", configurations.getShowAnnotations());
		}
		
		return map;
	}
	
	private Map<String, Object> bindSeriesColorOptions(FacesContext context,
			String graphJSVar, Dygraph dygraph) {
		Map<String, Object> map = new HashMap<String, Object>();
		SeriesColorOptions colorOptions = null;
		Object dataModel= dygraph.getDyDataModel();
		
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DyDataModel) dataModel;
			if(dyDataModel != null){
				colorOptions = dyDataModel.getSeriesColorOptions();
			}
		}else if(dataModel instanceof DataSeries){
			DataSeries dataseries = (DataSeries) dataModel;
			if(dataseries != null){
				colorOptions = dataseries.getSeriesColorOptions();
			}
		}
		if(colorOptions != null){
			String color = colorOptions.getColor();
			if(color != null && !color.isEmpty()){
				map.put("color", "'"+color+"'");
			}
			Float colorValue = colorOptions.getColorValue();
			map.put("colorValue", colorValue);
			Float fillAlpha = colorOptions.getFillAlpha();
			map.put("fillAlpha", fillAlpha);
			Float colorSaturation = colorOptions.getColorSaturation();
			map.put("colorSaturation ", colorSaturation);
		}
		return map;
	}

	private void addDyAnnotations(FacesContext context, String graphJSVar,
			Dygraph dygraph) throws IOException {
		
		ResponseWriter writer = context.getResponseWriter();
		
		List<AnnotationPoint> annotations =  dygraph.getAnnotations();
		
		StringBuilder graphBuilder = new StringBuilder("var annotations = ");
		graphBuilder.append(gson.toJson(annotations)).append(";");
		graphBuilder.append(graphJSVar).append(".setAnnotations(annotations);");
		writer.write(graphBuilder.toString());
		
	}

	/**
	 * 
	 * @param dygraph
	 * @return parsed Dygraph data
	 */
	private StringBuilder getDyGraphData(Dygraph dygraph) {
		Object dataModel= dygraph.getDyDataModel();
		Map<Object,List<Number>> seriesMap = new HashMap<Object, List<Number>>();
		StringBuilder data = new StringBuilder("[");
		
		/**
		 * datamodel
		 */
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DataModel) dataModel;
			
			if(dyDataModel != null){
				for (DataSeries series : dyDataModel.getDataSeries()) {
					List<Point> points = series.getDataPoints();
					for (Point point : points) {
						if(seriesMap.containsKey(point.getxValue())){
							List<Number> tmp = seriesMap.get(point.getxValue());
							tmp.add(point.getyValue());
						}else{
							List<Number> tmp = new ArrayList<Number>();
							tmp.add(point.getyValue());
							seriesMap.put(point.getxValue(), tmp);
						}
					}
				}
			}
			
			List<List<Object>> mergedSerieses = new ArrayList<List<Object>>();
			if(!seriesMap.isEmpty()){
				for(Map.Entry<Object, List<Number>> entry : seriesMap.entrySet()){
					Object key = entry.getKey();
					List<Number> value = entry.getValue();
					List<Object> dydata= new ArrayList<Object>();
					dydata.add(0, key);
					for (int i = 1; i <= value.size(); i++) {
						dydata.add(i, value.get(i-1));					
					}
					mergedSerieses.add(dydata);
				}
			}
			Collections.sort(mergedSerieses, new Comparator<List<Object>>() {

				@Override
				public int compare(List<Object> d1, List<Object> d2) {
					Object index1 = d1.get(0);
					Object index2 = d2.get(0);
			 
					if(index1 instanceof Number && index2 instanceof Number){
						Number no1 = (Number) index1;
						Number no2 = (Number) index2;
						
						if (no1.doubleValue() > no2.doubleValue()) {
							return 1;
						} else if (no1.doubleValue() < no2.doubleValue()) {
							return -1;
						} else {
							return 0;
						}
					}else if(index1 instanceof Date && index2 instanceof Date){
						Date no1 = (Date) index1;
						Date no2 = (Date) index2;
						if (no1.before(no2)) {
							return 1;
						} else if (no1.after(no2)) {
							return -1;
						} else {
							return 0;
						}
					}
					return 0;
				}
			});
			for (List<Object> list : mergedSerieses) {
				data.append(list).append(",");
			}
		}else if(dataModel instanceof List){
			/*
			 * Single Dygraph series with a List<DyPoint>
			 */
			try{
				List<Point> points = (List<Point>) dataModel;
				if(points != null && !points.isEmpty()){
					/*
					 * sorted on X axis ascending
					 */
					Collections.sort(points);
					for (Point point : points) {
						if(point.getxValue() instanceof Date){
							data.append(Arrays.asList(DyUtils.getJSDyDate(point.getxValue()),point.getyValue())).append(",");
						}else{
							data.append(Arrays.asList(point.getxValue(),point.getyValue())).append(",");
						}
						
					}
				}
			}catch(ClassCastException castException){
				List<DataSeries> dataSerieses = (List<DataSeries>) dataModel;
				if(dataSerieses != null && !dataSerieses.isEmpty()){
					//TODO 
				}
			}
			
		}else if(dataModel instanceof DataSeries){
			DataSeries dataseries = (DataSeries) dataModel;
			if(dataseries != null){
				List<Point> points = dataseries.getDataPoints();
				if(points != null){
					Collections.sort(points);
					for (Point point : points) {
						if(point.getxValue() instanceof Date){
							data.append(Arrays.asList(DyUtils.getJSDyDate(point.getxValue()),point.getyValue())).append(",");
						}else{
							data.append(Arrays.asList(point.getxValue(),point.getyValue())).append(",");
						}
						
					}
					
				}
			}
			
		}
	
		data.append("],");
		return data;
	}

	/**
	 * 
	 * @param dygraph
	 * @return Json string of Dygraph attributes
	 */
	private String getDygraphAttribures(Dygraph dygraph) {
		Map<String,Object> attr = dygraph.getAttributes();
		DyAttributes attributes = builder.create().fromJson(gson.toJson(attr), DyAttributes.class);
		
		/*
		 * attributes from datamodel and dataseries
		 */
		if(attributes.getXlabel() == null){
			String xl = dygraph.getXlabel();
			if(xl != null && !xl.isEmpty())
				attributes.setXlabel(xl);
		}
		if(attributes.getYlabel() == null){
			String yl = dygraph.getYlabel();
			if(yl != null && !yl.isEmpty())
				attributes.setYlabel(yl);
		}
		if(attributes.getLabels() == null){
			List<String> lbls = dygraph.getLabels();
			if(lbls != null && !lbls.isEmpty())
				attributes.setLabels(lbls);
		}
		if(attributes.getTitle() == null){
			String ttl = dygraph.getTitle();
			if(ttl != null && !ttl.isEmpty())
				attributes.setTitle(ttl);
		}
		attributes.setWidth(dygraph.getWidth());
		attributes.setHeight(dygraph.getHeight());
		return gson.toJson(attributes);
	}
	
	/**
	 * 
	 * @param dygraph
	 * @return Json string of Dygraph callback function name
	 */
	private String getDygraphCallbacks(Dygraph dygraph) {
		Map<String,Object> attr = dygraph.getAttributes();
		DyCallbacks attributes = builder.create().fromJson(gson.toJson(attr), DyCallbacks.class);
		return gson.toJson(attributes).replaceAll("\"", "");
	}
	

	private void syncDygraphs(String sync,FacesContext context,String dygraph) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		sync=sync+","+dygraph;
		String[] graphs = sync.split(",");
		StringBuilder graphBuilder = new StringBuilder("var sgs = ");
		graphBuilder.append(Arrays.toString(graphs)).append(";");
		graphBuilder.append("var sync = Dygraph.synchronize(sgs,{ selection: false, zoom: true})").append(";");
		writer.write(graphBuilder.toString());
	}
	
	private void initTooltip(FacesContext context,String dygraph) throws IOException{
		ResponseWriter writer = context.getResponseWriter();
		StringBuilder graphBuilder = new StringBuilder("var tooltip = $('#").append(dygraph).append("').qtip({id: '").append(dygraph).append("',prerender: true,content: ' ',position: {target: 'mouse',viewport: $('#").append(dygraph).append("'),adjust: { x: 5, y: 5 } },    show: false,});");
		writer.write(graphBuilder.toString());
	}

	@Override
	public void processEvent(ComponentSystemEvent event)
			throws AbortProcessingException {
		/*
		 * dynamic resource loading
		 */
		if (event instanceof PostAddToViewEvent) {
            final FacesContext context = FacesContext.getCurrentInstance();
            addScript(context,event);
        }
	}

	private void addScript(FacesContext context,ComponentSystemEvent event) {
		/*
		 * check if jquery is required
		 */
		Dygraph dygraph = (Dygraph) event.getSource();
		
		UIComponent head = context.getViewRoot().getFacet("javax_faces_location_HEAD");
        if (head == null) {
            return;
        }
        boolean jqueryAdded = true;
        for (UIComponent c : head.getChildren()) {
            if (c.getAttributes().get("name").toString().endsWith("jquery.js")) {
            	jqueryAdded = false;
            }
        }
        if (jqueryAdded) {
            DyfacesUtils.addScriptResource(context, "dyfaces-jquery", Version.JQUERY_RESOURCES+"/jquery.min.js");
        }
        boolean addQtip = false;
        if(dygraph.isTooltip() != null && dygraph.isTooltip() == true){
        	addQtip = true;
        }
        
        if(addQtip){
            DyfacesUtils.addStyleResource(context, "dyfaces-qtip2css", Version.QTIP2_RESOURCES+"/jquery.qtip.min.css");
            DyfacesUtils.addScriptResource(context, "dyfaces-qtip2js", Version.QTIP2_RESOURCES+"/jquery.qtip.min.js");
        }
        
        boolean addSyncjs = false;
        if(dygraph.getSynchronize() != null && !dygraph.getSynchronize().isEmpty()){
        	addSyncjs = true;
        }
        
        if(addSyncjs){
            DyfacesUtils.addScriptResource(context, "dyfaces-sync", "synchronizer.js","dygraph");
        }
	}

	public String getScript(FacesContext context, Dygraph dygraph, String event, String graphJSVar){
		ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(context, dygraph, event, graphJSVar, null);
	    String eventJs = dygraph.getClientBehaviors().get(event).get(0).getScript(behaviorContext);
	    return eventJs.replaceAll("\"", "'");
	}
}
