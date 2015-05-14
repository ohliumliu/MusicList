package com.ifun361.musiclist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.ifun361.music.application.BaseApplication;
import com.ifun361.musiclist.constants.UIConstants;
import com.ifun361.musiclist.media.PlayerEngine;
import com.ifun361.musiclist.media.PlayerEngineListener;
import com.ifun361.musiclist.media.Playlist;
import com.ifun361.musiclist.model.Playitems;
import com.ifun361.musiclist.model.Playlists;
import com.ifun361.musiclist.model.TrackModel;
import com.ifun361.musiclist.service.PlayerService;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：应用全局变量，设置。
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class MusicListApplication extends BaseApplication {

	public final static String TAG = "CDIMusicApplication";

	// 应用对象实例
	private static MusicListApplication instance;
	// Service 播放器实例引用，UI组件通过Application中实例控制播放状态
	private PlayerEngine mServicePlayerEngine;
	// 播放列表
	private Playlist mPlaylist;
	private PlayerEngineListener mUIEngineListener;
	//列表第一首歌的位置
	private LinkedList<Integer> listIndex;
	//列表最后一首歌的位置
	private LinkedList<Integer> lastIndex;
	
	@Override
	public void onCreate() {
		Log.i(TAG, "onCreate");
		super.onCreate();
		initApp();
	}

	private void initApp() {
		Log.i(TAG, "initApp");
		instance = this;
		tempInitData();
        //Fresco图片初始化
        Fresco.initialize(getApplicationContext());
		startAction(PlayerService.ACTION_SERVICE);
	}
	//TODO需要把列表和所有歌分别设置
	public void setInitPlaylistsData(ArrayList<Playlists> playlistSummaries){
		TrackModel tm = null;
		//自定义playitems集合
		ArrayList<Playitems> playitemsAll = new ArrayList<Playitems>();
		LinkedList<Integer> listIndex = new LinkedList<Integer>();
		LinkedList<Integer> lastIndex = new LinkedList<Integer>();
		//LinkedList<Integer> list = new LinkedList<Integer>();
		for(int i = 0; i<playlistSummaries.size();i++){
			ArrayList<Playitems> playitems = new ArrayList<Playitems>();
				Set<Map.Entry<Integer, Playitems>> entrySetItems = playlistSummaries.get(i).playitems.entrySet();
				for (Iterator<Map.Entry<Integer, Playitems>> iterators = entrySetItems
						.iterator(); iterators.hasNext();) {
					Map.Entry<Integer, Playitems> entrys = iterators
							.next();
					Playitems playlistsItemsSummary = entrys.getValue();
					playitems.add(playlistsItemsSummary);
				}
				//lastIndex.add(playitems.size());
				
				//根据position排序
				Collections.sort(playitems, new SortByPositon());
				//防止为空
				if(playitems.size()>0);
				playitemsAll.addAll(playitems);
				lastIndex.add(playitemsAll.size());
				for(int j = 0;j<playitemsAll.size();j++){
				
					if(playlistSummaries.get(i).getPlaylistId()==playitemsAll.get(j).getPlaylistId()&&playitemsAll.get(j).getPosition()==1)
						listIndex.add(j);
				}
				
		}
		setResideMenuPlayListsDataIndex(listIndex);
		setResideMenuPlayListsDataLast(lastIndex);
		
		for(int j = 0 ;j<playitemsAll.size();j++){
			tm = new TrackModel();
			String result = playitemsAll.get(j).getAudioFileURL();
			result=result.substring(result.lastIndexOf("/")+1,result.length());
			tm.name = result;
			tm.playlistId =playitemsAll.get(j).getPlaylistId();
			tm.url = playitemsAll.get(j).getAudioFileURL();
		mPlaylist.addTrack(tm);
		Log.i(TAG, tm.name+""+playitemsAll.size());
		Log.i(TAG, tm.url);
		}
	}   
	//TODO 排序
	class SortByPositon implements Comparator{

		@Override
		public int compare(Object lhs, Object rhs) {
			Playitems playitems1 = (Playitems) lhs;
			Playitems playitems2 = (Playitems) rhs;
			if (playitems1.getPosition()>playitems2.getPosition())
				return 1;
				return -1;
		}
		
	}
	
	class SortByPlayitemId implements Comparator{

		@Override
		public int compare(Object lhs, Object rhs) {
			Playitems playitems1 = (Playitems) lhs;
			Playitems playitems2 = (Playitems) rhs;
			if (playitems1.getPlayitemId()>playitems2.getPlayitemId())
				return 1;
				return -1;
		}
		
	}
	//设置列表首歌index
	public void setResideMenuPlayListsDataIndex(LinkedList<Integer> listIndex){
		this.listIndex = listIndex;
	}
	//获取列表首歌index
	public LinkedList<Integer> getResideMenuPlayListsDataIndex(){
		return listIndex;
	}
	
	//设置列表首歌index
	public void setResideMenuPlayListsDataLast(LinkedList<Integer> lastIndex){
			this.lastIndex = lastIndex;
		}
	//获取列表首歌index
	public LinkedList<Integer> getResideMenuPlayListsDataLast(){
			return lastIndex;
		}
	
	public LinkedHashMap<Integer,Integer> getFirstInitResideMenuPlayListsData(ArrayList<Playlists> playlistSummaries){
		//自定义playitems集合
		ArrayList<Playitems> playitems = new ArrayList<Playitems>();
		LinkedHashMap<Integer,Integer> list = new LinkedHashMap<Integer,Integer>();
		for(int i = 0; i<playlistSummaries.size();i++){
				Set<Map.Entry<Integer, Playitems>> entrySetItems = playlistSummaries.get(i).playitems.entrySet();
				for (Iterator<Map.Entry<Integer, Playitems>> iterators = entrySetItems
						.iterator(); iterators.hasNext();) {
					Map.Entry<Integer, Playitems> entrys = iterators
							.next();
					Playitems playlistsItemsSummary = entrys.getValue();
					playitems.add(playlistsItemsSummary);
				}
				
				for(int j = 0;j<playitems.size();j++){
					if(playlistSummaries.get(i).getPlaylistId()==playitems.get(j).getPlaylistId()){
						if(playitems.get(j).getPosition()==1)
							list.put(i, j);
							//返回位置
							
					}
				}
			
		}
		return list;
	}
	
	
	private void tempInitData() {
		mPlaylist = new Playlist();
		/*TrackModel tm = null;
		for (int i = 0; i < UIConstants.TEST_MUSIC_URLS.length; i++) {
			tm = new TrackModel();
			tm.name = i + tm.name;
			tm.url = UIConstants.TEST_MUSIC_URLS[i];
			mPlaylist.addTrack(tm);
		}*/
	}

	public static MusicListApplication getAPP() {
		return instance;
	}

	public void setPlayerEngine(PlayerEngine playerEngine) {
		this.mServicePlayerEngine = playerEngine;
	}

	public PlayerEngine getPlayerEngine() {
		return mServicePlayerEngine;
	}

	public void setPlaylist(Playlist list) {
		this.mPlaylist = list;
	}

	public Playlist getPlaylist() {
		return mPlaylist;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}

	private void startAction(String action) {
		Intent intent = new Intent(this, PlayerService.class);
		intent.setAction(action);
		startService(intent);
	}

	public void exitApp() {
		// 终止播放
		stopService(new Intent(this, PlayerService.class));
		
		mUIEngineListener = null;
		mServicePlayerEngine = null;
		mPlaylist = null;
		instance = null;
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
		/*ActivityManager activityMgr = (ActivityManager) context
		 * 
				.getSystemService(ACTIVITY_SERVICE);
		activityMgr.killBackgroundProcesses(getPackageName());*/
	}

	public PlayerEngineListener getUIEngineListener() {
		return mUIEngineListener;
	}

	public void setUIEngineListener(PlayerEngineListener listener) {
		this.mUIEngineListener = listener;
	}
}