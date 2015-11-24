package com.example.readerjoy.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.readerjoy.BookStore;
import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class BookcaseFragment extends Fragment{
	private GridView gview_book;
	private View mainView;
	private List<Book> dataList;
	private BookCaseAdapter adapter;
	private LocalBook localbook;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
    }
 
    @Override
    public View onCreateView(LayoutInflater inflater,
           ViewGroup container, Bundle savedInstanceState) {
    	mainView =  inflater.inflate(R.layout.fragment_book_case, null);
        init();
        initEvent();
        return mainView;
    }

	private void init() {
		dataList = new ArrayList<Book>();
		dataList.clear();
		localbook = new LocalBook(getActivity());
		SQLiteDatabase dbSelect = localbook.getReadableDatabase();
		
		String col[] = { "bookName","imgBookId","path","author","intrdoduction","type","money","isRead"};
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
				Book book = new Book();
				book.setAuthor(author);
				book.setBookName(bookName);
				book.setImgBookId(Integer.parseInt(imgBookId));
				book.setPath(path);
				book.setIntrdoduction(intrdoduction);
				book.setType(Integer.parseInt(type));
				book.setMoney(Integer.parseInt(money));
				dataList.add(book);
			}
			
		}
		Book addBook = new Book();
		addBook.setImgBookId(R.drawable.book_add);
		addBook.setType(3);
		dataList.add(addBook);
		gview_book = (GridView) mainView.findViewById(R.id.gview_book);
		
		adapter = new BookCaseAdapter(getActivity(),dataList);
		gview_book.setAdapter(adapter);
		
	}

	private void initEvent() {
		gview_book.setOnItemClickListener(new OnItemClickListener() {
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
					it.putExtra("bookPath", book.getPath());
					startActivity(it);
				}
				
				/*Intent mIntent = new Intent(getActivity(),BookDetailActivity.class);
				Bundle mBundle = new Bundle();     
		        mBundle.putSerializable("book",book);     
		        mIntent.putExtras(mBundle);     
				startActivity(mIntent);*/
			}});
	}
}
