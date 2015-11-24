package com.example.readerjoy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocalBook extends SQLiteOpenHelper {
	private static String DATABASE_NAME = "book.db";
	private static int DATABASE_VERSION = 1;
	private String PATH = "path";
	private String TYPE = "type";
	private String s = "localbook";
	private String USER = "user";
	public LocalBook(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		//创建用户表
		String sql2 = "CREATE TABLE " + USER + " ( name text not null, password text not null,isBY text not null)";
		//创建本地阅读表
		String sql3 = "CREATE TABLE localbook"+ "( bookName text not null, imgBookId text not null,"
				+ "path text not null,author text not null,intrdoduction text not null,type text not null,money text not null,"
				+ "isRead text not null)";
		db.execSQL(sql2);
		db.execSQL(sql3);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
