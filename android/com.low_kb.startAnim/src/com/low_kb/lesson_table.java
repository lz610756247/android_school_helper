package com.low_kb;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;





import operation.OutPutStream_kb;
import operation.kb_opreation;

import org.xmlpull.v1.XmlPullParserException;

import service.Download_info;



import com.SQLite.*;






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
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;



public class lesson_table extends Activity{

	DBHelper_SaveUserInfo SaveUserInfo=null;
	Bundle bundle=null;
	String username="",password="",isnum="",yat="",flag="",yat_part="";
	boolean check=true;
	ListView lesson_listview=null;
	SimpleAdapter adapter=null;
	GetYAT getyat=null;
	LinearLayout ll = null;
	private WebView webView = null;
	kb_opreation str = null;
	String kb_body = "";
	Message mess ;
	String result , admin , update_table_name_les , update_table_name_sco;
	ProgressDialog progressDialog;
	ConnectivityManager cwjManager;
	Download_info download_info;
	Lesson lesson;
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SaveUserInfo = new DBHelper_SaveUserInfo(lesson_table.this);
		//���ؽ�����Ϣ,�鿴��Ҫ�����ĸ���1���б��ʽ,2�ǿα��ʽ
		switch (init())
		{
			case 0:
			case 1:
			{
				setContentView(R.layout.lessontable);
				ll =  (LinearLayout) findViewById(R.id.lesson_table);
				File img = img = new File(Environment.getExternalStorageDirectory()+"/startAnim/lesson_table.jpg");
			     if(img.exists())
			     {
			    	 Drawable d = Drawable.createFromPath(Environment.getExternalStorageDirectory()+"/startAnim/lesson_table.jpg");
			    	 ll.setBackgroundDrawable(d);
			     }
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
		    	str = new kb_opreation();
//		    	//�ж��Ƿ���Ҫ�������ݲ��ұ���������ݿ�
//		    	if(flag.equals("3"))
//		    	{
//		    		//��flag����3ʱ��˵�����ڵ���Ϣ�������صģ���Ҫ��������
//		    		//�������������γ���Ϣ������������
//		    		analysis();
//		    	}
				lesson_listview = (ListView) findViewById(R.id.lessons_listview);
				lesson_listview.setCacheColorHint(Color.TRANSPARENT);
				//�α�Ķ�ȡ     
			    List<Map<String ,Object>> ListItem;
		        //����α���Ϣ
		        ListItem = query_all(username+yat_part);   
		        //���ؿα��б�
		        adapter = new SimpleAdapter(this,ListItem,R.layout.lesson_item
            			, new String[]{"xkbh" , "kcmc","s1","k1","qzz"}
            			,new int[]{R.id.xkbh , R.id.kcmc,R.id.s1,R.id.k1,R.id.qzz});
		        lesson_listview.setAdapter(adapter);
		        lesson_listview.setOnItemClickListener(new OnItemClickListener()
		        {
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						// TODO Auto-generated method stub
						String res = arg0.getItemAtPosition(arg2).toString();
						Toast.makeText(lesson_table.this, res , Toast.LENGTH_SHORT).show();
					}
		        });
				break;
			}
			case 2:
			{
				setContentView(R.layout.kbwebview);
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
//		    	//�ж��Ƿ���Ҫ�������ݲ��ұ���������ݿ�
//		    	if(flag.equals("3"))
//		    	{
//		    		//��flag����3ʱ��˵�����ڵ���Ϣ�������صģ���Ҫ��������
//		    		//�������������γ���Ϣ������������
//		    		analysis();
//		    	}
				 str = new kb_opreation();
				 webView = (WebView)findViewById(R.id.webView);
			     webView.getSettings().setJavaScriptEnabled(true);
			     webView.getSettings().setSupportZoom(true);
			     webView.getSettings().setBuiltInZoomControls(true);
			     //����javaScript����
			     //window.open()
			     webView.addJavascriptInterface(lesson_table.this, "contact");
			     query_all1(username+yat_part);
				break;
			}
			default:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("������ʾ").setMessage("�����ڲ�����,�����������").setCancelable(false)
				.setPositiveButton("�ر�", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).show();
				break;
			}
		}		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(group1,1,1,"��½����");
    	menu.add(group1,2,2,"����α�");
    	menu.add(group1,3,3,"�л���ʽ");
    	//menu.add(group1,4,4,"��������");
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
	    		   SaveUserInfo = new DBHelper_SaveUserInfo(lesson_table.this);
    			   SaveUserInfo.del_userinfo_item(username);
	    		   Intent intent = new Intent(lesson_table.this,login_table.class);
	    		   login_table.check=false;
	    		   File file = new File("/data/data/"+this.getPackageName()+ "/shared_prefs/happyforever.xml");
				   file.delete();
				   Bundle bundle = new Bundle();
			       bundle.putString("button", "lesson");
			       intent.putExtra("bundle", bundle);
			       SaveUserInfo.close();
	    		   startActivity(intent);
	    		   finish();
	    		   break;
	    	   }	
	    	   case 2:
	    	   {
	    		   //�����ж�SD���Ƿ����
	    		   if(Environment.getExternalStorageDirectory().exists())
	    		   {
	    			   //ProgressDialog progressDialog = new ProgressDialog(lesson_table.this);
	    			   //progressDialog.show(lesson_table.this, "��ʾ", "���ڱ���\n���Ե�...");    			   
	    			   //����Ѿ���������ݣ��Ͳ��ڴ���
						if(str.isDone()==false)
						{
							//������Ϣ
							AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
							builder.setTitle("��ʾ").setMessage("�����л�Ϊ�α��ʽ��").setCancelable(false)
							.setPositiveButton("�ر�", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							}).show();
						}
						else
						{
							//������ֱ�ӱ���
							//���Ŀ¼
							try 
							{
								str.getKB_1();
					            
					            AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
								builder.setTitle("��ʾ").setMessage("�ļ��ѱ�����:\nsdcard/�γ̱�.xls").setCancelable(false)
								.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										dialog.cancel();
									}
								}).show();
								
							}
							catch (Exception e2) 
							{
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}							
						}
	    		   }
	    		   else
	    		   {
	    			   //���SD�����ڣ���ʾ�û�
	    			   AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("������ʾ").setMessage("������ڴ濨").setCancelable(false)
						.setPositiveButton("�ر�", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						}).show();
	    		   }
	    		   break;
	    	   }
	    	   case 3:
	    	   {
	    		   //�ı���沼��
	    		   SQLiteDatabase db = openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);
	    			Cursor c = db.rawQuery("SELECT * FROM layouttable", null);
	    			 while (c.moveToNext()) 
	    			 {  	 
	    				 if(c.getString(c.getColumnIndex("actname")).equals("lessontable"))
	    				 {	    	
	    					 if(c.getString(c.getColumnIndex("layout")).equals("listmod"))
	    					 {
	    						 ContentValues values = new ContentValues();
		    					 values.put("actname", "lessontable");
		    					 values.put("layout", "lessonmod");
		    					 db.update("layouttable", values, null, null);
	    						 break;
	    					 }
	    					 else
	    					 {
	    						 ContentValues values = new ContentValues();
		    					 values.put("actname", "lessontable");
		    					 values.put("layout", "listmod");
	    						 db.update("layouttable", values, null, null);
	    						 break;
	    					 }
	    					 
	    				 }
	    		     }  
	    			 c.close();
	    			 db.close();
	    			 AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("�����ʾ").setMessage("�¸�ʽ��������Ч").setCancelable(false)
						.setPositiveButton("�ر�", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
							}
						}).show();
	    		   break;
	    	   }
	    	   case 4:
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
	//��ѯ�����ʽ
	public int init()
	{
		String mod = "null";
		SQLiteDatabase db = openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM layouttable", null);
		 while (c.moveToNext()) 
		 {  	 
			 if(c.getString(c.getColumnIndex("actname")).equals("lessontable"))
			 {
			     mod = c.getString(c.getColumnIndex("layout"));;
				 break;
			 }
	     }  
		 c.close();
		 if(mod.equals("null"))
		 {
			 //Ĭ��ģʽlist_mod
			 ContentValues cv = new ContentValues();  
			 cv.put("actname", "lessontable");  
			 cv.put("layout", "listmod");  
			 //����ContentValues�е�����  
			 db.insert("layouttable", null, cv);
			 db.close();
			 return 0;
		 }
		 else
		 {
			 if(mod.equals("lessonmod"))
			 {
				 db.close();
				 return 2;
			 }
			 if(mod.equals("listmod"))
			 {
				 db.close();
				 return 1;
			 }
		 }
		 db.close();
		return -1;
		 
	}
	
	//�����ݿ���ȡ��Ӧ�����ݽ���ListView���أ����б��ʽ��ѯ
	public List<Map<String ,Object>> query_all(String keyname_getName)
	{
	    String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh , qzz;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		List<Map<String ,Object>> ListItem = new ArrayList<Map<String ,Object>>();
		Map<String,Object> map;
		TabName tan = new  TabName();
		//getName()�����ṩ����
		//ʹ��TabMane��ķ�����ϳ������ı���
		Cursor c = db.rawQuery("SELECT * FROM "+tan.getTableName(keyname_getName, "les" , "")+" ORDER BY k1 asc", null);
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
	         map.put("s1", s1);
	         map.put("k1", k1);
	         map.put("qzz", qzz);
	         ListItem.add(map);
	     }  
		 c.close();
		 db.close();
		return ListItem;
	}
	//�α��ʽ��ѯ
	public void query_all1(String keyname_getName)
	{
		String lesson_time = "" , lesson_other = "" , lesson_address = "";
		String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh , qzz;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		TabName tan = new  TabName();
		ArrayList al = new ArrayList();
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
			 lesson_time = k1;
			 lesson_other = "����"+kcmc+"����������:"+qzz+"�������:"
					 +xkbh;
			 lesson_address = s1;
			 str.setKB(lesson_time , lesson_other , lesson_address);
			 //���ִ���ʽ
			 //al.add(item);
			 //al.add(item1);
			 //Toast.makeText(lesson_table.this, item, Toast.LENGTH_SHORT).show();
	     }  
		 c.close();
		 db.close();
		 /*for(int i=0;i<al.size();i+=2)
		 {
			 str.setKB(al.get(i).toString() , al.get(i+1).toString());
		 }*/
		 
		 mess = mHandler.obtainMessage();
		 mess.what=0;
         mHandler.sendMessage(mess);
	}
	public void showKB()
    {
		kb_body = str.getKB();
    	webView.loadUrl("javascript:show(\""+kb_body+"\")");
    }
	/***********************����********************************/
	public void analysis()
	{
		Lesson lesson = new Lesson(lesson_table.this , username , yat_part);
		Score score = new Score(lesson_table.this , username , yat_part , yat);
		//��ʼ��������α�ɼ���Ϣ
		if(lesson.start()&&score.start())
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
	public void update(String update_table_name_les , String update_table_name_sco , String username , String password , String yat)
	{
		if(!getInternet())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
    		builder.setMessage("����������").setCancelable(false)
    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show();
        	return;
		}
		progressDialog = ProgressDialog.show(lesson_table.this, "ע��", "���ڸ���", true, false);
		mess = mHandler.obtainMessage();
		//ʹ�����ط������пα�ɼ�������
        download_info = new Download_info(username
        		, password , yat);
        lesson = new Lesson(lesson_table.this , username , yat_part);
        //�����µ��߳�����������
		new Thread(new Runnable() 
        {  		                    
            public void run()  
            {             	
                //��ʼ����
                if(download_info.getKB())
                {
                	//���سɹ�
                	//�жϽ�ɫ����������Ϣ��UI���ı�UI�����û�(���֮��ʵ��)
                	mess.what=0;
                	//�ɹ�����֮��resutl�л����ѧ�����߽�ʦ�ֶΣ���֮û��
         		    if(download_info.result.contains("ѧ��")||download_info.result.contains("�ον�ʦ"))
         			{
         				result = download_info.result;
         				admin = download_info.getAdmin();
         				SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
         		        db.execSQL("drop table if exists "+lesson_table.this.update_table_name_les);
         		        //db.execSQL("drop table if exists "+lesson_table.this.update_table_name_sco);
         		        db.close();        		        
         				//��ʼ��������α�ɼ���Ϣ
         				if(lesson.start())
         				{
         					//Ϊ�������Ա�����������ƶ����򣺶��߱���ͬʱ�ɹ���������Ϊ�������ɾ����Ӧ���ļ�(��Lesson��Score���е�˵��)
         					//�ɹ�ʲô������
         				}
         				else
         				{
         					//����һ������ʧ�ܣ����߶�ʧ�ܣ�������ʾ�������û�����������
         					AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
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
                	AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
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
		            case 0:
		            {
		            	webView.loadUrl("file:///android_asset/kb.html");
		            	break;
		            }
	            	case 1:
	            	{	            		
	            		progressDialog.dismiss();
	            		Toast.makeText(lesson_table.this, "���³ɹ�", 100).show();
	            		break;
	            	}
	            	case 2:
	            	{ 		
	            		Toast.makeText(lesson_table.this, "����ʧ��", 100).show();
	            		progressDialog.dismiss();
	            		break;
	            	}
	            	
	            }
	        }
	};	
}
