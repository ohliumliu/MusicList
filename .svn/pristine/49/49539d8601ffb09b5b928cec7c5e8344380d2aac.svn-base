package com.ifun361.music.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.ifun361.musiclist.constants.ApiConstants;
import com.ifun361.musiclist.db.DaoMaster;
import com.ifun361.musiclist.db.DaoMaster.OpenHelper;
import com.ifun361.musiclist.db.DaoSession;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
/**
 * 取得DaoMaster和DaoSession对象
 * @author pengying
 *
 */
public class BaseApplication extends Application {

	private static final String TAG = "BaseApplication";
	private static BaseApplication mInstance;
	private static DaoMaster daoMaster;
	private static DaoSession daoSession;
	private ImageLoader imageLoader;
	
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		if(mInstance == null)
			mInstance = this;
		Log.i(TAG, "onCreate");
	}
	
	 private void initImageLoader(Context context) {
		 ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
			config.threadPriority(Thread.NORM_PRIORITY - 2);
			config.denyCacheImageMultipleSizesInMemory();
			config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
			config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
			config.tasksProcessingOrder(QueueProcessingType.LIFO);
			config.writeDebugLogs(); // Remove for release app

			// Initialize ImageLoader with configuration.
			ImageLoader.getInstance().init(config.build());
	}

	/** 
     * 取得DaoMaster 
     *  
     * @param context 
     * @return 
     */  
    public static DaoMaster getDaoMaster(Context context) {  
        if (daoMaster == null) {  
            OpenHelper helper = new DaoMaster.DevOpenHelper(context,ApiConstants.DB_NAME, null);  
            daoMaster = new DaoMaster(helper.getWritableDatabase());  
        }  
        return daoMaster;  
    }  
      
    /** 
     * 取得DaoSession 
     *  
     * @param context 
     * @return 
     */  
    public static DaoSession getDaoSession(Context context) {  
        if (daoSession == null) {  
            if (daoMaster == null) {  
                daoMaster = getDaoMaster(context);  
            }  
            daoSession = daoMaster.newSession();  
        }  
        return daoSession;  
    }
    
}
