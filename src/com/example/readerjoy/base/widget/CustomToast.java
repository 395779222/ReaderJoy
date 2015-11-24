package com.example.readerjoy.base.widget;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.example.readerjoy.R;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.Toast;


/**
 * 自定义TOAST
 */
public class CustomToast {
	private static CustomToast reflectToast = null;
	private Field mNextViewField;
	private Toast mToast;
	private Field field;
	private Object obj;
	private Method showMethod, hideMethod;
	private TextView textView;
	private Handler handler = new Handler();
	private boolean isShow = false;

	private CustomToast(Context context) {
		mToast = new Toast(context);
		mToast.setGravity(Gravity.CENTER, 0, 0);
		textView = (TextView) LayoutInflater.from(context).inflate(
				R.layout.toast, null);
		reflectionTN();
	}

	public static CustomToast getInstance(Context context) {
		if (reflectToast == null) {
			reflectToast = new CustomToast(context);
		}
		return reflectToast;
	}

	private Runnable showRunnable = new Runnable() {

		@Override
		public void run() {
			cancel();
			isShow = false;
		}
	};;

	public void showToast(String textString) {
		long mDuration = 1000;
		showToast(textString, mDuration);

	}

	public void showToast(String textString, long mDuration) {
		textView.setText(textString);
		if (isShow == false) {
			show();
		} else {
			handler.removeCallbacks(showRunnable);
		}
		handler.postDelayed(showRunnable, mDuration);
	}

	private void show() {
		try {

			mNextViewField.set(obj, textView);
			showMethod.invoke(obj);
			isShow = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void cancel() {
		try {
			hideMethod.invoke(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reflectionTN() {
		try {
			field = mToast.getClass().getDeclaredField("mTN");
			field.setAccessible(true);
			obj = field.get(mToast);
			mNextViewField = obj.getClass().getDeclaredField("mNextView");
			mNextViewField.setAccessible(true);
			showMethod = obj.getClass().getDeclaredMethod("show");
			hideMethod = obj.getClass().getDeclaredMethod("hide");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
