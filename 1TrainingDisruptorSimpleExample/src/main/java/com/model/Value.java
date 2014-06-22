package com.model;

public class Value {

	private String id;

	public Value(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Value ID: " + id;
	}
}
