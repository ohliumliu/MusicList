package com.ifun361.musiclist.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifun361.musiclist.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：FragmentActivity界面基础类
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class BaseFragmentActivity extends FragmentActivity {

	private static final String TAG = "BaseFragmentActivity";
	protected RelativeLayout titleLayout;
	protected RelativeLayout contentContainer;
	protected ImageView topTitleLeftIcon;
	protected ImageView topTitleRightIcon;
	protected TextView topTitleCenterText;
	protected ImageLoader imageLoader;
	// 显示图片的设置
	protected DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initImagLoader();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	private void initImagLoader() {
		// 图片初始化
				imageLoader = ImageLoader.getInstance();
				options = new DisplayImageOptions.Builder()
						.showStubImage(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
						.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
						.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
						.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
						.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
						.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
						.build(); // 创建配置过得DisplayImageOption对象
	}

	@Override
	public void setContentView(int layoutResId) {
		super.setContentView(R.layout.activity_base);
		titleLayout = (RelativeLayout) findViewById(R.id.base_top_title);
		topTitleLeftIcon = (ImageView) findViewById(R.id.top_title_left_icon);
		topTitleRightIcon = (ImageView) findViewById(R.id.top_title_right_icon);
		topTitleCenterText = (TextView) findViewById(R.id.top_title_text);
		contentContainer = (RelativeLayout) findViewById(R.id.base_content_container);
		getLayoutInflater().inflate(layoutResId, contentContainer);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void showTitleBar() {
		titleLayout.setVisibility(View.VISIBLE);
	}

	public void hideTitleBar() {
		titleLayout.setVisibility(View.GONE);
	}

	public void setTitleViewDisplay(boolean left, boolean center, boolean right) {
		topTitleLeftIcon.setVisibility(left ? View.VISIBLE : View.GONE);
		topTitleRightIcon.setVisibility(right ? View.VISIBLE : View.GONE);
		topTitleCenterText.setVisibility(center ? View.VISIBLE : View.GONE);
	}

	
	public void setTitleViewCenterText(String titleView){
		Log.i(TAG, "setTitleViewCenterText");
		topTitleCenterText.setText(titleView);
	}

	public void setTitleViewLeftIcon(String uri){
		imageLoader.displayImage(uri, topTitleLeftIcon);
	}

	public void setTitleViewRightIcon(String uri){
		imageLoader.displayImage(uri, topTitleRightIcon);
	}
}
