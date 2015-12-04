/**   
* @Title: BookCityFragment.java
* @Package com.example.readerjoy.fragment
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2015年11月18日 下午6:12:18
* @version V1.0   
*/


package com.example.readerjoy.fragment;

import java.util.List;

import com.example.readerjoy.BookStore;
import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
import com.example.readerjoy.activity.BaseActivity;
import com.example.readerjoy.activity.BookDetailActivity;
import com.example.readerjoy.activity.ReadActivity;
import com.example.readerjoy.adapter.BookCaseAdapter;
import com.example.readerjoy.adapter.CategoryAdapter;
import com.example.readerjoy.entity.Book;
import com.example.readerjoy.entity.BookCategory;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

/**
* @ClassName: BookCityFragment
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月18日 下午6:12:18
* 
*/

@SuppressLint("NewApi")
public class BookCityFragment extends Fragment{
	private View mainView;
	private GridView gview_jptj;
	private List<Book> dataJPList;
	private BookCaseAdapter jpAdapter;
	private GridView gview_byzq;
	private List<Book> dataBYList;
	private BookCaseAdapter byAdapter;
	private RelativeLayout jptj;
	private RelativeLayout byzq;
	
	GridView gview_book_category;
	List<BookCategory>categoryList ;
	CategoryAdapter categoryAdapter ;
	
    @Override
    public View onCreateView(LayoutInflater inflater,
           ViewGroup container, Bundle savedInstanceState) {
   
    	mainView =  inflater.inflate(R.layout.fragment_book_city, null);
        initView();
        initAdapter();
        initEvent();
        return mainView;
    }
  
	/** 
	* @Title: initView 
	* @Description: TODO(初始化控件和数据) 
	* @param     设定文件 
	* @date 2015年11月23日 上午10:23:29
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void initView() {
		gview_jptj = (GridView) mainView.findViewById(R.id.gview_jptj);
		gview_byzq = (GridView) mainView.findViewById(R.id.gview_byzq);
		dataJPList = BookStore.getInstance(getActivity()).getMainJPBook();
		dataBYList = BookStore.getInstance(getActivity()).getMainBYBook();
		jptj = (RelativeLayout) mainView.findViewById(R.id.jptj);
		byzq = (RelativeLayout) mainView.findViewById(R.id.byzq);
		gview_book_category = (GridView) mainView.findViewById(R.id.gview_book_category);
	}
	
	/** 
	* @Title: initAdapter 
	* @Description: TODO(初始化适配器) 
	* @param     设定文件 
	* @date 2015年11月23日 上午10:30:27
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void initAdapter() {
		categoryList = BookStore.getInstance(getActivity()).getBookCategoryList();
		categoryAdapter = new CategoryAdapter(getActivity(),categoryList);
		gview_book_category.setAdapter(categoryAdapter);
		
		jpAdapter = new BookCaseAdapter((BaseActivity)getActivity(),dataJPList,false);
		gview_jptj.setAdapter(jpAdapter);
		
		byAdapter = new BookCaseAdapter((BaseActivity)getActivity(),dataBYList,false);
		gview_byzq.setAdapter(byAdapter);
		
	}
	
	  
		/** 
		* @Title: initEvent 
		* @Description: TODO(初始化事件) 
		* @param     设定文件 
		* @date 2015年11月23日 上午10:30:30
		* @author jerry
		* @return void    返回类型
		* @throws 
		*/ 
		private void initEvent() {
			// 精品推荐
			jptj.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity activity = (MainActivity) getActivity();
					activity.updateFragment("jp");
				}
			});
			
			// 精品推荐
			byzq.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MainActivity activity = (MainActivity) getActivity();
					activity.updateFragment("by");
				}
			});
			
			gview_jptj.setOnItemClickListener(new OnItemClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Book book = dataJPList.get(position);
					jumpToBookDetail(book);
				}});
			
			gview_byzq.setOnItemClickListener(new OnItemClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Book book = dataBYList.get(position);
					jumpToBookDetail(book);
				}});
			
			gview_book_category.setOnItemClickListener(new OnItemClickListener() {
				@SuppressLint("NewApi")
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					BookCategory  cate = categoryList.get(position);
					MainActivity activity = (MainActivity) getActivity();
					activity.updateFragmentByCategory(cate.getIndex(),cate.getName());
					/*List<Book>dataList = BookStore.getInstance(getActivity()).getAllBook();
					List<Book> bookDateList = new Arry
					BookCategory  cate = categoryList.get(position);
					for(Book book : dataList){
						if(book.getCateGoryType().indexOf(cate.getIndex()+"")>-1){
							
							bookDateList.add(book);
						}
					}*/
				}
			});
		}
		
		private void jumpToBookDetail(Book book){
			Intent mIntent = new Intent(getActivity(),BookDetailActivity.class);
			Bundle mBundle = new Bundle();     
	        mBundle.putSerializable("book",book);     
	        mIntent.putExtras(mBundle);     
			startActivity(mIntent);
		}
		
}
