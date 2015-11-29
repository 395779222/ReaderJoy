package com.example.readerjoy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.readerjoy.activity.BaseActivity;
import com.example.readerjoy.activity.LoginActivity;
import com.example.readerjoy.activity.ReadActivity;
import com.example.readerjoy.activity.SearchActivity;
import com.example.readerjoy.adapter.GridBottomMenuAdapter;
import com.example.readerjoy.base.widget.CustomToast;
import com.example.readerjoy.base.widget.SlidingMenu;
import com.example.readerjoy.db.LocalBook;
import com.example.readerjoy.entity.Book;
import com.example.readerjoy.entity.BottomMenu;
import com.example.readerjoy.fragment.BookListFragment;
import com.example.readerjoy.fragment.BookCityFragment;
import com.example.readerjoy.fragment.BookcaseFragment;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends BaseActivity {
	
	private boolean isInit = false;
	private TextView tvTitle;
	private ImageView btnExit;
	//后退按钮的位置有俩种情况在首页时是目录，在其他地方时后退0：目录 1：后退
	private int btnExitType = 0;
	private ImageView rightSign;
	private TextView rightText;
	private GridView gview_bottom_menu;
	private GridBottomMenuAdapter grid_bottom_adapter;
	private List<BottomMenu> dataMenuList;
	private static final String TAG = "MainActivity";
	private SharedPreferences sp;
	private LocalBook localbook;
	private ArrayList<HashMap<String, Object>> listItem = null;
	public List<Book> listToShowByType;
	private SlidingMenu slidingmenu = null;// 滑动控件
	RelativeLayout mainRelativeLayout;
	private LinearLayout menuLinearLayout = null;// 滑动左侧菜单
	
	private ImageView ico_user;
	private TextView user_name;
	private Button btn_isby;
	private RelativeLayout ktby;
	private RelativeLayout byzq;
	Fragment fragment = null;
	//书架长按要编辑书架
	PopupWindow popupWindowHeader = null; 
	View winPopView;
	boolean winPopHeaderIsShow = false;
	
	private ImageView btnExit_winpop;
	TextView tvTitle_winpop;
	ImageView rightDelete_winpop;
	boolean isEdit = false;
	@Override
	public void bindWidget() {
		//setContentView(R.layout.activity_main);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.activity_main, null);
		mainRelativeLayout = (RelativeLayout) view;
		menuLinearLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.activity_my_center, null);
		slidingmenu = new SlidingMenu(this);
		slidingmenu.addViews(menuLinearLayout, mainRelativeLayout);
		setContentView(slidingmenu);
		
		ico_user =  (ImageView) menuLinearLayout.findViewById(R.id.ico_user);
		user_name =  (TextView) menuLinearLayout.findViewById(R.id.user_name);
		btn_isby =  (Button) menuLinearLayout.findViewById(R.id.btn_isby);
		ktby =  (RelativeLayout) menuLinearLayout.findViewById(R.id.ktby);
		byzq =  (RelativeLayout) menuLinearLayout.findViewById(R.id.byzq);
		
		tvTitle = (TextView)mainRelativeLayout.findViewById(R.id .tvTitle);
		rightSign = (ImageView)mainRelativeLayout.findViewById(R.id .rightSign);
		rightText = (TextView)mainRelativeLayout.findViewById(R.id .rightText);
		btnExit = (ImageView)mainRelativeLayout.findViewById(R.id .btnExit);
		gview_bottom_menu =  (GridView)mainRelativeLayout.findViewById(R.id .gview_bottom_menu);
		initFrame();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		isInit = sp.getBoolean("isInit", false);
		//初始化本地书籍列表
		localbook = new LocalBook(this);
		if (!isInit) {
			new AsyncSetApprove().execute("");
		}
	}

	
	@SuppressLint("NewApi")
	private void initFrame() {
		fragment =  new BookcaseFragment();
		FragmentManager fm = getFragmentManager();  
        FragmentTransaction transaction = fm.beginTransaction();  
        transaction.replace(R.id.fragmentContent, fragment);  
        transaction.commit(); 
		
	}


	@Override
	public void bindAdapter() {
		dataMenuList = new ArrayList<BottomMenu>();
		BottomMenu menu1 = new BottomMenu();
		menu1.setName("书架");
		menu1.setSelected(true);
		menu1.setClassTo(BookcaseFragment.class);
		menu1.setSelectedImg(R.drawable.book_case);
		menu1.setUnSelectedImg(R.drawable.book_case_un);
		BottomMenu menu2 = new BottomMenu();
		menu2.setName("书城");
		menu2.setSelected(false);
		menu2.setSelectedImg(R.drawable.book_city);
		menu2.setUnSelectedImg(R.drawable.book_city_un);
		menu2.setClassTo(BookCityFragment.class);
		dataMenuList.add(menu1);
		dataMenuList.add(menu2);
		grid_bottom_adapter = new GridBottomMenuAdapter(MainActivity.this,dataMenuList);
		gview_bottom_menu.setSelector(R.drawable.grid_selector);
		gview_bottom_menu.setAdapter(grid_bottom_adapter);
		
	}
	
	@Override
	public void bindWidgetEevent() {
		gview_bottom_menu.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//关闭弹出的winpop
				if(winPopHeaderIsShow){
					popupWindowHeader.dismiss();
					if(fragment!=null&&fragment.getClass().getSimpleName().equals("BookcaseFragment")){
						((BookcaseFragment)fragment).canCleEditBook();
					}
					winPopHeaderIsShow = false;
					updateSelectNum(0);
				}
				//showDefault();
				if(position == 0){
					showDefaultBookCase();
				}else{
					showDefaultBookCity();
				}
				BottomMenu curMenu = dataMenuList.get(position);
				for(int i=0;i<dataMenuList.size();i++){
					BottomMenu menu = dataMenuList.get(i);
					if(position == i){
						menu.setSelected(true);
					}
					else{
						menu.setSelected(false);
					}
				}
				grid_bottom_adapter.notifyDataSetChanged();
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
				
				try {
					fragment = (Fragment) curMenu.getClassTo().newInstance();
					fragmentTransaction.replace(R.id.fragmentContent,fragment);
					fragmentTransaction.addToBackStack(null);
					//提交修改
					fragmentTransaction.commit();
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		});
		if (null != btnExit){
			btnExit.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(btnExitType==1){
						updateFragment("back");
					}else{
						slidingmenu.showLeftView();
					}
				}
			});
		}
		// 开通包月
		ktby.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!sp.getBoolean("isBY", false)){
					//如果是登陆了
					if(sp.getBoolean("isLogin", false)){
						CustomToast.getInstance(MainActivity.this).showToast("恭喜您开通包月成功");
						sp.edit().putBoolean("isBY", true).commit();
					}else{
						CustomToast.getInstance(MainActivity.this).showToast("您尚未登录请登录后开通包月");
						//登陆成功跳转到主页
						Intent mIntent = new Intent(MainActivity.this,LoginActivity.class); 
						mIntent.putExtra("isLoginPage", true);
						startActivity(mIntent);
						
					}
				}else{
					CustomToast.getInstance(MainActivity.this).showToast("您已经是包月用户，无需重新开通");
				}
			}
		});
		//包月专区
		byzq.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateFragment("by");
				slidingmenu.showLeftView();
			}
		});
		user_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!sp.getBoolean("isLogin", false)){
					//登陆成功跳转到主页
					Intent mIntent = new Intent(MainActivity.this,LoginActivity.class); 
					mIntent.putExtra("isLoginPage", true);
					startActivity(mIntent);
				}
			}
		});
		rightSign.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//登陆成功跳转到主页
				Intent mIntent = new Intent(MainActivity.this,SearchActivity.class); 
				startActivity(mIntent);
			}
		});
		rightText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(rightText.getText().equals("编辑")){
					if(!winPopHeaderIsShow){
						if(popupWindowHeader==null){
							creatPopupWindowHeader();
							((BookcaseFragment)fragment).editBookCase();
							isEdit = true;
							winPopHeaderIsShow = true;
							//popupWindowHeader.dismiss();
							
						}else{
							popupWindowHeader.showAtLocation(mainRelativeLayout.findViewById(R.id.relativeLayout_parent), Gravity.TOP,
									0, 50);
							((BookcaseFragment)fragment).editBookCase();
							isEdit = true;
							winPopHeaderIsShow = true;
							
						}
						
					}
				}
				isEdit = true;
			}
		});
	}

	
	@SuppressLint("NewApi")
	@Override
	public void process() {
		/*tvTitle.setText("芝士阅读");
		btnExit.setImageResource(R.drawable.menu);
		rightSign.setVisibility(View.VISIBLE);*/
		showDefaultBookCase();
		if(sp.getBoolean("isLogin", false)){
			ico_user.setBackgroundResource(R.drawable.alreay_login);
			user_name.setText(sp.getString("name", ""));
			if(!sp.getBoolean("isBY", false)){
				btn_isby.setText("未包月");
			}else{
				btn_isby.setText("包月");
			}
			
		}else{
			ico_user.setBackgroundResource(R.drawable.unlogin);
			user_name.setText(Html.fromHtml("<u>"+"登陆"+"</u>"));
			btn_isby.setText("未包月");
		}
	}
	
	class AsyncSetApprove extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			if (!isInit) {
				File path = getFilesDir();
				String[] strings = getResources().getStringArray(R.array.bookid);// 获取assets目录下的文件列表
				for (int i = 0; i < strings.length; i++) {
					try {
						FileOutputStream out = new FileOutputStream(path + "/" + strings[i]);
						BufferedInputStream bufferedIn = new BufferedInputStream(getResources().openRawResource(R.raw.book0 + i));
						BufferedOutputStream bufferedOut = new BufferedOutputStream(out);
						byte[] data = new byte[2048];
						int length = 0;
						while ((length = bufferedIn.read(data)) != -1) {
							bufferedOut.write(data, 0, length);
						}
						// 将缓冲区中的数据全部写出
						bufferedOut.flush();
						// 关闭流
						bufferedIn.close();
						bufferedOut.close();
						sp.edit().putBoolean("isInit", true).commit();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				ArrayList<HashMap<String, String>> insertList = new ArrayList<HashMap<String, String>>();
				File[] f1 = path.listFiles();
				int len = f1.length;
				for (int i = 0; i < len; i++) {
					if (f1[i].isFile()) {
						if (f1[i].toString().contains(".txt")) {
							HashMap<String, String> insertMap = new HashMap<String, String>();
							insertMap.put("parent", f1[i].getParent());
							insertMap.put("path", f1[i].toString());
							insertList.add(insertMap);
						}
					}
				}
				SQLiteDatabase db = localbook.getWritableDatabase();
				List<Book>dataList = BookStore.getInstance(MainActivity.this).getAllBook();
				//加载所有书籍入库
				for(Book book : dataList){
					String sql1 = "insert into " + "localbook" + " (bookName,imgBookId,path,author,intrdoduction,type,money,isRead,bookIsPurchase"
							+ ") values('" + book.getBookName() + "','" + book.getImgBookId() + "','" + book.getPath() + "','" + book.getAuthor() + "','" + book.getIntrdoduction() + "','" + book.getType() + "','" + book.getMoney() + "','0','0');";
					db.execSQL(sql1);
				}
				db.close();
				/*db.delete("localbook", "type='" + 2 + "'", null);

				for (int i = 0; i < insertList.size(); i++) {
					try {
						if (insertList.get(i) != null) {
							String s = insertList.get(i).get("parent");
							String s1 = insertList.get(i).get("path");
							String sql1 = "insert into " + "localbook" + " (parent,path" + ", type" + ",now,ready) values('" + s + "','" + s1 + "',2,0,null" + ");";
							db.execSQL(sql1);
						}
					} catch (SQLException e) {
						Log.e(TAG, "setApprove SQLException", e);
					} catch (Exception e) {
						Log.e(TAG, "setApprove Exception", e);
					}
				}
				db.close();*/
			}
			isInit = true;
			sp.edit().putBoolean("isInit", true).commit();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
		}
	}
	
	/**
	 * 
	* @Title: updateFragment 
	* @Description: TODO(Fragment内部操作，更新父类的UI,包括书城的精品推荐，变更头部标题等) 
	* @param @param type    设定文件 
	* @date 2015年11月23日 上午11:53:29
	* @author jerry
	* @return void    返回类型
	* @throws
	 */
	@SuppressLint("NewApi")
	public void updateFragment(String type){
		
		//书城内点击包月专区
		if(type.equals("by")){
			fragment = new BookListFragment();
			listToShowByType = BookStore.getInstance(MainActivity.this).getAllBYBook();
			tvTitle.setText("包月专区");
			rightText.setVisibility(View.VISIBLE);
			showSecond();
		}
		//书城内点击精品专区
		else if(type.equals("jp")){
			fragment = new BookListFragment();
			listToShowByType = BookStore.getInstance(MainActivity.this).getAllJPBook();
			tvTitle.setText("精品推荐");
			rightText.setVisibility(View.GONE);
			showSecond();
		}
		//精品专区，或者包月专区页面点击返回按钮，直接返回书城首页
		else if(type.equals("back")){
			//书城变为选中
			dataMenuList.get(0).setSelected(false);
			dataMenuList.get(1).setSelected(true);
			grid_bottom_adapter.notifyDataSetChanged();
			fragment = new BookCityFragment();
			showDefaultBookCity();
			
		}
		//精品专区，或者包月专区页面点击返回按钮，直接返回书城首页
		else if(type.equals("bookCase")){
			//书城变为选中
			dataMenuList.get(0).setSelected(true);
			dataMenuList.get(1).setSelected(false);
			grid_bottom_adapter.notifyDataSetChanged();
			fragment = new BookcaseFragment();
			showDefaultBookCase();
			
		}
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
		try {
			fragmentTransaction.replace(R.id.fragmentContent,fragment);
			fragmentTransaction.addToBackStack(null);
			//提交修改
			fragmentTransaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}


	/** 
	* @Title: showDefaultBooCity 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月24日 下午5:49:23
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void showDefaultBookCity() {
		tvTitle.setText("芝士阅读");
		btnExit.setImageResource(R.drawable.menu);
		rightText.setVisibility(View.GONE);
		btnExitType = 0;
		rightSign.setVisibility(View.VISIBLE);
	}

	/** 
	* @Title: showSecond 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月23日 下午3:24:06
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void showSecond() {
		btnExit.setImageResource(R.drawable.ico_back);
		btnExitType = 1;
	
		rightSign.setVisibility(View.GONE);
	}
	/** 
	* @Title: showDefault 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月23日 下午3:22:37
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void showDefaultBookCase() {
		tvTitle.setText("芝士阅读");
		btnExit.setImageResource(R.drawable.menu);
		rightText.setText("编辑");
		rightText.setVisibility(View.VISIBLE);
		btnExitType = 0;
		rightSign.setVisibility(View.GONE);
		//rightSign.setVisibility(View.VISIBLE);
	}
	/**
	 * 
	* @Title: creatPopupWindowHeader 
	* @Description: TODO(创建头部书架编辑弹出 ) 
	* @param     设定文件 
	* @date 2015年11月24日 下午9:40:23
	* @author jerry
	* @return void    返回类型
	* @throws
	 */
	private void creatPopupWindowHeader(){
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		winPopView = layoutInflater.inflate(R.layout.edit_book_case_header, null);
	    btnExit_winpop = (ImageView) winPopView.findViewById(R.id.btnExit_winpop);
	    tvTitle_winpop = (TextView) winPopView.findViewById(R.id.tvTitle_winpop);
	    rightDelete_winpop = (ImageView) winPopView.findViewById(R.id.rightDelete_winpop);
		popupWindowHeader = new PopupWindow(winPopView,LayoutParams.FILL_PARENT,100);
		//popupWindowHeader.showAtLocation(mPageWidget, Gravity.TOP, 0, 0);
		// 使其聚集
		popupWindowHeader.setFocusable(false);
		popupWindowHeader.setTouchable(true); // 设置PopupWindow可触摸
		// 设置允许在外点击消失
		popupWindowHeader.setOutsideTouchable(true);
		popupWindowHeader.showAtLocation(mainRelativeLayout.findViewById(R.id.relativeLayout_parent), Gravity.TOP,
				0, 50);
		//popupWindowHeader.setBackgroundDrawable(new BitmapDrawable());
		//popupWindowHeader.showAsDropDown(mainRelativeLayout.findViewById(R.id.relativeLayout_parent), xPos, 0);
		initPopEvent();
	}
	
	/** 
	* @Title: initPopEvent 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月25日 下午12:29:48
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void initPopEvent(){
		//popupWindowHeader后退执行的操作
		if(btnExit_winpop!=null){
			btnExit_winpop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					isEdit = false;
					if(popupWindowHeader!=null){
						popupWindowHeader.dismiss();
						((BookcaseFragment)fragment).canCleEditBook();
						winPopHeaderIsShow = false;
					}
					updateSelectNum(0);
				}
			});
		}
		if(rightDelete_winpop!=null){
			rightDelete_winpop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					isEdit = false;
					if(popupWindowHeader!=null){
						popupWindowHeader.dismiss();
						((BookcaseFragment)fragment).delete();
						CustomToast.getInstance(MainActivity.this).showToast("移除成功");
						winPopHeaderIsShow = false;
					}
					updateSelectNum(0);
				}
			});
			
		}
	}


	/**
	 * 
	* @Title: updateSelectNum 
	* @Description: TODO(书架移除的时候变更信息) 
	* @param @param num    设定文件 
	* @date 2015年11月25日 下午12:18:04
	* @author jerry
	* @return void    返回类型
	* @throws
	 */
	public void updateSelectNum(int num){
		if(tvTitle_winpop!=null){
			tvTitle_winpop.setText("选中了"+num+" 项");
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(fragment!=null&&fragment.getClass().getSimpleName().equals("BookcaseFragment")){
			initFrame();
		}
	}
	/**
	 * 
	* @Title: isBookCase 
	* @Description: TODO(判断当前是书城还是，书架) 
	* @param @return    设定文件 
	* @date 2015年11月28日 下午8:05:07
	* @author jerry
	* @return boolean    返回类型
	* @throws
	 */
	public boolean isBookCase(){
		if(dataMenuList.get(0).isSelected()){
			return true;
		}
		return false;
	}


	public boolean isEdit() {
		return isEdit;
	}


	public void setEdit(boolean isEdit) {
		this.isEdit = isEdit;
	}
	
}
