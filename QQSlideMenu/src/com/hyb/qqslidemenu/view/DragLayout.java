package com.hyb.qqslidemenu.view;

import com.hyb.qqslidemenu.R;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class DragLayout extends ViewGroup {

	private View redView;
	private View blueView;
	ViewDragHelper viewDragHelper;
	Scroller scroller;
	public DragLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public DragLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public DragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
		init();
	}
	private void init()
	{
		scroller=new Scroller(getContext());
		viewDragHelper=ViewDragHelper.create(this, new Callback() {
			//控制view在水平方向的移动
			@Override
			public int clampViewPositionHorizontal(View child, int left, int dx) {
				// TODO Auto-generated method stub
				if(left<0)
					left=0;
				if(left>getMeasuredWidth()-child.getMeasuredWidth())
					left=getMeasuredWidth()-child.getMeasuredWidth();
				return left;
			}
			
			@Override
			public int clampViewPositionVertical(View child, int top, int dy) {
				// TODO Auto-generated method stub
				return top;
			}

			@Override
			public int getOrderedChildIndex(int index) {
				// TODO Auto-generated method stub
				return super.getOrderedChildIndex(index);
			}
			//获取view水平拖动的范围,但是目前不能限制位置,返回的值目前用于在手指抬起时view缓慢移动的动画时间计算上
			//最好不要返回0
			@Override
			public int getViewHorizontalDragRange(View child) {
				// TODO Auto-generated method stub
				return super.getViewHorizontalDragRange(child);
			}

			@Override
			public int getViewVerticalDragRange(View child) {
				// TODO Auto-generated method stub
				return super.getViewVerticalDragRange(child);
			}

			@Override
			public void onEdgeDragStarted(int edgeFlags, int pointerId) {
				// TODO Auto-generated method stub
				super.onEdgeDragStarted(edgeFlags, pointerId);
			}

			@Override
			public boolean onEdgeLock(int edgeFlags) {
				// TODO Auto-generated method stub
				return super.onEdgeLock(edgeFlags);
			}

			@Override
			public void onEdgeTouched(int edgeFlags, int pointerId) {
				// TODO Auto-generated method stub
				super.onEdgeTouched(edgeFlags, pointerId);
			}
			//当view被开始捕获和解析的回调
			@Override
			public void onViewCaptured(View capturedChild, int activePointerId) {
				// TODO Auto-generated method stub
				super.onViewCaptured(capturedChild, activePointerId);
			}

			@Override
			public void onViewDragStateChanged(int state) {
				// TODO Auto-generated method stub
				super.onViewDragStateChanged(state);
			}
			//changedView的位置改变的时候执行
			@Override
			public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
				// TODO Auto-generated method stub
				super.onViewPositionChanged(changedView, left, top, dx, dy);
				if(changedView==blueView){
					redView.layout(redView.getLeft()+dx, redView.getTop()+dy, redView.getRight()+dx, redView.getBottom()+dy);
				}
				//计算view移动的百分比
				float fraction=changedView.getLeft() * 1f / (getMeasuredWidth() - changedView.getMeasuredWidth());
				//执行一系列的伴随动画
				executeAnim(fraction);
			}
			//手指抬起时执行的方法
			//xvel:x方向的移动速度
			@Override
			public void onViewReleased(View releasedChild, float xvel, float yvel) {
				// TODO Auto-generated method stub
				int centerLeft=(getMeasuredWidth() - releasedChild.getMeasuredWidth()) / 2;
				if(releasedChild.getLeft()>centerLeft){
					//在右半边,应缓慢向右移动
					viewDragHelper.smoothSlideViewTo(releasedChild,getMeasuredWidth() - releasedChild.getMeasuredWidth(), releasedChild.getTop());
					ViewCompat.postInvalidateOnAnimation(DragLayout.this);
					//scroller.startScroll(releasedChild.getLeft(), releasedChild.getTop(), dx, dy, duration);
				}
				else{
					viewDragHelper.smoothSlideViewTo(releasedChild, 0, releasedChild.getTop());
					ViewCompat.postInvalidateOnAnimation(DragLayout.this);
				}
				super.onViewReleased(releasedChild, xvel, yvel);
			}
			//用于判断是否捕获当前childview的触摸事件
			//返回true就捕获并解析
			@Override
			public boolean tryCaptureView(View view, int arg1) {
				// TODO Auto-generated method stub
				return view==blueView;//捕获blueview的触摸
			}
		});
	}
	//执行伴随动画
	private void executeAnim(float fraction)
	{
		redView.setAlpha(fraction);
	}
	public void computeScroll() {
		// TODO Auto-generated method stub
		super.computeScroll();
		if(viewDragHelper.continueSettling(true))
		{
			ViewCompat.postInvalidateOnAnimation(DragLayout.this);
		}
		/*
		if(scroller.computeScrollOffset())
		{
			scrollTo(scroller.getCurrX(), scroller.getCurrY());
			invalidate();
		}
		*/
	}
	@Override
	public boolean onInterceptHoverEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//让viewDragHelper帮我们判断是否应该拦截
		boolean reasult=viewDragHelper.shouldInterceptTouchEvent(event);
		return reasult;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//将触摸事件交给viewDragHelper处理
		viewDragHelper.processTouchEvent(event);
		return true;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//测量自己的子view
		int size=(int) getResources().getDimension(R.dimen.tv_width);
		int spec= MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
		redView.measure(spec, spec);
		blueView.measure(spec, spec);
		//测量ziview还可以用这种方法
		//measureChild(redView, widthMeasureSpec, heightMeasureSpec);
	}

	//当DragLayout的xml布局结束标签读取完成执行该方法,此时会知道自己有几个view
	//一般用来初始化子view的引用
	@Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
		redView = getChildAt(0);
		blueView = getChildAt(1);
	}
	@Override
	protected void onLayout(boolean arg0, int left, int top, int right, int bottom) {
		// TODO Auto-generated method stub
		int _top=0;
		//int _left=getMeasuredWidth();//画在左上角
		//画在中间
		int _left=getMeasuredWidth() / 2 - redView.getMeasuredWidth() / 2+getPaddingLeft();
		redView.layout(_left, _top, redView.getMeasuredWidth()+_left, _top+redView.getMeasuredHeight());
		blueView.layout(_left, redView.getBottom(), _left+blueView.getMeasuredWidth(),
				redView.getBottom()+blueView.getMeasuredHeight());
		
	}

}
