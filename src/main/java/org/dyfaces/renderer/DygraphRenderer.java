package org.dyfaces.renderer;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehavior;
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

import org.dyfaces.DyCallbacks;
import org.dyfaces.DyConstants.Callback;
import org.dyfaces.Version;
import org.dyfaces.component.Dygraph;
import org.dyfaces.data.api.AnnotationConfigurations;
import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.GridOptions;
import org.dyfaces.data.api.GridOptions.Axes;
import org.dyfaces.data.api.GridOptions.PerAxis;
import org.dyfaces.data.api.HighlightRegion;
import org.dyfaces.data.api.HighlightSeriesOpts;
import org.dyfaces.utils.DyUtils;
import org.dyfaces.utils.DyfacesUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

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
		@ResourceDependency(library = "webjars", name = Version.QTIP2_RESOURCES
				+ "/jquery.qtip.min.css", target = "head"),
		@ResourceDependency(library = "dyfaces", name = "js/dyfaces.js")})
@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class DygraphRenderer extends Renderer implements ComponentSystemEventListener{
	public static final String RENDERER_TYPE = "org.dyfaces.component.graph.renderer";
	private static final Gson gson = new Gson();
	private static final GsonBuilder builder = new GsonBuilder();

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
		
		Boolean showTooltip = dygraph.isTooltip();
		if(showTooltip != null && showTooltip){
			DataModel dataModel = (DataModel) dygraph.getValue();
			HighlightSeriesOpts highlightSeriesOpts = dataModel.getConfigOptions().getHighlightSeriesOpts();
			Double strokWidth = highlightSeriesOpts.getStrokeWidth();
			if(strokWidth == null || strokWidth == 0D){
				highlightSeriesOpts.setStrokeWidth(3D);
			}
			Integer borderWidth = highlightSeriesOpts.getStrokeBorderWidth();
			if(borderWidth == null || borderWidth == 0){
				highlightSeriesOpts.setStrokeBorderWidth(1);
			}
			Integer circleSize = highlightSeriesOpts.getHighlightCircleSize();
			if(circleSize == null || circleSize == 0){
				highlightSeriesOpts.setHighlightCircleSize(5);
			}
			initTooltip(context, graphJSVar);
		}
		/*
		 * get Dygraph data defined with either value or model attribute
		 */
		
		StringBuilder data = dygraph.prepareDygraphData();
		graphBuilder.append(data).append(",{");
		/*
		 * get Dygraph attributes
		 */
		setDygraphAttribures(dygraph,graphBuilder);
		
		/*
		 * append graph attributes to Dygraph
		 */
		graphBuilder.append("});");
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
		Boolean showTooltip = dygraph.isTooltip();
		if(showTooltip != null && showTooltip){
			String dyHighlightCallback = "''";
			 if(callBackMap.containsKey(Callback.HighlightCallback)){
				 dyHighlightCallback = "'"+callBackMap.get(Callback.HighlightCallback)+"'";
			 }
		    callBackMap.put(Callback.HighlightCallback, "dyHighlightCallbackFn("+dyHighlightCallback+",'"+graphJSVar+"')");
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
		
		/*Map<String,Object> gridOptions= bindGridOptions(context, graphJSVar, dygraph); 
		
		if(gridOptions != null && !gridOptions.isEmpty()){
			callBackMap.putAll(gridOptions);
		}*/
		
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
	
	
	private void setGridOptions(Dygraph dygraph,StringBuilder graphBuilder){
		DataModel dataModel = (DataModel) dygraph.getValue();
		GridOptions gridOptions = dataModel.getGridOptions();
		
		Boolean drawGrid = gridOptions.getDrawGrid();
		if(drawGrid != null){
			graphBuilder.append(DyUtils.getAttribute("drawGrid", drawGrid));
		}
		if(drawGrid != null && drawGrid){
			Boolean drawXGrid = gridOptions.getDrawXGrid();
			if(drawXGrid != null && !drawXGrid){
				graphBuilder.append(DyUtils.getAttribute("drawXGrid", drawGrid));
			}
			Boolean drawYGrid = gridOptions.getDrawYGrid();
			if(drawYGrid != null && !drawYGrid){
				graphBuilder.append(DyUtils.getAttribute("drawYGrid", drawGrid));
			}
		}
		String gridLineColor = gridOptions.getGridLineColor();
		if(gridLineColor != null){
			graphBuilder.append(DyUtils.getAttribute("gridLineColor", gridLineColor));
		}
		/*
		 * check per axes options
		 */
		List<PerAxis> perAxesList = gridOptions.getAxisGridOptions();
		if(perAxesList != null){
			Map<Axes,PerAxis> tmp = new HashMap<Axes, PerAxis>(3);
			for (PerAxis perAxis : perAxesList) {
				if(perAxis.getAxis() == Axes.x && dygraph.isDateAxis()){
					perAxis.setValueFormatter("Dygraph.dateString_");
					perAxis.setAxisLabelFormatter("Dygraph.dateAxisFormatter");
					perAxis.setTicker("Dygraph.dateTicker");
				}
				tmp.put(perAxis.getAxis(), perAxis);
			}
			graphBuilder.append(DyUtils.getAttribute("axes", tmp));
		}
		
	}

	private Map<String, Object> bindAnnotationConfiurations(FacesContext context,String graphJSVar,Dygraph dygraph) {
		Map<String, Object> map = new HashMap<String, Object>();
		AnnotationConfigurations configurations = null;
		DataModel dataModel= (DataModel) dygraph.getValue();
		configurations = dataModel.getAnnotationConfigurations();
		
		if(configurations != null){
			Map<String,List<ClientBehavior>> clientBehaviours= dygraph.getClientBehaviors();
			String clickHandler = configurations.getClickHandler();
			if((clickHandler != null && !clickHandler.isEmpty()) || clientBehaviours.containsKey(Dygraph.EVENT_ANNOCLICKED)){
			    String annoClick = getScript(context, dygraph, Dygraph.EVENT_ANNOCLICKED, graphJSVar);
				
				map.put("annotationClickHandler", "dyAnnotationClickHandlerFn('"+clickHandler+"',\""+annoClick+"\",'"+graphJSVar+"')");
			}
			String dblClickHandler = configurations.getDblClickHandler();
			if((dblClickHandler != null && !dblClickHandler.isEmpty()) || clientBehaviours.containsKey(Dygraph.EVENT_ANNODBLCLICKED)){
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
		/*SeriesColorOptions colorOptions = null;
		Object dataModel= dygraph.getDyDataModel();
		
		if(dataModel instanceof DataSeries){
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
		}*/
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
	 * @return Json string of Dygraph attributes
	 */
	private void setDygraphAttribures(Dygraph dygraph,StringBuilder graphBuilder) {
		
		graphBuilder.append(DyUtils.getAttribute("width", dygraph.getWidth()));
		graphBuilder.append(DyUtils.getAttribute("height", dygraph.getHeight()));
		graphBuilder.append(DyUtils.getAttribute("labels", dygraph.getLabels()));

		String xl = dygraph.getXlabel();
		if(xl != null && !xl.isEmpty()){
			graphBuilder.append(DyUtils.getAttribute("xlabel", xl));
		}
		String yl = dygraph.getYlabel();
		if(yl != null && !yl.isEmpty()){
			graphBuilder.append(DyUtils.getAttribute("ylabel", yl));
		}
		String ttl = dygraph.getTitle();
		if(ttl != null && !ttl.isEmpty()){
			graphBuilder.append(DyUtils.getAttribute("title", ttl));
		}
		
		Boolean isLabelsUTC = dygraph.isLabelsUTC();
		if(isLabelsUTC != null){
			graphBuilder.append(DyUtils.getAttribute("labelsUTC", isLabelsUTC));
		}
		
		DataModel dataModel = (DataModel) dygraph.getValue();
		String hoJson = gson.toJson(dataModel.getConfigOptions().getHighlightSeriesOpts());
		if(!hoJson.equals("{}")){
			graphBuilder.append(DyUtils.getAttribute("highlightSeriesOpts", hoJson));
		}
		
		setGridOptions(dygraph,graphBuilder);
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
	
	private void initTooltip(FacesContext context,String graphJSVar) throws IOException{
		ResponseWriter writer = context.getResponseWriter();
		StringBuilder graphBuilder = new StringBuilder("var tooltip = $('#").append(graphJSVar).append("').qtip({id: '").append(graphJSVar).append("',prerender: true,content: ' ',position: {target: 'mouse',viewport: $('#").append(graphJSVar).append("'),adjust: { x: 5, y: 5 } },    show: false,});");
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
            //DyfacesUtils.addStyleResource(context, "dyfaces-qtip2css", Version.QTIP2_RESOURCES+"/jquery.qtip.min.css");
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
