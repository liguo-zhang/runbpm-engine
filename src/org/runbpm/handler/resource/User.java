package org.runbpm.handler.resource;

import java.io.Serializable;

public class User implements Serializable {
	
	private static final long serialVersionUID = 9009477858455656656L;

	private String id;

	private String name;

	private String description;
	
	public User(String id){
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
