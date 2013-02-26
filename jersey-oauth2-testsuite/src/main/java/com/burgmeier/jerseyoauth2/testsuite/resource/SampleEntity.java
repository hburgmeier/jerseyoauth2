package com.burgmeier.jerseyoauth2.testsuite.resource;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SampleEntity {

	private String id;
	private String text;
	
	public SampleEntity()
	{
		
	}
	
	public SampleEntity(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
	
}
