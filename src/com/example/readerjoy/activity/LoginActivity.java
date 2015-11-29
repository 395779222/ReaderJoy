/**   
* @Title: LoginActivity.java
* @Package com.example.readerjoy.activity
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月23日 下午6:04:05
* @version V1.0   
*/


package com.example.readerjoy.activity;

import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;
import com.example.readerjoy.base.widget.CustomToast;
import com.example.readerjoy.db.LocalBook;
import com.example.readerjoy.vo.MarkVo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
* @ClassName: LoginActivity
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月23日 下午6:04:05
* 
*/

public class LoginActivity extends BaseActivity{
	Button btn_login;
	private SharedPreferences sp;
	private boolean isLogin;
	private EditText name;
	private EditText password;
	private LocalBook localbook;
	// 是否是登陆页面，还是注册页面
	private boolean isLoginPage;
	private TextView text_forget;
	private TextView text_register;
	/* (非 Javadoc) 
	* <p>Title: bindWidget</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidget() 
	*/ 
	@Override
	public void bindWidget() {
		setContentView(R.layout.activity_login);
		btn_login = (Button) findViewById(R.id.btn_login);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		isLogin = sp.getBoolean("isLogin", false);
		name =  (EditText) findViewById(R.id.name);
		password =  (EditText) findViewById(R.id.password);
		localbook = new LocalBook(this);
		Bundle extras = getIntent().getExtras();
		isLoginPage = extras.getBoolean("isLoginPage", true);
		text_forget = (TextView) findViewById(R.id.text_forget);
		text_register = (TextView) findViewById(R.id.text_register);
	}

	/* (非 Javadoc) 
	* <p>Title: bindWidgetEevent</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindWidgetEevent() 
	*/ 
	@Override
	public void bindWidgetEevent() {
		// 精品推荐
		btn_login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isLoginPage){
					login();
				}
				else{
					regist();
				}	
			}
		});
		text_register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isLoginPage = false;
				btn_login.setText("注册");
				text_register.setVisibility(View.GONE);
			}
		});
	}

	/** 
	* @Title: regist 
	* @Description: TODO(注册) 
	* @param     设定文件 
	* @date 2015年11月23日 下午8:58:05
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	protected void regist() {
		SQLiteDatabase db = localbook.getWritableDatabase();
		String sql1 = "insert into " + "user" + " (name,password,isBY) values('" + name.getText().toString() + "','" + password.getText().toString() + "','0')";
		db.execSQL(sql1);
		db.close();
		sp.edit().putBoolean("isLogin", true).commit();
		sp.edit().putString("name", name.getText().toString()).commit();
		sp.edit().putString("password", password.getText().toString()).commit();
		sp.edit().putString("isBY", "0").commit();
		CustomToast.getInstance(LoginActivity.this).showToast("注册成功");
		
		//登陆成功跳转到主页
		Intent mIntent = new Intent(LoginActivity.this,MainActivity.class); 
		startActivity(mIntent);
		
	}

	/** 
	* @Title: login 
	* @Description: TODO(登陆) 
	* @param     设定文件 
	* @date 2015年11月23日 下午8:57:33
	* @author jerry
	* @return void    返回类型
	* @throws 
	*/ 
	protected void login() {
		SQLiteDatabase dbSelect = localbook.getReadableDatabase();
		String col[] = { "name", "password", "isBY" };
		String[] val = {name.getText().toString(),password.getText().toString()};
		Cursor cur = dbSelect.query("user", col, "name=? and password=?",val, null,null,null);
		Integer num = cur.getCount();
		//若用户不存在
		if(num==0){
			CustomToast.getInstance(LoginActivity.this).showToast("无此用户");
		}
		//登陆成功
		else{
			while (cur.moveToNext()) {
				String userType = cur.getString(cur.getColumnIndex("isBY"));
				if(userType.equals("0")){
					sp.edit().putBoolean("isBY", false).commit();
				}else{
					sp.edit().putBoolean("isBY", true).commit();
				}
			}
			sp.edit().putBoolean("isLogin", true).commit();
			sp.edit().putString("name", name.getText().toString()).commit();
			sp.edit().putString("password", password.getText().toString()).commit();
			//登陆成功跳转到主页
			Intent mIntent = new Intent(LoginActivity.this,MainActivity.class); 
			startActivity(mIntent);
		}
		
	}

	/* (非 Javadoc) 
	* <p>Title: bindAdapter</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#bindAdapter() 
	*/ 
	@Override
	public void bindAdapter() {
		
	}

	/* (非 Javadoc) 
	* <p>Title: process</p> 
	* <p>Description: </p>  
	* @see com.example.readerjoy.activity.BaseActivity#process() 
	*/ 
	@Override
	public void process() {
		text_forget.setText(Html.fromHtml("<u>"+"忘记密码"+"</u>"));
		text_register.setText(Html.fromHtml("<u>"+"注册"+"</u>"));
		if(isLoginPage){
			if (isLogin){
				name.setText(sp.getString("name", ""));
				password.setText(sp.getString("password", ""));
			}
			btn_login.setText("登陆");
		}else{
			btn_login.setText("注册");
			text_register.setVisibility(View.GONE);
		}
		
		
	}

}
