package com.ifun361.musiclist.ui;

import com.ifun361.musiclist.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

public abstract class LazyFragment extends Fragment {
	//懒加载
	protected boolean isVisible;
	protected ImageLoader imageLoader;
	// 显示图片的设置
	protected DisplayImageOptions options;

@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	initImageloader();
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
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		if(getUserVisibleHint()){
			isVisible = true;
			onVisible();
		}else {
			isVisible = false;
			onInvisible();
		}
	}
	protected void onVisible() {
		lazyLoad();
	}
	protected abstract void lazyLoad();
	
    protected void onInvisible(){}
}
