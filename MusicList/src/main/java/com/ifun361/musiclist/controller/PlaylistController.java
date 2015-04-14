package com.ifun361.musiclist.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.ifun361.musiclist.api.CommonHandler;
import com.ifun361.musiclist.api.HttpUtil;
import com.ifun361.musiclist.constants.ApiConstants;
import com.ifun361.musiclist.controller.MyThemeController.ThemeCallback;
import com.ifun361.musiclist.model.ApiResult;
import com.ifun361.musiclist.model.BaseModel;
import com.ifun361.musiclist.model.Playlists;
import com.ifun361.musiclist.model.PlaylistsModel;
import com.loopj.android.http.RequestParams;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：主题逻辑控制类
 * 
 * @author pengying
 * @version 1.0
 * @since JDK1.6
 */
public class PlaylistController extends BaseController{
	private Context ctx;
	private PlaylistCallback callback;
	private RequestParams params;
	
	public PlaylistController(Context ctx, PlaylistCallback cb,RequestParams params) {
		this.callback = cb;
		this.ctx = ctx;
		this.params = params;
	}

	
	public interface PlaylistCallback{
		 void onGetPlaylist(ApiResult<Playlists>result);
	}
	
	public void getPlaylistDataSet(Context context){
		HttpUtil.getClient().get(ApiConstants.URL_MUSIC_LIST, params, 
				new CommonHandler(PlaylistsModel.class){
			@Override
			public void onSuccess(BaseModel obj) {
				PlaylistsModel playlistsModel = (PlaylistsModel) obj;
				ApiResult<Playlists> result = new ApiResult<Playlists>();
				ArrayList<Playlists> playlists = new ArrayList<Playlists>();
				Set<Map.Entry<Integer, Playlists>> entrySet = playlistsModel.playlists
						.entrySet();
				for (Iterator<Map.Entry<Integer, Playlists>> iterator = entrySet
						.iterator(); iterator.hasNext();) {
					Map.Entry<Integer, Playlists> entry = iterator
							.next();
					Playlists playlistsSummary = entry.getValue();
					if(!playlistsSummary.playitems.isEmpty())
					playlists.add(playlistsSummary);
				}
				
				Collections.sort(playlists,new SortByPositon());
				//排序大的列表
				result.resCode = ApiResult.RESULT_CODE_OK;
				result.resMsg = "";
				result.listModel = playlists;
				if (callback != null)
					callback.onGetPlaylist(result);
			}
			
			@Override
			public void onFailure(BaseModel obj) {
				ApiResult<Playlists> result = new ApiResult<Playlists>();
				result.resData = ((ApiResult)obj).resData;
				result.resCode = ((ApiResult)obj).resCode;
				if (callback != null)
					callback.onGetPlaylist(result);
			}
			
		});
	}
	
	class SortByPositon implements Comparator{

		@Override
		public int compare(Object lhs, Object rhs) {
			Playlists playlists1 = (Playlists) lhs;
			Playlists playlists2 = (Playlists) rhs;
			if (playlists1.getPosition()>playlists2.getPosition())
				return 1;
				return -1;
		}
		
	}
}
