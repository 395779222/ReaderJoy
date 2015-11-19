/**   
* @Title: BookStore.java
* @Package com.example.readerjoy
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2015年11月18日 下午11:22:06
* @version V1.0   
*/


package com.example.readerjoy;

import java.util.List;

import com.example.readerjoy.entity.Book;

import android.app.Activity;

/**
* @ClassName: BookStore
* @Description: TODO(这里用一句话描述这个类的作用)
* @author jerry
* @date 2015年11月18日 下午11:22:06
* 
*/

public class BookStore {
	
	public   List<Book> list;
	private static BookStore bookStore = null;
	private BookStore(){
		
	}
	
	public static BookStore  getInstance(Activity activity){
		if(bookStore == null){
			bookStore = new BookStore();
			String[] bookids = activity.getResources().getStringArray(R.array.bookid);
			String[] booknames = activity.getResources().getStringArray(R.array.bookname);
			String[] bookauthors = activity.getResources().getStringArray(R.array.bookauthor);
			
			for(int i=0;i<bookids.length;i++){
				Book book = new Book();
				book.setAuthor(bookauthors[i]);
				book.setBookName(booknames[i]); 
				book.setImgBookId(R.drawable.book0 + i);
				book.setPath(activity.getFilesDir()+"/"+bookids[i]);
				bookStore.list.add(book);
			}
		}
		return bookStore;
	}
   /**
	* @Title: getAllBYBook 
	* @Description: 获取所有包月小说
	* @param @return    设定文件 
	* @date 2015年11月19日 上午12:07:36
	* @author jerry
	* @return List<Book>    返回类型
	* @throws
	 */
	public List<Book> getAllBYBook(){
		return null;
	}
	/**
	* @Title: getAllJPBook 
	* @Description: 获取所有精品推荐小说
	* @param @return    设定文件 
	* @date 2015年11月19日 上午12:07:36
	* @author jerry
	* @return List<Book>    返回类型
	* @throws
	 */
	public List<Book> getAllJPBook(){
		return null;
	}
	
	/**
	* @Title: getMainBYBook 
	* @Description: 获取包月小说用来在书城首页展示
	* @param @return    设定文件 
	* @date 2015年11月19日 上午12:07:36
	* @author jerry
	* @return List<Book>    返回类型
	* @throws
	 */
	public List<Book> getMainBYBook(){
		return null;
	}
	
	/**
	* @Title: getMainJPBook 
	* @Description: 获取所有精品推荐小说
	* @param @return    设定文件 
	* @date 2015年11月19日 上午12:07:36
	* @author jerry
	* @return List<Book>    返回类型
	* @throws
	 */
	public List<Book> getMainJPBook(){
		return null;
	}
	
}
