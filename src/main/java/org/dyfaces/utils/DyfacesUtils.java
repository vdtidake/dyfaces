package org.dyfaces.utils;

import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

import org.dyfaces.DyConstants.UIOutputResourceType;

public class DyfacesUtils {
	private static void addUIOutputResource(FacesContext context,UIOutputResourceType resourceType,String id,String name,String library){
		String rendererType = "javax.faces.resource."+resourceType;
		UIOutput uiOutput = (UIOutput) context.getApplication().createComponent(context, "javax.faces.Output", rendererType);
    	uiOutput.getAttributes().put("library", library);
    	uiOutput.getAttributes().put("name", name);
    	uiOutput.getAttributes().put("target", "head");
    	uiOutput.setId(id);
    	uiOutput.setRendererType(rendererType);
    	context.getViewRoot().addComponentResource(context, uiOutput, "head");
	}
	
	public static void addResource(FacesContext context,UIOutputResourceType resourceType,String id,String name,String lib){
		addUIOutputResource(context,resourceType,id,name,lib);
	}
	
	public static void addScriptResource(FacesContext context,String id,String name,String... others){
		String lib = "webjars";
		if(others != null && others.length > 0){
			lib=others[0];
		}
		addResource(context,UIOutputResourceType.Script,id,name,lib);
	}
	
	public static void addStyleResource(FacesContext context,String id,String name,String... others){
		String lib = "webjars";
		if(others != null && others.length > 0){
			lib=others[0];
		}
		addResource(context,UIOutputResourceType.Style,id,name,lib);
	}
}
