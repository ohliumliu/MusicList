package com.ifun361.musiclist.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

public class BlurToggleUtils {
	
	private static final int DEFAULT_RADIUS = 20;
	private static final int DEFAULT_DOWN_SAMPLING = 3;
	private final Context mContext;
	private Bitmap mSource;
	private Bitmap mBitmapView ;
	private ImageView mView ;
	private ImageView mBlurView;
	private float f;
	private Handler mHandler = new Handler();

	
	public BlurToggleUtils(Context context, Bitmap source, ImageView v,
			ImageView bv) {
		mContext = context;
		mSource = source;
		mView = v;
		mBlurView = bv;
		new Thread(new Runnable() {
			@Override
			public void run() {
				setBlurImage();
			}

		}).start();
	}
	
	public BlurToggleUtils(Context context, ImageView v,
			ImageView bv,final Float floats) {
		mContext = context;
		mView = v;
		mSource = ((BitmapDrawable)mView.getDrawable()).getBitmap();
		mBlurView = bv;
		f = floats;
		new Thread(new Runnable() {
			@Override
			public void run() {
				setBlurFloatImage();
			}

		}).start();
	}
	
	/**
	 * Bitmap多态
	 */
	public BlurToggleUtils(Context context, Bitmap bitmap,
			ImageView bv) {
		mContext = context;
		mSource = bitmap;
		//mSource = ((BitmapDrawable)mView.getDrawable()).getBitmap();
		mBlurView = bv;
		new Thread(new Runnable() {
			@Override
			public void run() {
				setBlurFloatImage();
			}

		}).start();
	}

	public void onDrawerSlide(final float slideOffset, final float fullOffset) {
		if (slideOffset > 0f) {
			mBlurView.setVisibility(View.VISIBLE);
			setBlurAlpha(slideOffset / fullOffset);
		} else {
			mBlurView.setVisibility(View.GONE);
		}
	}

	private void setBlurAlpha(float slideOffset) {
		setAlpha(mBlurView, slideOffset);
	}
	
	

	public void setBlurImage() {
		final Bitmap blurred = blurBitmap(mContext, mSource, DEFAULT_RADIUS);
		mSource.recycle();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mBlurView.setVisibility(View.VISIBLE);
				mBlurView.setImageBitmap(null);
				mBlurView.setImageBitmap(blurred);
				((View) mBlurView).setAlpha(0);
			}
		});
	}
	
	public void setBlurFloatImage() {
		final Bitmap blurred = blurBitmap(mContext, mSource, DEFAULT_RADIUS);
		mSource.recycle();
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mBlurView.setVisibility(View.VISIBLE);
				mBlurView.setImageBitmap(null);
				mBlurView.setImageBitmap(blurred);
			}
		});
	}

	public static void setAlpha(View view, float alpha) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			view.setAlpha(alpha);
		} else {
			AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);
			alphaAnimation.setDuration(0);
			alphaAnimation.setFillAfter(true);
			view.startAnimation(alphaAnimation);
		}
	}

	public static Bitmap blurBitmap(Context context, Bitmap sentBitmap,
			float radius) {
		Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

		final RenderScript rs = RenderScript.create(context);
		final Allocation input = Allocation.createFromBitmap(rs, sentBitmap,
				Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_SCRIPT);
		final Allocation output = Allocation.createTyped(rs, input.getType());
		final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs,
				Element.U8_4(rs));
		script.setRadius(radius);
		script.setInput(input);
		script.forEach(output);
		output.copyTo(bitmap);

		rs.destroy();
		input.destroy();
		output.destroy();
		script.destroy();

		return bitmap;

	}

}