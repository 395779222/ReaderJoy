package com.example.readerjoy.entity;

public class BottomMenu {
	private String name;
	private boolean isSelected = false;
	private Class classTo;
	
	
	public Class getClassTo() {
		return classTo;
	}

	public void setClassTo(Class classTo) {
		this.classTo = classTo;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
