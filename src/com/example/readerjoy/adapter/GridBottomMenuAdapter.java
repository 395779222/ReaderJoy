package com.example.readerjoy.adapter;

import java.util.List;

import com.example.readerjoy.R;
import com.example.readerjoy.entity.BottomMenu;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_bottom_menu, null);
			bottomMenuHolder = new BottomMenuHolder(convertView);
			convertView.setTag(bottomMenuHolder);
		}
		else{
			bottomMenuHolder = (BottomMenuHolder) convertView.getTag();
		}
		if(bottomMenList!=null&&bottomMenList.size()>0){
			BottomMenu menu = bottomMenList.get(position);
			bottomMenuHolder.text_name.setText(menu.getName());
			if(menu.isSelected()){
				Resources resource = (Resources) context.getResources();  
				ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.header);
				bottomMenuHolder.text_name.setTextColor(csl);
				bottomMenuHolder.img_menu.setBackgroundResource(menu.getSelectedImg());
				bottomMenuHolder.top_line.setVisibility(View.VISIBLE);
				bottomMenuHolder.top_line_un.setVisibility(View.GONE);
			}else{
				Resources resource = (Resources) context.getResources();  
				ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.botton_un_select);
				bottomMenuHolder.text_name.setTextColor(csl);
				bottomMenuHolder.img_menu.setBackgroundResource(menu.getUnSelectedImg());
				bottomMenuHolder.top_line.setVisibility(View.GONE);
				bottomMenuHolder.top_line_un.setVisibility(View.VISIBLE);
			}
		
		}
		
		return convertView;
	}
	
	private class BottomMenuHolder {
		TextView text_name;
		ImageView img_menu;
		ImageView top_line;
		ImageView top_line_un;
		private BottomMenuHolder(View convertView) {
			text_name = (TextView) convertView.findViewById(R.id.text_name);
			img_menu = (ImageView) convertView.findViewById(R.id.img_menu);
			top_line = (ImageView) convertView.findViewById(R.id.top_line);
			top_line_un = (ImageView) convertView.findViewById(R.id.top_line_un);
		}
	}
}
