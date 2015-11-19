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
import com.example.readerjoy.adapter.GridBottomMenuAdapter;
import com.example.readerjoy.db.LocalBook;
import com.example.readerjoy.entity.BottomMenu;
import com.example.readerjoy.fragment.BookcaseFragment;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;

public class MainActivity extends BaseActivity {
	
	private boolean isInit = false;
	private TextView tvTitle;
	private ImageView btnExit;
	private GridView gview_bottom_menu;
	private GridBottomMenuAdapter grid_bottom_adapter;
	private List<BottomMenu> dataMenuList;
	
	private static final String TAG = "MainActivity";
	private SharedPreferences sp;
	private LocalBook localbook;
	private ArrayList<HashMap<String, Object>> listItem = null;
	@Override
	public void bindWidget() {
		setContentView(R.layout.activity_main);
		tvTitle = (TextView)findViewById(R.id .tvTitle);
		btnExit = (ImageView)findViewById(R.id .btnExit);
		gview_bottom_menu =  (GridView)findViewById(R.id .gview_bottom_menu);
		initFrame();
		sp = getSharedPreferences("mark", MODE_PRIVATE);
		isInit = sp.getBoolean("isInit", false);
		//初始化本地书籍列表
		localbook = new LocalBook(this, "localbook");
		if (!isInit) {
			new AsyncSetApprove().execute("");
		}
	}

	
	@SuppressLint("NewApi")
	private void initFrame() {
		FragmentManager fm = getFragmentManager();  
        FragmentTransaction transaction = fm.beginTransaction();  
        transaction.replace(R.id.fragmentContent, new BookcaseFragment());  
        transaction.commit(); 
		
	}


	@Override
	public void bindAdapter() {
		dataMenuList = new ArrayList<BottomMenu>();
		
		
		BottomMenu menu1 = new BottomMenu();
		menu1.setName("书架");
		menu1.setSelected(true);
		menu1.setClassTo(BookcaseFragment.class);
		BottomMenu menu2 = new BottomMenu();
		menu2.setName("书城");
		dataMenuList.add(menu1);
		dataMenuList.add(menu2);
	
		grid_bottom_adapter = new GridBottomMenuAdapter(MainActivity.this,dataMenuList);
		gview_bottom_menu.setAdapter(grid_bottom_adapter);
		
	}
	
	@Override
	public void bindWidgetEevent() {
		gview_bottom_menu.setOnItemClickListener(new OnItemClickListener() {
			@SuppressLint("NewApi")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*BottomMenu menu = dataMenuList.get(position);
				FragmentManager fragmentManager = getFragmentManager();
				FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
				try {
					fragmentTransaction.replace(R.id.fragmentContent,(Fragment) menu.getClassTo().newInstance());
					fragmentTransaction.addToBackStack(null);
					//提交修改
					fragmentTransaction.commit();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}*/
			}});
		
	}

	
	@Override
	public void process() {
		
		
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
				db.delete("localbook", "type='" + 2 + "'", null);

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
				db.close();
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
	
	
}
