package com.zulridzwan.alma;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.zulridzwan.lib.camera.BitmapScaler;

public class FourthActivity extends Activity  
{
	
	protected static final int PICK_FROM_CAMERA = 1;

	protected static final int PICK_FROM_FILE = 2;

	private static final int CROP_FROM_CAMERA = 3;
	
	ProgressDialog dialog = null;
	String uploadFilePath = "";
    
    private Uri mImageCaptureUri;
    private AlertDialog imagedialog;
    private ImageView mImageView;
    
    String upLoadServerUri = null;
    int serverResponseCode = 0;
    Double xp, yp;
    String addressStreet, addressTown, addressDistrict, addressState;
    String reporterName, reporterEmail, reporterPhone, reportDetails, reporterPhoneNumber;
    TextView messageText;
    
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth);
                      
        Intent intent=getIntent();
        Bundle bundle = intent.getParcelableExtra("mainbundle");
        TextView lati = (TextView)findViewById(R.id.latitude);
        TextView longi = (TextView)findViewById(R.id.longitude);
        LatLng point = bundle.getParcelable("point");
        xp = point.latitude;
        yp = point.longitude;
        lati.setText(xp.toString());
        longi.setText(yp.toString());
        
        List<Address> address = getAddress(point);
        if(address!=null){
        	EditText addrtxt1 = (EditText)findViewById(R.id.address);
        	EditText addrtxt2 = (EditText)findViewById(R.id.town);
        	EditText addrtxt3 = (EditText)findViewById(R.id.district);
        	EditText addrtxt4 = (EditText)findViewById(R.id.state);
        	/* Sample:..
        	[Address[addressLines=[0:"Jalan Waja",1:"Taman Perindustrian Bukit Raja",2:"41050 Klang, Selangor",3:"Malaysia"],
        	feature=Jalan Waja,admin=Selangor,sub-admin=null,locality=Klang,thoroughfare=Jalan Waja,postalCode=41050,
        	countryCode=MY,countryName=Malaysia,hasLatitude=true,latitude=3.074816,hasLongitude=true,longitude=101.459087,
        	phone=null,url=null,extras=null]]
        	 */
        	addressStreet = address.get(0).getAddressLine(0);
        	addrtxt1.setText(addressStreet);
        	
        	addressTown = address.get(0).getAddressLine(1);
        	addrtxt2.setText(addressTown);
        	
        	addressDistrict = address.get(0).getLocality();
        	addrtxt3.setText(addressDistrict);
        	
        	addressState = address.get(0).getAdminArea();
        	addrtxt4.setText(addressState);

        	
        }
        
        final Map<String,String> valuemap = new HashMap<String,String>();
        reporterPhoneNumber = getMyPhoneNumber();
        
        /************* Php script path ****************/
        upLoadServerUri = getResources().getString(R.string.insert_url);
        Button submit = (Button)findViewById(R.id.btnsubmit);
        messageText  = (TextView)findViewById(R.id.msgtxt);
        captureImageInitialization();

        Button chooseimage = (Button) findViewById(R.id.btnimage);
        Button cancelreport = (Button) findViewById(R.id.btncancel);
        mImageView = (ImageView) findViewById(R.id.asnafpicture);
        //messageText.setText("Uploading file path :- '/mnt/sdcard/DCIM/"+uploadFileName+"'");
        submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = ProgressDialog.show(FourthActivity.this, "", "Submitting report...", true);
				new Thread(new Runnable() {
                    public void run() {
                         /*runOnUiThread(new Runnable() {
                                public void run() {
                                    messageText.setText("uploading started.....");
                                }
                            });*/                     
                         
                         EditText editxt_reportername = (EditText)findViewById(R.id.reportername);
                         EditText editxt_email = (EditText)findViewById(R.id.email);
                         EditText editxt_details = (EditText)findViewById(R.id.details);
                         TextView txtview_latitude = (TextView)findViewById(R.id.latitude);
                         TextView txtview_longitude = (TextView)findViewById(R.id.longitude);
                         EditText editxt_address = (EditText)findViewById(R.id.address);
                         EditText editxt_town = (EditText)findViewById(R.id.town);
                         EditText editxt_district = (EditText)findViewById(R.id.district);
                         EditText editxt_state = (EditText)findViewById(R.id.state);
                         
                         valuemap.put("reportername", editxt_reportername.getText().toString());
                         valuemap.put("email", editxt_email.getText().toString());
                         valuemap.put("details", editxt_details.getText().toString());
                         valuemap.put("latitude", txtview_latitude.getText().toString());
                         valuemap.put("longitude", txtview_longitude.getText().toString());
                         valuemap.put("address", editxt_address.getText().toString());
                         valuemap.put("town", editxt_town.getText().toString());
                         valuemap.put("district", editxt_district.getText().toString());
                         valuemap.put("state", editxt_state.getText().toString());
                         valuemap.put("reporterphone", reporterPhoneNumber);
                         
                         serverResponseCode = uploadFile(uploadFilePath, valuemap);
                         
                         if(serverResponseCode == 200){
                        	 runOnUiThread(new Runnable() {
                                 public void run() {
                                     FourthActivity.this.finish();
                                 }
                             });
                         }
                                                  
                    }
                  }).start(); 
			}
        	
        }
        );
        
        chooseimage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	imagedialog.show();
            }
        }
        );
        
        cancelreport.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	finish();
            }
        }
        );
        
        
        
        
	
	}
	
	 @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if (resultCode != RESULT_OK)
             return;
		 
		 String selectedImagePath;
		 Bitmap bitmap;
		 Bitmap bitmapscaled;
		 switch (requestCode) {
         case PICK_FROM_CAMERA:
                /**
                 * After taking a picture, do the crop
                 */
                //doCrop();
        	   //Not using the crop function
        	 selectedImagePath = mImageCaptureUri.getPath();
        	 //File file = new File(selectedImagePath);
        	 //bitmap = decodeSampledBitmapFromFile(file.getAbsolutePath(), 220, 140);
        	 bitmap = BitmapFactory.decodeFile(selectedImagePath);
             bitmapscaled = BitmapScaler.scaleToFill(bitmap, 220, 140);
             Matrix matrix = new Matrix();
             matrix.postRotate(180);
             Bitmap rotated = Bitmap.createBitmap(bitmapscaled, 0, 0, bitmapscaled.getWidth(), bitmapscaled.getHeight(),matrix, true);
             mImageView.setImageBitmap(rotated);
             //Scan newly captured image
             Intent mediaScanIntent = new Intent( Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
             mediaScanIntent.setData(mImageCaptureUri);
             sendBroadcast(mediaScanIntent);
             bitmap.recycle();
             bitmapscaled.recycle();
             uploadFilePath = selectedImagePath;
                break;
         case PICK_FROM_FILE:
             /**
              * After selecting image from files, save the selected path
              */
             mImageCaptureUri = data.getData();

             //doCrop();
             //Not using crop function
             selectedImagePath = getPath(mImageCaptureUri);
             bitmap = BitmapFactory.decodeFile(selectedImagePath);
             bitmapscaled = BitmapScaler.scaleToFitHeight(bitmap, 140);
             mImageView.setImageBitmap(bitmapscaled);
             uploadFilePath = selectedImagePath;

             break;
             
         case CROP_FROM_CAMERA:
             Bundle extras = data.getExtras();
             /**
              * After cropping the image, get the bitmap of the cropped image and
              * display it on imageview.
              */
             if (extras != null) {
                   Bitmap photo = extras.getParcelable("data");

                   mImageView.setImageBitmap(photo);
             }

             File f = new File(mImageCaptureUri.getPath());
             /**
              * Delete the temporary image
              */
             if (f.exists())
                   f.delete();

             break;


		 }
	 }


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
    	return false;
	}
    
    private String getMyPhoneNumber(){
      TelephonyManager mTelephonyMgr;
      mTelephonyMgr = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE); 
      return mTelephonyMgr.getLine1Number();
    }
    
    private List<Address> getAddress(LatLng point){
		List<Address> address =null;
		Geocoder geocoder= new Geocoder(this, Locale.getDefault());
		try{
			address = geocoder.getFromLocation(point.latitude, point.longitude, 1);
		}
		catch (IOException e) {
			Toast.makeText(this, "You need to connect to the Internet to use this application", Toast.LENGTH_SHORT).show();
    	
    	}
		return address;
    }
    
    private int uploadFile(String sourceFileUri, Map<String,String> map) {
    	String fileName = sourceFileUri;
    	  
        HttpURLConnection conn = null;
        DataOutputStream dos = null;  
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024; 
        File sourceFile = new File(sourceFileUri);
        
        //if (!sourceFile.isFile()) {
            
            /*dialog.dismiss(); 
             
            Log.e("uploadFile", "Source File not exist :"
                                +uploadFilePath);
             
            runOnUiThread(new Runnable() {
                public void run() {
                    messageText.setText("Source File not exist :"
                            +uploadFilePath);
                }
            });*/
             
            //return 0;
         
       //} 
        
       //else
       //{
            try { 
                  
                   // open a URL connection to the Servlet
                 URL url = new URL(upLoadServerUri);
                  
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
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd); 
                 dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""+ fileName + "\"" + lineEnd);
                  
                 dos.writeBytes(lineEnd);
                 
                 if (sourceFile.isFile()) 
                 {       
                   FileInputStream fileInputStream = new FileInputStream(sourceFile);
                   // create a buffer of  maximum size
                   bytesAvailable = fileInputStream.available(); 
        
                   bufferSize = Math.min(bytesAvailable, maxBufferSize);
                   buffer = new byte[bufferSize];
        
                   // read file and write it into form...
                   bytesRead = fileInputStream.read(buffer, 0, bufferSize);  
                    
                   while (bytesRead > 0) {
                      
                     dos.write(buffer, 0, bufferSize);
                     bytesAvailable = fileInputStream.available();
                     bufferSize = Math.min(bytesAvailable, maxBufferSize);
                     bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
                    
                    }
                   //close the streams //
                   fileInputStream.close();
                   dos.writeBytes(lineEnd);
                   
                 }
        
                 // send multipart form data necesssary after file data...

                 //Thanks to this: http://stackoverflow.com/questions/5785803/androidhow-to-upload-mp3-file-and-image-to-http-server
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"reportername\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("reportername"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"email\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("email"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"details\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("details"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"latitude\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("latitude"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"longitude\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("longitude"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"address\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("address"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"town\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("town"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"district\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("district"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"state\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("state"));
                 dos.writeBytes(lineEnd);
                 
                 dos.writeBytes(twoHyphens + boundary + lineEnd);
                 dos.writeBytes("Content-Disposition: form-data; name=\"reporterphone\"" + lineEnd);
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(map.get("reporterphone"));
                 dos.writeBytes(lineEnd);
                 dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
        
                 // Responses from the server (code and message)
                 serverResponseCode = conn.getResponseCode();
                 String serverResponseMessage = conn.getResponseMessage();
                   
                 Log.i("uploadFile", "HTTP Response is : "
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
                      
                     runOnUiThread(new Runnable() {
                          public void run() {
                               
                              //String msg = "File Upload Completed.";
                              //messageText.setText(msg);
                        	  String msg;
                        	  msg = sb.toString();
                        	  msg = msg.trim();
                        	  String msg1 = "success|success|";
                        	  String msg2 = "success|";
                        	  
                        	  serverResponseCode = 400;
                        	  if(msg.equals(msg1) || msg.equals(msg2)){
                        		  msg = "Report successfully submitted.";
                        		  serverResponseCode = 200;
                        	  } 
                        	  //int msglen;
                        	  //msglen=msg.length();
                        	  //String msgstr = Integer.toString(msglen);
                              Toast.makeText(FourthActivity.this, msg, 
                                           Toast.LENGTH_LONG).show();
                              Log.i("uploadFile", "HTTP Response is : "
                                      + msg );
                              
                          }
                      });
                     
                      
                 
                 }
                 
                 
            }  catch (MalformedURLException ex) {
                
               dialog.dismiss();  
               ex.printStackTrace();
                
               runOnUiThread(new Runnable() {
                   public void run() {
                       //messageText.setText("MalformedURLException Exception : check script url.");
                       Toast.makeText(FourthActivity.this, "MalformedURLException", 
                                                           Toast.LENGTH_SHORT).show();
                       serverResponseCode = 400;
                   }
               });
                
               Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
           } catch (Exception e) {
                
               dialog.dismiss();  
               e.printStackTrace();
                
               runOnUiThread(new Runnable() {
                   public void run() {
                       //messageText.setText("Got Exception : see logcat ");
                       Toast.makeText(FourthActivity.this, "Got Exception : see logcat ", 
                               Toast.LENGTH_SHORT).show();
                       serverResponseCode = 400;
                   }
               });
               Log.e("Upload file to server Exception", "Exception : "
                                                + e.getMessage(), e);  
           }
           dialog.dismiss();       
           return serverResponseCode;
            
            
       //}
        
    }
    
    //Choose image functions
    private void captureImageInitialization() {
    	
    	 /**
         * a selector dialog to display two image source options, from camera
         * ‘Take from camera’ and from existing files ‘Select from gallery’
         */
        final String[] items = new String[] { "Take from camera",
                     "Select from gallery" };
        
        
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.select_dialog_item, items);
        
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int item) {
				// TODO Auto-generated method stub
				if (item == 0) {
					 /**
                     * To take a photo from camera, pass intent action
                     * ‘MediaStore.ACTION_IMAGE_CAPTURE‘ to open the camera app.
                     */
					Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					/**
                     * Also specify the Uri to save the image on specified path
                     * and file name. Note that this Uri variable also used by
                     * gallery app to hold the selected image path.
                     */
					//mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_"+ String.valueOf(System.currentTimeMillis())+ ".jpg"));
					//Use Gallery path
                     mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory()+File.separator+"DCIM"+File.separator+"asnaf_" + String.valueOf(System.currentTimeMillis())+ ".jpg"));
                     //Putting this will cause the camera to write the file and not return data to intent
					//http://blog-emildesign.rhcloud.com/?p=590
					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                            mImageCaptureUri);
					
					
					try {
                        intent.putExtra("return-data", true);

                        startActivityForResult(intent, PICK_FROM_CAMERA);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
				} 
				else {
					
					// pick from file
                    /**
                     * To select an image from existing files, use
                     * Intent.createChooser to open image chooser. Android will
                     * automatically display a list of supported applications,
                     * such as image gallery or file manager.
                     */
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    
                    startActivityForResult(Intent.createChooser(intent,
                            "Complete action using"), PICK_FROM_FILE);
				
				}
			}
        	
        }
    	);
        
        imagedialog= builder.create();
    }
    
    public String getPath(Uri uri) {
    	String picturePath = "";
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor =  getContentResolver().query(uri,projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        picturePath = cursor.getString(column_index);
        cursor.close();
        return picturePath;
    }
    
    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) 
    { // BEST QUALITY MATCH
         
        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
     
        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;
     
        if (height > reqHeight) 
        {
            inSampleSize = Math.round((float)height / (float)reqHeight);
        }
        int expectedWidth = width / inSampleSize;
     
        if (expectedWidth > reqWidth) 
        {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float)width / (float)reqWidth);
        }
     
        options.inSampleSize = inSampleSize;
     
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
     
        return BitmapFactory.decodeFile(path, options);
    }
    
    /*
    private void doCrop() {
    	final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
    	///**
        // * Open image crop app by starting an intent
        // * ‘com.android.camera.action.CROP‘.
        
        Intent intent = new Intent("com.android.camera.action.CROP");
        //intent.setType("image/*"); uncomment to use docrop()
        ///**
        // * Check if there is image cropper app installed.
        
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(
                     intent, 0);
        int size = list.size();
        if (size == 0) {

            Toast.makeText(this, "Can not find image crop app",
                         Toast.LENGTH_SHORT).show();

            return;
         } else {
             ///**
             // * Specify the image path, crop dimension and scale
             
             intent.setData(mImageCaptureUri);
             
             intent.putExtra("outputX", 200);
             intent.putExtra("outputY", 200);
             intent.putExtra("aspectX", 1);
             intent.putExtra("aspectY", 1);
             intent.putExtra("scale", true);
             intent.putExtra("return-data", true);
             ///**
             // * There is posibility when more than one image cropper app exist,
             // * so we have to check for it first. If there is only one app, open
             // * then app.
             
             if (size == 1) {
                 Intent i = new Intent(intent);
                 ResolveInfo res = list.get(0);

                 i.setComponent(new ComponentName(res.activityInfo.packageName,
                               res.activityInfo.name));

                 startActivityForResult(i, CROP_FROM_CAMERA);
             } 
             else {
                 ///**
                  //* If there are several app exist, create a custom chooser to
                  //* let user selects the app.
                  
            	 for (ResolveInfo res : list) {
                     final CropOption co = new CropOption();

                     co.title = getPackageManager().getApplicationLabel(
                                   res.activityInfo.applicationInfo);
                     co.icon = getPackageManager().getApplicationIcon(
                                   res.activityInfo.applicationInfo);
                     co.appIntent = new Intent(intent);

                     co.appIntent
                                   .setComponent(new ComponentName(
                                                 res.activityInfo.packageName,
                                                 res.activityInfo.name));

                     cropOptions.add(co);
                }
            	CropOptionAdapter adapter = new CropOptionAdapter(
                         getApplicationContext(), cropOptions);

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose Crop App");
                builder.setAdapter(adapter,
                         new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int item) {
                                       startActivityForResult(
                                                     cropOptions.get(item).appIntent,
                                                     CROP_FROM_CAMERA);
                                }
                         });
                
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                           if (mImageCaptureUri != null) {
                                  getContentResolver().delete(mImageCaptureUri, null,
                                                null);
                                  mImageCaptureUri = null;
                           }
                    }
                 });
                
                AlertDialog alert = builder.create();

                alert.show();
             }
         }
        
    }
    */
    
    
    /*
    int cameraCount = 0;
        Camera camera = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
        	Camera.getCameraInfo( camIdx, cameraInfo );
        	if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK  ) {
                //try {
                //    cam = Camera.open( camIdx );
                    Log.d("TAG", "Rear camera detected");
                //    rearCameraFound = true;
                //} catch (RuntimeException e) {
                //    Log.e("TAG", "Camera failed to open: " + e.getLocalizedMessage());
                //}
                try{
                  camera =Camera.open(camIdx);
                  setCameraDisplayOrientation(this, camIdx, camera);
                  camera.release();
                  
                } 
                catch (RuntimeException e) {
                        Log.e("TAG", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                Log.d("TAG", "Front camera detected");
            }
        }
     
    
    public static void setCameraDisplayOrientation(Activity activity,
            int cameraId, android.hardware.Camera camera) {
    	

        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }*/

	

}

