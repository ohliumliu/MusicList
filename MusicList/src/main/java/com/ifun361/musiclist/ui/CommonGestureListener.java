package com.ifun361.musiclist.ui;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public class CommonGestureListener extends SimpleOnGestureListener {
	private int selectedPosition ;
	 
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("QueryViewFlipper", "====> Jieqi: do onDown...");
				return false;
			}
	 
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("QueryViewFlipper", "====> Jieqi: do onShowPress...");
				super.onShowPress(e);
			}
	 
			@Override
		    public void onLongPress(MotionEvent e) {
		        // TODO Auto-generated method stub
				Log.d("QueryViewFlipper", "----> Jieqi: do onLongPress...");
		    }
	 
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("QueryViewFlipper", "====> Jieqi: do onSingleTapConfirmed...");
				return false;
			}
	 
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				Log.d("QueryViewFlipper", "====> Jieqi: do onSingleTapUp...");
				return false;
			}
	 
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY){
				// TODO Auto-generated method stub
				Log.d("QueryViewFlipper", "====> Jieqi: do onFling...");
				if (e1.getX() - e2.getX() > 100 && Math.abs(velocityX) > 50) {
					Log.d("QueryViewFlipper", "====> Jieqi: do onFling...50");
					//向左
					/*selectedPosition = selectedPosition + 1 < titleArray.length ? (selectedPosition + 1) : 0;
					viewFlipper.addView(getContentView());
					viewFlipper.setInAnimation(AnimationControl.inFromRightAnimation());
	                viewFlipper.setOutAnimation(AnimationControl.outToLeftAnimation());
	                viewFlipper.showNext();
	                viewFlipper.removeViewAt(0);*/
				} else if (e2.getX() - e1.getX() > 100 && Math.abs(velocityX) > 50) {
					Log.d("QueryViewFlipper", "====> Jieqi: do onFling...50");
					//向右
					/*selectedPosition = selectedPosition > 0 ? (selectedPosition - 1) : (titleArray.length - 1);
					viewFlipper.addView(getContentView());
					viewFlipper.setInAnimation(AnimationControl.inFromLeftAnimation());
	                viewFlipper.setOutAnimation(AnimationControl.outToRightAnimation());
					viewFlipper.showNext();
					viewFlipper.removeViewAt(0);*/
				}
				return true;
			}
	 
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				Log.d("QueryViewFlipper", "====> Jieqi: do onScroll...");
				
				return super.onScroll(e1, e2, distanceX, distanceY);
			}
	 
			
	

}
