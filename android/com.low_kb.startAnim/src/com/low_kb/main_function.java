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
			//��ʼ���ݵĽ�������
			Toast.makeText(main_function.this, "���ڴ�����Ϣ", 100).show();
			SaveUserInfo = new DBHelper_SaveUserInfo(main_function.this);
			
			//��ʽ��ʼ����ÿ������������
			loadActivity();
			/*������Ϊҳ����ÿһ���İ������÷���*******************************/
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
			//�γ̲鿴�Ķ���
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
			//�ɼ��鿴�Ķ���
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
			//�ƴ����Ų鿴�Ķ���
			/*yn_news.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, yn_news.class);
					startActivity(i1);
				}
			});*/
			//У԰����Ķ���
			notice.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, notice_table.class);
					startActivity(i1);
				}
			});
			//��������Ķ���
			book.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, library_table.class);
					startActivity(i1);
				}
			});
			//����ƽ̨�Ķ���
			shoping.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, second_hand.class);
					startActivity(i1);
				}
			});
			//���Ͷ���
			rice.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, order_food_table.class);
					startActivity(i1);
				}
			});
			//�ܱ߲鿴����
			map.setOnClickListener(new View.OnClickListener() 
			{			
				public void onClick(View v) 
				{
					Intent i1 = new Intent(context, map.class);
					startActivity(i1);
				}
			});
			//�༶����
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
	    	menu.add(group1,1,1,"˵��");
	    	menu.add(group1,2,2,"�������");
	    	menu.add(group1,3,3,"����");
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
					AlertDialog.Builder builder = new AlertDialog.Builder(main_function.this);
       	    		builder.setMessage("�������QQ��610756247").setCancelable(false)
       	    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
       					
       					public void onClick(DialogInterface dialog, int which) {
       						// TODO Auto-generated method stub
       						dialog.cancel();
       					}
       				}).show();
						break;
				   }
		    	   case 2:
		    	   {
		    		   //������棬��ֵ ���
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
		    		 //���������汾�Ƿ���Ҫ����  
		    		   if(getInternet())
		    		   {
//		    			   mUpdateManager = new UpdateManager(this);  
//			    	       if(mUpdateManager.checkUpdateInfo())
//			    	       {
//			    	    	  //��ʼ���� 
//			    	       }
//			    	       else
//			    	       {
//			    	    	   AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				   	    		builder.setMessage("�Ѿ������°汾��").setCancelable(false)
//				   				.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
//				   					
//				   					public void onClick(DialogInterface dialog, int which) {
//				   						// TODO Auto-generated method stub
//				   						dialog.cancel();												
//				   					}
//				   				}).show();
//			    	       }
		    			   AlertDialog.Builder builder = new AlertDialog.Builder(this);
			   	    		builder.setMessage("���ܴ������ƽ׶�").setCancelable(false)
			   				.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {			   					
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
	    //Ϊ�����趨����
	    public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage("��ȷ���˳���").setCancelable(false)
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();												
					}
				})
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
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
			       //������ 
		    	   return true; 
			  }else
			  { 
			       //do something 
			       //�������� 
				  AlertDialog.Builder builder = new AlertDialog.Builder(this);
		    		builder.setMessage("δ��������").setCancelable(false)
		    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {					
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
