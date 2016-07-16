package com.hyb.qqslidemenu.view;

import com.hyb.qqslidemenu.view.SlideMenu.DragState;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;


//当slidemenu打开的时候,拦截并消费掉触摸事件
public class CustomLinearLayout extends LinearLayout {
	SlideMenu slideMenu;
	public CustomLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public void setSlideMenu(SlideMenu slideMenu) {
		this.slideMenu = slideMenu;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if(slideMenu!=null && slideMenu.getCurrentState()==DragState.open){
			//如果slidemenu打开则应该拦截并消费掉触摸事件
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if(slideMenu!=null && slideMenu.getCurrentState()==DragState.open){
			//如果slidemenu打开则应该拦截并消费掉触摸事件
			if(event.getAction()==MotionEvent.ACTION_UP){
				slideMenu.SlideMenuClose();
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
}
