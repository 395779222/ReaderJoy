package com.example.readerjoy.activity;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.example.readerjoy.BookPageFactory;
import com.example.readerjoy.MainActivity;
import com.example.readerjoy.PageWidget;
import com.example.readerjoy.R;
import com.example.readerjoy.base.widget.CustomToast;
import com.example.readerjoy.dialog.MarkDialog;
import com.example.readerjoy.entity.Book;
import com.example.readerjoy.helper.MarkHelper;
import com.example.readerjoy.vo.MarkVo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ReadActivity extends Activity  implements OnClickListener, OnSeekBarChangeListener{
	private static final String TAG = "Read2";
	private static int begin = 0;// 记录的书籍开始位置
	public static Canvas mCurPageCanvas, mNextPageCanvas;
	private static String word = "";// 记录当前页面的文字
	private int a = 0, b = 0;// 记录toolpop的位置
	private TextView bookBtn1, bookBtn2, bookBtn3, bookBtn4;
	private Book book;
	private String ccc = null;// 记录是否为快捷方式调用
	protected long count = 1;
	private SharedPreferences.Editor editor;
	private ImageButton imageBtn2, imageBtn3_1, imageBtn3_2;
	private ImageButton imageBtn4_1, imageBtn4_2;
	private Boolean isNight; // 亮度模式,白天和晚上
	protected int jumpPage;// 记录跳转进度条
	private int light; // 亮度值
	private WindowManager.LayoutParams lp;
	private TextView markEdit4;
	private MarkHelper markhelper;
	private Bitmap mCurPageBitmap, mNextPageBitmap;
	private MarkDialog mDialog = null;
	private Context mContext = null;
	private PageWidget mPageWidget;
	private PopupWindow mPopupWindow, mToolpop, mToolpop1, mToolpop2, mToolpop3, mToolpop4;
	//头部
	PopupWindow popupWindowHeader = null; 
	View winPopView;
	private ImageView btnExit_winpop;
	TextView tvTitle_winpop;
	ImageView rightDelete_winpop;
	
	protected int PAGE = 1;
	private BookPageFactory pagefactory;
	private View popupwindwow, toolpop, toolpop1, toolpop2, toolpop3, toolpop4;
	int screenHeight;
	int readHeight; // 电子书显示高度
	int screenWidth;
	private SeekBar seekBar1, seekBar2, seekBar4;
	private Boolean show = false;// popwindow是否显示
	private int size = 30; // 字体大小
	private SharedPreferences sp;
	int defaultSize = 0;
	
	boolean isBY = false;
	boolean bookIsPurchase = false;
	boolean isLogin;
	// 实例化Handler
	public Handler mHandler = new Handler() {
		// 接收子线程发来的消息，同时更新UI
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				begin = msg.arg1;
				pagefactory.setM_mbBufBegin(begin);
				pagefactory.setM_mbBufEnd(begin);
				postInvalidateUI();
				break;
			case 1:
				pagefactory.setM_mbBufBegin(begin);
				pagefactory.setM_mbBufEnd(begin);
				postInvalidateUI();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * 记录数据 并清空popupwindow
	 */
	private void clear() {
		closeWinPop();
	}

	/**
	 * 读取配置文件中亮度值
	 */
	private void getLight() {
		light = sp.getInt("light", 5);
		isNight = sp.getBoolean("night", false);
	}

	/**
	 * 读取配置文件中字体大小
	 */
	private void getSize() {
		size = sp.getInt("size", defaultSize);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 字体按钮
		case R.id.bookBtn1:
			a = 1;
			setToolPop(a);
			break;
		// 亮度按钮
		case R.id.bookBtn2:
			a = 2;
			setToolPop(a);
			break;
		// 书签按钮
		case R.id.bookBtn3:
			a = 3;
			setToolPop(a);
			break;
		// 跳转按钮
		case R.id.bookBtn4:
			a = 4;
			setToolPop(a);
			break;

		// 夜间模式按钮
		case R.id.imageBtn2:
			if (isNight) {
				pagefactory.setM_textColor(Color.parseColor("#555555"));
				imageBtn2.setImageResource(R.drawable.reader_switch_off);
				isNight = false;
				pagefactory.setNight(false);
				//pagefactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.color.white));
			} else {
				pagefactory.setM_textColor(Color.rgb(128, 128, 128));
				imageBtn2.setImageResource(R.drawable.reader_switch_on);
				isNight = true;
				pagefactory.setNight(true);
				//pagefactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.main_bg));
			}
			setLight();
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			postInvalidateUI();
			break;
		// 添加书签按钮
		case R.id.imageBtn3_1:
			SQLiteDatabase db = markhelper.getWritableDatabase();
			try {
				SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm ss");
				String time = sf.format(new Date());
				db.execSQL("insert into markhelper (path ,begin,word,time) values (?,?,?,?)", new String[] { book.getPath(), begin + "", word, time });
				db.close();
				Toast.makeText(ReadActivity.this, "书签添加成功", Toast.LENGTH_SHORT).show();
			} catch (SQLException e) {
				Toast.makeText(ReadActivity.this, "该书签已存在", Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				Toast.makeText(ReadActivity.this, "添加书签失败", Toast.LENGTH_SHORT).show();
			}
			mToolpop.dismiss();
			mToolpop3.dismiss();
			break;
		// 我的书签按钮
		case R.id.imageBtn3_2:
			SQLiteDatabase dbSelect = markhelper.getReadableDatabase();
			String col[] = { "begin", "word", "time" };
			Cursor cur = dbSelect.query("markhelper", col, "path = '" + book.getPath() + "'", null, null, null, null);
			Integer num = cur.getCount();
			if (num == 0) {
				Toast.makeText(ReadActivity.this, "您还没有书签", Toast.LENGTH_SHORT).show();
			} else {
				ArrayList<MarkVo> markList = new ArrayList<MarkVo>();
				while (cur.moveToNext()) {
					String s1 = cur.getString(cur.getColumnIndex("word"));
					String s2 = cur.getString(cur.getColumnIndex("time"));
					int b1 = cur.getInt(cur.getColumnIndex("begin"));
					int p = 0;
					int count = 10;
					MarkVo mv = new MarkVo(s1, p, count, b1, s2, book.getPath());
					markList.add(mv);
				}
				mDialog = new MarkDialog(this, markList, mHandler, R.style.FullHeightDialog);

				mDialog.setCancelable(false);

				mDialog.setTitle("我的书签");
				mDialog.show();
			}
			dbSelect.close();
			cur.close();
			mToolpop.dismiss();
			mToolpop3.dismiss();
			break;
		case R.id.imageBtn4_1:
			clear();
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			postInvalidateUI();
			break;
		case R.id.imageBtn4_2:
			clear();
			break;
		}
	}

	@SuppressLint("WrongCall")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		mContext = getBaseContext();

		WindowManager manage = getWindowManager();
		Display display = manage.getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		defaultSize = (screenWidth * 20) / 380;
		readHeight = screenHeight - (50 * screenWidth) / 320;

		mCurPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mNextPageBitmap = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
		mCurPageCanvas = new Canvas(mCurPageBitmap);
		mNextPageCanvas = new Canvas(mNextPageBitmap);

		mPageWidget = new PageWidget(this, screenWidth, readHeight);// 页面
		setContentView(R.layout.activity_read);
		RelativeLayout rlayout = (RelativeLayout) findViewById(R.id.readlayout);
		rlayout.addView(mPageWidget);

		Intent intent = getIntent();
		book = (Book) intent.getSerializableExtra("book");
		ccc = intent.getStringExtra("ccc");
		mPageWidget.setBitmaps(mCurPageBitmap, mCurPageBitmap);
		// 提取记录在sharedpreferences的各种状态
		sp = getSharedPreferences("config", MODE_PRIVATE);
		editor = sp.edit();
		getSize();// 获取配置文件中的size大小
		getLight();// 获取配置文件中的light值
		count = sp.getLong(book.getPath() + "count", 1);
		//获取是否是包月
		isBY = sp.getBoolean("isBY", false);
		//该本书是否已经被购买
		bookIsPurchase = sp.getBoolean(book.getPath()+"bookIsPurchase", false); 	
		isLogin = sp.getBoolean("isLogin", false); 	
		
		mPageWidget.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("WrongCall")
			@Override
			public boolean onTouch(View v, MotionEvent e) {
				if (v == mPageWidget) {
					if (!show) {
						pagefactory.onDraw(mCurPageCanvas);
						if (e.getAction() == MotionEvent.ACTION_DOWN) {		
							mPageWidget.doTouchEventDown(e);
						}else if(e.getAction() == MotionEvent.ACTION_UP){
							int dragType = mPageWidget.doTouchEventUp(e);
							if(dragType==0){
								dealWinPop();
								return false;
							}else if(dragType==-1){
								try {
									pagefactory.prePage();
									begin = pagefactory.getM_mbBufBegin();// 获取当前阅读位置
									word = pagefactory.getFirstLineText();// 获取当前阅读位置的首行文字
								} catch (IOException e1) {
									Log.e(TAG, "onTouch->prePage error", e1);
								}
								if (pagefactory.isfirstPage()) {
									Toast.makeText(mContext, "当前是第一页", Toast.LENGTH_SHORT).show();
									return false;
								}
								//画下个页面
								pagefactory.onDraw(mNextPageCanvas);
								mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
								mPageWidget.startAnimation(1200);
								//页面更新,会调用onDraw()l
								mPageWidget.postInvalidate();
							}else{
								if(pagefactory.getfPercent()>0.05){
									
									if(!checkHasReadRole()){
										return false;
									}
								}
								
								try {
									pagefactory.nextPage();
									begin = pagefactory.getM_mbBufBegin();// 获取当前阅读位置
									word = pagefactory.getFirstLineText();// 获取当前阅读位置的首行文字
								} catch (IOException e1) {
									Log.e(TAG, "onTouch->nextPage error", e1);
								}
								if (pagefactory.islastPage()) {
									Toast.makeText(mContext, "已经是最后一页了", Toast.LENGTH_SHORT).show();
									return false;
								}
								pagefactory.onDraw(mNextPageCanvas);
								mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
								//mPageWidget.startAnimation(1200);
								//页面更新,会调用onDraw()l
								mPageWidget.postInvalidate();
							}
							editor.putInt(book.getPath() + "begin", begin).commit();
							DecimalFormat df = new DecimalFormat("#0.0");
							String strPercent = df.format(pagefactory.getfPercent() * 100) + "%";
							editor.putString(book.getPath() + "fPercent", strPercent);
						}
							
					}
					else{
						closeWinPop();
						return false;
					}
				}
				return true;
	
			}
		
		});
		
		setPop();
		lp = getWindow().getAttributes();
		lp.screenBrightness = light / 10.0f < 0.01f ? 0.01f : light / 10.0f;
		getWindow().setAttributes(lp);
		pagefactory = new BookPageFactory(screenWidth, readHeight);// 书工厂
		
		if (isNight) {
			//pagefactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.main_bg));
			pagefactory.setNight(true);
			pagefactory.setM_textColor(Color.rgb(128, 128, 128));
		} else {
			//pagefactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.color.white));
			pagefactory.setNight(false);
			pagefactory.setM_textColor(Color.parseColor("#555555"));
		}
		begin = sp.getInt(book.getPath() + "begin", 0);
		try {
			pagefactory.openbook(book.getPath(), begin);// 从指定位置打开书籍，默认从开始打开
			pagefactory.setM_fontSize(size);
			pagefactory.onDraw(mCurPageCanvas);
		} catch (IOException e1) {
			Log.e(TAG, "打开电子书失败", e1);
			Toast.makeText(this, "打开电子书失败", Toast.LENGTH_SHORT).show();
		}

		markhelper = new MarkHelper(this);

	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		pagefactory = null;
		mPageWidget = null;
		finish();
	}

	/**
	 * 判断是从哪个界面进入的READ
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (ccc == null) {
				if (show) {// 如果popwindow正在显示
					popDismiss();
					//getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
					show = false;
					mPopupWindow.dismiss();
					popupWindowHeader.dismiss();
				} else {
					ReadActivity.this.finish();
				}
			} else {
				if (!ccc.equals("ccc")) {
					if (show) {// 如果popwindow正在显示
						closeWinPop();
					} else {
						this.finish();
					}
				} else {
					this.finish();
				}
			}
		}
		return true;
	}

	/**
	 * 添加对menu按钮的监听
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_MENU) {
			
			dealWinPop();
			
		}
		return super.onKeyUp(keyCode, event);
	}

	/** 
	* @Title: dealWinPop 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月26日 上午10:59:49
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void dealWinPop() {
		if (show) {
			closeWinPop();

		} else {
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			show = true;

			pop();
		}
		
	}

	/** 
	* @Title: closeWinPop 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param     设定文件 
	* @date 2015年11月24日 下午1:03:59
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	private void closeWinPop() {
		//getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		show = false;
		mPopupWindow.dismiss();
		popupWindowHeader.dismiss();
		popDismiss();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		switch (seekBar.getId()) {
		// 字体进度条
		case R.id.seekBar1:
			size = seekBar1.getProgress() + 15;
			setSize();
			pagefactory.setM_fontSize(size);
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			postInvalidateUI();
			break;
		// 亮度进度条
		case R.id.seekBar2:
			light = seekBar2.getProgress();
			setLight();
			lp.screenBrightness = light / 10.0f < 0.01f ? 0.01f : light / 10.0f;
			getWindow().setAttributes(lp);
			break;
		// 跳转进度条
		case R.id.seekBar4:
			int s = seekBar4.getProgress();
			markEdit4.setText(s + "%");
			begin = (pagefactory.getM_mbBufLen() * s) / 100;
			editor.putInt(book.getPath() + "begin", begin).commit();
			pagefactory.setM_mbBufBegin(begin);
			pagefactory.setM_mbBufEnd(begin);
			try {
				if (s == 100) {
					pagefactory.prePage();
					pagefactory.getM_mbBufBegin();
					begin = pagefactory.getM_mbBufEnd();
					pagefactory.setM_mbBufBegin(begin);
					pagefactory.setM_mbBufBegin(begin);
				}
			} catch (IOException e) {
				Log.e(TAG, "onProgressChanged seekBar4-> IOException error", e);
			}
			postInvalidateUI();
			break;
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

	/**
	 * popupwindow的弹出,工具栏
	 */
	public void pop() {
		mPopupWindow.setFocusable(false);
		mPopupWindow.setTouchable(true); // 设置PopupWindow可触摸
		mPopupWindow.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, 0);
		bookBtn1 = (TextView) popupwindwow.findViewById(R.id.bookBtn1);
		bookBtn2 = (TextView) popupwindwow.findViewById(R.id.bookBtn2);
		bookBtn3 = (TextView) popupwindwow.findViewById(R.id.bookBtn3);
		bookBtn4 = (TextView) popupwindwow.findViewById(R.id.bookBtn4);
		bookBtn1.setOnClickListener(this);
		bookBtn2.setOnClickListener(this);
		bookBtn3.setOnClickListener(this);
		bookBtn4.setOnClickListener(this);
		
		popupWindowHeader.showAtLocation(findViewById(R.id.relativeLayout_parent), Gravity.TOP,0, 0);
	}

	/**
	 * 关闭55个弹出pop
	 */
	public void popDismiss() {
		mToolpop.dismiss();
		mToolpop1.dismiss();
		mToolpop2.dismiss();
		mToolpop3.dismiss();
		mToolpop4.dismiss();
	}

	/**
	 * 记录配置文件中亮度值和横竖屏
	 */
	private void setLight() {
		try {
			light = seekBar2.getProgress();
			editor.putInt("light", light);
			if (isNight) {
				editor.putBoolean("night", true);
			} else {
				editor.putBoolean("night", false);
			}
			editor.commit();
		} catch (Exception e) {
			Log.e(TAG, "setLight-> Exception error", e);
		}
	}

	/**
	 * 初始化所有POPUPWINDOW
	 */
	private void setPop() {
		popupwindwow = this.getLayoutInflater().inflate(R.layout.bookpop, null);
		toolpop = this.getLayoutInflater().inflate(R.layout.toolpop, null);
		mPopupWindow = new PopupWindow(popupwindwow, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		mToolpop = new PopupWindow(toolpop, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toolpop1 = this.getLayoutInflater().inflate(R.layout.tool11, null);
		mToolpop1 = new PopupWindow(toolpop1, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toolpop2 = this.getLayoutInflater().inflate(R.layout.tool22, null);
		mToolpop2 = new PopupWindow(toolpop2, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toolpop3 = this.getLayoutInflater().inflate(R.layout.tool33, null);
		mToolpop3 = new PopupWindow(toolpop3, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		toolpop4 = this.getLayoutInflater().inflate(R.layout.tool44, null);
		mToolpop4 = new PopupWindow(toolpop4, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		
		creatPopupWindowHeader();
	}

	/**
	 * 记录配置文件中字体大小
	 */
	private void setSize() {
		try {
			size = seekBar1.getProgress() + 15;
			editor.putInt("size", size);
			editor.commit();
		} catch (Exception e) {
			Log.e(TAG, "setSize-> Exception error", e);
		}
	}

	/**
	 * 设置popupwindow的显示与隐藏
	 * 
	 * @param a
	 */
	public void setToolPop(int a) {
		if (a == b && a != 0) {
			if (mToolpop.isShowing()) {
				popDismiss();
			} else {
				mToolpop.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				// 当点击字体按钮
				if (a == 1) {
					mToolpop1.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
					seekBar1 = (SeekBar) toolpop1.findViewById(R.id.seekBar1);
					size = sp.getInt("size", 20);
					seekBar1.setProgress((size - 15));
					seekBar1.setOnSeekBarChangeListener(this);
				}
				// 当点击亮度按钮
				if (a == 2) {
					mToolpop2.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
					seekBar2 = (SeekBar) toolpop2.findViewById(R.id.seekBar2);
					imageBtn2 = (ImageButton) toolpop2.findViewById(R.id.imageBtn2);
					getLight();

					seekBar2.setProgress(light);
					if (isNight) {
						imageBtn2.setImageResource(R.drawable.reader_switch_on);
					} else {
						imageBtn2.setImageResource(R.drawable.reader_switch_off);
					}
					imageBtn2.setOnClickListener(this);
					seekBar2.setOnSeekBarChangeListener(this);
				}
				// 当点击书签按钮
				if (a == 3) {
					mToolpop3.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, toolpop.getHeight());
					imageBtn3_1 = (ImageButton) toolpop3.findViewById(R.id.imageBtn3_1);
					imageBtn3_2 = (ImageButton) toolpop3.findViewById(R.id.imageBtn3_2);
					imageBtn3_1.setOnClickListener(this);
					imageBtn3_2.setOnClickListener(this);
				}
				// 当点击跳转按钮
				if (a == 4) {
					mToolpop4.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
					imageBtn4_1 = (ImageButton) toolpop4.findViewById(R.id.imageBtn4_1);
					imageBtn4_2 = (ImageButton) toolpop4.findViewById(R.id.imageBtn4_2);
					seekBar4 = (SeekBar) toolpop4.findViewById(R.id.seekBar4);
					markEdit4 = (TextView) toolpop4.findViewById(R.id.markEdit4);
					// begin = sp.getInt(bookPath + "begin", 1);
					float fPercent = (float) (begin * 1.0 / pagefactory.getM_mbBufLen());
					DecimalFormat df = new DecimalFormat("#0");
					String strPercent = df.format(fPercent * 100) + "%";
					markEdit4.setText(strPercent);
					seekBar4.setProgress(Integer.parseInt(df.format(fPercent * 100)));
					seekBar4.setOnSeekBarChangeListener(this);
					imageBtn4_1.setOnClickListener(this);
					imageBtn4_2.setOnClickListener(this);
				}
			}
		} else {
			if (mToolpop.isShowing()) {
				// 对数据的记录
				popDismiss();
			}
			mToolpop.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
			// 点击字体按钮
			if (a == 1) {
				mToolpop1.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				seekBar1 = (SeekBar) toolpop1.findViewById(R.id.seekBar1);
				size = sp.getInt("size", 20);
				seekBar1.setProgress(size - 15);
				seekBar1.setOnSeekBarChangeListener(this);
			}
			// 点击亮度按钮
			if (a == 2) {
				mToolpop2.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				seekBar2 = (SeekBar) toolpop2.findViewById(R.id.seekBar2);
				imageBtn2 = (ImageButton) toolpop2.findViewById(R.id.imageBtn2);
				getLight();
				seekBar2.setProgress(light);

				if (isNight) {
					//pagefactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.drawable.main_bg));
					pagefactory.setNight(true);
				} else {
					pagefactory.setNight(false);
					//pagefactory.setBgBitmap(BitmapFactory.decodeResource(this.getResources(), R.color.white));
				}

				if (isNight) {
					imageBtn2.setImageResource(R.drawable.reader_switch_on);
				} else {
					imageBtn2.setImageResource(R.drawable.reader_switch_off);
				}
				imageBtn2.setOnClickListener(this);
				seekBar2.setOnSeekBarChangeListener(this);
			}
			// 点击书签按钮
			if (a == 3) {
				mToolpop3.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				imageBtn3_1 = (ImageButton) toolpop3.findViewById(R.id.imageBtn3_1);
				imageBtn3_2 = (ImageButton) toolpop3.findViewById(R.id.imageBtn3_2);
				imageBtn3_1.setOnClickListener(this);
				imageBtn3_2.setOnClickListener(this);
			}
			// 点击跳转按钮
			if (a == 4) {
				mToolpop4.showAtLocation(mPageWidget, Gravity.BOTTOM, 0, screenWidth * 45 / 320);
				imageBtn4_1 = (ImageButton) toolpop4.findViewById(R.id.imageBtn4_1);
				imageBtn4_2 = (ImageButton) toolpop4.findViewById(R.id.imageBtn4_2);
				seekBar4 = (SeekBar) toolpop4.findViewById(R.id.seekBar4);
				markEdit4 = (TextView) toolpop4.findViewById(R.id.markEdit4);
				// jumpPage = sp.getInt(bookPath + "jumpPage", 1);
				float fPercent = (float) (begin * 1.0 / pagefactory.getM_mbBufLen());
				DecimalFormat df = new DecimalFormat("#0");
				String strPercent = df.format(fPercent * 100) + "%";
				markEdit4.setText(strPercent);
				seekBar4.setProgress(Integer.parseInt(df.format(fPercent * 100)));
				seekBar4.setOnSeekBarChangeListener(this);
				imageBtn4_1.setOnClickListener(this);
				imageBtn4_2.setOnClickListener(this);
			}
		}
		// 记录上次点击的是哪一个
		b = a;
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 刷新界面
	 */
	@SuppressLint("WrongCall")
	public void postInvalidateUI() {
		mPageWidget.abortAnimation();
		pagefactory.onDraw(mCurPageCanvas);
		try {
			pagefactory.currentPage();
			begin = pagefactory.getM_mbBufBegin();// 获取当前阅读位置
			word = pagefactory.getFirstLineText();// 获取当前阅读位置的首行文字
		} catch (IOException e1) {
			Log.e(TAG, "postInvalidateUI->IOException error", e1);
		}

		pagefactory.onDraw(mNextPageCanvas);

		mPageWidget.setBitmaps(mCurPageBitmap, mNextPageBitmap);
		mPageWidget.postInvalidate();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	private boolean checkHasReadRole(){
		if(book.getType()==1){
			if(isLogin){
				if(isBY){
					return true;
				}
				else{
					if(bookIsPurchase){
						return true;
					}
					else{
						CustomToast.getInstance(ReadActivity.this).showToast("您尚未购买此书");
						jumpToPuchase();
						return false;
					}
				}
			}
			else{
				CustomToast.getInstance(ReadActivity.this).showToast("您尚未登录");
				jumpToLogin();
				return false;
			}
		}
		else{
			return true;
		}
		
	}
	
	private void jumpToLogin() {
		//登陆成功跳转到主页
		Intent mIntent = new Intent(ReadActivity.this,LoginActivity.class); 
		mIntent.putExtra("isLoginPage", true);
		startActivity(mIntent);
		finish();
	}

	private void jumpToPuchase(){
		//登陆成功跳转到主页
		Intent mIntent = new Intent(ReadActivity.this,BookByActivity.class); 
		Bundle mBundle = new Bundle();     
        mBundle.putSerializable("book",book);   
        mIntent.putExtras(mBundle); 
		startActivity(mIntent);
		finish();
	}

	private void creatPopupWindowHeader(){
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		winPopView = layoutInflater.inflate(R.layout.edit_book_case_header, null);
		winPopView.setBackgroundResource(R.color.read_head_pop);
	    btnExit_winpop = (ImageView) winPopView.findViewById(R.id.btnExit_winpop);
	    tvTitle_winpop = (TextView) winPopView.findViewById(R.id.tvTitle_winpop);
	    tvTitle_winpop.setText(book.getBookName());
	    rightDelete_winpop = (ImageView) winPopView.findViewById(R.id.rightDelete_winpop);
	    rightDelete_winpop.setVisibility(View.GONE);
		popupWindowHeader = new PopupWindow(winPopView,LayoutParams.FILL_PARENT,100);
		//popupWindowHeader.showAtLocation(mPageWidget, Gravity.TOP, 0, 0);
		// 使其聚集
		popupWindowHeader.setFocusable(false);
		popupWindowHeader.setTouchable(true); // 设置PopupWindow可触摸
		// 设置允许在外点击消失
		popupWindowHeader.setOutsideTouchable(true);
		//popupWindowHeader.showAtLocation(findViewById(R.id.relativeLayout_parent), Gravity.TOP,0, 50);
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
					if(popupWindowHeader!=null){
						//登陆成功跳转到主页
						Intent mIntent = new Intent(ReadActivity.this,MainActivity.class); 
						startActivity(mIntent);
					}
									}
			});
		}
		if(rightDelete_winpop!=null){
			rightDelete_winpop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(popupWindowHeader!=null){
						popupWindowHeader.dismiss();
					
					}
				
				}
			});
			
		}
	}
	
}