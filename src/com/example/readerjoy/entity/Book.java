package com.example.readerjoy.entity;

import java.io.Serializable;

public class Book implements Serializable{
	
	private String bookName;
	private int imgBookId;
	private String path;
	private String author;
	private String intrdoduction="暂无介绍";
	private int type = 0;//0:精品推荐，1：包月专区
	private int money = 0;//包月钱
	
	
	public String getIntrdoduction() {
		return intrdoduction;
	}
	public void setIntrdoduction(String intrdoduction) {
		this.intrdoduction = intrdoduction;
	}
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public int getImgBookId() {
		return imgBookId;
	}
	public void setImgBookId(int imgBookId) {
		this.imgBookId = imgBookId;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
