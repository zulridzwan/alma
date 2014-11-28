package com.zulridzwan.lib.map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.google.android.gms.maps.model.Marker;

public abstract class OnInfoWindowElemTouchListener  implements OnTouchListener {
	private final View view;
    private final Drawable bgDrawableNormal;
    private final Drawable bgDrawablePressed;
    private final Handler handler = new Handler();
    
    private Marker marker;
    private boolean pressed=false;

	public OnInfoWindowElemTouchListener(View view, Drawable bgDrawableNormal, Drawable bgDrawablePressed) {
		// TODO Auto-generated constructor stub
		this.view = view;
        this.bgDrawableNormal = bgDrawableNormal;
        this.bgDrawablePressed = bgDrawablePressed;
	}

	@Override
	public boolean onTouch(View vv, MotionEvent event) {
		
		// TODO Auto-generated method stub
		float f1=event.getRawX();
		float f2=event.getRawY();//me: I guess getY will return relative position to the view (in this case the button) that's why the value passed from infoWindow.dispatchTouchEvent(copyEv) different here. Checking getRawY confirms the values are similar.
		float f3=view.getWidth();
		float f4=view.getHeight();
		if (0 <= event.getX() && event.getX() <= view.getWidth() &&
	            0 <= event.getY() && event.getY() <= view.getHeight())
	        {
	            switch (event.getActionMasked()) {
	            case MotionEvent.ACTION_DOWN: startPress(); break;

	            // We need to delay releasing of the view a little so it shows the pressed state on the screen
	            case MotionEvent.ACTION_UP: handler.postDelayed(confirmClickRunnable, 150); break;

	            case MotionEvent.ACTION_CANCEL: endPress(); break;
	            default: break;
	            }
	        }
	        else {
	            // If the touch goes outside of the view's area
	            // (like when moving finger out of the pressed button)
	            // just release the press
	            endPress();
	        }
	        return false;
	}
	
	public void setMarker(Marker marker){
		this.marker=marker;
	}
	
	@SuppressLint("NewApi")
	private void startPress() {
        if (!pressed) {
            pressed = true;
            handler.removeCallbacks(confirmClickRunnable);
            //view.setBackground(bgDrawablePressed);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {             	
            	view.setBackgroundDrawable(bgDrawablePressed);             
            }else{             	
            	view.setBackground(bgDrawablePressed);
            }
            if (marker != null) 
            	//override the parent activity's infowindow to display the button normal/pressed appearance (a workaround / hack)
                marker.showInfoWindow();
        }
    }

    @SuppressLint("NewApi")
	private boolean endPress() {
        if (pressed) {
            this.pressed = false;
            handler.removeCallbacks(confirmClickRunnable);
            //view.setBackground(bgDrawableNormal);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {             	
            	view.setBackgroundDrawable(bgDrawableNormal);             
            }else{             	
            	view.setBackground(bgDrawableNormal);
            }
            if (marker != null) 
                marker.showInfoWindow();
            return true;
        }
        else
            return false;
    }

    private final Runnable confirmClickRunnable = new Runnable() {
        public void run() {
            if (endPress()) {
                onClickConfirmed(view, marker);
            }
        }
    };
    
    protected abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    
    protected abstract void onClickConfirmed(View v, Marker marker);

}
