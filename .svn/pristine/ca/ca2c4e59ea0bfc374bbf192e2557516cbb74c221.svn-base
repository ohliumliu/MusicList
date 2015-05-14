package com.ifun361.musiclist.api;

import java.io.UnsupportedEncodingException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.R.integer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.ifun361.musiclist.model.ApiResult;
import com.ifun361.musiclist.model.BaseModel;
import com.ifun361.musiclist.utils.GsonUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
/**
 * 通用处理Handler
 * @author pengying
 *
 */
public abstract class CommonHandler extends AsyncHttpResponseHandler {
	Class<?> clazz;

	public CommonHandler(Class<? extends BaseModel> clazz) {
		this.clazz = clazz;
	}

	public void onSuccess(BaseModel obj) {
	}

	public void onSuccess(JsonArray listObj) {
	}

	public void onSuccess(String str) {
	}

	public void onFailure(String str) {
	}

	public void onFailure(BaseModel obj) {
	}

	

	//抛出异常到上层
	private BaseModel jsonToObj(Object jsonObj) throws JSONException{
		return (BaseModel) GsonUtil.getInstance().fromJson(((JSONObject)jsonObj).getString("result"),
				clazz);
	}
	
	private ApiResult<BaseModel> errorToModel(int statusCode,String responseBody){
		ApiResult<BaseModel> result = new ApiResult<BaseModel>();
		result.resMsg = String.valueOf(statusCode);
		result.resData = responseBody;
		result.resCode = ApiResult.RESULT_CODE_ERROR;
		return result;
	}
	

	private JsonArray jsonToList(Object jsonObj) {
		JsonParser parser = new JsonParser();
		JsonElement el = parser.parse(jsonObj.toString());
		if (el.isJsonArray()) {
			return el.getAsJsonArray();
		}
		return null;
	}

	@Override
	public void onFailure(int statusCode, Header[] headers,
			byte[] responseBody, Throwable error) {
		String jsonString = getResponseString(responseBody, getCharset());
		onFailure(errorToModel(statusCode,jsonString));
	}

	
	
	@Override
	public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
		try {
			Object jsonResponse = parseResponse(responseBody);
			if (jsonResponse instanceof JSONObject) {
				onSuccess(jsonToObj(jsonResponse));
			} else if (jsonResponse instanceof JSONArray) {
				onSuccess(jsonToList(jsonResponse));
			} else if (jsonResponse instanceof String) {
				onSuccess(jsonResponse.toString());
			} else {
				onFailure("");
			}
		} catch (JSONException e) {
			onFailure(e.getMessage());
		}
	}

	/**
	 * Returns Object of type {@link JSONObject}, {@link JSONArray}, String,
	 * Boolean, Integer, Long, Double or {@link JSONObject#NULL}, see
	 * {@link org.json.JSONTokener#nextValue()}
	 * 
	 * @param responseBody
	 *            response bytes to be assembled in String and parsed as JSON
	 * @return Object parsedResponse
	 * @throws org.json.JSONException
	 *             exception if thrown while parsing JSON
	 */
	protected Object parseResponse(byte[] responseBody) throws JSONException {
		if (null == responseBody)
			return null;
		Object result = null;
		// trim the string to prevent start with blank, and test if the string
		// is valid JSON, because the parser don't do this :(. If JSON is not
		// valid this will return null
		String jsonString = getResponseString(responseBody, getCharset());
		if (jsonString != null) {
			jsonString = jsonString.trim();
			if (jsonString.startsWith(UTF8_BOM)) {
				jsonString = jsonString.substring(1);
			}
			if (jsonString.startsWith("{") || jsonString.startsWith("[")) {
				result = new JSONTokener(jsonString).nextValue();
			}
		}
		if (result == null) {
			result = jsonString;
		}
		return result;
	}

	/**
	 * Attempts to encode response bytes as string of set encoding
	 * 
	 * @param charset
	 *            charset to create string with
	 * @param stringBytes
	 *            response bytes
	 * @return String of set encoding or null
	 */
	public static String getResponseString(byte[] stringBytes, String charset) {
		try {
			String toReturn = (stringBytes == null) ? null : new String(
					stringBytes, charset);
			if (toReturn != null && toReturn.startsWith(UTF8_BOM)) {
				return toReturn.substring(1);
			}
			return toReturn;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
}
