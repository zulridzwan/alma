package com.zulridzwan.alma;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener;
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.zulridzwan.lib.map.MapWrapperLayout;
import com.zulridzwan.lib.map.OnInfoWindowElemTouchListener;
import com.zulridzwan.lib.model.MyItem;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class FifthActivity extends Activity implements ClusterManager.OnClusterClickListener<MyItem>, 
ClusterManager.OnClusterItemClickListener<MyItem> {
	
	private GoogleMap Gmap;
	private MapWrapperLayout mapWrapperLayout;
	private MapFragment mapFragment;
	private ClusterManager<MyItem> mClusterManager;
	private Cluster<MyItem> clickedCluster;
    private MyItem clickedClusterItem;
    private View infoWindow;
    private TextView infoTitle;
    private TextView infoTitle2;
    private TextView infoReporter;
    private TextView infoSnippet;
    private Button infoButton;
    private OnInfoWindowElemTouchListener infoButtonListener;
    private int serverResponseCode = 0;
    private String baseFolder;
    private String jsonMarkers;
	private double coordX;
	private double coordY;
	private float zlvl;
	private int resultCode = -2;


	
    //Function to verify map availability and get a handle to GoogleMap object
  	//https://developers.google.com/maps/documentation/android/map
  	private void setUpMap() {
  	    // Do a null check to confirm that we have not already instantiated the map.
  	    if (Gmap == null) {
  	        //Gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
  	    	//Test MapWrapperLayout
  	    	mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mapwrapped);
  	    	Gmap = mapFragment.getMap();
  	        mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_wrapper_layout);
  	        // Check if we were successful in obtaining the map.
  	        if (Gmap != null) {
  	            // The Map is verified. It is now safe to manipulate the map.

  	        }
  	    }
  	}
  	
  	/* Not USed
  	private void readItems() throws JSONException {
        InputStream inputStream = getResources().openRawResource(R.raw.radar_search);
        List<MyItem> items = new MyItemReader().read(inputStream);
        
            
            //for (int i = 0; i < 10; i++) {
            //double offset = i / 60d;
            //You need the "d" after 60 to make 60 a double, otherwise it's an int and your result would therefore be an int too, 
            //making hours a whole number double. By making it a double, you make java up-cast min to a double for the calculation, 
            //which is what you want.
             
            for (MyItem item : items) {
                LatLng position = item.getPosition();
                //double lat = position.latitude + offset;
                //double lng = position.longitude + offset;
                double lat = position.latitude;
                double lng = position.longitude;
                MyItem offsetItem = new MyItem(lat, lng);
                mClusterManager.addItem(offsetItem);
            }
        //}
    }*/
  	
  	private void readMapDataFile(){
    
  	try {	
  	  File file = new File(baseFolder + "/" + getResources().getString(R.string.mapdata));
  	  String result="";
	  BufferedReader reader = new BufferedReader(new FileReader(file));
	  StringBuilder sb = new StringBuilder();
	  String line = "";
      while ((line = reader.readLine()) != null){
        sb.append(line + "\n");
      }
      result = sb.toString();
      this.jsonMarkers = result;
      
	  reader.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  	}
  	
  	private void redrawMarkers(){
  	try {
	  JSONArray jArray = new JSONArray(jsonMarkers);
	  for (int i = 0; i < jArray.length(); i++) 
	  {
	    JSONObject jObject = jArray.getJSONObject(i);
		double lat = jObject.getDouble("lat");
	    double lng = jObject.getDouble("lng");
	    Integer rowid = jObject.getInt("rowid");
	    String reporterid = jObject.getString("reporterid");
	    String reportername = jObject.getString("reportername");
	    String address = jObject.getString("address");
	    String town = jObject.getString("town");
	    String district = jObject.getString("district");
	    String state = jObject.getString("state");
	    String details = jObject.getString("details");
	    String filename =jObject.getString("filename");
	    String emailaddress =jObject.getString("email");
			 
		MyItem markerItem = new MyItem(lat, lng);
	    markerItem.setRowid(rowid);
	    markerItem.setReporterid(reporterid);
	    markerItem.setReportername(reportername);
	    markerItem.setAddress(address);
	    markerItem.setTown(town);
	    markerItem.setDistrict(district);
	    markerItem.setState(state);
	    markerItem.setDetails(details);
	    markerItem.setFilename(filename);
	    markerItem.setEmailaddress(emailaddress);
	    mClusterManager.addItem(markerItem);
	  }
	  mClusterManager.cluster();
	  
	            
	} catch (Exception e) {
	  // TODO Auto-generated catch block
	  e.printStackTrace();
	}
  	}
  	
  	
  	
  	
  	
  	public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }

    	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth);
        setUpMap();
        int googleplayserviceinstalled = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        String googleplayservice_err_desc = GooglePlayServicesUtil.getErrorString(googleplayserviceinstalled);
        if(googleplayservice_err_desc!="SUCCESS"){
          Toast.makeText(getApplicationContext(), googleplayservice_err_desc, Toast.LENGTH_SHORT).show();
        }
        
        // check if external storage is available
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
         //baseFolder = Environment.getExternalStoragePublicDirectory(Environment.MEDIA_MOUNTED).toString();
           baseFolder = this.getExternalFilesDir(null).getAbsolutePath();
         //baseFolder = this.getFilesDir().getAbsolutePath();
        }
        // revert to using internal storage (not sure if there's an equivalent to the above)
        else {
          baseFolder = this.getFilesDir().getAbsolutePath();
        }
        
        String coordinate = getResources().getString(R.string.default_position);
        String[] coord = coordinate.split(",");
        coordX = Double.parseDouble(coord[0]);
        coordY = Double.parseDouble(coord[1]);
        
        
        String readurl = getResources().getString(R.string.read_url);
        AsyncTask<String, Void, Boolean> task = new ReportFetcher(this);
        task.execute(new String[] {readurl});
       
        
        
        // MapWrapperLayout initialization
        // 39 - default marker height
        // 20 - offset between the default InfoWindow bottom edge and it's content bottom edge 
        mapWrapperLayout.init(Gmap, getPixelsFromDp(this, 39 + 20));
        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.custom_info_contents, null);
        this.infoTitle = (TextView)infoWindow.findViewById(R.id.title);
        this.infoTitle2 = (TextView)infoWindow.findViewById(R.id.title2);
        this.infoSnippet = (TextView)infoWindow.findViewById(R.id.snippet);
        this.infoReporter = (TextView)infoWindow.findViewById(R.id.reporter);
        this.infoButton = (Button)infoWindow.findViewById(R.id.view_detail);
        
        infoButtonListener = new OnInfoWindowElemTouchListener(infoButton,
                getResources().getDrawable(R.drawable.btn_default_holo_light),
                getResources().getDrawable(R.drawable.btn_default_pressed_holo_light))
        {

			@Override
			protected void onClickConfirmed(View v, Marker marker) {
				// TODO Auto-generated method stub
				// Here we can perform some action triggered after clicking the button
                //Toast.makeText(getApplicationContext(), marker.getTitle() + "'s button clicked!", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(FifthActivity.this,SixthActivity.class);
				Bundle b = new Bundle();
			    b.putParcelable("reportdetail", clickedClusterItem);
			    intent.putExtra("mainbundle", b);
			    startActivityForResult(intent,1);
			    marker.hideInfoWindow();
			}
			
			@Override
		    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				return;
			}
			
        	
        };
        infoButton.setOnTouchListener(infoButtonListener);

        
        Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordX, coordY), 9));
        mClusterManager = new ClusterManager<MyItem>(this, Gmap);
        mClusterManager.setRenderer(new MyClusterRenderer(this, Gmap, mClusterManager));
        //ClusterManager implement GoogleMap.InfoWindowAdapter
        Gmap.setInfoWindowAdapter(mClusterManager.getMarkerManager());
        //Gmap.setOnInfoWindowClickListener(mClusterManager);
        Gmap.setOnMarkerClickListener(mClusterManager);
        
        /*Not Used
        mClusterManager.getClusterMarkerCollection().setOnInfoWindowAdapter(new InfoWindowAdapter()
        {

			@Override
			public View getInfoContents(Marker marker) {
				if (clickedCluster != null) {
					infoTitle.setText(String
	                        .valueOf(clickedCluster.getItems().size())
	                        + " more offers available");
	            }
				infoButtonListener.setMarker(marker);
				mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
				return infoWindow;
			}

			@Override
			public View getInfoWindow(Marker marker) {
				if (clickedCluster != null) {
					
				}
				return null;
			}
        	
        }
        );*/
        
        mClusterManager.getMarkerCollection().setOnInfoWindowAdapter(new InfoWindowAdapter()
        {

			@Override
			public View getInfoContents(Marker marker) {
				if (clickedClusterItem != null) {
					infoTitle.setText(clickedClusterItem.getAddress() +" "+ clickedClusterItem.getTown());
					infoTitle2.setText(clickedClusterItem.getDistrict());
					infoReporter.setText(" by: " + clickedClusterItem.getReportername());
					infoSnippet.setText(clickedClusterItem.getDetails());
					

	            }
				//Need to be set to display the button normal/pressed appearance (a workaround / hack)
				infoButtonListener.setMarker(marker);
				mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);
				return infoWindow;
			}

			@Override
			public View getInfoWindow(Marker marker) {
				if (clickedClusterItem != null) {
					
				}
				return null;
			}
        	
        }
        );
        	
        
        
        
        //Not used
        //mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        
        //Not Used
        /*mClusterManager.setOnClusterClickListener(new OnClusterClickListener<MyItem>() {
            @Override
            public boolean onClusterClick(Cluster<MyItem> cluster) {
                clickedCluster = cluster;
                return false;
            }
        });*/
        
        mClusterManager.setOnClusterItemClickListener(new OnClusterItemClickListener<MyItem>() {
            @Override
            public boolean onClusterItemClick(MyItem item) {
                clickedClusterItem = item;
                return false;
            }
        });
        
        Gmap.setOnCameraChangeListener(mClusterManager);
        
        /*try {
            readItems();
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }*/

   
        //String helpmsg = getResources().getString(R.string.view_detail_instruct);
        
        //Toast.makeText(this, helpmsg, Toast.LENGTH_LONG).show();
	}
	
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.resultCode = resultCode;
		if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	        	//Bundle bundle = data.getParcelableExtra("ret");
	        	//test = bundle.getParcelable("marker");
	        	//mClusterManager.removeItem(test); got exception maybe due to the below:
	        	//http://stackoverflow.com/questions/22048872/issue-on-removing-item-from-clustermanager
	        	this.zlvl = Gmap.getCameraPosition().zoom;
	        	LatLng point = clickedClusterItem.getPosition();
	            this.coordX = point.latitude;
	            this.coordY = point.longitude;
	        	String readurl = getResources().getString(R.string.read_url);
	        	mClusterManager.clearItems();
	        
	        if(Gmap!=null){//Check if map is still cached 
	        	new ReportFetcher(this).execute(new String[]{readurl});
				  Handler handler = new Handler(); 
			      handler.postDelayed(new Runnable() { //delay executing for 3 seconds
			         public void run() { 
			        	 readMapDataFile(); 
			         } 
			      }, 3000);
			      Gmap =null;

	        	}
	        	

	        }
	        if (resultCode == RESULT_CANCELED) {
	            //Write your code if there's no result
	        }
	    }
	}
	
	
	private class MyClusterRenderer extends DefaultClusterRenderer<MyItem> {

		public MyClusterRenderer(Context context, GoogleMap map,
				ClusterManager<MyItem> clusterManager) {
			super(context, map, clusterManager);
			// TODO Auto-generated constructor stub
		}
		
		@Override
	    protected void onBeforeClusterItemRendered(MyItem item,
	            MarkerOptions markerOptions) {
			// you can change marker options
	        super.onBeforeClusterItemRendered(item, markerOptions);
	    }

	    @Override
	    protected void onClusterItemRendered(MyItem clusterItem, Marker marker) {
	        super.onClusterItemRendered(clusterItem, marker);
	    }
	    
	    //http://stackoverflow.com/questions/24159284/map-clustering-max-zoom-markers-still-clustered
	    //http://stackoverflow.com/questions/23363188/does-the-google-maps-android-api-utility-cluster-manager-have-a-minimum-number-o
	    @Override
	    protected boolean shouldRenderAsCluster(Cluster<MyItem> cluster) {
	        //return cluster.getSize() > 5; // if markers <=5 then not clustering
	    	//start clustering if at least 2 items overlap
	        return cluster.getSize() > 1;
	    }
	    
	}
	
	private class ReportFetcher extends AsyncTask<String, Void, Boolean>{

		HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
		private Activity activity;
		private ProgressDialog dialog;
        
        public ReportFetcher(Activity activity){
        	this.activity = activity;
            dialog = new ProgressDialog(activity);
        }
		
		@Override
		protected Boolean doInBackground(String... params) {
			
			try {
	        	 // open a URL connection to the Servlet
	            URL url = new URL(params[0]);
	             
	            // Open a HTTP  connection to  the URL
	            conn = (HttpURLConnection) url.openConnection(); 
	            conn.setDoInput(true); // Allow Inputs
	            conn.setDoOutput(true); // Allow Outputs
	            conn.setUseCaches(false); // Don't use a Cached Copy
	            conn.setRequestMethod("POST");
	            conn.setRequestProperty("Connection", "Keep-Alive");
	            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	            
	            dos = new DataOutputStream(conn.getOutputStream());
	            
	            //if(params.length>1){
	            	dos.writeBytes(twoHyphens + boundary + lineEnd);
	                dos.writeBytes("Content-Disposition: form-data; name=\"dummy\"" + lineEnd);
	                dos.writeBytes(lineEnd);
	                dos.writeBytes("dummy");
	                Log.i("ReportFetcher", "Value sent is dummy");
	                dos.writeBytes(lineEnd);
	            //}
	            //end message
	            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	            // Responses from the server (code and message)
	            serverResponseCode = conn.getResponseCode();
	            String serverResponseMessage = conn.getResponseMessage();
	              
	            Log.i("ReportFetcher", "HTTP Response is : "
	                    + serverResponseMessage + ": " + serverResponseCode);
	            
	            dos.flush();
	            dos.close();
	            String line="";
	            InputStreamReader isr = new InputStreamReader(conn.getInputStream());
	            BufferedReader reader = new BufferedReader(isr);
	            final StringBuilder sb = new StringBuilder();
	            while ((line = reader.readLine()) != null)
	            {
	                sb.append(line + "\n");
	            }
	            isr.close();
	            reader.close();
	            
	            if(serverResponseCode == 200){
	               String msg;
	               msg = sb.toString();
	               msg = msg.trim();
	               serverResponseCode = 400;
	               if(msg!=null){
	                 serverResponseCode = 200;
	                 File file = new File(baseFolder + "/" + getResources().getString(R.string.mapdata));
	                 FileOutputStream fos = new FileOutputStream(file);
	                 fos.write(msg.getBytes());
	                 fos.flush();
	                 fos.close();
	               }
	               
	               return true;
                   
                }
	            
	        	
	        }
	        catch(Exception ex){
	        	ex.printStackTrace();
	        	Log.e("ReportFetcher fail to read from server / database", "Exception : "+ ex.getMessage(), ex);
	        }
			return false;
	  		 		
			
		}
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Loading Map...");
            this.dialog.show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			
			Log.i("ReportFetcher", "return result =" + result.toString());
			
			if (result==false) {
               Toast.makeText(activity, "Unable to update map", Toast.LENGTH_SHORT).show();
            }
			readMapDataFile();		
			redrawMarkers();
		}

		
		
	}

	@Override
	public boolean onClusterItemClick(MyItem item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onClusterClick(Cluster<MyItem> cluster) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
    protected void onResume() {
        super.onResume();
        if(this.resultCode !=RESULT_CANCELED && this.resultCode!=-2){
          setUpMap();
          if(this.resultCode==RESULT_OK){
        	  //A hack to refresh marker; also see the onActivityResult method
        	  Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordX, coordY), zlvl+1));
        	  Gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(coordX, coordY), zlvl));
          }
          this.resultCode = -2;
        } 

    }
	
	
  

}




