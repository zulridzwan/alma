package com.zulridzwan.alma;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.Menu;
import android.view.View;

public class SecondActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
	}
	
	public void logout(View view){
	  SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
	  Editor editor = sharedpreferences.edit();
	  editor.clear();
	  editor.commit();
	  //moveTaskToBack(true); 
	  Intent i = new Intent(this,com.zulridzwan.alma.MainActivity.class);
	  i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
	  startActivity(i);
	  SecondActivity.this.finish();
	}
	   
	public void exit(View view){
	  //moveTaskToBack(true); 
	  SecondActivity.this.finish();
	}
	
	public void newreport(View view){
	  Intent i = new Intent(this,com.zulridzwan.alma.ThirdActivity.class);
	  startActivity(i);
	}
	
	public void reportmap(View view){
		  Intent i = new Intent(this,com.zulridzwan.alma.FifthActivity.class);
		  startActivity(i);
	}
	
	public void officemap(View view){
		  Intent i = new Intent(this,com.zulridzwan.alma.SeventhActivity.class);
		  startActivity(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
