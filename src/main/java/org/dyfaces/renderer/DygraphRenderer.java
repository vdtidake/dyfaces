package org.dyfaces.renderer;

import java.io.IOException;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.FacesRenderer;
import javax.faces.render.Renderer;

import org.dyfaces.Version;
import org.dyfaces.component.Dygraph;

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
		
		writer.startElement("script", dygraph);
		writer.writeAttribute("type", "text/javascript", null);
		StringBuilder graphBuilder = new StringBuilder("new Dygraph(");
		graphBuilder.append("document.getElementById(\"").append(divId).append("\"),");
		graphBuilder.append("[[1,10,100],[2,20,80],[3,50,60],[4,70,80]],");
		graphBuilder.append("{labels: [ 'x', 'A', 'B' ]}");
		graphBuilder.append(")");
		writer.write(graphBuilder.toString());
		writer.endElement("script");
		
	}
}
