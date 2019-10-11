package com.yumatechnical.konnectandroid.Helper;

import android.view.GestureDetector;
import android.view.MotionEvent;


public class Gesture extends GestureDetector.SimpleOnGestureListener {

	public boolean onDoubleTap(MotionEvent event) {
		return false;
	}

	public void onLongPress(MotionEvent event) {
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
		return false;
	}

}