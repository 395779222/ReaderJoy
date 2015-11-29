/**   
* @Title: SearchActivity.java
* @Package com.example.readerjoy.activity
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月24日 下午2:35:48
* @version V1.0   
*/


package com.example.readerjoy.activity;

import java.util.ArrayList;
import java.util.List;

import com.example.readerjoy.BookStore;
import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
import com.example.readerjoy.adapter.BookAdapter;
import com.example.readerjoy.adapter.BookCaseAdapter;
import com.example.readerjoy.base.widget.CustomToast;
import com.example.readerjoy.base.widget.GridViewForScrollView;
import com.example.readerjoy.base.widget.ListViewForScrollView;
import com.example.readerjoy.entity.Book;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
* @ClassName: SearchActivity
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月24日 下午2:35:48
* 
*/

public class SearchActivity extends BaseActivity {
	private RelativeLayout header;
	private RelativeLayout rl_search;
	private RelativeLayout rl_show;
	EditText ed_search;
	Button btn_search;
	private TextView tvTitle;
	TextView text_change;
	private ImageView btnExit;
	GridViewForScrollView gview_book;
	List<Book>gridBoolList;
	private BookCaseAdapter gridAdapter;
	
	ListViewForScrollView list_book;
	List<Book>searchBookList;
	BookAdapter searchBookAdapter;
	
	List<Book>allBookList;
	/* (非 Javadoc) 
	* <p>Title: bindWidget</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidget() 
	*/ 
	@Override
	public void bindWidget() {
		setContentView(R.layout.activity_search);
		header = (RelativeLayout) findViewById(R.id.header);
		rl_search = (RelativeLayout) findViewById(R.id.rl_search);
		rl_show = (RelativeLayout) findViewById(R.id.rl_show);
		ed_search = (EditText) findViewById(R.id.ed_search);
		btn_search = (Button) findViewById(R.id.btn_search);
		text_change = (TextView) findViewById(R.id.text_change);
		tvTitle  = (TextView) findViewById(R.id.tvTitle);
		gview_book = (GridViewForScrollView) findViewById(R.id.gview_book);
		list_book = (ListViewForScrollView) findViewById(R.id.list_book);
		btnExit = (ImageView) findViewById(R.id.btnExit);
		allBookList = BookStore.getInstance(SearchActivity.this).getAllBook();
	}

	/* (非 Javadoc) 
	* <p>Title: bindWidgetEevent</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidgetEevent() 
	*/ 
	@Override
	public void bindWidgetEevent() {
		//搜索
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				loadSearchBook();
				if(searchBookList.size()<1){
					CustomToast.getInstance(SearchActivity.this).showToast("暂无数据");
				}else{
					list_book.setVisibility(View.VISIBLE);
					header.setVisibility(View.VISIBLE);
					rl_search.setVisibility(View.GONE);
					rl_show.setVisibility(View.GONE);
					searchBookAdapter = new BookAdapter(SearchActivity.this,searchBookList);
					list_book.setAdapter(searchBookAdapter);
				}
			}
		});
		text_change.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeGridViewBook();
				gridAdapter = new BookCaseAdapter(SearchActivity.this,gridBoolList,false);
				gview_book.setAdapter(gridAdapter);
			}
		});
		
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(SearchActivity.this,MainActivity.class); 
				startActivity(mIntent);
			}
		});
		
		gview_book.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Book book = gridBoolList.get(position);
				jumpToBookDetail(book);
			}});
		
		list_book.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Book book = searchBookList.get(position);
				jumpToBookDetail(book);
			}});
	}
	
	/** 
	* @Title: loadSearchBook 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月24日 下午2:56:12
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	protected void loadSearchBook() {
		String searhValue = ed_search.getText().toString();
		int allCount = allBookList.size();
		if(allCount>=1){
			searchBookList = new ArrayList<Book>();
			for(int i=0;i<allCount;i++){
				if(allBookList.get(i).getAuthor().indexOf(searhValue)>-1||
						allBookList.get(i).getBookName().indexOf(searhValue)>-1	){
					searchBookList.add(allBookList.get(i));
				}
			}
		}
		
	}

	/* (非 Javadoc) 
	* <p>Title: bindAdapter</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindAdapter() 
	*/ 
	@Override
	public void bindAdapter() {
		changeGridViewBook();
		gridAdapter = new BookCaseAdapter(this,gridBoolList,false);
		gview_book.setAdapter(gridAdapter);
		
	}

	/** 
	* @Title: changeGridViewBook 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月24日 下午2:55:41
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void changeGridViewBook() {
		int allCount = allBookList.size();
		if(allCount>=2){
			gridBoolList = new ArrayList<Book>();
			for(int i=0;i<3;i++){
				int x=(int)(Math.random()*allCount-1);
				gridBoolList.add(allBookList.get(x));
			}
		}
	}

	/* (非 Javadoc) 
	* <p>Title: process</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#process() 
	*/ 
	@Override
	public void process() {
		tvTitle.setText("搜索");
	}
	
	private void jumpToBookDetail(Book book){
		Intent mIntent = new Intent(SearchActivity.this,BookDetailActivity.class);
		Bundle mBundle = new Bundle();     
        mBundle.putSerializable("book",book);     
        mIntent.putExtras(mBundle);     
		startActivity(mIntent);
	}
}
