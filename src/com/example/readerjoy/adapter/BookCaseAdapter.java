package com.example.readerjoy.adapter;

import java.util.List;

import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
import com.example.readerjoy.activity.BaseActivity;
import com.example.readerjoy.activity.ReadActivity;
import com.example.readerjoy.entity.Book;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookCaseAdapter extends BaseAdapter{
	private BaseActivity activity = null;
	private List<Book> dataList;
	private int selectNum = 0;
	boolean isCase = false;
	private SharedPreferences sp;
	public BookCaseAdapter(BaseActivity activity, List<Book> dataList,boolean isCase) {
		this.activity = activity;
		this.dataList = dataList;
		this.isCase = isCase; 
		sp = activity.getSharedPreferences("config", activity.MODE_PRIVATE);
	}

	@Override
	public int getCount() {
		return dataList != null ? dataList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return dataList != null ? dataList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BookCaseHolder bookCaseHolder = null;
		if(dataList!=null&&dataList.size()>0){
			Book book = dataList.get(position);
			convertView = LayoutInflater.from(activity).inflate(
					R.layout.item_book_case_menu, null);
			bookCaseHolder = new BookCaseHolder(convertView,book);
			bookCaseHolder.book_name.setText(book.getBookName());
			bookCaseHolder.img_book.setImageResource(book.getImgBookId());
			if(book.isEditStatue()&&book.getType()!=3){
				bookCaseHolder.img_book.setColorFilter(Color.GRAY,PorterDuff.Mode.MULTIPLY);
			}if(book.getType()==3){
				bookCaseHolder.book_status.setVisibility(View.GONE);
				bookCaseHolder.book_status_name.setVisibility(View.GONE);
			}else{
				bookCaseHolder.book_status.getBackground().setAlpha(125);
			}
			String progress = sp.getString(book.getPath() + "fPercent", null);
			if(progress!=null){
				bookCaseHolder.book_progress.setText(progress);
			}else{
				bookCaseHolder.book_progress.setVisibility(View.GONE);
			}
		
		}
		
		return convertView;
	}
	
	private class BookCaseHolder {
		private ImageView img_book;
		private TextView book_name;
		private ImageView img_selected;
		private TextView book_status;
		private TextView book_status_name; 
		private TextView book_progress;
		Book book;
		
		private BookCaseHolder(View convertView,Book book) {
			img_book = (ImageView) convertView.findViewById(R.id.img_book);
			img_selected =  (ImageView) convertView.findViewById(R.id.img_selected);
			book_name = (TextView) convertView.findViewById(R.id.book_name);
			book_status = (TextView) convertView.findViewById(R.id.book_status);
			book_status_name = (TextView) convertView.findViewById(R.id.book_status_name);
			book_progress = (TextView) convertView.findViewById(R.id.book_progress);
			this.book = book;
			if(isCase){
				initEvent();
			}
		}


		/** 
		* @Title: initEvent 
		* @Description: TODO(这里用一句话描述这个方法的作用) 
		* @param     设定文件 
		* @date 2015年11月25日 下午12:02:47
		* @author jerry
		* @return void    返回类型
		* @throws 
		*/ 
		private void initEvent() {
			img_book.setOnLongClickListener(new View.OnLongClickListener() {
	            @Override
	            public boolean onLongClick(View v) {
	            	((MainActivity) activity).bookCaseEdit();
					return isCase;

	            }
	        });
	            
			// 精品推荐
			img_book.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(!book.isEditStatue()){
						if(book.getType()==3){
							//直接跳转到书城
							((MainActivity) activity).updateFragment("back");
						}else{
							Intent it = new Intent();
							it.setClass(activity	, ReadActivity.class);
							Bundle mBundle = new Bundle();     
					        mBundle.putSerializable("book",book);     
					        it.putExtras(mBundle);    
							activity.startActivity(it);
						}
					}
					else{
						if(book.getType()!=3){
							if(book.isSelected()){
								img_selected.setVisibility(View.GONE);
								selectNum = selectNum-1;
							}
							else{
								img_selected.setVisibility(View.VISIBLE);
								selectNum = selectNum+1;
							}
							book.setSelected(!book.isSelected());
							((MainActivity) activity).updateSelectNum(selectNum);
						}
					}
				}
			});
			
		}
	}

	public int getSelectNum() {
		return selectNum;
	}

	public void setSelectNum(int selectNum) {
		this.selectNum = selectNum;
	}
	
	
}
