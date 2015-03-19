package com.low_kb;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import listening_function.Net_state_listening;

import operation.UpdateManager;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.SQLite.*;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LocalActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class main_function extends Activity
{
	DBHelper_SaveUserInfo SaveUserInfo= null ;
	Bundle bundle= null;
	String username="" , password="" , isnum="" , yat="" , flag="";
	Boolean check=false;
	LocalActivityManager manager = null;
	Context context = null;
	Button lesson= null , score= null , forum= null , notice= null , book= null , shoping= null , rice= null , yn_news=null
			 , map = null , com = null;
	Lesson lesson_op= null;
	Score score_op= null;
	private UpdateManager mUpdateManager;
	 @Override
	 public void onCreate(Bundle savedInstanceState) 
	 {
		 super.onCreate(savedInstanceState);
	     this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	     setContentView(R.layout.main_function);
	    	manager = new LocalActivityManager(this, true);
			manager.dispatchCreate(savedInstanceState);
			context = main_function.this;
			//开始数据的解析保存
			Toast.makeText(main_function.this, "正在处理信息", 100).show();
			SaveUserInfo = new DBHelper_SaveUserInfo(main_function.this);
			
			//正式开始加载每个容器的内容
			loadActivity();
			/*接下来为页面上每一个的按键设置方法*******************************/
			lesson = (Button) findViewById(R.id.slesson);
			score= (Button) findViewById(R.id.sscore);
			//forum= (Button) findViewById(R.id.sforum);
			//yn_news= (Button) findViewById(R.id.yn_news);
			notice= (Button) findViewById(R.id.snotice);
			book= (Button) findViewById(R.id.slibrary);
			shoping= (Button) findViewById(R.id.ssecondhand);
			rice= (Button) findViewById(R.id.sfood);
			map = (Button) findViewById(R.id.map);
			com = (Button) findViewById(R.id.com);
			//课程查看的动作
			lesson.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, login_table.class);
					Bundle bundle = new Bundle();
			        bundle.putString("button", "lesson");
			        i1.putExtra("bundle", bundle);
					startActivity(i1);
				}
			});
			//成绩查看的动作
			score.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, login_table.class);
					Bundle bundle = new Bundle();
			        bundle.putString("button", "score");
			        i1.putExtra("bundle", bundle);
					startActivity(i1);
				}
			});
			//财大新闻查看的动作
			/*yn_news.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, yn_news.class);
					startActivity(i1);
				}
			});*/
			//校园公告的动作
			notice.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, notice_table.class);
					startActivity(i1);
				}
			});
			//借书情况的动作
			book.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, library_table.class);
					startActivity(i1);
				}
			});
			//二手平台的动作
			shoping.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, second_hand.class);
					startActivity(i1);
				}
			});
			//订餐动作
			rice.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, order_food_table.class);
					startActivity(i1);
				}
			});
			//周边查看动作
			map.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, map.class);
					startActivity(i1);
				}
			});
			//班级交流
			com.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, login_table.class);
					Bundle bundle = new Bundle();
			        bundle.putString("button", "com");
			        i1.putExtra("bundle", bundle);
					startActivity(i1);
				}
			});
	 }
	 
	 public boolean onCreateOptionsMenu(Menu menu) {
	        getMenuInflater().inflate(R.menu.activity_main, menu);
	        int group1=1;
	    	menu.add(group1,1,1,"说明");
	    	menu.add(group1,2,2,"清除缓存");
	    	menu.add(group1,3,3,"更新");
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
					AlertDialog.Builder builder = new AlertDialog.Builder(main_function.this);
       	    		builder.setMessage("详情请见QQ：610756247").setCancelable(false)
       	    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
       					
       					public void onClick(DialogInterface dialog, int which) {
       						// TODO Auto-generated method stub
       						dialog.cancel();
       					}
       				}).show();
						break;
				   }
		    	   case 2:
		    	   {
		    		   //清除缓存，数值 清空
		    		   this.deleteDatabase("userinfo.db");
	    			   this.deleteDatabase("userdatainfo.db");
	    			   this.deleteDatabase("key.db");
		    		   login_table.check=false;
		    		   File file = new File("/data/data/"+this.getPackageName()+ "/shared_prefs/happyforever.xml");
					   file.delete();
		    		   break;
		    	   }	
		    	   case 3:
		    	   {
		    		 //这里来检测版本是否需要更新  
		    		   if(getInternet())
		    		   {
//		    			   mUpdateManager = new UpdateManager(this);  
//			    	       if(mUpdateManager.checkUpdateInfo())
//			    	       {
//			    	    	  //开始下载 
//			    	       }
//			    	       else
//			    	       {
//			    	    	   AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				   	    		builder.setMessage("已经是最新版本了").setCancelable(false)
//				   				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//				   					
//				   					public void onClick(DialogInterface dialog, int which) {
//				   						// TODO Auto-generated method stub
//				   						dialog.cancel();												
//				   					}
//				   				}).show();
//			    	       }
		    			   AlertDialog.Builder builder = new AlertDialog.Builder(this);
			   	    		builder.setMessage("功能处于完善阶段").setCancelable(false)
			   				.setNegativeButton("确定", new DialogInterface.OnClickListener() {			   					
			   					public void onClick(DialogInterface dialog, int which) {
			   						// TODO Auto-generated method stub
			   						dialog.cancel();												
			   					}
			   				}).show();
		    		   }		    		   
		    	        
		    		   break;
		    	   }
		    	}
				return true;
		    }
		
		
		/*------------------------------------------------------------*/
	    //为按键设定方法
	    public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage("你确定退出吗？").setCancelable(false)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();												
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).show();
	    	}
	    	if(keyCode==KeyEvent.KEYCODE_MENU)
	    	{
	    		openOptionsMenu();
	    	}
	    	
			return true;   	
	    }
	    
	    
	    public boolean getInternet()
	    {
	    	ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
				  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setMessage("未连接网络").setCancelable(false)
		    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {					
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();
			        return false; 
			  }
	    }
	    
	    
	    public void loadActivity()
	    {
	    	/*context = s_table.this;
	    	Intent i1 = new Intent(context, lesson_table.class);
	    	Intent i2 = new Intent(context, score_table.class);
	    	manager.startActivity("i1", i1);
	    	manager.startActivity("i2", i2);*/

	    }
	/****************************************************************************************/
	    

}
