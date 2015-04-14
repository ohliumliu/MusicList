package com.ifun361.musiclist.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.ifun361.musiclist.MusicListApplication;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：连接监听
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class ConnectivityActionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(final Context context, Intent intent) {

		if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
				&& MusicListApplication.getAPP() != null) {

			ConnectivityManager cm = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo ni = cm.getActiveNetworkInfo();

			if (ni == null || !ni.isConnected()) {
				MusicListApplication.getAPP().getPlayerEngine().pause();
			} else if (ni.isConnected()) {
				// MusicListApplication.getAPP().getPlayerEngine().play();
			}
		}
	}
}