package com.ifun361.musiclist.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifun361.musiclist.MusicListApplication;
import com.ifun361.musiclist.R;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：主界面
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class MainActivity extends BaseFragmentActivity implements
		OnClickListener, OnBackStackChangedListener {

	public static final int TAB_NO_SELECTED = -1;
	public static final int TAB_ZERO = 0;
	public static final int TAB_ONE = 1;
	public static final int TAB_TWO = 2;
	public static final int TAB_THREE = 3;

	private int mCurrentTab = TAB_NO_SELECTED;
	private String mCurrentFullScreenTag = "";

	private RelativeLayout mFragLayout;
	private RelativeLayout mFullScreenLayout;
	private TextView[] mTextViewTabs;

	private Fragment[] mFragments;
	private Fragment mFullScreenFragment;
	private long mExitTime;

	private Handler mMainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		setCurrentTag(TAB_ONE);
	}

	private void initView() {

		mFragLayout = (RelativeLayout) this
				.findViewById(R.id.main_fragment_layout);
		mFullScreenLayout = (RelativeLayout) this
				.findViewById(R.id.main_content_layout);
		mTextViewTabs = new TextView[4];
		mTextViewTabs[TAB_ZERO] = (TextView) findViewById(R.id.main_tab_item_recommend);
		mTextViewTabs[TAB_ONE] = (TextView) findViewById(R.id.main_tab_item_theme);
		mTextViewTabs[TAB_TWO] = (TextView) findViewById(R.id.main_tab_item_mystyle);
		mTextViewTabs[TAB_THREE] = (TextView) findViewById(R.id.main_tab_item_user);
		for (int i = 0; i < mTextViewTabs.length; i++) {
			mTextViewTabs[i].setOnClickListener(this);

		}

		// final int bw = getResources().getDimensionPixelOffset(
		// R.dimen.tab_button_width);
		// final int bh = getResources().getDimensionPixelOffset(
		// R.dimen.tab_button_height);

		// Drawable tabIcon = getResources().getDrawable(
		// R.drawable.btn_main_tab_01);
		// tabIcon.setBounds(0, 0, bw, bh);
		// mTextViewTabs[TAB_ZERO].setCompoundDrawables(null, tabIcon, null,
		// null);
		//
		// tabIcon = getResources().getDrawable(R.drawable.btn_main_tab_02);
		// tabIcon.setBounds(0, 0, bw, bh);
		// mTextViewTabs[TAB_ONE].setCompoundDrawables(null, tabIcon, null,
		// null);
		//
		// tabIcon = getResources().getDrawable(R.drawable.btn_main_tab_03);
		// tabIcon.setBounds(0, 0, bw, bh);
		// mTextViewTabs[TAB_TWO].setCompoundDrawables(null, tabIcon, null,
		// null);
		//
		// tabIcon = getResources().getDrawable(R.drawable.btn_main_tab_04);
		// tabIcon.setBounds(0, 0, bw, bh);
		// mTextViewTabs[TAB_THREE]
		// .setCompoundDrawables(null, tabIcon, null, null);

		mFragments = new Fragment[4];
		mFragments[TAB_ZERO] = new ThemeFragment();
		mFragments[TAB_ONE] = new ThemeFragment();
		mFragments[TAB_TWO] = new MystyleFragment();
		mFragments[TAB_THREE] = new UserFragment();

		this.getSupportFragmentManager().addOnBackStackChangedListener(this);
		
	}

	private void showFragment(Fragment frag, int tabIndex) {
		FragmentManager fragMgr = getSupportFragmentManager();
		FragmentTransaction ft = fragMgr.beginTransaction();

		if (!frag.isAdded()) {
			// ft.addToBackStack(null);
			ft.add(R.id.main_fragment_layout, frag, String.valueOf(tabIndex));
		}

		Fragment currFrag = fragMgr.findFragmentByTag(String
				.valueOf(mCurrentTab));
		if (currFrag != null) {
			ft.hide(currFrag);
		}

		mCurrentTab = tabIndex;
		ft.show(frag).commitAllowingStateLoss();
	}

	private void showFullScreenFragment(Fragment frag, String tag, int fragFlag) {
		mFullScreenFragment = null;
		mFullScreenFragment = frag;
		mFullScreenLayout.setVisibility(View.VISIBLE);

		FragmentManager fragMgr = getSupportFragmentManager();
		FragmentTransaction ft = fragMgr.beginTransaction();

		ft.replace(R.id.main_content_layout, frag, tag);
		mCurrentFullScreenTag = tag;
		ft.show(frag).commitAllowingStateLoss();
	}

	private void removeFullScreenFragment() {
		FragmentManager fragMgr = getSupportFragmentManager();
		FragmentTransaction ft = fragMgr.beginTransaction();

		mFullScreenLayout.setVisibility(View.GONE);
		if (mFullScreenFragment != null)
			ft.remove(mFullScreenFragment).commitAllowingStateLoss();

		mFullScreenFragment = null;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getRepeatCount() == 0) {
			if (mFullScreenFragment != null && mFullScreenLayout.isShown()) {
				removeFullScreenFragment();
				return true;
			} else {
				doQuitHandling();
			}
		}
		return false;
	}

	private void doQuitHandling() {
		if ((System.currentTimeMillis() - mExitTime) > 2000) {
			Toast.makeText(this, R.string.exit_sys, Toast.LENGTH_SHORT).show();
			mExitTime = System.currentTimeMillis();
		} else {
			finish();
			MusicListApplication.getAPP().exitApp();
		}
	}

	private void setCurrentTag(int tagIndex) {
		if (tagIndex == mCurrentTab
				&& !getFragmentManager().beginTransaction().isEmpty()) {
			return;
		}
		showFragment(mFragments[tagIndex], tagIndex);
		for (int i = 0; i < mTextViewTabs.length; i++) {
			mTextViewTabs[i].setSelected(tagIndex == i ? true : false);
		}
		switch (tagIndex) {
		case TAB_ZERO:
			break;
		case TAB_ONE:
			break;
		case TAB_TWO:
			break;
		case TAB_THREE:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		for (int i = 0; i < mTextViewTabs.length; i++) {
			if (v.equals(mTextViewTabs[i])) {
				setCurrentTag(i);
			}
		}
	}

	@Override
	public void onBackStackChanged() {
	}

}
