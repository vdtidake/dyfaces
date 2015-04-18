package org.dyfaces.data.api.impl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import org.dyfaces.data.DataSeries;
import org.dyfaces.data.api.DataModel;

public class DyDataModel implements Serializable, DataModel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6959678274477033126L;
	private List<DataSeries> dyDataSeries;

	public DyDataModel(){
		this.dyDataSeries=new LinkedList<DataSeries>();
	}
	public List<DataSeries> getDataSeries() {
		return dyDataSeries;
	}
	
}
