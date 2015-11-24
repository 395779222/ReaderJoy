/**   
* @Title: GridViewForScrollView.java
* @Package com.example.readerjoy.base.widget
* @Description: TODO(用一句话描述该文件做什么)
* @author jerry 
* @date 2015年11月23日 下午2:53:17
* @version V1.0   
*/


package com.example.readerjoy.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.GridView;

/**
* @ClassName: GridViewForScrollView
* @Description: TODO(这里用一句话描述这个类的作用)
* @author A18ccms a18ccms_gmail_com
* @date 2015年11月23日 下午2:53:17
* 
*/

public class GridViewForScrollView extends GridView{

	/**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param context
	*/
	public GridViewForScrollView(Context context) {
		super(context);
	}
	public GridViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
    }
	 @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
