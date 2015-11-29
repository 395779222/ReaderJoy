package com.example.readerjoy.base.widget;

import com.example.readerjoy.MainActivity;
import com.example.readerjoy.R;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;


/**
 * 自定义滑动控件
 */
public class SlidingMenu extends RelativeLayout {

	private View mSlidingView;
	private View mMenuView;
	private LinearLayout contentLinearLayout;
	private int screenWidth;
	private int lineWidth = 3;
	private Scroller mScroller;
	private VelocityTracker mVelocityTracker;
	private int mTouchSlop;
	private float mLastMotionX;
	private float mLastMotionY;
	private static final int VELOCITY = 50;
	private boolean mIsBeingDragged = true;
	private boolean tCanSlideLeft = true;
	private boolean hasClickLeft = false;
	int i = 0;
	long startTime = 0;
	public Context context;

	public SlidingMenu(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		mScroller = new Scroller(getContext());
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
		WindowManager windowManager = ((Activity) context).getWindow()
				.getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		screenWidth = display.getWidth();

	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public void addViews(View left, View center) {
		contentLinearLayout = new LinearLayout(context);
		contentLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(lineWidth,
				LayoutParams.MATCH_PARENT);
		View lineView = new View(context);
		lineView.setLayoutParams(lp);
		lineView.setBackgroundResource(R.color.lineColor);
		lp.setMargins(-lineWidth, 0, 0, 0);
		contentLinearLayout.addView(lineView);
		android.view.ViewGroup.LayoutParams lp2 = new android.view.ViewGroup.LayoutParams(
				screenWidth, LayoutParams.MATCH_PARENT);
		center.setLayoutParams(lp2);
		contentLinearLayout.addView(center);
		center = contentLinearLayout;
		setLeftView(left);
		setCenterView(center);
	}

	public void setLeftView(View view) {
		LayoutParams behindParams = new LayoutParams((int) (screenWidth * 0.8),
				LayoutParams.FILL_PARENT);
		addView(view, behindParams);
		mMenuView = view;
		mMenuView.bringToFront();
	}

	public void setCenterView(View view) {
		LayoutParams aboveParams = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		addView(view, aboveParams);
		mSlidingView = view;
		mSlidingView.bringToFront();
	}

	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		postInvalidate();
	}
	/*重写computeScroll()的原因
	调用startScroll()是不会有滚动效果的，只有在computeScroll()获取滚动情况，做出滚动的响应
	computeScroll在父控件执行drawChild时，会调用这个方法*/
	@Override
	public void computeScroll() {
		if (!mScroller.isFinished()) {
			if (mScroller.computeScrollOffset()) {
				int oldX = mSlidingView.getScrollX();
				int oldY = mSlidingView.getScrollY();
				int x = mScroller.getCurrX();
				int y = mScroller.getCurrY();
				if (oldX != x || oldY != y) {
					if (mSlidingView != null) {
						/**  scrollTo()方法就是将一个视图移动到指定位置，
						 * 偏移量 mScrollX、mScrollY就是视图初始位置的距离，默认是情况下当然是0。
						 * 如果视图要发生移动，比如要移动到（x，y），首先要检查这个点的坐标是否和偏移量一样，
						 * 因为 scrollTo()是移动到指定的点，如果这次移动的点的坐标和上次偏移量一样，
						 * 也就是说这次移动和上次移动的坐标是同一个，那么就没有必要进行移动了
						 */
						mSlidingView.scrollTo(x, y);
					}
				}
				invalidate();
			}
		} else {
		}
	}

	private boolean canSlideLeft = true;

	public void setCanSliding(boolean left) {
		canSlideLeft = left;
	}

	/* 拦截touch事件 */
	/*重点onInterceptTouchEvent这个事件是从父控件开始往子控件传的，
	 * 直到有拦截或者到没有这个事件的view，然后就往回从子到父控件，这次是onTouch的
	 * 一但返回True（代表事件在当前的viewGroup中会被处理）*/
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		MainActivity activity = (MainActivity) context;
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mIsBeingDragged = false;
			//若是可以显示左侧
			if (canSlideLeft&&activity.isBookCase()) {
				mMenuView.setVisibility(View.VISIBLE);
			}
			break;

		case MotionEvent.ACTION_MOVE:
			if(activity.isEdit()){
				return false;
			}
			float dx = x - mLastMotionX;
			//Math.abs取绝对值
			final float xDiff = Math.abs(dx);
			final float yDiff = Math.abs(y - mLastMotionY);
			//如果做滑动，且滑动小于45
			if (xDiff > mTouchSlop && xDiff > yDiff) {
				if (canSlideLeft) {
					float oldScrollX = mSlidingView.getScrollX();
					if (oldScrollX < 0) {
						mIsBeingDragged = true;
						mLastMotionX = x;
					} 
					else {
						//若是向右滑动
						if (dx > 0) {
							if(!activity.isBookCase()){
								activity.updateFragment("bookCase");
								return false;
							}
							else{
								mIsBeingDragged = true;
								mLastMotionX = x;
							}
						}
						//若是向左滑动
						else if(dx<0){
							//跳转到书城页面
							activity.updateFragment("back");
							mIsBeingDragged = false;
						}
					}
				}
			}
			break;
		}
		return mIsBeingDragged;
	}

	/* 处理拦截后的touch事件 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			//主要用跟踪触摸屏事件（flinging事件和其他gestures手势事件）的速率。
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		final float x = ev.getX();
		final float y = ev.getY();
		
		if(action == MotionEvent.ACTION_DOWN){
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;
			mLastMotionY = y;
			if (mSlidingView.getScrollX() == -getMenuViewWidth()
					&& mLastMotionX < getMenuViewWidth()) {
				return false;
			}
		}else if(action == MotionEvent.ACTION_MOVE){
			if (mIsBeingDragged) {
				final float deltaX = mLastMotionX - x;
				mLastMotionX = x;
				float oldScrollX = mSlidingView.getScrollX();
				float scrollX = oldScrollX + deltaX;
				if (canSlideLeft) {
					if (scrollX > 0)
						scrollX = 0;
				}
				if (deltaX < 0 && oldScrollX < 0) { // left view
					final float leftBound = 0;
					final float rightBound = -getMenuViewWidth();
					if (scrollX > leftBound) {
						scrollX = leftBound;
					} else if (scrollX < rightBound) {
						scrollX = rightBound;
					}
				} else if (deltaX > 0 && oldScrollX > 0) { // right view
					final float leftBound = 0;
					if (scrollX < leftBound) {
						scrollX = leftBound;
					}
				}
				if (mSlidingView != null) {
					mSlidingView.scrollTo((int) scrollX,
							mSlidingView.getScrollY());
				}

			}
		}
		if(action == MotionEvent.ACTION_UP){
			if (mIsBeingDragged) {
				final VelocityTracker velocityTracker = mVelocityTracker;
				velocityTracker.computeCurrentVelocity(100);
				float xVelocity = velocityTracker.getXVelocity();// 滑动的速度
				int oldScrollX = mSlidingView.getScrollX();
				int dx = 0;
				if (oldScrollX <= 0 && canSlideLeft) {// left view
					
					if (xVelocity > VELOCITY) {
						dx = -getMenuViewWidth() - oldScrollX;
					} else if (xVelocity < -VELOCITY) {
						dx = -oldScrollX;
						if (hasClickLeft) {
							hasClickLeft = false;
							setCanSliding(tCanSlideLeft);
						}
					} else if (oldScrollX < -getMenuViewWidth() / 2) {
						dx = -getMenuViewWidth() - oldScrollX;
					} else if (oldScrollX >= -getMenuViewWidth() / 2) {
						dx = -oldScrollX;
						if (hasClickLeft) {
							hasClickLeft = false;
							setCanSliding(tCanSlideLeft);
						}
					}
				}
				if (oldScrollX >= 0) {
					if (xVelocity > VELOCITY) {
						dx = -oldScrollX;
					}
				}
				smoothScrollTo(dx);
			}
		}
		
		return true;
	}

	private int getMenuViewWidth() {
		if (mMenuView == null) {
			return 0;
		}
		return mMenuView.getWidth();
	}

	public void smoothScrollTo(int dx) {
		int duration = 500;
		int oldScrollX = mSlidingView.getScrollX();
		mScroller.startScroll(oldScrollX, mSlidingView.getScrollY(), dx,
				mSlidingView.getScrollY(), duration);
		invalidate();
		startTime = System.currentTimeMillis();
	}

	/*
	 * 显示左侧边的view
	 */
	public void showLeftView() {
		int menuWidth = mMenuView.getWidth();
		int oldScrollX = mSlidingView.getScrollX();
		if (oldScrollX == 0) {
			mMenuView.setVisibility(View.VISIBLE);
			smoothScrollTo(-menuWidth);
			tCanSlideLeft = canSlideLeft;
			hasClickLeft = true;
			setCanSliding(true);
		} else if (oldScrollX == -menuWidth) {
			smoothScrollTo(menuWidth);
			if (hasClickLeft) {
				hasClickLeft = false;
				setCanSliding(tCanSlideLeft);
			}
		}
	}
}
