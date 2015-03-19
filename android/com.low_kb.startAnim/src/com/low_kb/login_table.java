package com.low_kb;


import java.io.File;
import java.io.StringReader;

import org.ksoap2.HeaderProperty;

import service.*;


import com.SQLite.*;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class login_table extends Activity {


	//查找相应的元素
	DBHelper_SaveUserInfo SaveUserInfo ;
	Button logon;
	private ProgressDialog loadingDialog = null;
	AutoCompleteTextView usernamefield;
	EditText passwordfield;
	CheckBox remenber;
	Spinner yats; 
	public static Boolean check = false;
	String isnum , button , yat_string , admin , result;
	int flag = 0;
	ProgressDialog progressDialog;
	LinearLayout ll = null;
	Message mess ;
	Intent intent = null;
	String term[] = new String[]{"2009-2010学年第一学期" , "2009-2010学年第二学期" , "2010-2011学年第一学期","2010-2011学年第二学期","2011-2012学年第一学期" , 
			                     "2011-2012学年第二学期" , "2012-2013学年第一学期" , "2012-2013学年第二学期" , "2013-2014学年第一学期" , "2013-2014学年第二学期"
			                     ,"2014-2015学年第一学期" , "2014-2015学年第二学期"};
	private static String saveUsername=null;
	ConnectivityManager cwjManager;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.PROGRESS_START);
        setContentView(R.layout.logon);
        ll =  (LinearLayout) findViewById(R.id.login_table);
		File img = img = new File(Environment.getExternalStorageDirectory()+"/startAnim/login_table.jpg");
	     if(img.exists())
	     {
	    	 Drawable d = Drawable.createFromPath(Environment.getExternalStorageDirectory()+"/startAnim/login_table.jpg");
	    	 ll.setBackgroundDrawable(d);
	     }
        //变量初始化
        isnum=""; button=""; yat_string="2009-2010学年第一学期"; admin=""; result="";
        SaveUserInfo = new DBHelper_SaveUserInfo(login_table.this);
        mess = mHandler.obtainMessage();
        //成绩和课程的登录统一使用这个界面，这里进行判断，是要跳转哪个：成绩？？课程？？
		final Intent button_intent = getIntent();
		Bundle button_bundle = button_intent.getBundleExtra("bundle");
        button = button_bundle.getString("button");
        //控件查找赋值
        logon = (Button) findViewById(R.id.logon);
        usernamefield = (AutoCompleteTextView) findViewById(R.id.usernamefiled);
        passwordfield = (EditText) findViewById(R.id.passwordfiled);
        yats = (Spinner) findViewById(R.id.yat);
        remenber = (CheckBox) findViewById(R.id.remenber);
        remenber.setChecked(check);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , term);
        yats.setAdapter(adapter);
        //查找是否有用户记住过密码
    	User user = SaveUserInfo.query_userinfo_item();
        //进入界面后判断是否记住密码
        if(user.isnum.equals("1"))
		{      	//如果是，则直接跳转

			usernamefield.setText(user.username);
			passwordfield.setText(user.password);
			//hasLogin(usernamefield.getText().toString(), passwordfield.getText().toString() , user.yat);
			this.SetIntent(button);
			
			
	        Bundle bundle = new Bundle();
	        bundle.putString("isnum", user.isnum);
	        bundle.putString("yat", user.yat);
	        //这个标记是说明是否已经下载并且解析保存过，1是已经保存本地数据但是记住密码
	        bundle.putString("flag", "1");
	        bundle.putString("username", usernamefield.getText().toString());
			bundle.putString("password", passwordfield.getText().toString());
			bundle.putBoolean("check", true);
			intent.putExtra("bundle", bundle);
			startActivity(intent);
			finish();
		}
        else
        {  
        //选择学年和学期	
        yats.setOnItemSelectedListener(new OnItemSelectedListener()
        {
    		public void onItemSelected(AdapterView<?> arg0, View arg1,
    				int pos, long arg3) {
    			// TODO Auto-generated method stub
    			yat_string = arg0.getItemAtPosition(pos).toString();
    		}
    		public void onNothingSelected(AdapterView<?> arg0) {
    			// TODO Auto-generated method stub
    				
    		}	
         });		
        //记住密码的状态的保存
        remenber.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub				
				check = isChecked;
				if(check)
				{
					Toast.makeText(login_table.this, "您选择了记住密码", Toast.LENGTH_SHORT).show();
				}
				
			}
        	
        });
        
        //登陆键的方法指定
        logon.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//判断账号密码是否为空,如果为空返回
				if(usernamefield.getText().toString().equals(""))
				{
					Toast.makeText(login_table.this, "请输入用户名", Toast.LENGTH_SHORT).show();
					return;
				}
				else if(passwordfield.getText().toString().equals(""))
				{
					Toast.makeText(login_table.this, "请输入密码", Toast.LENGTH_SHORT).show();
					return;
				}
				//是否记住密码
				if(check)
				{
					//1是记住
					isnum = "1";
				}
				else
				{
					//0是不记住
					isnum ="0";
				}
				//判断有没有下载过信息,0是下载过但是没有保存,1是没联网，2是没有下载过
				if(hasLogin(usernamefield.getText().toString(), passwordfield.getText().toString() , YAT())==0)
		    	{
					progressDialog = ProgressDialog.show(login_table.this, "注意", "正在加载", true, false);
					
					if(isnum.equals("1"))
		    		{//先删除，后写入
		    			SaveUserInfo.del_userinfo_item(usernamefield.getText().toString());
			    		SaveUserInfo.insert_item(usernamefield.getText().toString(), passwordfield.getText().toString()
			    				, isnum , YAT());
			    		SaveUserInfo.close();
		    		}
					
					SetIntent(button);	//跳转判定
					
			        Bundle bundle = new Bundle();
			        bundle.putString("isnum", isnum);
			        bundle.putString("yat", yat_string);
			        //这个标记是说明是否已经下载并且解析保存过，2是已经保存本地数据但是没有记住密码
			        bundle.putString("flag", "2");
					bundle.putString("username", usernamefield.getText().toString());
					bundle.putString("password", passwordfield.getText().toString());
					intent.putExtra("bundle", bundle);
					bundle.putBoolean("check", check);
					startActivity(intent);
					mess.what=5;    
     			  	mHandler.sendMessage(mess);
					finish();
		    	}
				else
				{
					if(hasLogin(usernamefield.getText().toString(), passwordfield.getText().toString() , YAT())==1)
					{
						return;
					}
					else if(hasLogin(usernamefield.getText().toString(), passwordfield.getText().toString() , YAT())==2)
					{
						//使用线程开始下载
						progressDialog = ProgressDialog.show(login_table.this, "注意", "正在下载", true, false);
		                Thread loading = new Thread(new Runnable() 
		                {  		                    
		                    public void run()  
		                    {  
		                    	//使用下载方法进行课表成绩的下载
		                        Download_info download_info = new Download_info(usernamefield.getText().toString()
		                        		, passwordfield.getText().toString() , yat_string);
		                        //开始下载
		                        if(download_info.run())
		                        {
		                        	//下载成功
		                        	//判断角色，并发送消息给UI，改变UI提醒用户(这个之后实现)
			                    	mess.what=0;
			                    	//成功下载之后，resutl中会包含学生或者教师字段，反之没有
			             		    if(download_info.result.contains("学生")||download_info.result.contains("任课教师"))
			             			{
			             				result = download_info.result;
			             				admin = download_info.getAdmin();
			             				mess.what=1;  
			             			  	//mHandler.sendMessage(mess);
			             				mHandler.sendEmptyMessage(0);
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
		                        	File sco_file = new File("/data/data/com.low_kb/"  , "score.txt");
		                        	if(les_file.exists())
		                        	{
		                        		les_file.delete();
		                        	}
		                        	if(sco_file.exists())
		                        	{
		                        		sco_file.delete();
		                        	}
		                        	//提示用户
		                        	AlertDialog.Builder builder = new AlertDialog.Builder(login_table.this);
		            	    		builder.setMessage("下载数据失败,可能是校服务器无响应，或网络信号不好导致的下载中断。请直接重新下载或清除缓存后再下载").setCancelable(false)
		            	    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {		
		            					public void onClick(DialogInterface dialog, int which) {
		            						// TODO Auto-generated method stub
		            						finish();
		            					}
		            				}).show();
		                        	return;
		                        }
		                        download_info = null;
		                        mess.what=4;    
	             			  	mHandler.sendMessage(mess);
		                     }  
		                 });  
		                loading.start();  
					}
				}    				
			}        	
        });
      }
    }
  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(group1,1,1,"注销");
    	//menu.add(group1,2,2,"设置背景");
    	menu.add(group1,3,3,"软件说明");
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
				   this.deleteDatabase("userinfo.db");
    			   this.deleteDatabase("userdatainfo.db");
    			   this.deleteDatabase("key.db");
					break;
			   }
	    	   case 2:
	    	   {	   
	    		   //Intent intent = new Intent(MainActivity.this,backgroundset.class);
	    		  	//startActivity(intent);
	    		   break;
	    	   }
			   case 3:
			   {  //为软件说明添加功能
				   AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("软件说明").setMessage("特别感谢：笨蛋雄").setCancelable(false)
					.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					}).show();
					break;
			   }
	    	}
			return true;
	    }
	    
	/*------------------------------------------------------------*/
	    //为手机的功能按键设定方法
	    public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage("你确定退出吗？").setCancelable(false)
	    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();											
					}
				}).show();
	    	}
	    	if(keyCode==KeyEvent.KEYCODE_MENU)
	    	{
	    		openOptionsMenu();
	    	}
	    	
			return true;   	
	    }    
	    
	    
	   //这个方法用来判断以前是否下载过该信息，如果下载过，就直接跳转下一个页面 
	   public int hasLogin(String username , String password , String yat)
	   {     //在userinfo数据库里面建立一张haslogin表，里面存放有哪些用户的课表，成绩保存在数据库里面，每次执行该
		     //Actitivy执行这个方法,0是正确，1是没有联网，2是用户信息错误
		   DBHelper_SaveUserInfo SaveUserInfo = new DBHelper_SaveUserInfo(login_table.this);
	       User user = SaveUserInfo.query_userinfo(username , yat);
	       if(user.username.equals(username))
	       {
	    	   if(user.password.equals(password))
		    	{
		    		
		    		if(user.yat.equals(yat))
		    		{
		    			if(user.username.substring(1,2).equals("z"))
		    			{
		    				admin = "teacher";
		    			}
		    			else
		    			{
		    				admin = "student";
		    			}
		    			user = null;
			    		SaveUserInfo.close();
			    		//所有信息正确
			    		return 0;
		    		}
		    		else
		    		{
		    			//需要下载时检查是否联网，如果未联网则返回
		    			cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		    			NetworkInfo info = cwjManager.getActiveNetworkInfo();
		    			if (info != null && info.isAvailable())
		    			{ 
		    			    //能联网 
		    				Toast.makeText(login_table.this, "已经连网", 100).show();
		    				return 2; 
									
		    			  }
		    			  else
		    			  {  
		    			      //不能联网 
		    				  Toast.makeText(login_table.this, "没有连网", 100).show();
		    			      return 1; 
		    			  } 		   
		    		}	
		    	}
		    	else
		    	{			
		    		//需要下载时检查是否联网，如果未联网则返回
		    		cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    			NetworkInfo info = cwjManager.getActiveNetworkInfo();
	    			if (info != null && info.isAvailable())
	    			{ 
	    			       //能联网 
	    				Toast.makeText(login_table.this, "已经连网", 100).show();
	    				return 2;		
	    			  }
	    			  else
	    			  { 
	    			       //不能联网 
	    				  Toast.makeText(login_table.this, "没有连网", 100).show();
	    			        return 1; 
	    			  } 
		    		
		    	}
	       }
	       else
	       {
	    	 //需要下载时检查是否联网，如果未联网则返回
	    	   cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
   			   NetworkInfo info = cwjManager.getActiveNetworkInfo();
	   			if (info != null && info.isAvailable())
	   			{ 
	   				//能联网 
	   				Toast.makeText(login_table.this, "已经连网", 100).show();
	   				SaveUserInfo.close();
		 	    	return 2; 
	   			  }else
	   			  { 
	   			      //不能联网 
	   				  Toast.makeText(login_table.this, "没有连网", 100).show();
	   			      return 1; 
	   			  } 	    	
	       }
	   }
	   
	   //跳转判定
	   public void SetIntent(String str)
	   {
		   if(button.equals("com"))
		   {
			   intent = new Intent(login_table.this, communication.class);
		   }
		   if(button.equals("lesson"))
			{
				
				intent = new Intent(login_table.this, lesson_table.class);
			}
		   if(button.equals("score"))
			{
				intent = new Intent(login_table.this, score_table.class);
			}
		   
	   }
	   
	   //这个方法用来处理学年学期的数据，之后保存入用户信息表
	   public String YAT()
	    {
		    //获取的数据为2011-2012第二学期
		    //存入数据库的信息为201120122，通过以下转换得到
	    	String s = null;
	    	s = yat_string.substring(0, 4);
	    	s+=yat_string.substring(5, 9);
	    	if(yat_string.substring(12, 13).equals("一"))
	    	{
	    		s+=1;
	    	}
	    	else
	    	{
	    		s+=2;
	    	}
			return s;
	    	
	    }
	   
	   
	   /***********************解析********************************/
		public void analysis()
		{
			Lesson lesson = new Lesson(login_table.this , usernamefield.getText().toString() 
					, new GetYAT(yat_string , "3").getYAT());
			Score score = new Score(login_table.this , usernamefield.getText().toString() 
					, new GetYAT(yat_string , "3").getYAT() , yat_string);
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
	   
	   
	   private Handler mHandler = new Handler(){
		   public void handleMessage(Message msg)  
	        {  
	            super.handleMessage(msg); 
	            switch(msg.what)
	            {
	            	case 0:{break;}
	            	case 1:{break;}
	            	case 2:{break;}
	            	case 4:
	            	{
	            		 if(result.contains("学生")||result.contains("任课教师"))
					        {}
					        else
					        {
					        	//登陆失败
					        	Toast.makeText(login_table.this, "账号或密码错误或网络信号不好", Toast.LENGTH_SHORT).show();
					        	progressDialog.dismiss();
								return;
					        }
					        //每次成功登陆后都会更新两个用户数据库中的对应用户信息				        
				    		SaveUserInfo.insert(usernamefield.getText().toString(), passwordfield.getText().toString() , YAT());
				    		//判断是否记住密码，如果记住就写入数据库
				    		if(isnum.equals("1"))
				    		{//先删除，后写入
				    			SaveUserInfo.del_userinfo_item(usernamefield.getText().toString());
					    		SaveUserInfo.insert_item(usernamefield.getText().toString(), passwordfield.getText().toString()
					    				, isnum , YAT());	
				    		}
				    		SaveUserInfo.close();
							SetIntent(button);
					        Bundle bundle = new Bundle();
					        bundle.putString("isnum", isnum);
					        bundle.putString("yat", yat_string);
					        //这个标记是说明是否已经下载并且解析保存过，3是需要在课表成绩面板解析并且保存数据
				        	bundle.putString("flag", "3");
							bundle.putString("username", usernamefield.getText().toString());
							bundle.putString("password", passwordfield.getText().toString());
							intent.putExtra("bundle", bundle);
							bundle.putBoolean("check", check);
							analysis();
							//跳转完成后关闭第一个Activity和提示对话框
							progressDialog.dismiss();
							startActivity(intent);
							finish();
							break;
	            	}
	            	case 5:
	            	{
	            		progressDialog.dismiss();
	            		break;
	            	}
	            }
	        }
	   };
	   
	    
}
