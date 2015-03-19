package com.low_kb;



import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import service.Download_info;


import com.SQLite.*;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;



public class score_table extends Activity{
	
	DBHelper_SaveUserInfo SaveUserInfo=null;
	Bundle bundle=null;
	String username="",password="",isnum="",yat="",flag="",yat_part="";
	boolean check=true;
	ListView score_listview=null;	
	SimpleAdapter adapter=null;
	GetYAT getyat=null;
	Message mess ;
	String result , admin , update_table_name_les , update_table_name_sco;
	ProgressDialog progressDialog;
	ConnectivityManager cwjManager;
	Download_info download_info;
	Score score;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoretable);		
		//��ȡ���������
		final Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        isnum = bundle.getString("isnum");
        yat = bundle.getString("yat");
        //��������˵���Ƿ��Ѿ����ز��ҽ����������1���Ѿ����汾�����ݵ��Ǽ�ס����,2���Ѿ����汾�����ݵ���"û��"��ס����
        //3����Ҫ�ڿα�ɼ����������ұ�������
        flag = bundle.getString("flag");
    	username = bundle.getString("username");
    	password = bundle.getString("password");
    	check = bundle.getBoolean("check");
    	//���ݻ�ô������Ϣ��ϳɾ���ı�����ƽ������ݵ���ȡ
    	getyat = new GetYAT(yat , flag);
    	yat_part = getyat.getYAT();
//    	//�ж��Ƿ���Ҫ�������ݲ��ұ���������ݿ�
//    	if(flag.equals("3"))
//    	{
//    		//��flag����3ʱ��˵�����ڵ���Ϣ�������صģ���Ҫ��������
//    		//�������������γ���Ϣ������������
//    		analysis();
//    	}		
		score_listview = (ListView) findViewById(R.id.score_listview);
		score_listview.setCacheColorHint(Color.TRANSPARENT);
		List<Map<String ,Object>> ListItem ;
		//�ɼ��Ķ�ȡ,д��
	       
	       ListItem = query_all(username+yat_part);
	       adapter = new SimpleAdapter(this,ListItem,R.layout.score_item
	       		,new String[]{"kcmc" , "kkjs" , "cj"},new int[]{R.id.kcmc , R.id.kkjs , R.id.cj});
	       score_listview.setAdapter(adapter);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(group1,1,1,"��½����");
    	//menu.add(group1,2,2,"��������");
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
	    		 //���ص�¼���棬���ݿ���ֵ����Ӧ�ļ����
	    		   SaveUserInfo = new DBHelper_SaveUserInfo(score_table.this);
    			   SaveUserInfo.del_userinfo_item(username);
	    		   Intent intent = new Intent(score_table.this,login_table.class);
	    		   login_table.check=false;
	    		   File file = new File("/data/data/"+this.getPackageName()+ "/shared_prefs/happyforever.xml");
				   file.delete();
				   Bundle bundle = new Bundle();
			       bundle.putString("button", "score");
			       intent.putExtra("bundle", bundle);
			       SaveUserInfo.close();
	    		   startActivity(intent);
	    		   finish();
	    		   break;
	    	   }	
	    	   case 2:
	    	   {	    		   
	    		   TabName tan = new  TabName();
	    		   update_table_name_les = tan.getTableName(username+yat_part, "les" , "");
	    		   update_table_name_sco = tan.getTableName(username+yat_part, "sco" , "");
	    		   update(update_table_name_les , update_table_name_sco , username, password , yat);
	    		   break;
	    	   }
	    	}
			return true;
	    }
	//�����ݿ���ȡ��Ӧ�����ݽ���ListView����
	public List<Map<String ,Object>> query_all(String keyname_getName)
	{
		String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		Log.i("GAT303", "GAT303");
		List<Map<String ,Object>> ListItem = new ArrayList<Map<String ,Object>>();
		Map<String,Object> map;
		TabName tan = new  TabName();
		//getName()�����ṩ����
		//ʹ��TabMane��ķ�����ϳ������ı���
		Cursor c = db.rawQuery("SELECT * FROM "+tan.getTableName(keyname_getName, "sco" , ""), null);
		 while (c.moveToNext()) 
		 {  	 
			 xkbh = c.getString(c.getColumnIndex("xkbh"));  
			 kcmc = c.getString(c.getColumnIndex("kcmc"));
			 kkjs = c.getString(c.getColumnIndex("kkjs"));
			 x = c.getString(c.getColumnIndex("x"));
			 xm = c.getString(c.getColumnIndex("xm"));
			 cj = c.getString(c.getColumnIndex("cj"));
			 pscj = c.getString(c.getColumnIndex("pscj"));
			 qzcj = c.getString(c.getColumnIndex("qzcj"));
			 qmcj = c.getString(c.getColumnIndex("qmcj"));
			 xh = c.getString(c.getColumnIndex("xh"));
			 map = new HashMap<String,Object>();
	         map.put("kcmc",kcmc);
	         map.put("kkjs", kkjs);
	         map.put("cj", cj);
	         ListItem.add(map);
	     }  
		 c.close();
		 db.close();
		return ListItem;
	}
	
	/***********************����********************************/
	public void analysis()
	{
		Lesson lesson = new Lesson(score_table.this , username , yat_part);
		Score score = new Score(score_table.this , username , yat_part , yat);
		//��ʼ��������α�ɼ���Ϣ
		if(score.start()&&lesson.start())
		{
			//Ϊ�������Ա�����������ƶ����򣺶��߱���ͬʱ�ɹ���������Ϊ�������ɾ����Ӧ���ļ�(��Lesson��Score���е�˵��)
			//�ɹ�ʲô������
		}
		else
		{
			//����һ������ʧ�ܣ����߶�ʧ�ܣ�������ʾ�������û�����������
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setMessage("��������ʧ��,������ϵͳ��Ӧ������������ԭ����ֱ���������ػ���������������").setCancelable(false)
    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			}).show();
			return;
		}
	}
	/********************************����**********************************/
	public void update(String update_table_name_les , String update_table_name_sco 
			, String username , String password , String yat)
	{
		if(!getInternet())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(score_table.this);
    		builder.setMessage("����������").setCancelable(false)
    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show();
        	return;
		}
		progressDialog = ProgressDialog.show(score_table.this, "ע��", "���ڸ���", true, false);
		mess = mHandler.obtainMessage();
		//ʹ�����ط������пα�ɼ�������
		download_info = new Download_info(username
        		, password , yat);
		score = new Score(score_table.this , username , yat_part , yat);
        //�����µ��߳�����������
		new Thread(new Runnable() 
        {  		                    
            public void run()  
            {             	
                //��ʼ����
                if(download_info.getSCO())
                {
                	//���سɹ�
                	//�жϽ�ɫ����������Ϣ��UI���ı�UI�����û�(���֮��ʵ��)
                	mess.what=0;
                	//�ɹ�����֮��resutl�л����ѧ�����߽�ʦ�ֶΣ���֮û��
         		    if(download_info.result.contains("ѧ��")||download_info.result.contains("�ον�ʦ"))
         			{
         				result = download_info.result;
         				admin = download_info.getAdmin();
         				//ɾ��ԭ���ľɱ��
         		        SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
         		        //db.execSQL("drop table if exists "+score_table.this.update_table_name_les);
         		        db.execSQL("drop table if exists "+score_table.this.update_table_name_sco);
         		        db.close();         		        
         		        //analysis();
         				
         				//��ʼ��������α�ɼ���Ϣ
         				if(score.start())
         				{
         					//Ϊ�������Ա�����������ƶ����򣺶��߱���ͬʱ�ɹ���������Ϊ�������ɾ����Ӧ���ļ�(��Lesson��Score���е�˵��)
         					//�ɹ�ʲô������
         				}
         				else
         				{
         					//����һ������ʧ�ܣ����߶�ʧ�ܣ�������ʾ�������û�����������
         					AlertDialog.Builder builder = new AlertDialog.Builder(score_table.this);
         		    		builder.setMessage("��������ʧ��,������ϵͳ��Ӧ������������ԭ����ֱ���������ػ���������������").setCancelable(false)
         		    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
         						
         						public void onClick(DialogInterface dialog, int which) {
         							// TODO Auto-generated method stub
         							finish();
         						}
         					}).show();
         					return;
         				}
         				mess.what=1;  
         			  	mHandler.sendMessage(mess);
         			}
         			else
         			{
         				result = download_info.result;
         				mess.what=2;    
         			  	mHandler.sendMessage(mess);
         			}
                }
                else
                {
                	//����ʧ�ܣ�������У�����������������������źŲ��õ��µ������жϣ�Ϊ���ٴ��������ж��Ƿ����ص�����
                	//�����(kb.txt����score.txt)��ɾ��������֮�����ķ���
                	File les_file = new File("/data/data/com.low_kb/" , "kb.txt");
                	File sco_file = new File("/data/data/com.low_kb/" , "score.txt");
                	if(les_file.exists())
                	{
                		les_file.delete();
                	}
                	if(sco_file.exists())
                	{
                		sco_file.delete();
                	}
                	//��ʾ�û�
                	AlertDialog.Builder builder = new AlertDialog.Builder(score_table.this);
    	    		builder.setMessage("��������ʧ��,������У����������Ӧ���������źŲ��õ��µ������жϡ���ֱ���������ػ���������������").setCancelable(false)
    	    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {		
    					public void onClick(DialogInterface dialog, int which) {
    						finish();
    					}
    				}).show();
                	return;
                }
             }  
         }).start();
	}
	public boolean getInternet()
    {
    	cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
		{ 
		       //do something 
		       //������ 
	    	   return true; 
		  }else
		  { 
		       //do something 
		       //�������� 			  
		        return false; 
		  } 		 
    }
	private Handler mHandler = new Handler()
	{
		   public void handleMessage(Message msg)  
	        {  
	            super.handleMessage(msg); 
	            switch(msg.what)
	            {
	            	case 1:
	            	{	            		
	            		progressDialog.dismiss();
	            		Toast.makeText(score_table.this, "���³ɹ�", 100).show();
	            		break;
	            	}
	            	case 2:
	            	{ 		
	            		progressDialog.dismiss();
	            		Toast.makeText(score_table.this, "����ʧ��", 100).show();
	            		break;
	            	}
	            	
	            }
	        }
	};
}
