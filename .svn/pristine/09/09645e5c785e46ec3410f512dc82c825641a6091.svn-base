package com.ifun361.musiclist.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.ifun361.musiclist.api.CommonHandler;
import com.ifun361.musiclist.api.HttpUtil;
import com.ifun361.musiclist.constants.ApiConstants;
import com.ifun361.musiclist.model.ApiResult;
import com.ifun361.musiclist.model.BaseModel;
import com.ifun361.musiclist.model.ThemeModel;
import com.ifun361.musiclist.model.ThemesList;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：主题逻辑控制类
 * 
 * @author pengying
 * @version 1.0
 * @since JDK1.6
 */
public class MyThemeController extends BaseController {
	private Context ctx;
	private ThemeCallback callback;

	public MyThemeController(Context ctx, ThemeCallback cb) {
		this.callback = cb;
		this.ctx = ctx;
	}

	/**
	 * 返回主题列表结果 x
	 * @author pengying
	 * @param result ThemesList的具体数据bean
	 *
	 */
	public interface ThemeCallback {
		void onGetTheme(ApiResult<ThemesList> result);
	}
	
	public void getThemeDataSet(Context context) {
		//newRequestQueue();
		HttpUtil.getClient().get(context, ApiConstants.URL_THEME_LIST,
				new CommonHandler(ThemeModel.class) {
					@Override
					public void onSuccess(BaseModel obj) {
						ThemeModel themeModel = (ThemeModel) obj;
						ApiResult<ThemesList> result = new ApiResult<ThemesList>();
						ArrayList<ThemesList> themesSummaries = new ArrayList<ThemesList>();
						Set<Map.Entry<Integer, ThemesList>> entrySet = themeModel.themes
								.entrySet();
						for (Iterator<Map.Entry<Integer, ThemesList>> iterator = entrySet
								.iterator(); iterator.hasNext();) {
							Map.Entry<Integer, ThemesList> entry = iterator
									.next();
							ThemesList themesSummary = entry.getValue();
							themesSummaries.add(themesSummary);
						}
						result.resCode = ApiResult.RESULT_CODE_OK;
						result.resMsg = "";
						result.listModel = themesSummaries;
						if (callback != null)
							callback.onGetTheme(result);
					}

					@Override
					public void onFailure(BaseModel obj) {
						ApiResult<ThemesList> result = new ApiResult<ThemesList>();
						result.resData = ((ApiResult)obj).resData;
						result.resCode = ((ApiResult)obj).resCode;
						if (callback != null)
							callback.onGetTheme(result);
					}
				});

	}

}
