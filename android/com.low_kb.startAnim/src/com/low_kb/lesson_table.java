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
		//加载界面信息,查看需要加载哪个，1是列表格式,2是课表格式
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
		    	str = new kb_opreation();
//		    	//判断是否需要解析数据并且保存进入数据库
//		    	if(flag.equals("3"))
//		    	{
//		    		//当flag等于3时，说明现在的信息是新下载的，需要解析保存
//		    		//这个类用来处理课程信息，解析，保存
//		    		analysis();
//		    	}
				lesson_listview = (ListView) findViewById(R.id.lessons_listview);
				lesson_listview.setCacheColorHint(Color.TRANSPARENT);
				//课表的读取     
			    List<Map<String ,Object>> ListItem;
		        //处理课表信息
		        ListItem = query_all(username+yat_part);   
		        //加载课表列表
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
//		    	//判断是否需要解析数据并且保存进入数据库
//		    	if(flag.equals("3"))
//		    	{
//		    		//当flag等于3时，说明现在的信息是新下载的，需要解析保存
//		    		//这个类用来处理课程信息，解析，保存
//		    		analysis();
//		    	}
				 str = new kb_opreation();
				 webView = (WebView)findViewById(R.id.webView);
			     webView.getSettings().setJavaScriptEnabled(true);
			     webView.getSettings().setSupportZoom(true);
			     webView.getSettings().setBuiltInZoomControls(true);
			     //设置javaScript可用
			     //window.open()
			     webView.addJavascriptInterface(lesson_table.this, "contact");
			     query_all1(username+yat_part);
				break;
			}
			default:
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("错误提示").setMessage("程序内部出错,请重启软件。").setCancelable(false)
				.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
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
    	menu.add(group1,1,1,"登陆界面");
    	menu.add(group1,2,2,"输出课表");
    	menu.add(group1,3,3,"切换格式");
    	//menu.add(group1,4,4,"更新数据");
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
	    		   //首先判断SD卡是否存在
	    		   if(Environment.getExternalStorageDirectory().exists())
	    		   {
	    			   //ProgressDialog progressDialog = new ProgressDialog(lesson_table.this);
	    			   //progressDialog.show(lesson_table.this, "提示", "正在保存\n请稍等...");    			   
	    			   //如果已经处理过数据，就不在处理
						if(str.isDone()==false)
						{
							//处理信息
							AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
							builder.setTitle("提示").setMessage("请先切换为课表格式。").setCancelable(false)
							.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							}).show();
						}
						else
						{
							//不处理，直接保存
							//输出目录
							try 
							{
								str.getKB_1();
					            
					            AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
								builder.setTitle("提示").setMessage("文件已保存在:\nsdcard/课程表.xls").setCancelable(false)
								.setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
	    			   //如果SD不存在，提示用户
	    			   AlertDialog.Builder builder = new AlertDialog.Builder(this);
						builder.setTitle("错误提示").setMessage("请插入内存卡").setCancelable(false)
						.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
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
	    		   //改变界面布局
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
						builder.setTitle("变更提示").setMessage("新格式重启后生效").setCancelable(false)
						.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
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
	//查询界面格式
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
			 //默认模式list_mod
			 ContentValues cv = new ContentValues();  
			 cv.put("actname", "lessontable");  
			 cv.put("layout", "listmod");  
			 //插入ContentValues中的数据  
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
	
	//从数据库提取相应的数据进行ListView加载，，列表格式查询
	public List<Map<String ,Object>> query_all(String keyname_getName)
	{
	    String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh , qzz;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		List<Map<String ,Object>> ListItem = new ArrayList<Map<String ,Object>>();
		Map<String,Object> map;
		TabName tan = new  TabName();
		//getName()方法提供表名
		//使用TabMane类的方法组合出完整的表名
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
	//课表格式查询
	public void query_all1(String keyname_getName)
	{
		String lesson_time = "" , lesson_other = "" , lesson_address = "";
		String head , foot;
		String xkbh , kcmc , kkjs , jsgh , kclb , k1 , s1 , x , xm , pscj , qzcj , qmcj , cj , xh , qzz;
		SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		TabName tan = new  TabName();
		ArrayList al = new ArrayList();
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
			 lesson_time = k1;
			 lesson_other = "——"+kcmc+"——开课周:"+qzz+"——班号:"
					 +xkbh;
			 lesson_address = s1;
			 str.setKB(lesson_time , lesson_other , lesson_address);
			 //两种处理方式
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
	/***********************解析********************************/
	public void analysis()
	{
		Lesson lesson = new Lesson(lesson_table.this , username , yat_part);
		Score score = new Score(lesson_table.this , username , yat_part , yat);
		//开始解析保存课表成绩信息
		if(lesson.start()&&score.start())
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
	public void update(String update_table_name_les , String update_table_name_sco , String username , String password , String yat)
	{
		if(!getInternet())
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
    		builder.setMessage("无网络连接").setCancelable(false)
    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {		
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			}).show();
        	return;
		}
		progressDialog = ProgressDialog.show(lesson_table.this, "注意", "正在更新", true, false);
		mess = mHandler.obtainMessage();
		//使用下载方法进行课表成绩的下载
        download_info = new Download_info(username
        		, password , yat);
        lesson = new Lesson(lesson_table.this , username , yat_part);
        //启用新的线程下载新数据
		new Thread(new Runnable() 
        {  		                    
            public void run()  
            {             	
                //开始下载
                if(download_info.getKB())
                {
                	//下载成功
                	//判断角色，并发送消息给UI，改变UI提醒用户(这个之后实现)
                	mess.what=0;
                	//成功下载之后，resutl中会包含学生或者教师字段，反之没有
         		    if(download_info.result.contains("学生")||download_info.result.contains("任课教师"))
         			{
         				result = download_info.result;
         				admin = download_info.getAdmin();
         				SQLiteDatabase db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
         		        db.execSQL("drop table if exists "+lesson_table.this.update_table_name_les);
         		        //db.execSQL("drop table if exists "+lesson_table.this.update_table_name_sco);
         		        db.close();        		        
         				//开始解析保存课表成绩信息
         				if(lesson.start())
         				{
         					//为方便程序员操作，这里制定规则：二者必须同时成功，否则视为编译错误，删除相应的文件(见Lesson和Score类中的说明)
         					//成功什么都不做
         				}
         				else
         				{
         					//其中一个编译失败，或者都失败，跳出提示框，提醒用户，重新下载
         					AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
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
                	AlertDialog.Builder builder = new AlertDialog.Builder(lesson_table.this);
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
		            case 0:
		            {
		            	webView.loadUrl("file:///android_asset/kb.html");
		            	break;
		            }
	            	case 1:
	            	{	            		
	            		progressDialog.dismiss();
	            		Toast.makeText(lesson_table.this, "更新成功", 100).show();
	            		break;
	            	}
	            	case 2:
	            	{ 		
	            		Toast.makeText(lesson_table.this, "更新失败", 100).show();
	            		progressDialog.dismiss();
	            		break;
	            	}
	            	
	            }
	        }
	};	
}
