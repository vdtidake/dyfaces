package org.dyfaces;

public enum FacesParam {
	AJAX("javax.faces.partial.ajax"), RENDER("javax.faces.partial.render"), 
	EXECUTE("javax.faces.partial.execute"), SOURCE("javax.faces.source"), 
	EVENT("javax.faces.behavior.event");

	private String name;

	private FacesParam(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
