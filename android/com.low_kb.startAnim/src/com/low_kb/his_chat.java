package com.low_kb;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ScrollView;
import android.widget.TextView;

public class his_chat extends Activity
{
	String xkbh="";
	TextView chatbox;
	
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.his_chat);
        
        getBundle();	//传入参数获取
        
        init();	//初始化
        
        loadMsg();	//加载信息
	}
	
	
	public void getBundle()		//获取去传入参数
	{
		final Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		xkbh = bundle.getString("xkbh");
		
	}
	
	
	public void init()
	{
		chatbox = (TextView) findViewById(R.id.his_chat_txt);
	}
	
	
	public void loadMsg()	//加载信息
	{
		Message msgMessage = new Message();
        msgMessage.what=0x1980;    
		mHandler.sendMessage(msgMessage);
	}
	
	private void query_Msg()
	{
		SQLiteDatabase db = openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM chattable where xkbh='"+xkbh+"' order by time desc", null);
		String temp="";
		 while (c.moveToNext()) 
		 {  	 
			 temp = c.getString(c.getColumnIndex("name"))
			 +"\n"+c.getString(c.getColumnIndex("msg"))+"\n\n"+temp;
			 
	     }  
		 c.close();
		 db.close();
		 chatbox.append(temp);
	}
	
	
	private Handler mHandler = new Handler()
  	{
  		   public void handleMessage(Message msg)  
  	        {  
  	            switch(msg.what)
  	            {
  		            case 0x1980:
  	            	{  	            	
  	            		query_Msg();  	            		
  	            		break;
  	            	}  		            
  	            }
  	        }
  	};
}
