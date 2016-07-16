package com.hyb.qqslidemenu.view;

import com.hyb.qqslidemenu.utils.ColorUtil;
import com.nineoldandroids.animation.FloatEvaluator;
import com.nineoldandroids.view.ViewHelper;

import android.animation.IntEvaluator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

public class SlideMenu extends FrameLayout {

	private View menuView;
	private View mainView;
	ViewDragHelper viewDragHelper;
	private int width;
	private float dragRange;
	FloatEvaluator floatEvaluator;
	IntEvaluator intEvaluator;
	//定义状态常量
	enum DragState{
		open,
		close;
	}
	DragState currentState=DragState.close;
	public interface OnDragStateChangeListener{
		//打开的回调
		void onOpen();
		
		//关闭的回调
		void onClose();
		//拖拽中回调
		void onDraging(float fraction);
	}
	private OnDragStateChangeListener listener;
	
	public void setOnDragStateChangeListener(OnDragStateChangeListener listener) {
		this.listener = listener;
	}

	public DragState getCurrentState() {
		return currentState;
	}

	public SlideMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public SlideMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public SlideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}
	//onmeasure()执行后执行,可以在该方法中初始化自己及子view的宽高
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		width = getMeasuredWidth();
		dragRange=width * 0.6f;
	}
	private void executeAnim(float fraction)
	{
		//缩小mainView
		float scalevalue=0.8f + 0.2f * (1 - fraction);
		//ViewHelper.setScaleX(mainView, scalevalue);
		//ViewHelper.setScaleY(mainView, scalevalue);
		ViewHelper.setScaleX(mainView, floatEvaluator.evaluate(fraction, 1f, 0.8f));
		ViewHelper.setScaleY(mainView, floatEvaluator.evaluate(fraction, 1f, 0.8f));
		
		ViewHelper.setTranslationX(menuView, 
				intEvaluator.evaluate(fraction, -menuView.getMeasuredWidth() / 2, 0));
		ViewHelper.setScaleX(menuView, floatEvaluator.evaluate(fraction, 0.5f, 1f));
		ViewHelper.setScaleY(menuView, floatEvaluator.evaluate(fraction, 0.5f, 1f));
		ViewHelper.setAlpha(menuView, floatEvaluator.evaluate(fraction, 0.3f, 1f));
		
		//给slidemenu的背景添加黑色的遮罩效果
		getBackground().setColorFilter((Integer) ColorUtil.evaluateColor(fraction, Color.BLACK, Color.TRANSPARENT), Mode.SRC_OVER);
	}
	private void init()
	{
		floatEvaluator=new FloatEvaluator();
		intEvaluator=new IntEvaluator();
		viewDragHelper=ViewDragHelper.create(this, new Callback() {
			
			@Override
			public boolean tryCaptureView(View view, int arg1) {
				// TODO Auto-generated method stub
				return view==mainView;
			}

			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				// TODO Auto-generated method stub
				if(child==mainView){
					if(left<0)left=0;
					if(left>dragRange)left=(int)dragRange;
				}
				
				return left;
			}

			@Override
			public int getViewHorizontalDragRange(View child) {
				// TODO Auto-generated method stub
				return (int)dragRange;
			}

			@Override
			public void onViewCaptured(View capturedChild, int activePointerId) {
				// TODO Auto-generated method stub
				super.onViewCaptured(capturedChild, activePointerId);
			}

			@Override
			public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
				// TODO Auto-generated method stub
				super.onViewPositionChanged(changedView, left, top, dx, dy);
				/*
				if(changedView==menuView){
					//固定menuview
					menuView.layout(0, 0, menuView.getMeasuredWidth(), menuView.getMeasuredHeight());
					//让mainview移动起来
					mainView.layout(mainView.getLeft()+dx, mainView.getTop()+dy, 
							mainView.getRight()+dx, mainView.getBottom()+dy);
				}
				*/
				//计算滑动百分比
				float fraction=mainView.getLeft() / dragRange;
				//执行伴随动画
				executeAnim(fraction);
				//更改状态,回调listener方法
				if(fraction==0 && currentState!=DragState.close){
					currentState=DragState.close;
					if(listener!=null)listener.onClose();
				}else if(fraction==1 && currentState!=DragState.open){
					currentState=DragState.open;
					if(listener!=null)listener.onOpen();
				}
				if(listener!=null)
					listener.onDraging(fraction);
			}

			@Override
			public void onViewReleased(View releasedChild, float xvel, float yvel) {
				// TODO Auto-generated method stub
				super.onViewReleased(releasedChild, xvel, yvel);
				if(mainView.getLeft()<dragRange / 2){
					//view在左半边,关闭slidemenu
					SlideMenuClose();
				}else{
					//view在右半边,打开slidemenu
					SlideMenuOpen();
				}
				//处理用户稍微滑动
				if(xvel>200 && currentState!=DragState.open)
					SlideMenuOpen();
				else if (xvel<-200 && currentState!=DragState.close) {
					SlideMenuClose();
				}
			}
			
		});
	}
	public void SlideMenuClose() {
		viewDragHelper.smoothSlideViewTo(mainView, 0, mainView.getTop());
		ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
	}
	public void SlideMenuOpen() {
		// TODO Auto-generated method stub
		viewDragHelper.smoothSlideViewTo(mainView, (int)dragRange, mainView.getTop());
		ViewCompat.postInvalidateOnAnimation(SlideMenu.this);
	}
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if(viewDragHelper.continueSettling(true)){
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		viewDragHelper.processTouchEvent(event);
		return true;
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return viewDragHelper.shouldInterceptTouchEvent(ev);
	}
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		if(getChildCount()!=2)
			new IllegalArgumentException("throw a exception");
		menuView = getChildAt(0);
		mainView = getChildAt(1);
	}

}
