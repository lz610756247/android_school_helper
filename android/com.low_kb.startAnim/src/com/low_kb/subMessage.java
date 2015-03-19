package com.low_kb;

import operation.message;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class subMessage extends Activity{

	TextView text = null;
	Button button = null;
	Bundle bundle=null;
	message message = null;
	String resphone="",dl="",adress="",myphone="";
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submessage);
		text = (TextView) findViewById(R.id.detail);
		button = (Button) findViewById(R.id.submessage_msg);
		
		Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        dl = bundle.getString("dl");
        resphone = bundle.getString("resphone");
        message = new message("resphone" , subMessage.this , "8");
        adress=message.getInfo().adress;
        myphone=message.getInfo().myphone;
        
        Message msg = new Message();
        msg.what=0;
        handler.sendMessage(msg);
        /*message.sendMessage("老板，刚才我订的我给你个清单：\n"+dl
				+"\n我的地址是:"+message.getAdress());*/
	}
	private Handler handler = new Handler(){  
        @Override  
        public void handleMessage(Message msg) { 
        	super.handleMessage(msg);
        	text.setText("请确认你的信息\n你订了：\n   "+dl+"\n你的地址是:"+adress
        			+"\n你的电话是:"+myphone);
        	button.setOnClickListener(new OnClickListener()
    		{
    			public void onClick(View v) {
    				// TODO Auto-generated method stub
    				message.sendMessage("你好，"+myphone+"刚才的订单：\n"+dl
    						+"\n我的地址是:"+adress
    						+"\n我的电话是:"+myphone);
    			}			
    		});
        }
	};
        
}
