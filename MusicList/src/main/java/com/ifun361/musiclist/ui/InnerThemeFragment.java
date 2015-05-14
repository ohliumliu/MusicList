package com.ifun361.musiclist.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ifun361.musiclist.R;
import com.ifun361.musiclist.constants.ApiConstants;
import com.ifun361.musiclist.model.ThemesList;
import com.ifun361.musiclist.utils.BlurToggleUtils;
import com.ifun361.musiclist.utils.CompressImg;
import com.ifun361.musiclist.utils.Utils;
import com.ifun361.musiclist.widget.EventScrollView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class InnerThemeFragment extends LazyFragment {
	protected static final String TAG = "InnerThemeFragment";
	private RelativeLayout mRelativeLayout;
	
	private EventScrollView mScrollView;
	private SimpleDraweeView mCoverView;
	private ImageView mBlurCoverView;
	private RelativeLayout mInRelativeLayout;
	
	private ImageView mPlayerView;
	private TextView mTitleText;
	private TextView mCountText;
	private TextView mInfoText;

	private Context mContext;
	private Handler mDataSetHandler;
	private ThemesList themesLists;

	private GestureDetector gestureDetector;
	private BlurToggleUtils mBlurToggleUtils;
	
	private View mFragmentsView;
	private Bitmap bitmap;
	private CompressImg compressImg;
	
	protected boolean mBackImageVisiable;

	 // 标志位，标志已经初始化完成。
    private boolean isPrepared;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			int y = mScrollView.getScrollY();
			switch (msg.what) {
			case 0:
			
				break;
			case 1:
				if(mBackImageVisiable)
				mBlurToggleUtils.onDrawerSlide(y, 400);
				break;
			case 2:
				if(mBackImageVisiable)
				mBlurToggleUtils.onDrawerSlide(y, 400);
			default:
				break;
			}
		};
	};
	

	public InnerThemeFragment(Context context, Handler handler,
			ThemesList themesLists) {
		this.mContext = context;
		this.mDataSetHandler = handler;
		this.themesLists = themesLists;
	}

	public InnerThemeFragment() {
	}

	//java单例模式-懒加载（保证线程安全性）
	/*static Fragment instance = null;
	public static Fragment getInstance(Context context, Handler handler,
			ThemesList themesLists) {
		if (instance == null) {
			createInstance(context, handler,
					themesLists);
		}
		return instance;
	}
	private synchronized static Fragment createInstance(Context context, Handler handler,
			ThemesList themesLists) {
		if (instance == null) {
		instance = new InnerThemeFragment(context, handler,
				themesLists);
		}
			return instance;
	}
*/
	
	public static Fragment getInstance(Context context, Handler handler,
			ThemesList themesLists) {
		InnerThemeFragment fragment = new InnerThemeFragment(context, handler,
				themesLists);
		Log.i(TAG, "getInstance");
		return fragment;
		
		}

	public static Fragment getInstance() {
		return new InnerThemeFragment();
	}

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.i(TAG, "onCreateView");
		return inflater.inflate(R.layout.viewpager_fragment_theme, null);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		Log.i(TAG, "onActivityCreated");
		imageLoader = ImageLoader.getInstance();
		setContentView();
		isPrepared = true;
		mBackImageVisiable = false;
		Log.i(TAG, "mBackImageVisiable:"+mBackImageVisiable);
		//initImageloader();
		lazyLoad();
		super.onActivityCreated(savedInstanceState);
	}

	private void initImageloader() {
		// 图片初始化
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.considerExifParams(true)
				.build(); // 创建配置过得DisplayImageOption对象
		
	}	
	
	public void setContentView() {
		long firstTime = System.nanoTime();
		//控件初始化
		mRelativeLayout = (RelativeLayout) getView().findViewById(
				R.id.relayout_theme_background);
		mCoverView =  (SimpleDraweeView)getView().findViewById(
				R.id.frag_theme_imageview1);
		//mCoverView.setImageResource(R.drawable.bg_sample_01);

		mBlurCoverView = (ImageView) getView().findViewById(
				R.id.frag_theme_blur_imageview2);

		mInRelativeLayout = (RelativeLayout) getView().findViewById(
				R.id.frag_theme_info_layout);
		
		//播放按钮
		mPlayerView = (ImageView) getView().findViewById(
				R.id.frag_theme_info_imageview1);
		mPlayerView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				PlayActivity.readyToPlayWithNotify(mContext,themesLists.getThemeId().toString());
			}
		});
		
		//scrollView
		mScrollView = (EventScrollView) getView().findViewById(
				R.id.frag_theme_scrollView1);
		/*//等绘制好了之后测量高度
		ViewTreeObserver viewTreeObserver = mScrollView.getViewTreeObserver();
		viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			
			@Override
			public void onGlobalLayout() {
				mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				//控件高度
				 int heightChild = mScrollView.getChildAt(0).getHeight();
				 int height = mScrollView.getHeight();
				//TODO 这里是PX，有可能会根据需求改成dp
				if(heightChild>height){
					Log.i("111", "heightChild-height"+(heightChild-height));
					 int value =(heightChild-height)<500?500:(heightChild-height);
					mInfoText.setPadding(0, 0, 0,value);
				}else{
					Log.i("111", "heightChild-height"+(heightChild-height));
					mInfoText.setPadding(0, 0, 0,(height+500-heightChild));
				}
			}
		});*/
		
		mScrollView.setOnTouchListener(new View.OnTouchListener() {
			//TODO scrollView位移位置
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final int actionMasked = event.getActionMasked();
				int y = mScrollView.getScrollY();
				switch (actionMasked) {
				case MotionEvent.ACTION_DOWN: {
					Log.i(TAG, "onTouchOutside");
					break;
				}
				case MotionEvent.ACTION_MOVE: {
					Log.i(TAG, "onTouchOutside1:"+y
							+" "+mScrollView.getChildAt(0).getHeight()
							+" "+mScrollView.getHeight()
							+" "+((mScrollView.getChildAt(0).getHeight())-Utils.getMobileHeight(mContext))
							+" "+Utils.getMobileHeight(mContext)
							+" "+mInRelativeLayout.getHeight()
							+" "+mRelativeLayout.getHeight()
							+" boolean"+mBackImageVisiable
							);
					mHandler.sendEmptyMessageDelayed(1, 150);
					Log.i("handler test-----", "false or true");
					if(y>40)
						mPlayerView.setVisibility(View.INVISIBLE);
					break;
				}
				case MotionEvent.ACTION_UP: {
					Log.i(TAG, "onTouchOutside2:"+y);
					mScrollView.getParent().requestDisallowInterceptTouchEvent(
							true);
					mHandler.sendEmptyMessageDelayed(2, 150);
					if(y<=40)
						mPlayerView.setVisibility(View.VISIBLE);
					break;
				}
				case MotionEvent.ACTION_CANCEL: {
					break;
				}
				default:
				}

				return false;

			}
		});

		mTitleText = (TextView) getView().findViewById(
				R.id.frag_theme_info_title1);
		mCountText = (TextView) getView().findViewById(
				R.id.frag_theme_info_count1);
		mInfoText = (TextView) getView().findViewById(
				R.id.frag_theme_info_text1);
			
			
		/*// 图片初始化
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.ic_launcher) // 设置图片下载期间显示的图片
				.showImageForEmptyUri(R.drawable.ic_launcher) // 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.ic_launcher) // 设置图片加载或解码过程中发生错误显示的图片
				.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
				.cacheOnDisk(true) // 设置下载的图片是否缓存在SD卡中
				.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.build(); // 创建配置过得DisplayImageOption对象
*/		

		// 设置具体参数(适配参数需要更改)
		//imageLoader.init(ImageLoaderConfiguration.createDefault(mContext));
	
		
	/*	mTitleText.setText(themesLists.getName());
		Log.i(TAG+"mTitleText+themesLists.getName()", themesLists.getName());
		mCountText.setText(themesLists.getPlaylistCount().toString());
		mInfoText.setText(themesLists.getIntro());*/
		
		
	//	((BaseFragmentActivity)mContext).setTitleViewCenterText(themesLists.getName());
		Log.i(TAG+"themesLists.getName()", themesLists.getName());
		
		long endTime = System.nanoTime();
		System.out.println(endTime-firstTime);
	};
	
	@Override
	public void onStart() {
		super.onStart();
		imageLoader.resume();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		imageLoader.resume();
		//initImageloader();
	}
	
	private void initData() {
		//等绘制好了之后测量高度
				ViewTreeObserver viewTreeObserver = mScrollView.getViewTreeObserver();
				viewTreeObserver.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					
					@Override
					public void onGlobalLayout() {
						mScrollView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						//控件高度
						 int heightChild = mScrollView.getChildAt(0).getHeight();
						 int height = mScrollView.getHeight();
						//TODO 这里是PX，有可能会根据需求改成dp
						if(heightChild>height){
							Log.i("111", "heightChild-height"+(heightChild-height));
							 int value =(heightChild-height)<500?500:(heightChild-height);
							mInfoText.setPadding(0, 0, 0,value);
						}else{
							Log.i("111", "heightChild-height"+(heightChild-height));
							mInfoText.setPadding(0, 0, 0,(height+500-heightChild));
						}
					}
				});
		
		mTitleText.setText(themesLists.getName());
		Log.i(TAG+"mTitleText+themesLists.getName()", themesLists.getName());
		mCountText.setText(themesLists.getPlaylistCount().toString());
		mInfoText.setText(themesLists.getIntro());
	}
//TODO 原图片处理方式
	/*private void initCoverImage() {
		Log.i(TAG, "initCoverImage-------------------");
	imageLoader.displayImage(ApiConstants.URL_THEME_LIST_COVER+themesLists.getThemeId()+"/"+themesLists.getThemeId()+"_cover_2.jpg",mCoverView,new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				//mCoverView.setImageResource(R.drawable.ic_launcher);
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				Resources res=getResources();    
				Bitmap bmp=BitmapFactory.decodeResource(res, R.drawable.ic_launcher);   
				mBlurToggleUtils = new BlurToggleUtils(mContext, bmp,
						mCoverView, mBlurCoverView);
				mBackImageVisiable = true;
				
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				//mCoverView.setImageBitmap(loadedImage);
				//mCoverView.setScaleType(ScaleType.FIT_XY);
				//Bitmap bitmap = loadedImage.copy(loadedImage.getConfig(), true);
				compressImg = new CompressImg();
				Bitmap comp = compressImg.comp(loadedImage);
				//模糊初始化
				mBlurToggleUtils = new BlurToggleUtils(mContext, comp,
						mCoverView, mBlurCoverView);
				mBackImageVisiable = true;
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				
			}
		});
	}*/
    private void initCoverImage() {
    mCoverView.setImageURI(Uri.parse(ApiConstants.URL_THEME_LIST_COVER+themesLists.getThemeId()+"/"+themesLists.getThemeId()+"_cover_2.jpg"));
        Bitmap bitmap1 = ((BitmapDrawable) mCoverView.getDrawable()).getBitmap();
        Bitmap bitmap2 = bitmap1.copy(bitmap1.getConfig(), true);
        mBlurToggleUtils = new BlurToggleUtils(mContext, bitmap2,
                 mBlurCoverView);
        mBackImageVisiable = true;
    }

	
	@Override
	public void onPause() {
		super.onPause();
		imageLoader.pause();
	}

	@Override
	public void onDestroyView() {
		Log.i(TAG, "onDestroyView");
		super.onDestroyView();
		mBackImageVisiable = false;
		//imageLoader.stop();
		imageLoader.cancelDisplayTask(mCoverView);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}
	
	@Override
	public void onDetach() {
		Log.i(TAG, "onDetach");
		super.onDetach();
	}

	@Override
	protected void lazyLoad() {
		 if(!isPrepared || !isVisible) {
	            return;
	        }
		initCoverImage();
		initData();
	}

}
	