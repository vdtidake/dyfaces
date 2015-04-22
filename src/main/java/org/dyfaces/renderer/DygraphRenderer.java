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
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.dyfaces.DyAttributes;
import org.dyfaces.DyCallbacks;
import org.dyfaces.Version;
import org.dyfaces.component.Dygraph;
import org.dyfaces.data.api.AnnotationPoint;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.HighlightRegion;
import org.dyfaces.data.api.Point;
import org.dyfaces.data.api.impl.DyDataModel;
import org.dyfaces.utils.DyUtils;

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
		@ResourceDependency(library = "dyfaces", name = "js/dyfaces.js")})
public class DygraphRenderer extends Renderer {
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
		String divId = dygraph.getClientId(context);
		/*
		 * create Dygraph div element
		 */
		writer.startElement("div", dygraph);
		writer.writeAttribute("id", divId, null);
		writer.endElement("div");
		
		
		/*
		 * start Dygraph creation inside <script> tag
		 */
		writer.startElement("script", dygraph);
		writer.writeAttribute("type", "text/javascript", null);
		Map<String,Object> attr = dygraph.getAttributes();
		String graphJSVar = (String) attr.get("var");
		if(graphJSVar == null){
			graphJSVar=divId;
		}
		/*
		 * assign Dygraph object to javascript variable defined with 'var' attribute
		 */
		StringBuilder graphBuilder = new StringBuilder("var "+graphJSVar+"= new Dygraph(");
		graphBuilder.append("document.getElementById(\"").append(divId).append("\"),");
		/*
		 * get Dygraph data defined with either value or model attribute
		 */
		Map<String,Object> datamodelAttributes = new HashMap<String, Object>(3);
		StringBuilder data = getDyGraphData(dygraph,datamodelAttributes);
		graphBuilder.append(data.toString());
		/*
		 * get Dygraph attributes
		 */
		String dygraphAttributes = getDygraphAttribures(dygraph,datamodelAttributes);
		
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
		 *Dygraph callback options 
		 */
		bindDyCallbacks(context,graphJSVar,dygraph);
		/*
		 *Add annotations if exists 
		 */
		List<AnnotationPoint> annotations =  dygraph.getAnnotations();
		if(annotations != null && !annotations.isEmpty()){
			addDyAnnotations(context,graphJSVar,dygraph);
		}
		
		writer.endElement("script");
		
		
	}

	private void bindDyCallbacks(FacesContext context,String graphJSVar,Dygraph dygraph) throws IOException {
		String callbacks = getDygraphCallbacks(dygraph);
		ResponseWriter writer = context.getResponseWriter();
		StringBuilder graphBuilder = new StringBuilder();
		if(callbacks != null && !"{}".equals(callbacks)){
			graphBuilder.append(graphJSVar).append(".updateOptions(").append(callbacks).append(");");
			writer.write(graphBuilder.toString());
		}
		graphBuilder = new StringBuilder();
		
		List<HighlightRegion> highlightRegions = dygraph.getHghlightRegions();
		if(highlightRegions != null && !highlightRegions.isEmpty()){
			String hData = gson.toJson(highlightRegions);
			graphBuilder.append("var hd=").append(hData).append(";");
			graphBuilder.append(graphJSVar).append(".updateOptions(").append("{underlayCallback : dyhighlightRegion(hd)}").append(");");
			writer.write(graphBuilder.toString());
		}
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
	private StringBuilder getDyGraphData(Dygraph dygraph,Map<String,Object> datamodelAttributes) {
		Object dataModel= dygraph.getDyDataModel();
		Map<Object,List<Number>> seriesMap = new HashMap<Object, List<Number>>();
		StringBuilder data = new StringBuilder("[");
		
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DyDataModel) dataModel;
			
			if(dyDataModel != null){
				if(dyDataModel.getGraphTitle() != null){
					datamodelAttributes.put("title", dyDataModel.getGraphTitle());
				}
				if(dyDataModel.getxAxisLable() != null){
					datamodelAttributes.put("xlabel", dyDataModel.getxAxisLable());
				}
				if(dyDataModel.getyAxisLable() != null){
					datamodelAttributes.put("ylabel", dyDataModel.getyAxisLable());
				}
				List<String> seriesLabels = new ArrayList<String>(dyDataModel.getDataSeries().size());
				seriesLabels.add("");
				for (DataSeries series : dyDataModel.getDataSeries()) {
					String name = series.getSeries();
					if(name != null && !name.isEmpty()){
						seriesLabels.add(name);
					}
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
				if(!seriesLabels.isEmpty()){
					datamodelAttributes.put("labels", seriesLabels);
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
			
		}else if(dataModel instanceof DataSeries){
			DataSeries dataseries = (DataSeries) dataModel;
			List<String> seriesLabels = new ArrayList<String>(dataseries.getDataPoints().size());
			seriesLabels.add("");
			if(dataseries != null){
				String name = dataseries.getSeries();
				if(name != null && !name.isEmpty()){
					seriesLabels.add(name);
				}
				List<Point> points = dataseries.getDataPoints();
				Collections.sort(points);
				for (Point point : points) {
					if(point.getxValue() instanceof Date){
						data.append(Arrays.asList(DyUtils.getJSDyDate(point.getxValue()),point.getyValue())).append(",");
					}else{
						data.append(Arrays.asList(point.getxValue(),point.getyValue())).append(",");
					}
					
				}
				if(!seriesLabels.isEmpty()){
					datamodelAttributes.put("labels", seriesLabels);
				}
				
				List<AnnotationPoint> annotationPoints = dataseries.getAnnotations();
				if(annotationPoints != null && !annotationPoints.isEmpty()){
					dygraph.setAnnotations(annotationPoints);
				}
				List<HighlightRegion> highlightRegions = dataseries.getHighlightRegions();
				if(highlightRegions != null && !highlightRegions.isEmpty()){
					dygraph.setHighlightRegions(highlightRegions);
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
	private String getDygraphAttribures(Dygraph dygraph,Map<String,Object> datamodelAttributes) {
		Map<String,Object> attr = dygraph.getAttributes();
		DyAttributes attributes = builder.create().fromJson(gson.toJson(attr), DyAttributes.class);
		if(!datamodelAttributes.isEmpty()){
			if(datamodelAttributes.containsKey("title")){
				attributes.setTitle((String) datamodelAttributes.get("title"));
			}
			if (datamodelAttributes.containsKey("xlabel")) {
				attributes.setXlabel((String) datamodelAttributes.get("xlabel"));
			}
			if (datamodelAttributes.containsKey("ylabel")) {
				attributes.setYlabel((String) datamodelAttributes.get("ylabel"));
			}
			if (datamodelAttributes.containsKey("labels")) {
				List<String> labels = List.class.cast(datamodelAttributes.get("labels"));
				if(labels.size() > 1){
					attributes.setLabels(labels);
				}
			}
			
		}
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
}
