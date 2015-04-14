package com.ifun361.musiclist.utils;

import java.lang.reflect.Type;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	
	private GsonUtil() {
	}

	private static Gson mGson = null;

	public static Gson getInstance() {
		if (mGson == null) {
			mGson = new GsonBuilder().create();
		}
		return mGson;
	}

	

	/**
	 * 
	 * @param jsonStr
	 *            待解析的JSON字符串
	 * @param clazz
	 *            待赋值的类
	 * @return T 异常则返回为null
	 */
	public static <T> T parseJson(String jsonStr, Class<T> clazz) {
		try {
			Log.i("parseJson", "no exception");
			return new Gson().fromJson(jsonStr, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("gson", e.getMessage());
			return null;
		}

	}

	/**
	 * 
	 * @param jsonStr
	 *            待解析处理的JSON字符串
	 * @param clazz
	 *            待赋值的类
	 * @return T 异常则返回为null
	 */
	public static <T> T parseJsonToObj(String jsonStr, Class<T> clazz) {
		try {
			Log.i("parseJson", "no exception");
			return new Gson().fromJson(jsonStr, clazz);
		} catch (Exception e) {
			e.printStackTrace();
			Log.i("gson", e.getMessage());
			return null;
		}

	}

	/**
	 * 
	 * @param jsonStr
	 *            待解析的JSON类
	 * @param clazz
	 *            待赋值的类
	 * @return T 异常则返回为null
	 */
	public static <T> T parseJsonToObj(JSONObject jsonStr, Class<T> clazz) {
		/*
		 * try { Log.i("parseJson", "no exception"); return new
		 * Gson().fromJson(jsonStr, clazz); } catch (Exception e) {
		 * e.printStackTrace(); Log.i("gson",e.getMessage());
		 * 
		 * }
		 */
		return null;
	}

	public static <T> List<T> parseJsonToList(String jsonStr, Type type) {
		try {
			List<T> list = new Gson().fromJson(jsonStr, type);
			return list;
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * 
	 * @param obj
	 *            待转换成JSON字符串的对象
	 * @return 异常返回为null
	 */

	public static String objToJsonStr(Object obj) {
		return new Gson().toJson(obj);
	}
}
