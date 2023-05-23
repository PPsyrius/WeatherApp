package com.androdocs.weatherapp.Gestures;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.MotionEvent;


public class OnSwipeTouchListener implements OnTouchListener {
    // Fields:
    /** Whether a swipe motion has been detected **/
    protected boolean isSwipeDetected = false;
    private final GestureDetector gestureDetector;


    // Constructors:
    public OnSwipeTouchListener (Context ctx) {
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    private final class GestureListener extends SimpleOnGestureListener {
        private static final int HORIZONTAL_SWIPE_THRESHOLD = 0;
        private static final int HORIZONTAL_SWIPE_VELOCITY_THRESHOLD = 0;
        private static final int VERTICAL_SWIPE_THRESHOLD = 100;
        private static final int VERTICAL_SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {    // More of a horizontal movement
                    if (Math.abs(diffX) > HORIZONTAL_SWIPE_THRESHOLD && Math.abs(velocityX) > HORIZONTAL_SWIPE_VELOCITY_THRESHOLD) {
                        isSwipeDetected = true;
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {    // Vertical movement
                    if (Math.abs(diffY) > VERTICAL_SWIPE_THRESHOLD && Math.abs(velocityY) > VERTICAL_SWIPE_VELOCITY_THRESHOLD) {
                        isSwipeDetected = true;
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                }
                result = true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.performClick();
        return gestureDetector.onTouchEvent(event); //Boolean whether if this is a swipe or not
    }
}
