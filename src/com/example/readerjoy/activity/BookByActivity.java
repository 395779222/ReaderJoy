/**   
* @Title: BookByActivity.java
* @Package com.example.readerjoy.activity
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月25日 下午2:05:39
* @version V1.0   
*/


package com.example.readerjoy.activity;

import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
import com.example.readerjoy.base.widget.CustomToast;
import com.example.readerjoy.entity.Book;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
* @ClassName: BookByActivity
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月25日 下午2:05:39
* 
*/

public class BookByActivity extends BaseActivity{
	private TextView book_name;
	private TextView text_book_money;
	private Button btn_by;
	private Button btn_shop;
	private Book book;
	private SharedPreferences sp;
	/* (非 Javadoc) 
	* <p>Title: bindWidget</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidget() 
	*/ 
	@Override
	public void bindWidget() {
		setContentView(R.layout.activity_book_by);
		book_name = (TextView) findViewById(R.id.book_name);
		text_book_money = (TextView) findViewById(R.id.text_book_money);
		btn_by = (Button) findViewById(R.id.btn_by);
		btn_shop = (Button) findViewById(R.id.btn_shop);
		Intent intent = getIntent();
		book = (Book) intent.getSerializableExtra("book");
		// 提取记录在sharedpreferences的各种状态
		sp = getSharedPreferences("config", MODE_PRIVATE);
	}

	/* (非 Javadoc) 
	* <p>Title: bindWidgetEevent</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidgetEevent() 
	*/ 
	@Override
	public void bindWidgetEevent() {
		btn_by.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sp.edit().putBoolean("isBY", true).commit();
				CustomToast.getInstance(BookByActivity.this).showToast("包月成功，请前往书架观看");
				Intent mIntent = new Intent(BookByActivity.this,MainActivity.class); 
				startActivity(mIntent);
			}
		});
		btn_shop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sp.edit().putBoolean(book.getPath()+"bookIsPurchase", true).commit();
				CustomToast.getInstance(BookByActivity.this).showToast("购买成功，请前往书架观看");
				
				Intent mIntent = new Intent(BookByActivity.this,MainActivity.class); 
				startActivity(mIntent);
			}
		});
		
	}

	/* (非 Javadoc) 
	* <p>Title: bindAdapter</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindAdapter() 
	*/ 
	@Override
	public void bindAdapter() {
	
		
	}

	/* (非 Javadoc) 
	* <p>Title: process</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#process() 
	*/ 
	@Override
	public void process() {
		book_name.setText(book.getBookName());
		text_book_money.setText("单本购买"+book.getMoney()+"元");
	}

}
