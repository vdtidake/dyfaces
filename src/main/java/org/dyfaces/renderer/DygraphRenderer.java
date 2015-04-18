package org.dyfaces.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
import org.dyfaces.Version;
import org.dyfaces.component.Dygraph;
import org.dyfaces.data.api.DataModel;
import org.dyfaces.data.api.DataSeries;
import org.dyfaces.data.api.Point;
import org.dyfaces.data.api.impl.DyDataModel;
import org.dyfaces.data.api.impl.DyDataSeries;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@FacesRenderer(componentFamily = Dygraph.COMPONENT_FAMILY, rendererType = DygraphRenderer.RENDERER_TYPE)
@ResourceDependencies({
		@ResourceDependency(name = "jsf.js", target = "head", library = "javax.faces"),
		@ResourceDependency(library = "webjars", name = Version.DYGRAPH_RESOURCES
				+ "/dygraph-combined.js"),
		@ResourceDependency(library = "webjars", name = Version.DYGRAPH_RESOURCES
				+ "/dygraph-interaction-model.js") })
public class DygraphRenderer extends Renderer {
	public static final String RENDERER_TYPE = "org.dyfaces.component.graph.renderer";

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
		writer.startElement("div", dygraph);
		writer.writeAttribute("id", divId, null);
		writer.endElement("div");
		
		String dygraphAttributes = getDygraphAttribures(dygraph);
		
		writer.startElement("script", dygraph);
		writer.writeAttribute("type", "text/javascript", null);
		StringBuilder graphBuilder = new StringBuilder("new Dygraph(");
		graphBuilder.append("document.getElementById(\"").append(divId).append("\"),");
		StringBuilder data = getDyGraphData(dygraph);
		graphBuilder.append(data.toString());
		//graphBuilder.append("{");
		graphBuilder.append(dygraphAttributes);
		//graphBuilder.append("labels: [ 'x', 'A', 'B' ]");
		//graphBuilder.append("}");
		graphBuilder.append(")");
		writer.write(graphBuilder.toString());
		writer.endElement("script");
		
		
	}

	private StringBuilder getDyGraphData(Dygraph dygraph) {
		Object dataModel= dygraph.getDyDataModel();
		Map<Object,List<Number>> seriesMap = new HashMap<Object, List<Number>>();
		StringBuilder data = new StringBuilder("[");
		
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DyDataModel) dataModel;
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
					}
					return 0;
				}
			});
			for (List<Object> list : mergedSerieses) {
				data.append(list).append(",");
			}
		}else if(dataModel instanceof List){
			List<Point> points = (List<Point>) dataModel;
			for (Point point : points) {
				data.append(Arrays.asList(point.getxValue(),point.getyValue())).append(",");
			}
		}
	
		data.append("],");
		return data;
	}

	private String getDygraphAttribures(Dygraph dygraph) {
		Map<String,Object> attr = dygraph.getAttributes();
		Gson gson = new Gson();
		GsonBuilder builder = new GsonBuilder();
		DyAttributes attributes = builder.create().fromJson(gson.toJson(attr), DyAttributes.class);
		return gson.toJson(attributes);
	}
}
