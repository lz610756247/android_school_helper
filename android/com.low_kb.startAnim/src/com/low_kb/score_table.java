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
		//获取传入的数据
		final Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        isnum = bundle.getString("isnum");
        yat = bundle.getString("yat");
        //这个标记是说明是否已经下载并且解析保存过，1是已经保存本地数据但是记住密码,2是已经保存本地数据但是"没有"记住密码
        //3是需要在课表成绩面板解析并且保存数据
        flag = bundle.getString("flag");
    	username = bundle.getString("username");
    	password = bundle.getString("password");
    	check = bundle.getBoolean("check");
    	//根据获得传入的信息组合成具体的表格名称进行数据的提取
    	getyat = new GetYAT(yat , flag);
    	yat_part = getyat.getYAT();
//    	//判断是否需要解析数据并且保存进入数据库
//    	if(flag.equals("3"))
//    	{
//    		//当flag等于3时，说明现在的信息是新下载的，需要解析保存
//    		//这个类用来处理课程信息，解析，保存
//    		analysis();
//    	}		
		score_listview = (ListView) findViewById(R.id.score_listview);
		score_listview.setCacheColorHint(Color.TRANSPARENT);
		List<Map<String ,Object>> ListItem ;
		//成绩的读取,写入
	       
	       ListItem = query_all(username+yat_part);
	       adapter = new SimpleAdapter(this,ListItem,R.layout.score_item
	       		,new String[]{"kcmc" , "kkjs" , "cj"},new int[]{R.id.kcmc , R.id.kkjs , R.id.cj});
	       score_listview.setAdapter(adapter);
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(group1,1,1,"登陆界面");
    	//menu.add(group1,2,2,"更新数据");
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
	    		 //返回登录界面，数据库数值和相应文件清空
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
	//从数据库提取相应的数据进行ListView加载
	public List<Map<String ,Object>> query_all(String keyname_getName)
	{
		String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		Log.i("GAT303", "GAT303");
		List<Map<String ,Object>> ListItem = new ArrayList<Map<String ,Object>>();
		Map<String,Object> map;
		TabName tan = new  TabName();
		//getName()方法提供表名
		//使用TabMane类的方法组合出完整的表名
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
	
	/***********************解析********************************/
	public void analysis()
	{
		Lesson lesson = new Lesson(score_table.this , username , yat_part);
		Score score = new Score(score_table.this , username , yat_part , yat);
		//开始解析保存课表成绩信息
		if(score.start()&&lesson.start())
		{
			//为方便程序员操作，这里制定规则：二者必须同时成功，否则视为编译错误，删除相应的文件(见Lesson和Score类中的说明)
			//成功什么都不做
		}
		else
		{
			//其中一个编译失败，或者都失败，跳出提示框，提醒用户，重新下载
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
    		builder.setMessage("保存数据失败,可能是系统反应过慢，或其他原因。请直接重新下载或清除缓存后再下载").setCancelable(false)
    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					finish();
				}
			}).show();
			return;
		}
	}
	/********************************更新**********************************/
	public void update(String update_table_name_les , String update_table_name_sco 
			, String username , String password , String yat)
	{
		if(!getInternet())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(score_table.this);
    		builder.setMessage("无网络连接").setCancelable(false)
    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show();
        	return;
		}
		progressDialog = ProgressDialog.show(score_table.this, "注意", "正在更新", true, false);
		mess = mHandler.obtainMessage();
		//使用下载方法进行课表成绩的下载
		download_info = new Download_info(username
        		, password , yat);
		score = new Score(score_table.this , username , yat_part , yat);
        //启用新的线程下载新数据
		new Thread(new Runnable() 
        {  		                    
            public void run()  
            {             	
                //开始下载
                if(download_info.getSCO())
                {
                	//下载成功
                	//判断角色，并发送消息给UI，改变UI提醒用户(这个之后实现)
                	mess.what=0;
                	//成功下载之后，resutl中会包含学生或者教师字段，反之没有
         		    if(download_info.result.contains("学生")||download_info.result.contains("任课教师"))
         			{
         				result = download_info.result;
         				admin = download_info.getAdmin();
         				//删除原来的旧表格
         		        SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
         		        //db.execSQL("drop table if exists "+score_table.this.update_table_name_les);
         		        db.execSQL("drop table if exists "+score_table.this.update_table_name_sco);
         		        db.close();         		        
         		        //analysis();
         				
         				//开始解析保存课表成绩信息
         				if(score.start())
         				{
         					//为方便程序员操作，这里制定规则：二者必须同时成功，否则视为编译错误，删除相应的文件(见Lesson和Score类中的说明)
         					//成功什么都不做
         				}
         				else
         				{
         					//其中一个编译失败，或者都失败，跳出提示框，提醒用户，重新下载
         					AlertDialog.Builder builder = new AlertDialog.Builder(score_table.this);
         		    		builder.setMessage("保存数据失败,可能是系统反应过慢，或其他原因。请直接重新下载或清除缓存后再下载").setCancelable(false)
         		    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
         						
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
                	//下载失败，可能是校服务器崩溃，可能是网络信号不好导致的下载中断，为减少错误，这里判断是否下载到数据
                	//如果有(kb.txt或者score.txt)则删除，减少之后出错的风险
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
                	//提示用户
                	AlertDialog.Builder builder = new AlertDialog.Builder(score_table.this);
    	    		builder.setMessage("下载数据失败,可能是校服务器无响应，或网络信号不好导致的下载中断。请直接重新下载或清除缓存后再下载").setCancelable(false)
    	    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {		
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
		       //能联网 
	    	   return true; 
		  }else
		  { 
		       //do something 
		       //不能联网 			  
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
	            		Toast.makeText(score_table.this, "更新成功", 100).show();
	            		break;
	            	}
	            	case 2:
	            	{ 		
	            		progressDialog.dismiss();
	            		Toast.makeText(score_table.this, "更新失败", 100).show();
	            		break;
	            	}
	            	
	            }
	        }
	};
}
