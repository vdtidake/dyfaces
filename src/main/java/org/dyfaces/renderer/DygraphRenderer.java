package org.dyfaces.renderer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
import org.dyfaces.data.DataSeries;
import org.dyfaces.data.api.DataModel;
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
		StringBuilder data = new StringBuilder();
		if(dataModel instanceof DataModel){
			DataModel dyDataModel = (DyDataModel) dataModel;
			if(dyDataModel != null){
				//List<Number> tmp = new ArrayList<Number>();
				for (DataSeries series : dyDataModel.getDataSeries()) {
					data.append(series.getDataPoints().toString());
				}
			}
		}
		data.append(",");
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
