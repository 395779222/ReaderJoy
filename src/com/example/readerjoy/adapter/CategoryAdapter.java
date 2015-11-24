/**   
* @Title: CategoryAdapter.java
* @Package com.example.readerjoy.adapter
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月23日 下午1:05:51
* @version V1.0   
*/


package com.example.readerjoy.adapter;

import java.util.List;

import com.example.readerjoy.R;
import com.example.readerjoy.entity.Book;
import com.example.readerjoy.entity.BookCategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
* @ClassName: CategoryAdapter
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月23日 下午1:05:51
* 
*/

public class CategoryAdapter extends BaseAdapter{
	private Context context;
	private List<BookCategory> dataList;

	public CategoryAdapter(Context context, List<BookCategory> dataList) {
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
		BookHolder bookCaseHolder = null;
		if(dataList!=null&&dataList.size()>0){
			BookCategory bookCategory = dataList.get(position);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_book_category, null);
			bookCaseHolder = new BookHolder(convertView);
			bookCaseHolder.book_category_name.setText(bookCategory.getName());
	
		}
		
		return convertView;
	}
	
	private class BookHolder {
		
		private TextView book_category_name;
		
		
		private BookHolder(View convertView) {
			
			book_category_name = (TextView) convertView.findViewById(R.id.book_category_name);
			
		}
	}
}
