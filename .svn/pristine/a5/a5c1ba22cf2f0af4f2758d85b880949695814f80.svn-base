/*
 * 创建日期：2015-1-8 下午2:06:36
 */
package com.ifun361.musiclist.utils;

import android.content.Context;
import android.util.DisplayMetrics;


/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class Utils {

	public static String secondsToString(int seconds) {
		String time = "";
		if (seconds == 0) {
			time = "00:00";
		} else {
			final int s = seconds / 60;
			time = "" + (s < 10 ? "0" + s : s) + ":";
			final int t = seconds % 60;
			time += t < 10 ? "0" + t : t;
		}
		return time;
	}
	
	/**
	 * 获取屏幕高度(px)
	 * 
	 * @param Context
	 * @return
	 */
	public static int getMobileHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int height = (int) Math.floor(dm.heightPixels);
		return height;
	}
	
	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		int value = (int) dpValue;
		if (null != context) {
			final float scale = context.getResources().getDisplayMetrics().density;
			value = (int) (dpValue * scale + 0.5f);
		}
		return value;
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		int value = (int) pxValue;
		if (null != context) {
			final float scale = context.getResources().getDisplayMetrics().density;
			value = (int) (pxValue / scale + 0.5f);
		}
		return value;
	}


}
