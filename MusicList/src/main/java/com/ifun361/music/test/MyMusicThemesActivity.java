package com.ifun361.music.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;

import com.ifun361.musiclist.R;
import com.ifun361.musiclist.api.CommonHandler;
import com.ifun361.musiclist.api.HttpUtil;
import com.ifun361.musiclist.constants.ApiConstants;
import com.ifun361.musiclist.db.DaoMaster;
import com.ifun361.musiclist.db.DaoMaster.DevOpenHelper;
import com.ifun361.musiclist.db.DaoSession;
import com.ifun361.musiclist.db.ThemesListDao;
import com.ifun361.musiclist.model.BaseModel;
import com.ifun361.musiclist.model.ThemeModel;
import com.ifun361.musiclist.model.ThemesList;
import com.ifun361.musiclist.utils.NetUtils;



public class MyMusicThemesActivity extends Activity{

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
	
	Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MUSICTHEMES_SUCCESS:
				ThemeModel result = (ThemeModel) msg.obj;
				if(themesSummaries == null)
					themesSummaries = new ArrayList<ThemesList>();
				themesSummaries.clear();
				Set<Map.Entry<Integer,  ThemesList>> entrySet = result.themes.entrySet();
				for(Iterator<Map.Entry<Integer, ThemesList>> iterator = entrySet.iterator();iterator.hasNext();){
					Map.Entry<Integer, ThemesList> entry = iterator.next();
					ThemesList themesSummary = entry.getValue();
					themesSummaries.add(themesSummary);
				}
				if(adapter==null){
					adapter = new MusicThemesListAdapter(mContext,themesSummaries);
					}else{
					adapter.notifyDataSetChanged();
				}
				mListViewMusicThemes.setAdapter(adapter);
				//TODO数据库操作 
				themesListDao.deleteAll();
				saveThemesLists(themesSummaries);
				
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
		this.mContext = MyMusicThemesActivity.this;
		
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
		getMusicThemesList();
	}
	private void getMusicThemesList() {
				//try {
					if(NetUtils.isNetworkConnected(mContext)){
						HttpUtil.getClient().get(mContext, ApiConstants.URL_THEME_LIST, new CommonHandler(ThemeModel.class) {
							@Override
							public void onSuccess(BaseModel obj) {
								ThemeModel themeModel  = (ThemeModel) obj;
								Message message = handler.obtainMessage();
								message.obj = themeModel;
								message.what = MUSICTHEMES_SUCCESS;
								handler.sendMessage(message);
							}
							@Override
							public void onFailure(BaseModel obj) {
								
							}
						});
						
					//ThemesResult musicThemesList = mIpOpenApi.getMusciThemesListService().getMusicThemesList();
				/*	Message message = handler.obtainMessage();
					message.obj = musicThemesList;
					message.what = MUSICTHEMES_SUCCESS;
					handler.sendMessage(message);*/
					}else{
						/*ArrayList<Commodity> commodities = (ArrayList<Commodity>) ProductsDao.getInstance(mContext).queryCommodity();
						Message message = handler.obtainMessage();
						message.obj = commodities;
						message.what = MUSICTHEMES_FAIL;
						handler.sendMessage(message);*/
					}
					
	  			/*} catch (ServiceException e) {
					e.printStackTrace();
				}
				*/
			
			}
	
}
