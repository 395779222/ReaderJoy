/**   
* @Title: BookStore.java
* @Package com.example.readerjoy
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2015年11月18日 下午11:22:06
* @version V1.0   
*/


package com.example.readerjoy;

import java.util.ArrayList;
import java.util.List;

import com.example.readerjoy.entity.Book;
import com.example.readerjoy.entity.BookCategory;

import android.app.Activity;

/**
* @ClassName: BookStore
* @Description: TODO(这里用一句话描述这个类的作用)
* @author jerry
* @date 2015年11月18日 下午11:22:06
* 
*/

public class BookStore {
	
	private  List<Book> list;
	private List<Book>allJPList;
	private List<Book>allJBYist;
	private List<Book>mainJPList;
	private List<Book>mainBYList;
	private List<BookCategory>bookCategoryList;
	private static BookStore bookStore = null;
	private BookStore(){
		
	}
	
	public static BookStore  getInstance(Activity activity){
		if(bookStore == null){
			bookStore = new BookStore();
			bookStore.list = new ArrayList<Book>();
			bookStore.bookCategoryList = new ArrayList<BookCategory>();
			String[] bookids = activity.getResources().getStringArray(R.array.bookid);
			String[] booknames = activity.getResources().getStringArray(R.array.bookname);
			String[] bookauthors = activity.getResources().getStringArray(R.array.bookauthor);
			String[] bookTypes = activity.getResources().getStringArray(R.array.booktype);
			String[] bookMoneys = activity.getResources().getStringArray(R.array.money);
			String[] bookIntroductions = activity.getResources().getStringArray(R.array.bookintroductions);
			String[] cateGoryTypes = activity.getResources().getStringArray(R.array.cateGoryTypes);
			String[] bookIsPurchases = activity.getResources().getStringArray(R.array.bookIsPurchases);
			for(int i=0;i<bookids.length;i++){
				Book book = new Book();
				book.setAuthor(bookauthors[i]);
				book.setBookName(booknames[i]); 
				book.setImgBookId(R.drawable.book0 + i);
				book.setPath(activity.getFilesDir()+"/"+bookids[i]);
				book.setType(Integer.parseInt(bookTypes[i]));
				book.setMoney(Integer.parseInt(bookMoneys[i]));
				book.setIntrdoduction(bookIntroductions[i]);
				book.setCateGoryType(Integer.parseInt(cateGoryTypes[i]));
				book.setBookIsPurchase(0);
				bookStore.list.add(book);
			}
			//初始化图书分类
			String bookCategorys [] ={"玄幻","武侠","都市","历史","游戏","奇幻","仙侠","言情","科幻","更多"};
			for(int i=0;i<bookCategorys.length;i++){
				BookCategory bookCategory = new BookCategory();
				bookCategory.setName(bookCategorys[i]);
				bookCategory.setIndex(i);
				bookStore.bookCategoryList.add(bookCategory);
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
	public List<Book> getAllBook(){
		return bookStore.list;
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
		if(bookStore.allJBYist==null){
			bookStore.allJBYist = new ArrayList<Book>();
			for(Book book:bookStore.list){
				if(book.getType()==1){
					bookStore.allJBYist.add(book);
				}
			}
		}
		return bookStore.allJBYist;
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
		if(bookStore.allJPList==null){
			bookStore.allJPList = new ArrayList<Book>();
			for(Book book:bookStore.list){
				if(book.getType()==0){
					bookStore.allJPList.add(book);
				}
			}
		}
		return bookStore.allJPList;
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
		if(bookStore.mainBYList==null){
			bookStore.mainBYList = new ArrayList<Book>();
			for(Book book:bookStore.list){
				if(book.getType()==1){
					bookStore.mainBYList.add(book);
					if(bookStore.mainBYList.size()==6){
						break;
					}
				}
			}
		}
		return bookStore.mainBYList;
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
		if(bookStore.mainJPList==null){
			bookStore.mainJPList = new ArrayList<Book>();
			for(Book book:bookStore.list){
				if(book.getType()==0){
					bookStore.mainJPList.add(book);
					if(bookStore.mainJPList.size()==6){
						break;
					}
				}
			}
		}
		return bookStore.mainJPList;
	}

	public List<BookCategory> getBookCategoryList() {
		return bookStore.bookCategoryList;
	}

	public void setBookCategoryList(List<BookCategory> bookCategoryList) {
		this.bookCategoryList = bookCategoryList;
	}
	
}
