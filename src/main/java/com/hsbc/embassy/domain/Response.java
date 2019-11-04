package com.hsbc.embassy.domain;

import java.io.Serializable;

public class Response implements  Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9071392053480836046L;
	String type="";
	Object [] items=null;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Object[] getFields() {
		return items;
	}
	public void setFields(Object[] fields) {
		this.items = fields;
	}
	public Response(String type, Object[] fields) {
		super();
		this.type = type;
		this.items = fields;
	}
	public Response() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	

}
