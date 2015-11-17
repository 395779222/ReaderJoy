package com.example.readerjoy.entity;

public class Book {
	
	private String bookName;
	private int imgBookId;
	private String path;
	private String author;
	
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
