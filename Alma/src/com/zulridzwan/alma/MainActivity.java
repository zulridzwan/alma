package com.zulridzwan.alma;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private EditText username,password;
	public static final String MyPREFERENCES = "MyPrefs" ;
	public static final String name = "nameKey"; 
	public static final String pass = "passwordKey"; 
	SharedPreferences sharedpreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		username = (EditText)findViewById(R.id.editText1);
	    password = (EditText)findViewById(R.id.editText2);
	    

		
		// First intent to use ACTION_VIEW action with correct data
	    /*Button startBrowser_a = (Button) findViewById(R.id.start_browser_a);
	    startBrowser_a.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View view) {
	        Intent i = new Intent(android.content.Intent.ACTION_VIEW, 
	        Uri.parse("http://www.example.com"));
	        startActivity(i);
	      }
	    });

	    // Second intent to use LAUNCH action with correct data
	    Button startBrowser_b = (Button) findViewById(R.id.start_browser_b);
	    startBrowser_b.setOnClickListener(new View.OnClickListener() {
	       public void onClick(View view) {
	         Intent i = new Intent("com.zulridzwan.alma.LAUNCH", 
	         Uri.parse("http://www.example.com"));
	         startActivity(i);
	       }
	    });

	    // Third intent to use LAUNCH action with incorrect data
	    Button startBrowser_c = (Button) findViewById(R.id.start_browser_c);
	    startBrowser_c.setOnClickListener(new View.OnClickListener() {
	      public void onClick(View view) {
	        Intent i = new Intent("com.zulridzwan.alma.LAUNCH", 
	        Uri.parse("https://www.example.com"));
	        startActivity(i);
	      }
	    });*/
		
	}
	
	/*public void login(View view){
	  if(username.getText().toString().equals("admin") && password.getText().toString().equals("admin")){
	    Toast.makeText(getApplicationContext(), "Redirecting...", 
	    Toast.LENGTH_SHORT).show();
	  }	
	  else {
	    Toast.makeText(getApplicationContext(), "Wrong Credentials",
	    Toast.LENGTH_SHORT).show();
	    attempts.setBackgroundColor(Color.RED);	
	    counter--;
	    attempts.setText(Integer.toString(counter));
	    if(counter==0){
	       login.setEnabled(false);
	    }

	  }
	}*/
	
	public void login(View view){
	  Editor editor = sharedpreferences.edit();
	  String u = username.getText().toString();
	  String p = password.getText().toString();
	  if(u.equals("admin") && p.equals("admin")){
	    //write into sharedpreference
		editor.putString(name, u);
	    editor.putString(pass, p);
	    editor.commit();
	    Intent i = new Intent(this,com.zulridzwan.alma.SecondActivity.class);
	    //Make the called activity root and removes history for back button
	    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	    startActivity(i);
	  } else {
		Toast.makeText(getApplicationContext(), "Wrong Credentials",
	    Toast.LENGTH_SHORT).show();  
	  }
	}
	
	@Override
	   protected void onResume() {
		sharedpreferences=getSharedPreferences(MyPREFERENCES, 
	    Context.MODE_PRIVATE);
	    if (sharedpreferences.contains(name)){
		  if(sharedpreferences.contains(pass)){
		    Intent i = new Intent(this,com.zulridzwan.alma.SecondActivity.class);
		    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			  startActivity(i);
			}
		  }
		 super.onResume();
       }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
