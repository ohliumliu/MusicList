package com.ifun361.music.test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.ifun361.musiclist.R;
import com.ifun361.musiclist.controller.MyThemeController;
import com.ifun361.musiclist.controller.MyThemeController.ThemeCallback;
import com.ifun361.musiclist.db.DaoMaster;
import com.ifun361.musiclist.db.DaoMaster.DevOpenHelper;
import com.ifun361.musiclist.db.DaoSession;
import com.ifun361.musiclist.db.ThemesListDao;
import com.ifun361.musiclist.model.ApiResult;
import com.ifun361.musiclist.model.ThemesList;



public class MusicThemesActivity extends Activity implements ThemeCallback{

	private Context mContext;
	private ListView mListViewMusicThemes;
	private ArrayList<ThemesList> themesSummaries;
	private MusicThemesListAdapter adapter;
	
	private static final int MUSICTHEMES_SUCCESS = 1;
	private static final int MUSICTHEMES_FAIL = 2;
	
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private ThemesListDao themesListDao;
	
	private MyThemeController mThemeController;
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MUSICTHEMES_SUCCESS:
				if (mThemeController ==null)
					mThemeController = new MyThemeController(mContext, MusicThemesActivity.this);
				mThemeController.getThemeDataSet(mContext);
				break;
			default:
				break;
				
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_musicthemes);
		this.mContext = MusicThemesActivity.this;
		
		initView();
		
		
		/**--------GreenDao数据库--------**/
		DevOpenHelper helper = new DevOpenHelper(mContext, "music-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		themesListDao = daoSession.getThemesListDao();
		
		
	}
	//保持批量数据
	 public void saveThemesLists(final List<ThemesList> list){  
         if(list == null || list.isEmpty()){  
              return;  
         }  
         themesListDao.getSession().runInTx(new Runnable() {  
         @Override  
         public void run() {  
             for(int i=0; i<list.size(); i++){  
            	 ThemesList themeslists = list.get(i);  
            	 themesListDao.insertOrReplace(themeslists); 
             }  
         }  
     });  
       
 }  
	
	
	private void initView() {
		mListViewMusicThemes = (ListView) findViewById(R.id.lv_music_themes);
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		handler.sendEmptyMessageDelayed(MUSICTHEMES_SUCCESS, 400);
	}
	
	@Override
	public void onGetTheme(ApiResult<ThemesList> result) {
		
		themesSummaries  =  result.listModel;
		if(adapter==null){
			adapter = new MusicThemesListAdapter(mContext,themesSummaries);
			}else{
			adapter.notifyDataSetChanged();
		}
		mListViewMusicThemes.setAdapter(adapter);
		//TODO数据库操作 
		themesListDao.deleteAll();
		saveThemesLists(themesSummaries);
	}
	
}
