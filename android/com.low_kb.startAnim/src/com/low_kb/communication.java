package com.low_kb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import operation.call;

import service.WebService;

import com.SQLite.GetYAT;
import com.SQLite.TabName;

import listening_function.Net_state_listening;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class communication extends Activity
{
	Socket socket;
	boolean isRev = true;
	static boolean isApp = false;
	SocketClient sc;
	static PrintWriter writer;  
    static BufferedReader reader; 
    static String message = "";
    String class_all="";
    String class_item="";
    String name="";
    static List message_quee = new ArrayList();
    SQLiteDatabase db;
	
	ListView listview=null;		//班级列表
	SimpleAdapter adapter=null;		//列表信息适配器
	List<Map<String ,Object>> ListItem=null;		//适配器填充
	List<Map<String ,Object>> list=null;
	private ProgressDialog progressDialog = null;	//进度条
	Message mess;	//消息类
	Net_state_listening nsl = null;		//联网检测类
	GetYAT getyat=null;		//学期学年转换类
	String username="",password="",isnum="",yat="",flag="",yat_part="";		//传入变量
	WebService ws = null;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communication);
		
		getBundle();	//传入参数获取
		
		init();		//变量初始化
		
		if(!Net_Check()){
			//网络检测
			return;
		}
		
		ItemActionSet();		//控件动作设置
		
		startSer();	//启动服务
	}
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(1,1,1,"清除全部记录");
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
				   AlertDialog.Builder builder = new AlertDialog.Builder(communication.this);
      	    		builder.setMessage("删除原来的记录，可以提高速度").setCancelable(false)
      	    		.setNegativeButton("取消", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}     	    			
      	    		})
      	    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {     					
      					public void onClick(DialogInterface dialog, int which) {
      						// TODO Auto-generated method stub
      						//删除旧表
      						db.execSQL("DROP TABLE IF EXISTS chattable");
      						//创建新表
      						String CREATE_TABLE5 = "create table "+"chattable"+"("+"_Id"+" INTEGER PRIMARY KEY AUTOINCREMENT, "
      								+"xkbh"+" DEFAULT 'xkbh', "+"name"+" DEFAULT 'name', "+"msg"+" DEFAULT 'msg', "+"time"+" DEFAULT 'time'"+")";
      						db.execSQL(CREATE_TABLE5);
      					}
      				}).show();
					break;
			   }	    	   
	    	}
			return true;
	    }
		
	public void getBundle()		//获取去传入参数
	{
		final Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
		isnum = bundle.getString("isnum");
	    yat = bundle.getString("yat");
	    flag = bundle.getString("flag");
	    username = bundle.getString("username");
	    password = bundle.getString("password");
	    getyat = new GetYAT(yat , flag);
	    yat_part = getyat.getYAT();
	}	
	
	//获取列表项
	private List<Map<String ,Object>> getListItem(String keyname_getName)
	{
		list = new ArrayList<Map<String ,Object>>();		
		String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh , qzz;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);		
		Map<String,Object> map;
		TabName tan = new  TabName();
		//getName()方法提供表名
		//使用TabMane类的方法组合出完整的表名
		Cursor c = db.rawQuery("SELECT * FROM "+tan.getTableName(keyname_getName, "les" , ""), null);
		 while (c.moveToNext()) 
		 {  	 
			 xkbh = c.getString(c.getColumnIndex("xkbh"));  
			 kcmc = c.getString(c.getColumnIndex("kcmc"));
			 kkjs = c.getString(c.getColumnIndex("kkjs"));
			 jsgh = c.getString(c.getColumnIndex("jsgh"));
			 x = c.getString(c.getColumnIndex("x"));
			 kclb = c.getString(c.getColumnIndex("kclb"));
			 k1 = c.getString(c.getColumnIndex("k1"));
			 s1 = c.getString(c.getColumnIndex("s1"));
			 qzz = c.getString(c.getColumnIndex("qzz"));
			 map = new HashMap<String,Object>();
			 map.put("xkbh",xkbh);
	         map.put("kcmc",kcmc);
	         map.put("qzz", "");
	         list.add(map);
	         class_all += xkbh+"|";
	     }  
		 c.close();
		 db.close();		
		return list;
	}
	
	
	public void init()		//初始化控件、变量的方法
	{
		db = openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);	
		progressDialog = ProgressDialog
				.show(communication.this, "注意", "正在加载", true, false);		
		mess = mHandler.obtainMessage();	//初始化消息接受类
		nsl = new Net_state_listening(this);
		listview = (ListView) findViewById(R.id.com_listview);
		listview.setCacheColorHint(Color.TRANSPARENT);	
		ListItem = this.getListItem(username+yat_part);	//列表项操作，获取
		
		ws = new WebService();
		
		//name = ws.getName();
		this.sendMsg(0);
				
	}
		
	public boolean Net_Check()
	{
		//判断是否联网
		return nsl.getInternet();
	}
		
	//数据库操作
	public void DB_Insert(String xkbh , String name , String msg)
	{
		if(xkbh!=null&&name!=null&&msg!=null)
		{
			if(!xkbh.equals("")&&!name.equals("")&&!msg.equals(""))
			{				
				if(!xkbh.contains("服务器"))
				{
					Calendar c = Calendar.getInstance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					String time = sdf.format(c.getTime());
					
					ContentValues values = new ContentValues();
					values.put("xkbh", xkbh);
					values.put("name", name);
					values.put("msg", msg);
					values.put("time", time);
					db.insert("chattable" , null , values);
				}					
			}			
		}
		
	}
		
	//控件动作
	public void ItemActionSet()
	{
		//为列表添加点击功能
        listview.setOnItemClickListener(new OnItemClickListener()
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				//取得列表项中的信息，传入下一个界面，方便下载
				Intent intent = new Intent(communication.this,chat_table.class);
				TextView xkbh = (TextView) arg1.findViewById(R.id.xkbh);
				TextView kcmc = (TextView) arg1.findViewById(R.id.kcmc);
				Bundle bundle = new Bundle();
		        bundle.putString("xkbh", xkbh.getText().toString());
		        bundle.putString("name", name);
				intent.putExtra("bundle", bundle);
				startActivity(intent);	
			}        	
        });
               
	}
	
	//启动链接
	public void startSer()
	{		
        try 
        {
			sc = new SocketClient();
			sc.start();
//			if(sc.iswork()){
//				sc.start();
//			}
//			else{
//				return;
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	public class SocketClient extends Thread	//客户端通信进程，负责接收消息的线程  
    {
    	final String HostIP = "192.168.1.102";
//		final String HostIP = "192.168.1.107";
    	final int port = 6999;  
    	boolean work = false;
    	public SocketClient()
    	{
    		try 
			{
				socket = new Socket(HostIP , port);
				setwork(true);
				reader = new BufferedReader(new InputStreamReader(
                		socket  
                        .getInputStream(),"utf-8"));
                writer = new PrintWriter
						(new BufferedWriter(new OutputStreamWriter
								(socket.getOutputStream(),"utf-8")),true);                                 
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				setwork(false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}     	    
    	}
    	
    	public boolean setwork(boolean work){
    		return this.work;
    	}
    	
    	public boolean iswork(){
    		return work;
    	}
    	
		@Override
		public void run() 
		{			
//			name = ws.getName(username , password);
			name="test";
			sendMsg(name + "@" + socket.getLocalAddress().toString()+"@"+class_all+"@");
            System.out.println("接受开始"); 
            while(isRev) 
            {  
            	synchronized(message)
            	{
            		try 
                    {  
                        message = reader.readLine()+"";                                         
                        if( ! message.equals("")||message != null)
                        {
                        	synchronized(message_quee)
                        	{
                        		message_quee.add(message);
                        	}                       	                        	
    	                    Message msgMessage = new Message();
    	                    msgMessage.what=0x1981;    
    	            		mHandler.sendMessage(msgMessage);
    	            		Thread.sleep(100);
                        } 
                    } catch (IOException e) {  
                        e.printStackTrace();  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                }  
            }              
		} 	
    }
	//发送消息的方法
    public void sendMsg(String msg)
    {
    	writer.println(msg);  
        writer.flush();
    }
    
	
	//停止链接
	public synchronized void stopSer()
	{
		try 
		{  
            sendMsg("CLOSE");// 发送断开连接命令给服务器               
            this.isRev = false;
            // 释放资源  
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }    
        } 
		catch (IOException e1) 
		{  
            e1.printStackTrace();    
        }  
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopSer();
	}

	//消息传递
	public void sendMsg(int i)
	{
		mess.what=i;    
		mHandler.sendMessage(mess);
	}
	//消息控制
	private Handler mHandler = new Handler()
	{
		   public void handleMessage(Message msg)  
	        {  
	            super.handleMessage(msg); 
	            switch(msg.what)
	            {
		            case 0:
	            	{
	            		//适配器设置
	            		adapter = new SimpleAdapter(communication.this,ListItem,R.layout.chat_item
		            			, new String[]{"xkbh" , "kcmc" , "qzz"}
		            			,new int[]{R.id.xkbh , R.id.kcmc , R.id.qzz});
	            		//添加适配器
		            	listview.setAdapter(adapter);
	            		progressDialog.dismiss();
	            		break;
	            	}
		            case 0x1981:
  		            {  		         
  		            	StringTokenizer stringTokenizer = new StringTokenizer(message, "@"); 
  		            	String p;
  		            	String message1;
  		            	String flag;
  		            	try
  		            	{
  		            		p = stringTokenizer.nextToken();
  	                        message1 = stringTokenizer.nextToken(); 
  	                        flag = stringTokenizer.nextToken();
  	                        
  	                        DB_Insert(flag , p , message1);
  		            	}
  		            	catch(Exception e)
  		            	{
  		            		p = "";
  	                        message1 = ""; 
  	                        flag = "";
  		            	}
  		            	 		            	
  		            	for(int i=0;i<listview.getCount();i++)
	  		            {
	  		              	View v = listview.getAdapter().getView(i, null, null);
	  		              	TextView xkbh = (TextView) v.findViewById(R.id.xkbh);
	  		      			TextView kcmc = (TextView) v.findViewById(R.id.kcmc);
	  		      			TextView qzz = (TextView) v.findViewById(R.id.qzz);
	  			        
	  		      			if(flag.equals(xkbh.getText().toString()))
	  		      			{
	  		      				Map map = new HashMap<String,Object>();
			  					map.put("xkbh",xkbh.getText().toString());
			  			        map.put("kcmc",kcmc.getText().toString());
			  			        map.put("qzz", message1);
			  			        qzz.setText(message1);
			  			        list.set(i, map);			  			        
	  		      			}		  		      		 
	  		            }  		            	
  		            	adapter.notifyDataSetChanged();
//  		            	message="";
  		            	isApp = true;
  		            	break;
  		            }
	            }
	        }
	};
}
