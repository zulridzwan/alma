package com.zulridzwan.alma;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gms.maps.model.LatLng;
import com.zulridzwan.lib.model.MyItem;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SixthActivity extends Activity {
	
	private TextView infoReportername;
	private TextView infoEmail;
	private TextView infoDetails;
	private TextView infoLat;
	private TextView infoLng;
	private TextView infoAddress;
	private ImageView infoPicture;
	private String imgpath="";
	private MyItem report;
	private int serverResponseCode = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sixth);
		
		this.infoReportername = (TextView)findViewById(R.id.reportername_val);
		this.infoEmail = (TextView)findViewById(R.id.email_val);
		this.infoDetails = (TextView)findViewById(R.id.details_val);
		this.infoLat = (TextView)findViewById(R.id.latitude_val);
		this.infoLng = (TextView)findViewById(R.id.longitude_val);
		this.infoAddress = (TextView)findViewById(R.id.address_val);
		this.infoPicture = (ImageView)findViewById(R.id.asnafpicture);
		
		Intent intent=getIntent();
        Bundle bundle = intent.getParcelableExtra("mainbundle");
        report = bundle.getParcelable("reportdetail");
        LatLng point = report.getPosition();
        Double x = point.latitude;
        Double y = point.longitude;
        
        
        infoReportername.setText(report.getReportername());
        infoEmail.setText(report.getEmailaddress());
        infoDetails.setText(report.getDetails());
        infoLat.setText(x.toString());
        infoLng.setText(y.toString());
        
        String addr = report.getAddress() + " " + report.getTown() + " " + report.getDistrict() +
        		" " + report.getState();
        infoAddress.setText(addr);
        
        AsyncTask<String, Void, Boolean> task = new ImageFetcher(this);
	    task.execute(new String[] {report.getFilename()});
	    
	    infoPicture.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(imgpath!=""){
				  //Uri uri = Uri.parse("file://"+imgpath);
				  //http://stackoverflow.com/questions/1550657/android-image-viewer-from-app
				  Uri uri = Uri.parse("content://com.zulridzwan.alma/"+imgpath);
				  Intent intent = new Intent();                   
				  intent.setAction(android.content.Intent.ACTION_VIEW);
				  intent.setDataAndType(uri, "image/*");
				  startActivity(intent);
				}
				
			}
	    	
	    });
		
	}
	
	
	   
	public void exitdetail(View view){ 
	  Intent returnintent = new Intent();
	  setResult(RESULT_CANCELED, returnintent);
	  finish();
	}
	
	public void markdone(View view){
		  //Intent returnintent = new Intent();
		  //Bundle retbundle = new Bundle();
		  //retbundle.putParcelable("marker", report);
		  //returnintent.putExtra("ret", retbundle);
		  String reportid = report.getRowid().toString();
		  String readurl = getResources().getString(R.string.update_url);
	      AsyncTask<String, Void, Boolean> task = new ReportUpdater(this);
	      task.execute(new String[] {readurl,reportid});
	      
		  
	}
	
	private class ImageFetcher extends AsyncTask<String, Void, Boolean>{
		
		private Activity activity;
		private  Bitmap bmImg;
		private String baseFolder;
		
		public ImageFetcher(Activity activity){
        	this.activity = activity;
        }

		@Override
		protected Boolean doInBackground(String... params) {
			if(params[0].equals("")){
			  imgpath="";
			  return false;
			}
			String addr = getResources().getString(R.string.web_root) +"image/"+ params[0];
			try {
				URL url = new URL(addr);
				imgpath="";
				// check if external storage is available
		        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
		           baseFolder = activity.getExternalFilesDir(null).getAbsolutePath();
		        }
		        // revert to using internal storage (not sure if there's an equivalent to the above)
		        else {
		           baseFolder = activity.getFilesDir().getAbsolutePath();
		        }
		        imgpath=baseFolder+"/"+params[0].toString();
		        File cacheFile = new File(imgpath);
		        
		        InputStream content = null;
		        if(cacheFile.exists()==false){
				  content = (InputStream)url.getContent();
		        } else {
		          content = new FileInputStream(cacheFile.getAbsolutePath());
		        }
				  Bitmap bufferbitmap = BitmapFactory.decodeStream(content);
				  content.close();
				  Log.i("ImageFetcher", "Getting image : " + addr);
				
		          BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(cacheFile));
		          Bitmap mutable = Bitmap.createScaledBitmap(bufferbitmap,bufferbitmap.getWidth(),bufferbitmap.getHeight(),true);
		          mutable.compress(Bitmap.CompressFormat.JPEG, 100, out);
		          out.flush();
		          out.close();
		          bmImg = mutable;
		          Log.i("ImageFetcher", "Save image to: " + imgpath);
		         

				return true;
			} catch (Exception e) {
				Log.e("ImageFetcher", "ImageFetcher Exception: " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result==true) {
				runOnUiThread(new Runnable() {
	                public void run() {
	                  infoPicture.setImageBitmap(bmImg);
	                }
			});
			} else {
				runOnUiThread(new Runnable() {
	                public void run() {
				infoPicture.setImageResource(R.drawable.ic_launcher);
	                }});
			}
		}
		
	}
	
	private class ReportUpdater extends AsyncTask<String, Void, Boolean>{

		HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
		private Activity activity;
		private ProgressDialog dialog;
        
        public ReportUpdater(Activity activity){
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
	            
	            if(params.length>1){
	            	dos.writeBytes(twoHyphens + boundary + lineEnd);
	                dos.writeBytes("Content-Disposition: form-data; name=\"reportid\"" + lineEnd);
	                dos.writeBytes(lineEnd);
	                dos.writeBytes(params[1].toString());
	                Log.i("ReportUpdater", "Value sent is : "+ params[1].toString());
	                dos.writeBytes(lineEnd);
	            }
	            //end message
	            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	            // Responses from the server (code and message)
	            serverResponseCode = conn.getResponseCode();
	            String serverResponseMessage = conn.getResponseMessage();
	              
	            Log.i("ReportUpdater", "HTTP Response is : "
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
	               Log.i("ReportUpdater", "HTTP Received is : "+ msg);
	               if(msg!=null){
	            	   if(msg.equalsIgnoreCase("success|")){
	                     serverResponseCode = 200;
	            	   }  else {
	            		   serverResponseCode = 400;
	            	   }
	               } else {
	            	   serverResponseCode = 400;
	               }
	               return true;
	            } 	            
	        	
	        }
	        catch(Exception ex){
	        	ex.printStackTrace();
	        	Log.e("ReportUpdater fail to read from server / database", "Exception : "+ ex.getMessage(), ex);
	        }
			return false;
	  		 		
			
		}
		
		@Override
		protected void onPreExecute() {
			this.dialog.setMessage("Updating record...");
            this.dialog.show();
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (dialog.isShowing()) {
                dialog.dismiss();
			}
			Log.i("ReportUpdater", "return result =" + result.toString());
			if (result==false) {
				 runOnUiThread(new Runnable() {
		                public void run() {
               Toast.makeText(activity, "Unable to update record", Toast.LENGTH_SHORT).show();
		                }
				 });
            } else {
            	Intent returnintent = new Intent();
            	if(serverResponseCode == 200){
        		  setResult(RESULT_OK, returnintent);
        		  runOnUiThread(new Runnable() {
		                public void run() {
		                	Toast.makeText(activity, "Record updated successfully", Toast.LENGTH_SHORT).show();
		                }
				 });
        	    } else {
        	      setResult(RESULT_CANCELED, returnintent);  
        	    }
            }
			activity.finish();

		}

		
		
	}



}
