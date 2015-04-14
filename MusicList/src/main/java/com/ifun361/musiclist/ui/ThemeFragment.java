package com.ifun361.musiclist.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ifun361.musiclist.R;
import com.ifun361.musiclist.controller.MyThemeController;
import com.ifun361.musiclist.controller.MyThemeController.ThemeCallback;
import com.ifun361.musiclist.db.DaoMaster;
import com.ifun361.musiclist.db.DaoMaster.DevOpenHelper;
import com.ifun361.musiclist.db.DaoSession;
import com.ifun361.musiclist.db.ThemesListDao;
import com.ifun361.musiclist.model.ApiResult;
import com.ifun361.musiclist.model.ThemesList;
import com.ifun361.musiclist.ui.adapter.ThemeCoverAdapter;
import com.ifun361.musiclist.utils.BlurToggleUtils;
import com.ifun361.musiclist.widget.FriendlyViewpager;
import com.ifun361.musiclist.widget.PageListener;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：主题界面
 * 
 * @author baoxinyuan
 * @version 1.0
 * @since JDK1.6
 */
public class ThemeFragment extends BaseFragment implements ThemeCallback  {
	//数据部分
	private ArrayList<ThemesList> themesSummaries;
	
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private ThemesListDao themesListDao;

	private RelativeLayout mFragmentLayout;
	private ScrollView mScrollView;
	private FriendlyViewpager mViewPager;
	private ImageView mCoverView;
	private ImageView mBlurCoverView;
	private ImageView mPlayerView;
	private TextView mTitleText;
	private TextView mCountText;
	private TextView mInfoText;

	private MyThemeController mThemeController;
	private ThemeCoverAdapter adapter;
	
	private BlurToggleUtils mBlurToggleUtils;
	
	private PageListener pageListener;

	private Handler mDataSetHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//int y = mScrollView.getScrollY();
			switch (msg.what) {
			case 0:
				//initDataSet();
				if (mThemeController ==null)
					mThemeController = new MyThemeController(getActivity(), ThemeFragment.this);
				
				if(adapter==null)
					adapter = new ThemeCoverAdapter(getActivity(),getChildFragmentManager(),mDataSetHandler);
				mThemeController.getThemeDataSet(getActivity());
				break;
			case 1:
				/*int y1 = (Integer) msg.obj;
				mBlurToggleUtils.onDrawerSlide(y1, 400);*/
				break;
			case 2:
				/*int y2 = (Integer) msg.obj;
				mBlurToggleUtils.onDrawerSlide(y2, 400);*/
				break;
			default:
			}
			//System.out.println("handler =" + y);
		}
	};

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		setContentView(mFragmentLayout);
		
		//Fragment fragment = InnerThemeFragment.getInstance(getActivity(), mDataSetHandler);
		
		/**--------GreenDao数据库--------**/
		DevOpenHelper helper = new DevOpenHelper(getActivity(), "music-db", null);
		db = helper.getWritableDatabase();
		daoMaster = new DaoMaster(db);
		daoSession = daoMaster.newSession();
		themesListDao = daoSession.getThemesListDao();
		Bundle args = savedInstanceState;
		if (args == null) {
			args = getArguments();
		}
		
		initAlbumState(args);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mFragmentLayout = (RelativeLayout) inflater.inflate(
				R.layout.fragment_mytheme, null);
		//TODO viewPager 
		mViewPager = (FriendlyViewpager) mFragmentLayout.findViewById(R.id.vp_theme);
		//mViewPager.setGestureDetector(new GestureDetector(new CommonGestureListener()));		
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (hidden) {
			super.onHiddenChanged(hidden);
		} else {
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onFragmentRefresh() {
		initAlbumState(null);
	}

	private void initAlbumState(Bundle state) {
		if (state != null) {
		}
		setFragmentLoading();
		/*Bitmap bmp = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_sample_01);*/
		//TODO 模糊处理
		/*mBlurToggleUtils = new BlurToggleUtils(this.getActivity(), bmp,
				mCoverView, mBlurCoverView);*/
		mDataSetHandler.sendEmptyMessageDelayed(0, 400);
	}
	/**
	 * 假数据
	 */
	private void initDataSet() {
		InputStream in = null;
		try {
			in = getResources().getAssets().open("test.properties");
			Properties props = new Properties();
			props.load(in);
			String value = props.getProperty("ifun.test.text1");
			mInfoText.setText(value);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
		setFragmentShowData();
	}

	@Override
	public void onGetTheme(ApiResult<ThemesList> result) {
		// TODO Auto-generated method stub
		themesSummaries  =  result.listModel;
        setFragmentShowData();
        
      //TODO数据库操作
      		if(themesSummaries!=null&&themesSummaries.size()>0){
      		themesListDao.deleteAll();
      		saveThemesLists(themesSummaries);
      		setThemeFragmentDate(themesSummaries);
      		}else{
      			ArrayList<ThemesList> themesLists = getThemesLists();
      			themesSummaries = themesLists;
      			setThemeFragmentDate(themesSummaries);
      		}
			
        
		
	}

	private void setThemeFragmentDate(ArrayList<ThemesList> themesLists) {
		adapter = new ThemeCoverAdapter(getActivity(),getChildFragmentManager(),mDataSetHandler);
        adapter.setData(themesLists);
		mViewPager.setAdapter(adapter);
		pageListener = new PageListener(getActivity(),themesLists);
		mViewPager.setOnPageChangeListener(pageListener);
		//mViewPager.setOffscreenPageLimit(21);
		//int currentItem = mViewPager.getCurrentItem();
		//((BaseFragmentActivity)getActivity()).setTitleViewCenterText(themesLists.get(currentItem).getName());
	}

	private void saveThemesLists(final List<ThemesList> list) {
		//保持批量数据
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
	
	private ArrayList<ThemesList> getThemesLists(){
		List<ThemesList> themesListsDb = themesListDao.loadAll();
		return (ArrayList<ThemesList>) themesListsDb;
	}
	
	
}