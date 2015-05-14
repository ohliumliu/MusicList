package com.ifun361.musiclist.controller;

import android.content.Context;

import com.ifun361.musiclist.model.ApiResult;
import com.ifun361.musiclist.model.ThemeModel;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：主题逻辑控制类
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class ThemeController extends BaseController {

	private ThemeCallback callback;

	public ThemeController(Context ctx, ThemeCallback cb) {
		this.callback = cb;
	}

	public interface ThemeCallback {
		void onGetTheme(ApiResult<ThemeModel> result, String requestKey);
	}

	public void getThemeDataSet(final String requestKey, String pageno) {
		// newRequestQueue();
		// final String url = String.format(ApiConstants.URL_THEME_LIST,
		// pageno);
		//
		// HttpRequest request = new HttpRequest(this, url);
		//
		// request.setResponseHandler(new JsonHttpResponseHandler() {
		//
		// @Override
		// protected void onSuccess(String errmsg, long time, JSONObject data) {
		// ApiResult<ThemeModel> result = new ApiResult<ThemeModel>();
		// ArrayList<ThemeModel> list = new ArrayList<ThemeModel>();
		// try {
		// result.resCode = ApiResult.RESULT_CODE_OK;
		// final JSONArray array = data.optJSONArray("list");
		// if (array != null && array.length() > 0)
		// list = ThemeModel.jsonToModel(data);
		// } catch (Exception e) {
		// result.resCode = ApiResult.RESULT_CODE_ERROR;
		// result.resMsg = "";
		// }
		//
		// result.listModel = list;
		// if (callback != null)
		// callback.onGetFeedDataSet(result, requestKey);
		// }
		//
		// @Override
		// protected void onFailure(int statusCode, int errno, String errmsg,
		// long time, JSONObject data) {
		// ApiResult<ThemeModel> result = new ApiResult<ThemeModel>();
		// result.resCode = errno + "";
		// result.resMsg = errmsg;
		// if (callback != null)
		// callback.onGetFeedDataSet(result, requestKey);
		// }
		// });
		//
		// addRequestQueue(request);
	}

}
