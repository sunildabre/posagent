package com.gsd.pos;


import java.io.Serializable;

@SuppressWarnings("serial")
public class Message implements Serializable{

	private String name;
	private String version;
	private String[] properties;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String[] getProperties() {
		return properties;
	}
	public void setProperties(String[] properties) {
		this.properties = properties;
	}
	

}
