package com.openteach.sox.common;

import com.openteach.sox.common.configuration.annotation.SessionConfiguration;

@SessionConfiguration(key = "_item_")
public class Item {

	private String id;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
