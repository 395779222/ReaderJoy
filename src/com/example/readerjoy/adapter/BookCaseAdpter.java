package com.example.readerjoy.adapter;

import java.util.List;

import com.example.readerjoy.R;
import com.example.readerjoy.entity.Book;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BookCaseAdpter extends BaseAdapter{
	private Context context;
	private List<Book> dataList;
	
	public BookCaseAdpter(Context context, List<Book> dataList) {
		this.context = context;
		this.dataList = dataList;
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BookCaseHolder bookCaseHolder = null;
		if(dataList!=null&&dataList.size()>0){
			Book book = dataList.get(position);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_book_case_menu, null);
			bookCaseHolder = new BookCaseHolder(convertView);
			bookCaseHolder.book_name.setText(book.getBookName());
			bookCaseHolder.img_book.setBackgroundResource(R.drawable.book0);
		}
		
		return convertView;
	}
	
	private class BookCaseHolder {
		private ImageView img_book;
		private TextView book_name;
		private BookCaseHolder(View convertView) {
			img_book = (ImageView) convertView.findViewById(R.id.img_book);
			book_name = (TextView) convertView.findViewById(R.id.book_name);
		}
	}
}
