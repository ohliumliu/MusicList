/*
 * 创建日期：2014-12-29 下午1:27:17
 */
package com.ifun361.musiclist.media;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

/**
 * 版权所有 2005-2014 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class PlayUtils {

	private static final String CHUNK_DATA_FILE_NAME = "chunk_data";
	private static final long KILOBYTE = 1024;
	private static final long MEGABYTE = 1024 * KILOBYTE;
	private static final long GIGABYTE = 1024 * MEGABYTE;

	public static final long CHUNK_UNIT = KILOBYTE * 100l;
	public static final int CHUNK_NULL = 48;// char '0'
	public static final int CHUNK_NOT_NULL = 49;// char '1'

	/**
	 * 初始化下载分块对象
	 * 
	 * @param context
	 * @return
	 */
	public static HashMap<String, char[]> getChunkDataMap(Context context) {
		HashMap<String, char[]> map = new HashMap<String, char[]>();
		SharedPreferences sp = context.getSharedPreferences(
				CHUNK_DATA_FILE_NAME, Activity.MODE_PRIVATE);

		Map<String, ?> xmlMap = sp.getAll();
		if (xmlMap != null && xmlMap.size() > 0) {
			for (Map.Entry<String, ?> entry : xmlMap.entrySet()) {
				String key = entry.getKey();
				String val = entry.getValue().toString();
				map.put(key, val.toCharArray());
			}

		}
		return map;
	}

	/**
	 * 取得分块数量
	 * 
	 * @param size
	 *            文件大小
	 * @return
	 */
	public static long getChunkCountWithKB(long size) {
		return (size / CHUNK_UNIT);
	}

	/**
	 * 如果存在未下载部分，取得开始下载位置
	 * 
	 * @param charArr
	 *            分块下载数据
	 * @return
	 */
	public static long getChunkDownloadStartSize(char[] charArr) {
		long p1 = -1;
		for (int i = 0; i < charArr.length; i++) {
			if (charArr[i] == CHUNK_NULL) {
				p1 = i * PlayUtils.CHUNK_UNIT;
				break;
			}
		}
		return p1;
	}

	/**
	 * 检查文件是否已经建立分块数据
	 * 
	 * @param context
	 * @param map
	 *            分块数据集合
	 * @param name
	 *            文件名
	 * @param size
	 *            文件大小
	 */
	public static void checkChunkData(Context context,
			HashMap<String, char[]> map, String name, long size) {
		SharedPreferences sp = context.getSharedPreferences(
				CHUNK_DATA_FILE_NAME, Activity.MODE_PRIVATE);
		if (!sp.contains(name)) {
			long count = getChunkCountWithKB(size);
			char[] charArr = initChunkCharArr((int) count);
			sp.edit().putString(name, String.copyValueOf(charArr)).commit();
			map.put(name, charArr);
		}
	}

	// 初始化分块标识数组
	private static char[] initChunkCharArr(int size) {
		char[] charArr = new char[size];
		for (int i = 0; i < size; i++) {
			charArr[i] = CHUNK_NULL;
		}
		return charArr;
	}

	/**
	 * 更新XML分块下载数据 当前下载线程完成后更新XML分块下载数据
	 * 
	 * @param context
	 * @param key
	 *            文件名
	 * @param charArr
	 *            分块下载信息
	 */
	public static void updateChunkDataMap(Context context, String name,
			char[] charArr) {
		SharedPreferences sp = context.getSharedPreferences(
				CHUNK_DATA_FILE_NAME, Activity.MODE_PRIVATE);
		sp.edit().putString(name, String.copyValueOf(charArr)).commit();
	}

	/**
	 * 取得下载开始位置 变更下载位置后使用
	 * 
	 * @param position
	 *            新下载位置百分比
	 * @param charArr
	 *            分块数据
	 * @return
	 */
	public static long getRangeChunkIndex(int position, char[] charArr) {
		float precent = position * 1f / 100f;
		int i = (int) (charArr.length * precent);
		long startRangeIndex = i * CHUNK_UNIT;
		return startRangeIndex;
	}

	public static boolean isChunkDone(int position, char[] charArr) {
		float precent = position * 1f / 100f;
		int i = (int) (charArr.length * precent);
		return charArr[i] == CHUNK_NOT_NULL;
	}

	/**
	 * 取得文件存储路径
	 * 
	 * @return
	 */
	public static String getMusicStoragePath() {
		File file = Environment.getExternalStorageDirectory();
		if (file == null || !file.exists()) {
			file = Environment.getDownloadCacheDirectory();
		}
		return file.getAbsolutePath();
	}
}
