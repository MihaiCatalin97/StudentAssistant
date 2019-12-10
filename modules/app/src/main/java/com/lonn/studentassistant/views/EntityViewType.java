package com.lonn.studentassistant.views;

public enum EntityViewType {
	FULL("full"),
	PARTIAL("partial");

	private String type;

	EntityViewType(String type) {
		this.type = type;
	}
}
