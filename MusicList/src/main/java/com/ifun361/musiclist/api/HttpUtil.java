package com.ifun361.musiclist.api;

import org.apache.http.HttpEntity;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpUtil {
	private static AsyncHttpClient client = new AsyncHttpClient(); // 实例话对象

	static {
		client.setTimeout(11000); // 设置链接超时，如果不设置，默认为10s
	}

	public static void get(String urlString, AsyncHttpResponseHandler res) // 用一个完整url获取一个string对象
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params,
			AsyncHttpResponseHandler res) // url里面带参数
	{
		client.get(urlString, params, res);
	}

	public static void get(String urlString, JsonHttpResponseHandler res) // 不带参数，获取json对象或者数组
	{
		client.get(urlString, res);
	}

	public static void get(String urlString, RequestParams params,
			JsonHttpResponseHandler res) // 带参数，获取json对象或者数组
	{
		client.get(urlString, params, res);
	}

	public static void get(String uString, BinaryHttpResponseHandler bHandler) // 下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}

	public static void postHasHeader(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Content-Type", "application/x-www-form-urlencoded");
		client.post(url, params, responseHandler);
	}

	public static void post(Context context, String url, HttpEntity entity,
			String contentType, AsyncHttpResponseHandler responseHandler) {
		client.post(context, url, entity, contentType, responseHandler);
	}

	public static void post(Context context, String url, String token,
			HttpEntity entity, String contentType,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Set-Cookie", token);
		client.post(context, url, entity, contentType, responseHandler);
	}

	public static void post(String url, String token, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.addHeader("Set-Cookie", token);
		client.post(url, params, responseHandler);
	}

	public static AsyncHttpClient getClient() {
		return client;
	}
}
