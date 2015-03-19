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
	
	ListView listview=null;		//�༶�б�
	SimpleAdapter adapter=null;		//�б���Ϣ������
	List<Map<String ,Object>> ListItem=null;		//���������
	List<Map<String ,Object>> list=null;
	private ProgressDialog progressDialog = null;	//������
	Message mess;	//��Ϣ��
	Net_state_listening nsl = null;		//���������
	GetYAT getyat=null;		//ѧ��ѧ��ת����
	String username="",password="",isnum="",yat="",flag="",yat_part="";		//�������
	WebService ws = null;
	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.communication);
		
		getBundle();	//���������ȡ
		
		init();		//������ʼ��
		
		if(!Net_Check()){
			//������
			return;
		}
		
		ItemActionSet();		//�ؼ���������
		
		startSer();	//��������
	}
		
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(1,1,1,"���ȫ����¼");
    	menu.setGroupVisible(1, true);
        return true;
    }	
	
	 /*------------------------------------------*/
    //Ϊ�˵�ָ������
    public void openOptionsMenu() {  
        // TODO Auto-generated method stub  
        super.openOptionsMenu(); 
	 }
	public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
	    	   case 1:
			   {
				   AlertDialog.Builder builder = new AlertDialog.Builder(communication.this);
      	    		builder.setMessage("ɾ��ԭ���ļ�¼����������ٶ�").setCancelable(false)
      	    		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}     	    			
      	    		})
      	    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {     					
      					public void onClick(DialogInterface dialog, int which) {
      						// TODO Auto-generated method stub
      						//ɾ���ɱ�
      						db.execSQL("DROP TABLE IF EXISTS chattable");
      						//�����±�
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
		
	public void getBundle()		//��ȡȥ�������
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
	
	//��ȡ�б���
	private List<Map<String ,Object>> getListItem(String keyname_getName)
	{
		list = new ArrayList<Map<String ,Object>>();		
		String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh , qzz;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);		
		Map<String,Object> map;
		TabName tan = new  TabName();
		//getName()�����ṩ����
		//ʹ��TabMane��ķ�����ϳ������ı���
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
	
	
	public void init()		//��ʼ���ؼ��������ķ���
	{
		db = openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);	
		progressDialog = ProgressDialog
				.show(communication.this, "ע��", "���ڼ���", true, false);		
		mess = mHandler.obtainMessage();	//��ʼ����Ϣ������
		nsl = new Net_state_listening(this);
		listview = (ListView) findViewById(R.id.com_listview);
		listview.setCacheColorHint(Color.TRANSPARENT);	
		ListItem = this.getListItem(username+yat_part);	//�б����������ȡ
		
		ws = new WebService();
		
		//name = ws.getName();
		this.sendMsg(0);
				
	}
		
	public boolean Net_Check()
	{
		//�ж��Ƿ�����
		return nsl.getInternet();
	}
		
	//���ݿ����
	public void DB_Insert(String xkbh , String name , String msg)
	{
		if(xkbh!=null&&name!=null&&msg!=null)
		{
			if(!xkbh.equals("")&&!name.equals("")&&!msg.equals(""))
			{				
				if(!xkbh.contains("������"))
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
		
	//�ؼ�����
	public void ItemActionSet()
	{
		//Ϊ�б���ӵ������
        listview.setOnItemClickListener(new OnItemClickListener()
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				//ȡ���б����е���Ϣ��������һ�����棬��������
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
	
	//��������
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
	
	public class SocketClient extends Thread	//�ͻ���ͨ�Ž��̣����������Ϣ���߳�  
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
            System.out.println("���ܿ�ʼ"); 
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
	//������Ϣ�ķ���
    public void sendMsg(String msg)
    {
    	writer.println(msg);  
        writer.flush();
    }
    
	
	//ֹͣ����
	public synchronized void stopSer()
	{
		try 
		{  
            sendMsg("CLOSE");// ���ͶϿ����������������               
            this.isRev = false;
            // �ͷ���Դ  
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

	//��Ϣ����
	public void sendMsg(int i)
	{
		mess.what=i;    
		mHandler.sendMessage(mess);
	}
	//��Ϣ����
	private Handler mHandler = new Handler()
	{
		   public void handleMessage(Message msg)  
	        {  
	            super.handleMessage(msg); 
	            switch(msg.what)
	            {
		            case 0:
	            	{
	            		//����������
	            		adapter = new SimpleAdapter(communication.this,ListItem,R.layout.chat_item
		            			, new String[]{"xkbh" , "kcmc" , "qzz"}
		            			,new int[]{R.id.xkbh , R.id.kcmc , R.id.qzz});
	            		//���������
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
