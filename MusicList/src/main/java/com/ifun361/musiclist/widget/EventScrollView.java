/*
 * 创建日期：2015-1-16 下午4:40:57
 */
package com.ifun361.musiclist.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * 版权所有 2005-2015 中国日报社网站。 保留所有权利。<br>
 * 项目名：Android客户端<br>
 * 描述：
 * 
 * @author pengying
 * @version 1.0
 * @since JDK1.6
 */
public class EventScrollView extends ScrollView {


    private OnBorderListener onBorderListener;
    private View contentView;
    // 滑动距离及坐标
    private float xDistance, yDistance, xLast, yLast;
    private GestureDetector gestureDetector;


    public EventScrollView(Context context) {
        super(context);
    }

    public EventScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setGestureDetector(GestureDetector gestureDetector) {
        this.gestureDetector = gestureDetector;
    }
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
			
			if(xDistance>yDistance){
				getParent().requestDisallowInterceptTouchEvent(true); 
			}
		}
		
		return super.onInterceptTouchEvent(ev);
	}
	
	/*@Override
	public boolean onTouchEvent(MotionEven t ev) {
		super.onTouchEvent(ev);
		return gestureDetector.onTouchEvent(ev); 
	}
	*/
	/*@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		boolean ret = super.dispatchTouchEvent(ev);
		  if(ret) 
		  {
		    requestDisallowInterceptTouchEvent(true);
		  }
		  return ret;
		
		//gestureDetector.onTouchEvent(ev);
		//super.dispatchTouchEvent(ev);
		//return true;
	}*/


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        doOnBorderListener();

    }

    public void setOnBorderListener(final OnBorderListener onBorderListener) {
        this.onBorderListener = onBorderListener;
        if (onBorderListener == null) {
            return;
        }

        if (contentView == null) {
            contentView = getChildAt(0);
        }
    }

    private void doOnBorderListener() {
        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
            if (onBorderListener != null) {
                onBorderListener.onBottom();
            }
            }else if (getScrollY() == 0) {
            if (onBorderListener != null) {
                onBorderListener.onTop();
            }

        }


    }
    public static interface OnBorderListener {

        /**
         * Called when scroll to bottom
         */
        public void onBottom();

        /**
         * Called when scroll to top
         */
        public void onTop();
    }
}