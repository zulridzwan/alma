package com.zulridzwan.alma;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SeventhActivity extends Activity {
	
	private LinearLayout l1;
	private LinearLayout l2;
	private LinearLayout l3;
	private LinearLayout l4;
	private LinearLayout l5;
	private LinearLayout l6;
	private LinearLayout l7;
	private LinearLayout l8;
	private LinearLayout l9;
	private LinearLayout l10;
	private int[] z;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seventh);
		
		TextView t1=(TextView)findViewById(R.id.layouttext1);
		TextView t2=(TextView)findViewById(R.id.layouttext2);
		TextView t3=(TextView)findViewById(R.id.layouttext3);
		TextView t4=(TextView)findViewById(R.id.layouttext4);
		TextView t5=(TextView)findViewById(R.id.layouttext5);
		TextView t6=(TextView)findViewById(R.id.layouttext6);
		TextView t7=(TextView)findViewById(R.id.layouttext7);
		TextView t8=(TextView)findViewById(R.id.layouttext8);
		TextView t9=(TextView)findViewById(R.id.layouttext9);
		TextView t10=(TextView)findViewById(R.id.layouttext10);
      
      
      
		l1=(LinearLayout)findViewById(R.id.layout1);
		l2=(LinearLayout)findViewById(R.id.layout2);
		l3=(LinearLayout)findViewById(R.id.layout3);
		l4=(LinearLayout)findViewById(R.id.layout4);
		l5=(LinearLayout)findViewById(R.id.layout5);
		l6=(LinearLayout)findViewById(R.id.layout6);
		l7=(LinearLayout)findViewById(R.id.layout7);
		l8=(LinearLayout)findViewById(R.id.layout8);
		l9=(LinearLayout)findViewById(R.id.layout9);
		l10=(LinearLayout)findViewById(R.id.layout10);
      
      
      
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);
        l4.setVisibility(View.GONE);
        l5.setVisibility(View.GONE);
        l6.setVisibility(View.GONE);
        l7.setVisibility(View.GONE);
        l8.setVisibility(View.GONE);
        l9.setVisibility(View.GONE);
        l10.setVisibility(View.GONE);
        
        z = new int[10];
        for(int i=0;i<10;i++){
        	z[i]=0;
        }
        
        
        t1.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[0]==1){
                    l1.setVisibility(View.GONE);
                    z[0]=0;
                }
                else {
                    l1.setVisibility(View.VISIBLE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                  
                  
                    z[0]=1;
                }
              
            }
        });
        
        t2.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[1]==1){
                    l2.setVisibility(View.GONE);
                    z[1]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.VISIBLE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[1]=1;
                }
              
            }
        });
        
        t3.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[2]==1){
                    l3.setVisibility(View.GONE);
                    z[2]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.VISIBLE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[2]=1;
                }
              
            }
        });
        
        t4.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[3]==1){
                    l4.setVisibility(View.GONE);
                    z[3]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.VISIBLE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[3]=1;
                }
              
            }
        });
        
        t5.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[4]==1){
                    l5.setVisibility(View.GONE);
                    z[4]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.VISIBLE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[4]=1;
                }
              
            }
        });
        
        t6.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[5]==1){
                    l6.setVisibility(View.GONE);
                    z[5]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.VISIBLE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[5]=1;
                }
              
            }
        });
        
        t7.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[6]==1){
                    l7.setVisibility(View.GONE);
                    z[6]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.VISIBLE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[6]=1;
                }
              
            }
        });
        
        t8.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[7]==1){
                    l8.setVisibility(View.GONE);
                    z[7]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.VISIBLE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[7]=1;
                }
              
            }
        });
        
        t9.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[8]==1){
                    l9.setVisibility(View.GONE);
                    z[8]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.VISIBLE);
                    l10.setVisibility(View.GONE);
                  
                  
                    z[8]=1;
                }
              
            }
        });
        
        t10.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if(z[9]==1){
                    l10.setVisibility(View.GONE);
                    z[9]=0;
                }
                else {
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    l3.setVisibility(View.GONE);
                    l4.setVisibility(View.GONE);
                    l5.setVisibility(View.GONE);
                    l6.setVisibility(View.GONE);
                    l7.setVisibility(View.GONE);
                    l8.setVisibility(View.GONE);
                    l9.setVisibility(View.GONE);
                    l10.setVisibility(View.VISIBLE);
                  
                  
                    z[9]=1;
                }
              
            }
        });
        
        
		
	}
	
  
	public void showmap(View view){
		
		Intent intent=new Intent(SeventhActivity.this,EighthActivity.class);
		Bundle b = new Bundle();
	    
	    
	    switch (view.getId()) {
        case R.id.mapbtn1:
        	b.putString("point",getResources().getString(R.string.hq_position));
            break;
        case R.id.mapbtn2:
        	b.putString("point",getResources().getString(R.string.branch1_position));
            break;
        case R.id.mapbtn3:
        	b.putString("point",getResources().getString(R.string.branch2_position));
            break;
        case R.id.mapbtn4:
        	b.putString("point",getResources().getString(R.string.branch3_position));
            break;
        case R.id.mapbtn5:
        	b.putString("point",getResources().getString(R.string.branch4_position));
            break;
        case R.id.mapbtn6:
        	b.putString("point",getResources().getString(R.string.branch5_position));
            break;
        case R.id.mapbtn7:
        	b.putString("point",getResources().getString(R.string.branch6_position));
            break;
        case R.id.mapbtn8:
        	b.putString("point",getResources().getString(R.string.branch7_position));
            break;
        case R.id.mapbtn9:
        	b.putString("point",getResources().getString(R.string.branch8_position));
            break;
        case R.id.mapbtn10:
        	b.putString("point",getResources().getString(R.string.branch9_position));
            break;
        case R.id.mapbtn11:
        	b.putString("point",getResources().getString(R.string.branch10_position));
            break;
        case R.id.mapbtn12:
        	b.putString("point",getResources().getString(R.string.branch11_position));
            break;
        case R.id.mapbtn13:
        	b.putString("point",getResources().getString(R.string.branch12_position));
            break;
        case R.id.mapbtn14:
        	b.putString("point",getResources().getString(R.string.branch13_position));
            break;
        case R.id.mapbtn15:
        	b.putString("point",getResources().getString(R.string.branch14_position));
            break;
        case R.id.mapbtn16:
        	b.putString("point",getResources().getString(R.string.branch15_position));
            break;
        case R.id.mapbtn17:
        	b.putString("point",getResources().getString(R.string.branch16_position));
            break;
        case R.id.mapbtn18:
        	b.putString("point",getResources().getString(R.string.branch17_position));
            break;
        case R.id.mapbtn19:
        	b.putString("point",getResources().getString(R.string.branch18_position));
            break;
        case R.id.mapbtn20:
        	b.putString("point",getResources().getString(R.string.branch19_position));
            break;
        case R.id.mapbtn21:
        	b.putString("point",getResources().getString(R.string.branch20_position));
            break;
        case R.id.mapbtn22:
        	b.putString("point",getResources().getString(R.string.branch21_position));
            break;
        case R.id.mapbtn23:
        	b.putString("point",getResources().getString(R.string.branch22_position));
            break;
        case R.id.mapbtn24:
        	b.putString("point",getResources().getString(R.string.branch23_position));
            break;
        }
        intent.putExtra("mainbundle", b);
        startActivity(intent);
	}
	


}
