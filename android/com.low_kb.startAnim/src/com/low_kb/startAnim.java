package com.low_kb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.security.*;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

import listening_function.NetstateReceiver;
import listening_function.Notice_score;
import listening_function.start_server;

public class startAnim extends Activity
{
	
	 @Override
     public void onCreate(Bundle savedInstanceState) {
             super.onCreate(savedInstanceState);
             this.requestWindowFeature(Window.FEATURE_NO_TITLE);
             this.requestWindowFeature(Window.PROGRESS_START);
             setContentView(R.layout.startanim);           


             
               
             final Thread get = new Thread(){
     			public void run()
     			{
     				  
                    //在Activity中的onDestroy中:'                
                    //unregisterReceiver(mNetworkStateReceiver); //取消监听
     			}
     		};
     		//get.start();
	           //延迟3秒后执行run方法中的页面跳转
	           new Handler().postDelayed(new Runnable() {
	                     public void run() {
	                             Intent intent = new Intent(startAnim.this, main_function.class);
	                             startActivity(intent);
	                             get.start();
	                             finish();
	                     }
	             }, 0);
	           
                    
	 }
	 
	 
	 public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{
	    		
	    	}
	    	if(keyCode==KeyEvent.KEYCODE_MENU)
	    	{

	    	}
	    	
			return true;   	
	    }


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}    

}
