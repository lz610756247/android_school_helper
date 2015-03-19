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


	//������Ӧ��Ԫ��
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
	String term[] = new String[]{"2009-2010ѧ���һѧ��" , "2009-2010ѧ��ڶ�ѧ��" , "2010-2011ѧ���һѧ��","2010-2011ѧ��ڶ�ѧ��","2011-2012ѧ���һѧ��" , 
			                     "2011-2012ѧ��ڶ�ѧ��" , "2012-2013ѧ���һѧ��" , "2012-2013ѧ��ڶ�ѧ��" , "2013-2014ѧ���һѧ��" , "2013-2014ѧ��ڶ�ѧ��"
			                     ,"2014-2015ѧ���һѧ��" , "2014-2015ѧ��ڶ�ѧ��"};
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
        //������ʼ��
        isnum=""; button=""; yat_string="2009-2010ѧ���һѧ��"; admin=""; result="";
        SaveUserInfo = new DBHelper_SaveUserInfo(login_table.this);
        mess = mHandler.obtainMessage();
        //�ɼ��Ϳγ̵ĵ�¼ͳһʹ��������棬��������жϣ���Ҫ��ת�ĸ����ɼ������γ̣���
		final Intent button_intent = getIntent();
		Bundle button_bundle = button_intent.getBundleExtra("bundle");
        button = button_bundle.getString("button");
        //�ؼ����Ҹ�ֵ
        logon = (Button) findViewById(R.id.logon);
        usernamefield = (AutoCompleteTextView) findViewById(R.id.usernamefiled);
        passwordfield = (EditText) findViewById(R.id.passwordfiled);
        yats = (Spinner) findViewById(R.id.yat);
        remenber = (CheckBox) findViewById(R.id.remenber);
        remenber.setChecked(check);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_item , term);
        yats.setAdapter(adapter);
        //�����Ƿ����û���ס������
    	User user = SaveUserInfo.query_userinfo_item();
        //���������ж��Ƿ��ס����
        if(user.isnum.equals("1"))
		{      	//����ǣ���ֱ����ת

			usernamefield.setText(user.username);
			passwordfield.setText(user.password);
			//hasLogin(usernamefield.getText().toString(), passwordfield.getText().toString() , user.yat);
			this.SetIntent(button);
			
			
	        Bundle bundle = new Bundle();
	        bundle.putString("isnum", user.isnum);
	        bundle.putString("yat", user.yat);
	        //��������˵���Ƿ��Ѿ����ز��ҽ����������1���Ѿ����汾�����ݵ��Ǽ�ס����
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
        //ѡ��ѧ���ѧ��	
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
        //��ס�����״̬�ı���
        remenber.setOnCheckedChangeListener(new OnCheckedChangeListener()
        {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub				
				check = isChecked;
				if(check)
				{
					Toast.makeText(login_table.this, "��ѡ���˼�ס����", Toast.LENGTH_SHORT).show();
				}
				
			}
        	
        });
        
        //��½���ķ���ָ��
        logon.setOnClickListener(new View.OnClickListener()
        {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//�ж��˺������Ƿ�Ϊ��,���Ϊ�շ���
				if(usernamefield.getText().toString().equals(""))
				{
					Toast.makeText(login_table.this, "�������û���", Toast.LENGTH_SHORT).show();
					return;
				}
				else if(passwordfield.getText().toString().equals(""))
				{
					Toast.makeText(login_table.this, "����������", Toast.LENGTH_SHORT).show();
					return;
				}
				//�Ƿ��ס����
				if(check)
				{
					//1�Ǽ�ס
					isnum = "1";
				}
				else
				{
					//0�ǲ���ס
					isnum ="0";
				}
				//�ж���û�����ع���Ϣ,0�����ع�����û�б���,1��û������2��û�����ع�
				if(hasLogin(usernamefield.getText().toString(), passwordfield.getText().toString() , YAT())==0)
		    	{
					progressDialog = ProgressDialog.show(login_table.this, "ע��", "���ڼ���", true, false);
					
					if(isnum.equals("1"))
		    		{//��ɾ������д��
		    			SaveUserInfo.del_userinfo_item(usernamefield.getText().toString());
			    		SaveUserInfo.insert_item(usernamefield.getText().toString(), passwordfield.getText().toString()
			    				, isnum , YAT());
			    		SaveUserInfo.close();
		    		}
					
					SetIntent(button);	//��ת�ж�
					
			        Bundle bundle = new Bundle();
			        bundle.putString("isnum", isnum);
			        bundle.putString("yat", yat_string);
			        //��������˵���Ƿ��Ѿ����ز��ҽ����������2���Ѿ����汾�����ݵ���û�м�ס����
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
						//ʹ���߳̿�ʼ����
						progressDialog = ProgressDialog.show(login_table.this, "ע��", "��������", true, false);
		                Thread loading = new Thread(new Runnable() 
		                {  		                    
		                    public void run()  
		                    {  
		                    	//ʹ�����ط������пα�ɼ�������
		                        Download_info download_info = new Download_info(usernamefield.getText().toString()
		                        		, passwordfield.getText().toString() , yat_string);
		                        //��ʼ����
		                        if(download_info.run())
		                        {
		                        	//���سɹ�
		                        	//�жϽ�ɫ����������Ϣ��UI���ı�UI�����û�(���֮��ʵ��)
			                    	mess.what=0;
			                    	//�ɹ�����֮��resutl�л����ѧ�����߽�ʦ�ֶΣ���֮û��
			             		    if(download_info.result.contains("ѧ��")||download_info.result.contains("�ον�ʦ"))
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
		                        	//����ʧ�ܣ�������У�����������������������źŲ��õ��µ������жϣ�Ϊ���ٴ��������ж��Ƿ����ص�����
		                        	//�����(kb.txt����score.txt)��ɾ��������֮�����ķ���
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
		                        	//��ʾ�û�
		                        	AlertDialog.Builder builder = new AlertDialog.Builder(login_table.this);
		            	    		builder.setMessage("��������ʧ��,������У����������Ӧ���������źŲ��õ��µ������жϡ���ֱ���������ػ���������������").setCancelable(false)
		            	    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {		
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
    	menu.add(group1,1,1,"ע��");
    	//menu.add(group1,2,2,"���ñ���");
    	menu.add(group1,3,3,"���˵��");
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
			   {  //Ϊ���˵����ӹ���
				   AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("���˵��").setMessage("�ر��л��������").setCancelable(false)
					.setPositiveButton("�ر�", new DialogInterface.OnClickListener() {
						
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
	    //Ϊ�ֻ��Ĺ��ܰ����趨����
	    public boolean onKeyDown(int keyCode,KeyEvent event)
	    {
	    	if(keyCode==KeyEvent.KEYCODE_BACK)
	    	{
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage("��ȷ���˳���").setCancelable(false)
	    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				})
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
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
	    
	    
	   //������������ж���ǰ�Ƿ����ع�����Ϣ��������ع�����ֱ����ת��һ��ҳ�� 
	   public int hasLogin(String username , String password , String yat)
	   {     //��userinfo���ݿ����潨��һ��haslogin������������Щ�û��Ŀα��ɼ����������ݿ����棬ÿ��ִ�и�
		     //Actitivyִ���������,0����ȷ��1��û��������2���û���Ϣ����
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
			    		//������Ϣ��ȷ
			    		return 0;
		    		}
		    		else
		    		{
		    			//��Ҫ����ʱ����Ƿ����������δ�����򷵻�
		    			cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		    			NetworkInfo info = cwjManager.getActiveNetworkInfo();
		    			if (info != null && info.isAvailable())
		    			{ 
		    			    //������ 
		    				Toast.makeText(login_table.this, "�Ѿ�����", 100).show();
		    				return 2; 
									
		    			  }
		    			  else
		    			  {  
		    			      //�������� 
		    				  Toast.makeText(login_table.this, "û������", 100).show();
		    			      return 1; 
		    			  } 		   
		    		}	
		    	}
		    	else
		    	{			
		    		//��Ҫ����ʱ����Ƿ����������δ�����򷵻�
		    		cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    			NetworkInfo info = cwjManager.getActiveNetworkInfo();
	    			if (info != null && info.isAvailable())
	    			{ 
	    			       //������ 
	    				Toast.makeText(login_table.this, "�Ѿ�����", 100).show();
	    				return 2;		
	    			  }
	    			  else
	    			  { 
	    			       //�������� 
	    				  Toast.makeText(login_table.this, "û������", 100).show();
	    			        return 1; 
	    			  } 
		    		
		    	}
	       }
	       else
	       {
	    	 //��Ҫ����ʱ����Ƿ����������δ�����򷵻�
	    	   cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
   			   NetworkInfo info = cwjManager.getActiveNetworkInfo();
	   			if (info != null && info.isAvailable())
	   			{ 
	   				//������ 
	   				Toast.makeText(login_table.this, "�Ѿ�����", 100).show();
	   				SaveUserInfo.close();
		 	    	return 2; 
	   			  }else
	   			  { 
	   			      //�������� 
	   				  Toast.makeText(login_table.this, "û������", 100).show();
	   			      return 1; 
	   			  } 	    	
	       }
	   }
	   
	   //��ת�ж�
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
	   
	   //���������������ѧ��ѧ�ڵ����ݣ�֮�󱣴����û���Ϣ��
	   public String YAT()
	    {
		    //��ȡ������Ϊ2011-2012�ڶ�ѧ��
		    //�������ݿ����ϢΪ201120122��ͨ������ת���õ�
	    	String s = null;
	    	s = yat_string.substring(0, 4);
	    	s+=yat_string.substring(5, 9);
	    	if(yat_string.substring(12, 13).equals("һ"))
	    	{
	    		s+=1;
	    	}
	    	else
	    	{
	    		s+=2;
	    	}
			return s;
	    	
	    }
	   
	   
	   /***********************����********************************/
		public void analysis()
		{
			Lesson lesson = new Lesson(login_table.this , usernamefield.getText().toString() 
					, new GetYAT(yat_string , "3").getYAT());
			Score score = new Score(login_table.this , usernamefield.getText().toString() 
					, new GetYAT(yat_string , "3").getYAT() , yat_string);
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
	            		 if(result.contains("ѧ��")||result.contains("�ον�ʦ"))
					        {}
					        else
					        {
					        	//��½ʧ��
					        	Toast.makeText(login_table.this, "�˺Ż��������������źŲ���", Toast.LENGTH_SHORT).show();
					        	progressDialog.dismiss();
								return;
					        }
					        //ÿ�γɹ���½�󶼻���������û����ݿ��еĶ�Ӧ�û���Ϣ				        
				    		SaveUserInfo.insert(usernamefield.getText().toString(), passwordfield.getText().toString() , YAT());
				    		//�ж��Ƿ��ס���룬�����ס��д�����ݿ�
				    		if(isnum.equals("1"))
				    		{//��ɾ������д��
				    			SaveUserInfo.del_userinfo_item(usernamefield.getText().toString());
					    		SaveUserInfo.insert_item(usernamefield.getText().toString(), passwordfield.getText().toString()
					    				, isnum , YAT());	
				    		}
				    		SaveUserInfo.close();
							SetIntent(button);
					        Bundle bundle = new Bundle();
					        bundle.putString("isnum", isnum);
					        bundle.putString("yat", yat_string);
					        //��������˵���Ƿ��Ѿ����ز��ҽ����������3����Ҫ�ڿα�ɼ����������ұ�������
				        	bundle.putString("flag", "3");
							bundle.putString("username", usernamefield.getText().toString());
							bundle.putString("password", passwordfield.getText().toString());
							intent.putExtra("bundle", bundle);
							bundle.putBoolean("check", check);
							analysis();
							//��ת��ɺ�رյ�һ��Activity����ʾ�Ի���
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
