package com.zulridzwan.alma;

import java.util.List;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.zulridzwan.lib.map.MapWrapperLayout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

public class ThirdActivity extends Activity implements OnMapLongClickListener{
	
	//Define dummy GPS coordinates
	//static final LatLng MYHOME = new LatLng(3.065570,101.455997);
	//static final LatLng TESTLOCATION = new LatLng(3.085570,101.455997);
	
	private GoogleMap Gmap;
	private MapWrapperLayout mapWrapperLayout;
	private MapFragment mapFragment;
	private LatLng latLng;
	private String SearchedAddr ="";
	MarkerOptions mopts; 

	
	/*= new MarkerOptions()
    .position(MYHOME) 
    .draggable(true) .title("My home") 
    .snippet("Blok A Flat Pelangi Indah") */
    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
    //me: cannot call BitmapDescriptorFactory before Googlemap object initialized?
    //http://stackoverflow.com/questions/13935725/ibitmapdescriptorfactory-is-not-initialized-error
    ;

    //Function to verify map availability and get a handle to GoogleMap object
  	//https://developers.google.com/maps/documentation/android/map
  	private void setUpMapIfNeeded() {
  	    // Do a null check to confirm that we have not already instantiated the map.
  	    if (Gmap == null) {
  	        //Gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
  	    	//Test MapWrapperLayout
  	    	mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapwrapped);
	        Gmap = mapFragment.getMap();
	        Gmap.setMyLocationEnabled(true);
  	        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_wrapper_layout);
  	        // Check if we were successful in obtaining the map.
  	        if (Gmap != null) {
  	            // The Map is verified. It is now safe to manipulate the map.
;
  	        }
  	    }
  	}
  	
  	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    //me: added when adding implements OnLongMapClickListener
  	@Override
  	public void onMapLongClick(final LatLng point) {
    /*Add new marker
  	  String markermsg= "New marker added@" + point.toString();
  	  Marker newMarker = Gmap.addMarker(new MarkerOptions()
          .position(point)
          .snippet(point.toString()) 
          .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maps_indicator_current_position)) 
          .draggable(true));
          newMarker.setTitle(newMarker.getId());
      Toast.makeText(getApplicationContext(), markermsg, Toast.LENGTH_LONG).show();
    */

     View popup_view = getLayoutInflater().inflate(R.layout.screen_popup, null);
     final PopupWindow popupWindow = new PopupWindow(popup_view); 
  	 View parent =  ((MapFragment)  getFragmentManager().findFragmentById(R.id.mapwrapped)).getView();
  	 WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
  	 popupWindow.setWidth(400);
  	 popupWindow.setHeight(300);
  		
  	 int xoff = 0;
  	 //int xoff = -(windowManager.getDefaultDisplay().getWidth() / 2) + popupWindow.getWidth() / 2;
  	 @SuppressWarnings("deprecation")
  	 int yoff = (windowManager.getDefaultDisplay().getHeight() / 2) - popupWindow.getHeight() / 2;
     //popupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
  	 popupWindow.setFocusable(true);
  	 popupWindow.setOutsideTouchable(false);
  	 popupWindow.showAtLocation(parent, Gravity.TOP, xoff, yoff);
     if(popupWindow.isShowing()){
       Log.i("ThirdActivity onMapLongClick", point.toString() + " Searched: "+ this.SearchedAddr);
     }
     popupWindow.update();
           
     Button close1 = (Button) popup_view.findViewById(R.id.add_text_btn);
     Button close2 = (Button) popup_view.findViewById(R.id.del_text_btn);
     close1.setOnClickListener(new OnClickListener() {
       public void onClick(View v) {
       popupWindow.dismiss();
       Intent intent=new Intent(ThirdActivity.this,FourthActivity.class);
       Bundle b = new Bundle();
       b.putParcelable("point", point);
       intent.putExtra("mainbundle", b);
       //Must call after declare extra data into intent
       startActivity(intent);
          	   
       }
     });
           
     close2.setOnClickListener(new OnClickListener() {
       public void onClick(View v) {
         popupWindow.dismiss();
       }
     });
  	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        
        String coordinate = getResources().getString(R.string.device_position);
        String[] coord = coordinate.split(",");
        double coordX = Double.parseDouble(coord[0]);
        double coordY = Double.parseDouble(coord[1]);
        
        setUpMapIfNeeded();
 

        Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordX, coordY), 12));
        
        //me: crude way to test Google Play Services
        //http://developer.android.com/reference/com/google/android/gms/common/GooglePlayServicesUtil.html
        int googleplayserviceinstalled = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        String googleplayservice_err_desc = GooglePlayServicesUtil.getErrorString(googleplayserviceinstalled);
        if(googleplayservice_err_desc!="SUCCESS"){
          Toast.makeText(getApplicationContext(), googleplayservice_err_desc, Toast.LENGTH_SHORT).show();
        }
        
        


        /*Gmap.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    Location curr_location = null;
                    curr_location = Gmap.getMyLocation();
                    if(curr_location!=null){
                    	
                    } else {
                    	
                    }
                    return true;
                }
            });*/
        
        Gmap.setOnMyLocationChangeListener(new OnMyLocationChangeListener() {
            @Override
			public void onMyLocationChange(Location arg0) {
				Gmap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("Current Location"));
				
			}
           });


        
        // Getting reference to btn_find of the layout activity_third.xml
        ImageButton btn_find = (ImageButton) findViewById(R.id.btn_find);
 
        // Defining button click event listener for the find button
        OnClickListener findClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting reference to EditText to get the user input location
                EditText etLocation = (EditText) findViewById(R.id.et_location);
 
                // Getting user input location
                String location = etLocation.getText().toString();
                SearchedAddr = location;
 
                if(location!=null && !location.equals("")){
                    new GeocoderTask().execute(location);
                }
            }
        };
 
        // Setting button click event listener for the find button
        btn_find.setOnClickListener(findClickListener);
        
        //Listen to long click
        Gmap.setOnMapLongClickListener(this);
        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
        mapWrapperLayout.init(Gmap, getPixelsFromDp(this, 39 + 20));
        
        String helpmsg = getResources().getString(R.string.long_click_instruct);
        Toast.makeText(getApplicationContext(), helpmsg, Toast.LENGTH_LONG).show();
	}
	
	// An AsyncTask class for accessing the GeoCoding Web Service
	//http://wptrafficanalyzer.in/blog/android-geocoding-showing-user-input-location-on-google-map-android-api-v2/
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{
 
        @Override
        protected List<Address> doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List<Address> addresses = null;
 
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return addresses;
        }
 
        @Override
        protected void onPostExecute(List<Address> addresses) {
 
            if(addresses==null || addresses.size()==0){
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
 
            // Clears all the existing markers on the map
            Gmap.clear();
 
            // Adding Markers on Google Map for each matching address
            for(int i=0;i<addresses.size();i++){
 
                Address address = (Address) addresses.get(i);
 
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
 
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
                String coord = latLng.toString();
 
                mopts = new MarkerOptions();
                mopts.position(latLng);
                mopts.title(addressText);
                mopts.snippet(coord);
 
                Gmap.addMarker(mopts);
 
                // Locate the first location
                if(i==0)
                    Gmap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        }
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

	

	

	


}
