package com.low_kb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import com.SQLite.GetYAT;





import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class chat_table extends Activity {
 
	boolean isRev = true;
    String message = "";
    String message1 = "";
    String p = "";
    TextView chatbox;
    ScrollView sl;
    EditText chattxt;
    Button chatOk;
    
    String xkbh="";
    String name="";
    
	Message mess;	//消息类
	Object ob = new Object();	//同步锁
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        
        getBundle();	//传入参数获取
        
        init();	//初始化
        
        loadMsg();	//加载信息
        
        startRev();		//开始接受
        
    }
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(1,1,1,"查看聊天记录");
    	menu.setGroupVisible(1, true);
        return true;
    }
	/*------------------------------------------*/
    //为菜单指定动作
    public void openOptionsMenu() {  
        // TODO Auto-generated method stub  
        super.openOptionsMenu(); 
	 }
	public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
	    	   case 1:
			   {
				   Intent intent = new Intent(chat_table.this,his_chat.class);
					Bundle bundle = new Bundle();
			        bundle.putString("xkbh", xkbh);
					intent.putExtra("bundle", bundle);
					startActivity(intent);
				   break;
			   }	    	   
	    	}
			return true;
	    }
	
	public void getBundle()		//获取去传入参数
	{
		final Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		xkbh = bundle.getString("xkbh");
		name = bundle.getString("name");
	}
	
	public void init()
	{
		chatbox = (TextView) findViewById(R.id.chatbox);
		sl = (ScrollView) findViewById(R.id.chat_sl);
        chattxt = (EditText) findViewById(R.id.chattxt);
        chatOk = (Button) findViewById(R.id.chatOk);
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
		Cursor c = db.rawQuery("SELECT * FROM chattable where xkbh='"+xkbh+"' order by time desc limit 30", null);
		String temp="";
		 while (c.moveToNext()) 
		 {  	 
			 temp = c.getString(c.getColumnIndex("name"))
			 +"\n"+c.getString(c.getColumnIndex("msg"))+"\n\n"+temp;
			 
	     }  
		 c.close();
		 chatbox.append(temp);
	}
	
	public void startRev()		//开始接受
	{
		synchronized(ob)
        {
        	SocketClient sc;
            try 
            {
    			sc = new SocketClient();
    			sc.start();
    		} catch (Exception e) {
    			// TODO Auto-generated catch block
    			chattxt.setText(e.toString());
    			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
    			e.printStackTrace();
    		}
        }
	
        
        synchronized(ob)
        {
        	chatOk.setOnClickListener(new View.OnClickListener()
            {
    			public void onClick(View v) 
    			{
    				if( ! chattxt.getText().toString().equals(""))
    				{
    					sendMsg(name + "@"+ "ALL" + "@" + chattxt.getText().toString()+"@"+xkbh+"@");
    					chattxt.setText("");
    				}
    			}
            });
        }
	}
    
    public class SocketClient extends Thread	//客户端通信进程，负责接收消息的线程  
    {
  
    	public SocketClient()
    	{ 		
    	    
    	}
		@Override
		public void run() 
		{         
            while(isRev) 
            {  
            	try 
                {  
                      
                    if( communication.isApp ) 
                    {
                    	message = communication.message+"";
                    	
                    	StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
                    	p = stringTokenizer.nextToken();
                        message1 = stringTokenizer.nextToken(); 
                        String flag = stringTokenizer.nextToken(); 
                    	if(flag.equals(xkbh))
                    	{
                    		Message msgMessage = new Message();
    	                    msgMessage.what=0x1981;    
    	            		mHandler.sendMessage(msgMessage);
    	            		Thread.sleep(100);
                    	}	                    
                    }
            		
                } catch (Exception e) {  
                    e.printStackTrace();  
                }                
            }  
		} 	
    }
    
    
    //发送消息的方法
    public void sendMsg(String msg)
    {
    	communication.writer.println(msg);  
    	communication.writer.flush();
    }
    
  //停止链接
  	public synchronized void stopSer()
  	{              
  		this.isRev = false;                        		 
  	}
    
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopSer();
	}
    
    
  //消息控制
  	private Handler mHandler = new Handler()
  	{
  		   public void handleMessage(Message msg)  
  	        {  
  	            switch(msg.what)
  	            {
  		            case 0x1980:
  	            	{
//  	            		for(int i=0;i<communication.message_quee.size();i++)
//	            			{
//	            				if(communication.message_quee.get(i).toString().contains(xkbh))
//	            				{
//	            					StringTokenizer stringTokenizer = 
//	            							new StringTokenizer(communication.message_quee.get(i).toString(), "@");  
//	        		            	String p = stringTokenizer.nextToken();
//	        		            	String message1 = stringTokenizer.nextToken(); 
//	        		            	String flag = stringTokenizer.nextToken(); 
//	        		            	chatbox.append(p+"\n"+message1+"\n\n");
////	        		            	communication.message_quee.remove(i);
////	        		            	i--;	清除记录
//	            				}
//	            			}
  	            		query_Msg();
  	            		this.post(new Runnable(){
							public void run() {
								// TODO Auto-generated method stub
								sl.fullScroll(ScrollView.FOCUS_DOWN);
							}});
  	            		break;
  	            	}
  		            case 0x1981:
  		            {
  		            	chatbox.append(p+"\n"+message1+"\n\n");
  		            	this.post(new Runnable(){
							public void run() {
								// TODO Auto-generated method stub
								sl.fullScroll(ScrollView.FOCUS_DOWN);
							}});
  		            	communication.message="";
  		            	communication.isApp = false;
  		            	break;
  		            }
  	            }
  	        }
  	};
  	
  	
}
