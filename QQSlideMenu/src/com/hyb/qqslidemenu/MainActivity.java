package com.hyb.qqslidemenu;

import com.hyb.qqslidemenu.utils.Constant;
import com.hyb.qqslidemenu.view.CustomLinearLayout;
import com.hyb.qqslidemenu.view.SlideMenu;
import com.hyb.qqslidemenu.view.SlideMenu.OnDragStateChangeListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity {
	
	private ImageView iv_head;
	private ListView main_listview;
	private ListView menu_listview;
	private SlideMenu slideMenu;
	private CustomLinearLayout ll_main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		menu_listview = (ListView)findViewById(R.id.menu_listview);
		menu_listview.setAdapter(new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_list_item_1, Constant.sCheeseStrings));
		main_listview = (ListView)findViewById(R.id.main_listview);
		slideMenu = (SlideMenu)findViewById(R.id.slideMenu);
		iv_head = (ImageView)findViewById(R.id.iv_head);
		ll_main = (CustomLinearLayout)findViewById(R.id.my_layout);
		ll_main.setSlideMenu(slideMenu);
		main_listview.setAdapter(new ArrayAdapter<String>(getApplicationContext(), 
				android.R.layout.simple_list_item_1, Constant.NAMES){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				// TODO Auto-generated method stub
				TextView tv = (TextView)super.getView(position, convertView, parent);
				tv.setTextColor(Color.BLACK);
				return tv;
			}
		});
		
		slideMenu.setOnDragStateChangeListener(new OnDragStateChangeListener() {
			
			@Override
			public void onOpen() {
				// TODO Auto-generated method stub
				Log.d("hyb", "onOpen");
			}
			
			@Override
			public void onDraging(float fraction) {
				// TODO Auto-generated method stub
				ViewHelper.setAlpha(iv_head, 1-fraction);
			}
			
			@Override
			public void onClose() {
				// TODO Auto-generated method stub
				Log.d("hyb", "onClose");
				ViewPropertyAnimator.animate(iv_head).translationXBy(15).setInterpolator(new CycleInterpolator(4)).setDuration(500);
				
			}
		});
	}
}
