/**   
* @Title: BookListFragment.java
* @Package com.example.readerjoy.fragment
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月23日 上午11:45:40
* @version V1.0   
*/


package com.example.readerjoy.fragment;

import java.util.List;

import com.example.readerjoy.BookStore;
import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
import com.example.readerjoy.activity.BookDetailActivity;
import com.example.readerjoy.adapter.BookAdapter;
import com.example.readerjoy.adapter.BookCaseAdapter;
import com.example.readerjoy.adapter.CategoryAdapter;
import com.example.readerjoy.base.widget.ListViewForScrollView;
import com.example.readerjoy.entity.Book;
import com.example.readerjoy.entity.BookCategory;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

/**
* @ClassName: BookListFragment
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月23日 上午11:45:40
* 
*/

@SuppressLint("NewApi")
public class BookListFragment extends Fragment{
	private View mainView;
	GridView gview_book_category;
	List<BookCategory>categoryList ;
	CategoryAdapter categoryAdapter ;
	
	ListViewForScrollView list_book;
	List<Book>bookList ;
	BookAdapter bookAdapter;
	 @Override
    public View onCreateView(LayoutInflater inflater,
           ViewGroup container, Bundle savedInstanceState) {
    	mainView =  inflater.inflate(R.layout.fragment_book_list, null);
    	initAdapter();
        initEvent();
        return mainView;
    }

	/** 
	* @Title: init 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月23日 下午12:54:40
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void initAdapter(){
		gview_book_category = (GridView) mainView.findViewById(R.id.gview_book_category);
		list_book = (ListViewForScrollView) mainView.findViewById(R.id.list_book);
		
		categoryList = BookStore.getInstance(getActivity()).getBookCategoryList();
		categoryAdapter = new CategoryAdapter(getActivity(),categoryList);
		gview_book_category.setAdapter(categoryAdapter);
		
		bookList = ((MainActivity)getActivity()).listToShowByType;
		bookAdapter = new BookAdapter(getActivity(),bookList);
		list_book.setAdapter(bookAdapter);
		
	}
	
	/** 
	* @Title: initEvent 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月23日 下午12:54:43
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void initEvent() {
		list_book.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Book book = bookList.get(position);
				Intent mIntent = new Intent(getActivity(),BookDetailActivity.class);
				Bundle mBundle = new Bundle();     
		        mBundle.putSerializable("book",book);     
		        mIntent.putExtras(mBundle);     
				startActivity(mIntent);
			}
		});
	}

	public List<Book> getBookList() {
		return bookList;
	}

	public void setBookList(List<Book> bookList) {
		this.bookList = bookList;
	}
	
	
}
