/**   
* @Title: BookDetailActivity.java
* @Package com.example.readerjoy.activity
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月19日 上午9:35:15
* @version V1.0   
*/


package com.example.readerjoy.activity;

import com.example.readerjoy.R;
import com.example.readerjoy.db.LocalBook;
import com.example.readerjoy.entity.Book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
* @ClassName: BookDetailActivity
* @Description: TODO( 小说详情页面)
* @author jerry
* @date 2015年11月19日 上午9:35:15
* 
*/
public class BookDetailActivity extends BaseActivity{
	private ImageView img_book;
	private TextView book_name;
	private TextView book_introduction;
	private TextView book_money;
	private TextView book_author;
	private TextView tvTitle;
	ImageView btnExit;
	Book book;
	Button btn_reader;
	Button btn_shop;
	LocalBook localbook;

	/* (非 Javadoc) 
	* <p>Title: bindWidget</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidget() 
	*/ 
	@Override
	public void bindWidget() {
		setContentView(R.layout.activity_book_detail);
		img_book = (ImageView) findViewById(R.id.img_book);
		btnExit  = (ImageView) findViewById(R.id.btnExit);
		book_name = (TextView) findViewById(R.id.book_name);
		book_introduction = (TextView) findViewById(R.id.book_introduction);
		book_money = (TextView) findViewById(R.id.book_money);
		book_author = (TextView) findViewById(R.id.book_author);
		Intent intent = this.getIntent();
		book = (Book) intent.getSerializableExtra("book");
		btn_reader = (Button) findViewById(R.id.btn_reader);
		btn_shop = (Button) findViewById(R.id.btn_shop);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		localbook = new LocalBook(this);
	}

	/* (非 Javadoc) 
	* <p>Title: bindWidgetEevent</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidgetEevent() 
	*/ 
	@Override
	public void bindWidgetEevent() {
		btnExit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
			
		});
		btn_reader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//editor.putInt(bookPath + "bookIsPurchase", begin).commit();
				updateLoackBookStatus();
				Intent it = new Intent();
				it.setClass(BookDetailActivity.this	, ReadActivity.class);
				Bundle mBundle = new Bundle();     
		        mBundle.putSerializable("book",book);   
		        it.putExtras(mBundle); 
				startActivity(it);
			}
			/**
			 * 
			* @Title: updateLoackBookStatus 
			* @Description: TODO(更新该本书已经被阅读了) 
			* @param     设定文件 
			* @date 2015年11月24日 下午5:03:27
			* @author jerry
			* @return void    返回类型
			* @throws
			 */
			private void updateLoackBookStatus() {
				SQLiteDatabase db = localbook.getWritableDatabase();
				String sql = "update localbook set isRead='1' where imgBookId='"+book.getImgBookId()+"'";
				db.execSQL(sql);
				db.close();
			}
		});
		
		btn_shop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
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
		if(book!=null){
			img_book.setBackgroundResource(book.getImgBookId());
			book_name.setText(book.getBookName());
			book_introduction.setText("  "+book.getIntrdoduction());
			book_money.setText("价格：包月|"+book.getMoney()+"元/本");
			book_author.setText(book.getAuthor());
			tvTitle.setText(book.getBookName()+" 详情");
		}
		
	}

}
