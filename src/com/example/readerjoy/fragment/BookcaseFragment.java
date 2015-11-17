package com.example.readerjoy.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.example.readerjoy.R;
import com.example.readerjoy.activity.ReadActivity;
import com.example.readerjoy.adapter.BookCaseAdpter;
import com.example.readerjoy.entity.Book;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
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
	private BookCaseAdpter adapter;
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
		gview_book = (GridView) mainView.findViewById(R.id.gview_book);
		String[] bookids = getResources().getStringArray(R.array.bookid);
		String[] booknames = getResources().getStringArray(R.array.bookname);
		String[] bookauthors = getResources().getStringArray(R.array.bookauthor);
		
		for(int i=0;i<bookids.length;i++){
			Book book = new Book();
			book.setAuthor(bookauthors[i]);
			book.setBookName(booknames[i]); 
			book.setImgBookId(R.drawable.book0 + i);
			book.setPath(getActivity().getFilesDir()+"/"+bookids[i]);
			dataList.add(book);
		}
		adapter = new BookCaseAdpter(getActivity(),dataList);
		gview_book.setAdapter(adapter);
		
	}

	private void initEvent() {
		gview_book.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Book book = dataList.get(position);
				Intent it = new Intent();
				it.setClass(getActivity()	, ReadActivity.class);
				it.putExtra("bookPath", book.getPath());
				startActivity(it);
			}});
	}
}
