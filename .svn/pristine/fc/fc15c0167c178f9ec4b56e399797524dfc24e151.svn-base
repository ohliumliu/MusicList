/*package com.ifun361.musiclist.ui;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ifun361.musiclist.R;
import com.ifun361.musiclist.model.ThemesList;

public class InnerThemeFragmentNew extends Fragment {
	private RelativeLayout mFragmentLayout;
	private ScrollView mScrollView;
	private ImageView mCoverView;
	private ImageView mBlurCoverView;
	private ImageView mPlayerView;
	private TextView mTitleText;
	private TextView mCountText;
	private TextView mInfoText;
	
	private Context mContext;
	private Handler mDataSetHandler;
	private ThemesList themesLists;
	
	public InnerThemeFragmentNew(Context context,Handler handler,ThemesList themesLists) {
		this.mContext = context;
		this.mDataSetHandler = handler;
		this.themesLists = themesLists;
	}
	public InnerThemeFragmentNew() {
		// TODO Auto-generated constructor stub
	}

	public static Fragment getInstance(Context context,Handler handler,ThemesList themesLists){
		InnerThemeFragmentNew fragment = new InnerThemeFragmentNew(context,handler,themesLists);
		return fragment;
	}
	
	 public static Fragment getInstance(){
	       return new InnerThemeFragmentNew();
	    }
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.viewpager_fragment_theme,null);
	}
	
	
	public void setContentView() {
		mFragmentLayout = (RelativeLayout) mFragmentLayout.findViewById(
				R.layout.viewpager_fragment_theme);
		mCoverView = (ImageView) mFragmentLayout
				.findViewById(R.id.frag_theme_imageview1);
		mCoverView.setImageResource(R.drawable.bg_sample_01);
		
		mBlurCoverView = (ImageView) mFragmentLayout
				.findViewById(R.id.frag_theme_blur_imageview2);
		
		mPlayerView = (ImageView) mFragmentLayout
				.findViewById(R.id.frag_theme_info_imageview1);
		mPlayerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayActivity.readyToPlayWithNotify(mContext);
			}
		});
		mScrollView = (ScrollView) mFragmentLayout
				.findViewById(R.id.frag_theme_scrollView1);
		mScrollView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int actionMasked = event.getActionMasked();
				switch (actionMasked) {
				case MotionEvent.ACTION_DOWN: {
					break;
				}
				case MotionEvent.ACTION_MOVE: {
					mDataSetHandler.sendEmptyMessageDelayed(1, 100);
					break;
				}
				case MotionEvent.ACTION_UP: {
					mDataSetHandler.sendEmptyMessageDelayed(2, 100);
					break;
				}
				case MotionEvent.ACTION_CANCEL: {
					break;
				}
				default:
				}

				return false;
			}
		});
		
		mTitleText = (TextView) mFragmentLayout
				.findViewById(R.id.frag_theme_info_title);
		mCountText = (TextView) mFragmentLayout
				.findViewById(R.id.frag_theme_info_count);
		mInfoText = (TextView) mFragmentLayout
				.findViewById(R.id.frag_theme_info_text);
	};
}
*/