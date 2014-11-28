package com.zulridzwan.alma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EighthActivity extends Activity {
	
	private GoogleMap Gmap;
	private MapFragment mapFragment;
	private MarkerOptions mopts; 


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eighth);
		Intent intent=getIntent();
        Bundle bundle = intent.getParcelableExtra("mainbundle");
        String ipoint = bundle.getString("point");
        LatLng point;
        setUpMapIfNeeded();
        
        String pointvar;
        String titlevar;
        String snippetvar;
        double coordX;
        double coordY;

        int pointid;
        int titleid;
        int snippetid;
        
        String pointstr;
        String titlestr;
        String snippetstr;
        
        String[] coordz = ipoint.split(",");
        coordX = Double.parseDouble(coordz[0]);
        coordY = Double.parseDouble(coordz[1]);
        point = new LatLng(coordX, coordY);
        
        for(int i=1;i<24;i++){
        	pointvar="branch" + i + "_position";
        	titlevar="branch" + i + "_title";
        	snippetvar="branch" + i + "_address";
        	
        	pointid = getResources().getIdentifier(pointvar, "string", this.getPackageName());
        	titleid = getResources().getIdentifier(titlevar, "string", this.getPackageName());
        	snippetid = getResources().getIdentifier(snippetvar, "string", this.getPackageName());
        	pointstr = getResources().getString(pointid);
        	titlestr = getResources().getString(titleid);
        	snippetstr = getResources().getString(snippetid);
        	
        	String[] coord = pointstr.split(",");
            coordX = Double.parseDouble(coord[0]);
            coordY = Double.parseDouble(coord[1]);
        	
        	mopts = new MarkerOptions();
            mopts.position(new LatLng(coordX, coordY));
            mopts.title(titlestr);
            mopts.snippet(snippetstr);

            Gmap.addMarker(mopts);
        }
        
        coordz = getResources().getString(R.string.hq_position).split(",");
        coordX = Double.parseDouble(coordz[0]);
        coordY = Double.parseDouble(coordz[1]);
        mopts = new MarkerOptions();
        mopts.position(new LatLng(coordX, coordY));
        mopts.title(getResources().getString(R.string.hq_title));
        mopts.snippet(getResources().getString(R.string.hq_address));
        Gmap.addMarker(mopts);
        Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 15));
        
		
	}
	
	private void setUpMapIfNeeded() {
  	    // Do a null check to confirm that we have not already instantiated the map.
  	    if (Gmap == null) {
  	        //Gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
  	    	//Test MapWrapperLayout
  	    	mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapwrapped);
	        Gmap = mapFragment.getMap();
	        Gmap.setMyLocationEnabled(true);
  	        // Check if we were successful in obtaining the map.
  	        if (Gmap != null) {
  	            // The Map is verified. It is now safe to manipulate the map.;
  	        }
  	    }
  	}
	
	 @Override
	    protected void onResume() {
	        super.onResume();
	        setUpMapIfNeeded();
	    }

	
	

}
