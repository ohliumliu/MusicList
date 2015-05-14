package com.ifun361.musiclist.db;

import com.ifun361.music.application.BaseApplication;

import android.content.Context;
/**
 * 数据库工具类
 * @author pengying
 *
 */
public class DbService {

	private static DbService instance;  
    private static Context appContext;  
    private DaoSession mDaoSession;  
    private ListRecommendGroupsListDao recommendGroupsListDao;
    private PlaylistsDao playlistsDao;
    private RecommendsListDao recommendsListDao;
    private ThemesListDao themesListDao;
    
    private DbService() {  
    }  
  
    public static DbService getRecommendGroupsListDaoInstance(Context context) {  
        if (instance == null) {  
            instance = new DbService();  
            if (appContext == null){  
                appContext = context.getApplicationContext();  
            }  
            instance.mDaoSession = BaseApplication.getDaoSession(context);  
            instance.recommendGroupsListDao = instance.mDaoSession.getListRecommendGroupsListDao();  
        }  
        return instance;  
    }
    
    public static DbService getPlaylistsDaoInstance(Context context) {  
        if (instance == null) {  
            instance = new DbService();  
            if (appContext == null){  
                appContext = context.getApplicationContext();  
            }  
            instance.mDaoSession = BaseApplication.getDaoSession(context);  
            instance.playlistsDao = instance.mDaoSession.getPlaylistsDao();  
        }  
        return instance;  
    }  
    
    public static DbService getRecommendsListDaoInstance(Context context) {  
        if (instance == null) {  
            instance = new DbService();  
            if (appContext == null){  
                appContext = context.getApplicationContext();  
            }  
            instance.mDaoSession = BaseApplication.getDaoSession(context);  
            instance.recommendsListDao = instance.mDaoSession.getRecommendsListDao();  
        }  
        return instance;  
    }  
    
    public static DbService getThemesListDaoInstance(Context context) {  
        if (instance == null) {  
            instance = new DbService();  
            if (appContext == null){  
                appContext = context.getApplicationContext();  
            }  
            instance.mDaoSession = BaseApplication.getDaoSession(context);  
            instance.themesListDao = instance.mDaoSession.getThemesListDao();  
        }  
        return instance;  
    }  
    
    
}
