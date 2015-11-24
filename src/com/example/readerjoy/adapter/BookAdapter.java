/**   
* @Title: BookAdapter.java
* @Package com.example.readerjoy.adapter
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月23日 下午12:59:44
* @version V1.0   
*/


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

/**
* @ClassName: BookAdapter
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月23日 下午12:59:44
* 
*/

public class BookAdapter extends BaseAdapter{
	private Context context;
	private List<Book> dataList;
	
	public BookAdapter(Context context, List<Book> dataList) {
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
		BookHolder bookHolder = null;
		if(dataList!=null&&dataList.size()>0){
			Book book = dataList.get(position);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_book, null);
			bookHolder = new BookHolder(convertView);
			bookHolder.book_name.setText(book.getBookName());
			bookHolder.img_book.setBackgroundResource(book.getImgBookId());
			bookHolder.book_author.setText(book.getAuthor());
			//如果是包月
			if(book.getType()==1){
				//bookHolder.book_money.setText(book.getMoney());				
			}
			
			bookHolder.book_induction.setText(book.getIntrdoduction());
		}
		
		return convertView;
	}
	
	private class BookHolder {
		private ImageView img_book;
		private TextView book_name;
		private TextView book_author;
		//private TextView book_money;
		private TextView book_induction;
		
		private BookHolder(View convertView) {
			img_book = (ImageView) convertView.findViewById(R.id.img_book);
			book_name = (TextView) convertView.findViewById(R.id.book_name);
			book_author = (TextView) convertView.findViewById(R.id.book_author);
			//book_money = (TextView) convertView.findViewById(R.id.book_money);
			book_induction = (TextView) convertView.findViewById(R.id.book_induction);
		}
	}
}
