package com.example.readerjoy.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,WindowManager.LayoutParams. FLAG_FULLSCREEN);
		bindWidget();
		bindAdapter();
		bindWidgetEevent();
		process();
	}
	/**
	 * 绑定控件，子类实现
	 */
	public abstract void bindWidget();

	/**
	 * 绑定控件事件
	 */
	public abstract void bindWidgetEevent();

	/**
	 * 初始化Adapter
	 */
	public abstract void bindAdapter();
	
	/**
	 * 业务处理
	 */
	public abstract void process();
}
