package com.zulridzwan.lib.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MapWrapperLayout extends RelativeLayout {
	
	private GoogleMap map;
	private int bottomOffsetPixels;
	private Marker marker;
	private View infoWindow;

	//Constructor Methods
	public MapWrapperLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}	
	
	public MapWrapperLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MapWrapperLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	/**
     * Must be called before we can route the touch events
     */
	public void init(GoogleMap map, int bottomOffsetPixels) {
        this.map = map;
        this.bottomOffsetPixels = bottomOffsetPixels;
    }
	
	/**
     * Best to be called from either the InfoWindowAdapter.getInfoContents 
     * or InfoWindowAdapter.getInfoWindow. 
     */
    public void setMarkerWithInfoWindow(Marker marker, View infoWindow) {
        this.marker = marker;
        this.infoWindow = infoWindow;
    }
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean ret = false;
        // Make sure that the infoWindow is shown and we have all the needed references
        if (marker != null && marker.isInfoWindowShown() && map != null && infoWindow != null) {
            // Get a marker position on the screen
            Point point = map.getProjection().toScreenLocation(marker.getPosition());

            // Make a copy of the MotionEvent and adjust it's location
            // so it is relative to the infoWindow left top corner
            MotionEvent copyEv = MotionEvent.obtain(ev);
            MotionEvent myEv = MotionEvent.obtain(ev);
            int f1= bottomOffsetPixels;
            float f2 = infoWindow.getWidth() / 2;
            float f3 = infoWindow.getHeight();
            float f4=point.x;
            float f5=point.y;
            float f6=-f4+f2;
            float f7=-f5+f3+f1;
            float f8 = myEv.getX();
            float f9 = myEv.getY();
            float f10=f8+f6;
            float f11=f9+f7;
            copyEv.offsetLocation(
                -point.x + (infoWindow.getWidth() / 2), //equals to marker point x minus half infowindow width to get the x point of infowindow top left corner
                -point.y + infoWindow.getHeight() + bottomOffsetPixels);//equals to marker point y minus height of infowindow minus the offset between marker and bottom border of infowindow
            //in other words, if the touch point is within the infowindow its values will be positive (f10 and f11), outside 
            float f12=copyEv.getRawX();
            float f13=copyEv.getRawY();

            // Dispatch the adjusted MotionEvent to the infoWindow
            ret = infoWindow.dispatchTouchEvent(copyEv);
        }
        // If the infoWindow consumed the touch event, then just return true.
        // Otherwise pass this event to the super class and return it's result
        return ret || super.dispatchTouchEvent(ev);
    }
	



}
