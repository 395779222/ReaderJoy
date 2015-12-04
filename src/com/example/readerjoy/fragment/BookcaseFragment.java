package com.example.readerjoy.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.readerjoy.BookStore;
import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
import com.example.readerjoy.activity.BaseActivity;
import com.example.readerjoy.activity.BookDetailActivity;
import com.example.readerjoy.activity.LoginActivity;
import com.example.readerjoy.activity.ReadActivity;
import com.example.readerjoy.adapter.BookCaseAdapter;
import com.example.readerjoy.base.widget.CustomToast;
import com.example.readerjoy.db.LocalBook;
import com.example.readerjoy.entity.Book;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class BookcaseFragment extends Fragment{
	private GridView gview_book;
	private View mainView;
	private List<Book> dataList;
	private BookCaseAdapter adapter;
	private LocalBook localbook;
	private RelativeLayout main;
	float beginx = 0;
	float beginy = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater,
           ViewGroup container, Bundle savedInstanceState) {
    	mainView =  inflater.inflate(R.layout.fragment_book_case, null);
    	main = (RelativeLayout) mainView.findViewById(R.id.main);
        init();
        initEvent();
        return mainView;
    }

	private void init() {
		dataList = new ArrayList<Book>();
		dataList.clear();
		localbook = new LocalBook(getActivity());
		SQLiteDatabase dbSelect = localbook.getReadableDatabase();
		
		String col[] = { "bookName","imgBookId","path","author","intrdoduction","type","money","isRead","bookIsPurchase"};
		String[] val = {"1"};
		Cursor cur = dbSelect.query("localbook", col, "isRead=?",val, null,null,null);
		Integer num = cur.getCount();
		
		if(num==0){
			
		}else{
			while (cur.moveToNext()) {
				String bookName = cur.getString(cur.getColumnIndex("bookName"));
				String imgBookId = cur.getString(cur.getColumnIndex("imgBookId"));
				String path = cur.getString(cur.getColumnIndex("path"));
				String author = cur.getString(cur.getColumnIndex("author"));
				String intrdoduction = cur.getString(cur.getColumnIndex("intrdoduction"));
				String type = cur.getString(cur.getColumnIndex("type"));
				String money = cur.getString(cur.getColumnIndex("money"));
				String bookIsPurchase = cur.getString(cur.getColumnIndex("bookIsPurchase"));
				Book book = new Book();
				book.setAuthor(author);
				book.setBookName(bookName);
				book.setImgBookId(Integer.parseInt(imgBookId));
				book.setPath(path);
				book.setIntrdoduction(intrdoduction);
				book.setType(Integer.parseInt(type));
				book.setMoney(Integer.parseInt(money));
				book.setBookIsPurchase(Integer.parseInt(bookIsPurchase));
				dataList.add(book);
			}
			
		}
		Book addBook = new Book();
		addBook.setImgBookId(R.drawable.book_add);
		addBook.setType(3);
		dataList.add(addBook);
		gview_book = (GridView) mainView.findViewById(R.id.gview_book);
		
		adapter = new BookCaseAdapter((BaseActivity)getActivity(),dataList,true);
		gview_book.setAdapter(adapter);
		
	}

	private void initEvent() {
		/*main.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("WrongCall")
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				if(!isEdit){
					if(e.getAction() == MotionEvent.ACTION_DOWN){
						beginx = e.getX();
						beginy = e.getY();
					}
					if(e.getAction()==MotionEvent.ACTION_UP){
						//右滑
						if(e.getX()-beginx<0){
							MainActivity activity = (MainActivity) getActivity();
							//跳转到书城页面
							activity.updateFragment("back");
						}
					}
				}
				else{
					return false;
				}
				return true;
				
			}
		});*/
		/*gview_book.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Book book = dataList.get(position);
				if(book.getType()==3){
					MainActivity activity = (MainActivity) getActivity();
					activity.updateFragment("jp");
				}else{
					Intent it = new Intent();
					it.setClass(getActivity()	, ReadActivity.class);
					Bundle mBundle = new Bundle();     
			        mBundle.putSerializable("book",book);   
			        it.putExtras(mBundle); 
					startActivity(it);
				}
				
				Intent mIntent = new Intent(getActivity(),BookDetailActivity.class);
				Bundle mBundle = new Bundle();     
		        mBundle.putSerializable("book",book);     
		        mIntent.putExtras(mBundle);     
				startActivity(mIntent);
			}});*/
	}
	
	public void editBookCase(){
		if(dataList!=null&&dataList.size()>0){
			for(Book book : dataList){
				book.setEditStatue(true);
				book.setSelected(false);
			}
			dataList.remove(dataList.size()-1);
			adapter.setSelectNum(0);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void canCleEditBook(){
		if(dataList!=null&&dataList.size()>0){
			for(Book book : dataList){
				book.setEditStatue(false);
				book.setSelected(false);
			}
			adapter.setSelectNum(0);
		}
		Book addBook = new Book();
		addBook.setImgBookId(R.drawable.book_add);
		addBook.setType(3);
		dataList.add(addBook);
		adapter.notifyDataSetChanged();
	}
	/**
	 * 
	* @Title: delete 
	* @Description: TODO(书架里的书移除) 
	* @param     设定文件 
	* @date 2015年11月25日 下午1:46:01
	* @author jerry
	* @return void    返回类型
	* @throws
	 */
	public void delete(){
		if(dataList!=null&&dataList.size()>0){
			SQLiteDatabase db = localbook.getWritableDatabase();
			for(int i = dataList.size() - 1;i >= 0;i--){
				if(dataList.get(i).isSelected()){
					String sql = "update localbook set isRead='0' where imgBookId='"+dataList.get(i).getImgBookId()+"'";
					db.execSQL(sql);
					dataList.remove(i);
				}else{
					dataList.get(i).setEditStatue(false);
					dataList.get(i).setSelected(false);
				}
			}
			db.close();
		}
		adapter.setSelectNum(0);
		Book addBook = new Book();
		addBook.setImgBookId(R.drawable.book_add);
		addBook.setType(3);
		dataList.add(addBook);
		adapter.notifyDataSetChanged();
	}
}
