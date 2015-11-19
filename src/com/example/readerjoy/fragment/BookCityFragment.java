/**   
* @Title: BookCityFragment.java
* @Package com.example.readerjoy.fragment
* @Description: TODO(用一句话描述该文件做什么)
* @author A18ccms A18ccms_gmail_com   
* @date 2015年11月18日 下午6:12:18
* @version V1.0   
*/


package com.example.readerjoy.fragment;

import com.example.readerjoy.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    @Override
    public View onCreateView(LayoutInflater inflater,
           ViewGroup container, Bundle savedInstanceState) {
   
    	mainView =  inflater.inflate(R.layout.fragment_book_city, null);
       
        return mainView;
    }

}
