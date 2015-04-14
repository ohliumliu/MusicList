package com.ifun361.musiclist.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.SimpleOnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ifun361.musiclist.model.ThemesList;
import com.ifun361.musiclist.ui.BaseFragmentActivity;
import com.ifun361.musiclist.ui.InnerThemeFragment;
import com.ifun361.musiclist.ui.InnerThemeFragment;

public class ThemeCoverAdapter extends FragmentStatePagerAdapter {
	private static final String TAG = "ThemeCoverAdapter";
	//TODO 判断非空themesLists
	private Context mContext;
	private ArrayList<ThemesList> themesLists = new ArrayList<ThemesList>();
	private LayoutInflater inflater;
	private Handler mDataSetHandler;
	private FragmentManager fragmentManager;
	private Fragment fragment;
	
	public ThemeCoverAdapter(Context context,FragmentManager fm,Handler handler) {
		super(fm);
		this.mContext = context;
		this.mDataSetHandler = handler;
		this.fragmentManager = fm;
		
	}

	public void setData(ArrayList<ThemesList> datas){
		if(themesLists!=null&&themesLists.size()>0){
			themesLists.clear();
		}
		themesLists = datas;
	}
	
	@Override
	public int getCount() {
		return themesLists.size();
	}


	/*@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView( (View) object);
	}*/
	
	/*@Override
	public Object instantiateItem(ViewGroup container, int position) {
		Fragment fragment = InnerThemeFragment.getInstance(mContext, mDataSetHandler,themesLists.get(position));
	        if(!fragment.isAdded()){ // 如果fragment还没有added
	            FragmentTransaction ft = fragmentManager.beginTransaction();
	            ft.add(fragment, fragment.getClass().getSimpleName());
	            ft.commit();
	            *//**
	             * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
	             * 会在进程的主线程中，用异步的方式来执行。
	             * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
	             * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
	             **//*
	            fragmentManager.executePendingTransactions();
	        }

	        if(fragment.getView().getParent() == null){
	            container.addView(fragment.getView()); // 为viewpager增加布局
	        }

	        return fragment.getView();
	}*/
	
	@Override
	public Fragment getItem(int position) {
		return InnerThemeFragment.getInstance(mContext, mDataSetHandler,themesLists.get(position));
		//return InnerThemeFragment.getInstance();
	}

	
	/*@Override
	public int getItemPosition(Object object) {
		return ThemeCoverAdapter.POSITION_NONE;
	}*/
}
