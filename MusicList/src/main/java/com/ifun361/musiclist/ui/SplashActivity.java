package com.ifun361.musiclist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.ifun361.musiclist.R;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class SplashActivity extends BaseActivity {

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			SplashActivity.this.finish();
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_splash);
		showApp();
	}

	private void showApp() {
		if (mHandler != null)
			mHandler.sendEmptyMessageDelayed(0, 1500);
	}

}