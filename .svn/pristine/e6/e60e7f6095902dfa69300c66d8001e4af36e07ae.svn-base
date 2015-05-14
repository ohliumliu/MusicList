package com.ifun361.musiclist.media;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BufferedHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.CharArrayBuffer;

import android.os.Looper;

public class RequestHttpUtil {

	public static final int CONNECTION_TIMEOUT = 8 * 1000;// 连接超时
	public static final int SO_TIMEOUT = 8 * 1000; // Socket超时
	public static final int SOCKET_BUFFER_SIZE = 8 * 1024; // 缓存容量
	public static final int RETRY_COUNT = 3; // HTTP连接失败重试测试

	private int mRetryTimes = 0;
	private List<String> headers = null;
	private int mRequestCode = 0;
	private String mRequestFileType;
	private HttpEntity mResponseEntity;
	private long mFullSize = 0l;

	public long getFullSize() {
		return mFullSize;
	}

	public int getRequestCode() {
		return mRequestCode;
	}

	public String getRequestFileType() {
		return mRequestFileType;
	}

	public HttpEntity getResponseEntity() {
		return mResponseEntity;
	}

	public void setHeader(String key, Object value) {
		if (headers == null)
			headers = new ArrayList<String>();
		if (value == null)
			value = "";
		headers.add(key + ":" + value.toString());
	}

	public HttpResponse request(String url) {
		HttpResponse response = null;
		try {
			if (getUIThreadId() == Thread.currentThread().getId())
				throw new Exception();

			response = retryRequest(url);

			if (response == null)
				throw new Exception();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	private HttpResponse retryRequest(String url) throws Exception {
		Exception le = null;
		while (mRetryTimes < RETRY_COUNT) {
			try {
				return requestInputStream(url, headers);
			} catch (Exception e) {
				mRetryTimes++;
				le = e;
			}
		}
		throw new Exception(le);
	}

	private HttpResponse requestInputStream(String url, List<String> headers)
			throws ClientProtocolException, IOException {
		HttpGet request = new HttpGet(url);
		DefaultHttpClient httpClient = getHttpClident();

		if (headers != null)
			request.setHeaders(getHeaders(headers));

		HttpResponse response = httpClient.execute(request);
		mRequestCode = response.getStatusLine().getStatusCode();
		if (mRequestCode == HttpStatus.SC_OK
				|| mRequestCode == HttpStatus.SC_PARTIAL_CONTENT) {

			mResponseEntity = response.getEntity();
			mFullSize = mResponseEntity.getContentLength();

			final Header typeHeader = mResponseEntity.getContentType();
			if (typeHeader != null)
				mRequestFileType = typeHeader.getValue();
		}
		return response;
	}

	private static Header[] getHeaders(List<String> headers) {
		if (headers == null || headers.size() == 0)
			return null;
		Header[] result = new Header[headers.size()];
		for (int i = 0; i < headers.size(); i++) {
			CharArrayBuffer buffer = new CharArrayBuffer(headers.get(i)
					.length() + 8);
			buffer.append(headers.get(i));
			result[i] = new BufferedHeader(buffer);
		}
		return result;
	}

	public static long getUIThreadId() {
		return Looper.getMainLooper().getThread().getId();
	}

	public static DefaultHttpClient getHttpClident() {
		HttpParams httpParams = new BasicHttpParams();
		// 设置连接超时和 Socket 超时，以及 Socket 缓存大小
		HttpConnectionParams.setConnectionTimeout(httpParams,
				CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, SO_TIMEOUT);
		HttpConnectionParams
				.setSocketBufferSize(httpParams, SOCKET_BUFFER_SIZE);
		// 设置重定向，缺省为 true
		HttpClientParams.setRedirecting(httpParams, true);
		// 设置 user agent
		HttpProtocolParams
				.setUserAgent(
						httpParams,
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6");
		DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

}