package com.example.readerjoy.entity;

public class BottomMenu {
	private String name;
	private boolean isSelected;
	private Class classTo;
	private int selectedImg;
	private int unSelectedImg;
	
	
	
	public int getSelectedImg() {
		return selectedImg;
	}

	public void setSelectedImg(int selectedImg) {
		this.selectedImg = selectedImg;
	}

	public int getUnSelectedImg() {
		return unSelectedImg;
	}

	public void setUnSelectedImg(int unSelectedImg) {
		this.unSelectedImg = unSelectedImg;
	}

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
