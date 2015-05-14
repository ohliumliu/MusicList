package com.ifun361.musiclist.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;

public class FriendlyViewpager extends ViewPager {
	private static final String TAG = "FriendlyViewpager";
	// 滑动距离及坐标  
    private float xDistance, yDistance, xLast, yLast;  
	private GestureDetector gestureDetector;
	
	public FriendlyViewpager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}


	public FriendlyViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public void setGestureDetector(GestureDetector gestureDetector){
		this.gestureDetector = gestureDetector;
	}

	/*@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

	    int height = 0;
	    for(int i = 0; i < getChildCount(); i++) {
	      View child = getChildAt(i);
	      child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	      int h = child.getMeasuredHeight();
	      if(h > height) height = h;
	    }

	    heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	  }*/
	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return gestureDetector.onTouchEvent(ev);
	}
	*/
	
	
	/*@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance - 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			
			xDistance += Math.abs(curX -xLast);
			yDistance += Math.abs(curY -yLast);
			xLast = curX;
			yLast = curY;
			Log.i(TAG, "xDistance:"+xDistance+"yDistance:"+yDistance);
			if(xDistance<yDistance){
				//getParent().requestDisallowInterceptTouchEvent(true); 
				//return super.onInterceptTouchEvent(ev);
//				requestDisallowInterceptTouchEvent(true);
				return false;
			}
		}
		
		return super.onInterceptTouchEvent(ev);
		
	}*/
	
	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {

		
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance - 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			
			xDistance += Math.abs(curX -xLast);
			yDistance += Math.abs(curY -yLast);
			xLast = curX;
			yLast = curY;
			
			if(xDistance>yDistance){
				//getParent().requestDisallowInterceptTouchEvent(true); 
				return false;
			}
		}
	
		return super.dispatchTouchEvent(ev);
	}*/
	
	/*@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance - 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();
			
			xDistance += Math.abs(curX -xLast);
			yDistance += Math.abs(curY -yLast);
			xLast = curX;
			yLast = curY;
			
			if(xDistance<yDistance){
				//getParent().requestDisallowInterceptTouchEvent(true); 
				//return false;
				//getParent().requestDisallowInterceptTouchEvent(true);
				Log.i(TAG, "ACTION_MOVE");
				getParent().requestDisallowInterceptTouchEvent(true);
			}
		}
	
		return super.onTouchEvent(ev);
	}*/
}
