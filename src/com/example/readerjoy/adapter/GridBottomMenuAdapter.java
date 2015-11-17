package com.example.readerjoy.adapter;

import java.util.List;

import com.example.readerjoy.R;
import com.example.readerjoy.entity.BottomMenu;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class GridBottomMenuAdapter extends BaseAdapter {
	private Context context;
	private List<BottomMenu> bottomMenList;
	
	public GridBottomMenuAdapter(Context context, List<BottomMenu> dataMenuList) {
		this.context = context;
		this.bottomMenList = dataMenuList;
	}

	@Override
	public int getCount() {
		return bottomMenList != null ? bottomMenList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		return bottomMenList != null ? bottomMenList.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BottomMenuHolder bottomMenuHolder = null;
		if(bottomMenList!=null&&bottomMenList.size()>0){
			BottomMenu menu = bottomMenList.get(position);
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_bottom_menu, null);
			bottomMenuHolder = new BottomMenuHolder(convertView);
			bottomMenuHolder.text_bottom_name.setText(menu.getName());
			if(menu.isSelected()){
				bottomMenuHolder.text_bottom_name.setBackgroundResource(R.drawable.corners_bg_selected);
			}else{
				bottomMenuHolder.text_bottom_name.setBackgroundResource(R.drawable.corners_bg);
			}
		}
		
		return convertView;
	}
	
	private class BottomMenuHolder {
		Button text_bottom_name;
		private BottomMenuHolder(View convertView) {
			text_bottom_name = (Button) convertView.findViewById(R.id.text_bottom_name);
		}
	}
}
