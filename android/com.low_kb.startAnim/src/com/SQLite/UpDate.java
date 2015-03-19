package com.SQLite;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import listening_function.Show_Down_Notice_Bar;


import org.ksoap2.HeaderProperty;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.low_kb.R;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import service.VonService;
import service.file_sr;

public class UpDate
{
	//这个类的使用：先调用login(),再调用其他方法
	String username = "" , yat_shortAndpart = "" , yat_longAndperfect = "" , password = "";
	SQLiteDatabase db = null;
	IntentService content = null;
	VonService vonservice = null;
	HeaderProperty header = null;
	public UpDate(String username , String password , String yat , IntentService content)
	{
		//short为短格式201120131
		//long为长格式2012-2013学年第一学期
		this.username = username;
		this.password = password;
		this.yat_shortAndpart = yat;		
		this.content = content;
		if(db==null)
		{
			db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		}
	}
	
	
	//登陆用户
	public void login()
	{
		vonservice = new VonService(username,  password);
		header = vonservice.lgoin();
	}
	
	
	//下载成绩
	public boolean Download_score()
	{
		this.login();
		String score = vonservice.getStudentScoreCard(header);
		file_sr sco_file = new file_sr();
		sco_file.save("score", score);	
		return true;
	}
	
	
	//更新所有成绩
	public boolean update_all_score()
	{
		if(!Download_score())
		{
			Log.i("update_down_score", "失败");
			content.stopSelf();
		}
		this.yat_longAndperfect = Change_yat(this.yat_shortAndpart);
		db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		//放弃以前的表格，重新解析并插入数据
    	db.execSQL("DROP TABLE IF EXISTS "+this.getTableName("sco"));
    	//找到下载信息，随后进行解析
    	File sco_file = new File("/data/data/com.low_kb/","score.txt");
		//如果文件存在就进行解析，解析的方法使用动态建立表格，如果重复建相同的表格，程序就停止运行
		if(sco_file.exists())
		{
			if(this.getSCO())
			{
				//无论是否编译成功，都删除下载个TXT文本，防止意外错误
				sco_file.delete();
				Log.i("update_down_score--1", "成功");
				return true;
			}
			else
			{
				//编译失败时，如果建立的错误的表格，将其删除，防止数据库重复建表错误
		    	db.execSQL("DROP TABLE IF EXISTS "+this.getTableName("sco"));
				sco_file.delete();
				db.close();
				Log.i("update_down_score--2", "失败");
				content.stopSelf();
				return false;
			}
		}
		return true;		
	}
	
	
	//更新成绩推送
	public boolean update_item_score(ArrayList score_xkbh) throws XmlPullParserException, IOException
	{
		//该方法返回值，true解析并且保存成功
		int length = 0;
    	db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/", "score.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(fis , "UTF-8");
        int eventType = parser.getEventType();// 产生第一个事件
        String name="" , team = "";
        Data data = data = new Data();  
        ContentValues values = new ContentValues();
        while (eventType != XmlPullParser.END_DOCUMENT) 
        {
        	
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT://开始文档事件
            	data = new Data();	
                break;
            case XmlPullParser.START_TAG://开始元素事件    
            	
                name = parser.getName();//获取解析器当前指向的元素的名称
                if ("psc".equals(name)) 
                {
                	data.head = name;
                }               
                if ("xkbh".equals(name)) //选课班号
                {
            		data.xkbh=parser.nextText();//取得当前元素节点下的文本节点的值
            		if(!data.xkbh.equals(score_xkbh.get(length)))
            			continue;
                }   
                if ("kcmc".equals(name)) //课程名称
                {
            		data.kcmc=parser.nextText();// 取得当前元素节点下的文本节点的值
                }
            	if ("cj".equals(name)) //总成绩
                {
            		data.cj=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("cj", data.cj);
                }            	                
                break;
            case XmlPullParser.END_TAG:// 结束元素事件
                if ("psc".equals(parser.getName()))
                {	
                	//更新成绩并且在通知栏提示
                	/*****************************通知栏设置*********************************/
            		//消息通知栏
                    //定义NotificationManager
                    NotificationManager mNotificationManager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                    //定义通知栏展现的内容信息
                    Notification notification = new Notification(R.drawable.notice_score, "你有成绩更新啦", System.currentTimeMillis());
                    //定义下拉通知栏时要展现的内容信息
                    Context context = content.getApplicationContext();
                    Intent notificationIntent = new Intent(content, Show_Down_Notice_Bar.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", length);
                    notificationIntent.putExtra("bundle", bundle);
                    PendingIntent contentIntent = PendingIntent.getActivity(content, 0,notificationIntent, 0);
                    notification.setLatestEventInfo(context, "课程名称"+data.kcmc, "成绩"+data.cj,contentIntent);  
                    //用mNotificationManager的notify方法通知用户生成标题栏消息通知
                    mNotificationManager.notify(length, notification);
                    /*****************************通知栏设置*********************************/
                	db.update(this.getTableName("sco"), values, "xkbh = ?", new String[]{data.xkbh});
                	length++;
                	values = new ContentValues();
                	data = new Data();
                }
             break;
            }      
            eventType = parser.next(); // 执行下一个事件
        }
        db.close();
        score_xkbh.clear();
        length = 0;
        return true;		
	}
	
	
	//学年学期的反向转换
	public String Change_yat(String yat)
	{
		String b = "";
		yat = yat.substring(yat.length()-9, yat.length());
		b += yat.substring(0, 4)+"-";
		b += yat.substring(4, 8)+"学年";
		if(yat.substring(8).equals("1"))
		{
			b += "第一学期";
		}
		else
		{
			if(yat.substring(8).equals("2"))
				b += "第二学期";
		}
		return b;
	}
	
	
	//获取表名
	public String getTableName(String mod)
	{
		mod = mod + username + yat_shortAndpart;
		return mod;
	}
	
	
	//成绩的处理
	private boolean getSCO()
    {
    	String DATABASE_NAME = "userdatainfo.db" ;
    	int DATABASE_VERSION = 1 ;
    	String ID = "_id" ;
    	String USERNAME="username";
    	String XKBH = "xkbh" ;
    	String KCMC = "kcmc" ;
    	String KKJS = "kkjs" ; 
    	String X= "x" ;
    	String XM= "xm" ;
    	String XH= "xh" ;
    	String PSCJ= "pscj" ;
    	String QZCJ= "qzcj" ;
    	String QMCJ= "qmcj" ;
    	String CJ= "cj" ;
    	String KCLB = "kclb" ;		    	
    	String CREATE_TABLE = "create table "+this.getTableName("sco")+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
    			+XKBH+" text not null, "+KCMC+" text not null , "+KKJS+" text not null, "+CJ+" DEFAULT '0', "
    			+XH+" text not null, "+XM+" text not null, "+PSCJ+" DEFAULT '0', "+QZCJ+" DEFAULT '0', "
    			+QMCJ+" DEFAULT '0', "+X+" text not null "+")";
    	db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
    	db.execSQL(CREATE_TABLE);
    	try {
			if(saveScoData())
			{
				return true;
			}
			else
			{
				return false;
			}
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	db.close();
    	return true;
    }
    
	private boolean saveScoData() throws XmlPullParserException, IOException
	{
		//该方法返回值，true解析并且保存成功
    	db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/", "score.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(fis , "UTF-8");
        int eventType = parser.getEventType();// 产生第一个事件
        String name="" , team = "";
        Data data = data = new Data();  
        ContentValues values = new ContentValues();
        while (eventType != XmlPullParser.END_DOCUMENT) 
        {
        	
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT://开始文档事件
            	data = new Data();	
                break;
            case XmlPullParser.START_TAG://开始元素事件    
            	
                name = parser.getName();// 获取解析器当前指向的元素的名称
                if ("psc".equals(name)) 
                {
                	data.head = name;
                }               
                if ("xkbh".equals(name)) //选课班号
                {
            		data.xkbh=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("xkbh", data.xkbh);
                }
            	if ("kcmc".equals(name)) //课程名称
                {
            		data.kcmc=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("kcmc", data.kcmc);
                }
            	if ("kkjs".equals(name)) //课程教师
                {
            		data.kkjs=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("kkjs", data.kkjs);
                }
            	if ("cj".equals(name)) //总成绩
                {
            		data.cj=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("cj", data.cj);
                }
            	if ("xh".equals(name)) //学生姓名
                {
            		data.xh=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("xh", data.xh);
                }
            	if ("xm".equals(name)) //学生姓名
                {
            		data.xm=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("xm", data.xm);
                }
                if ("pscj".equals(name)) //平时成绩
                {
                	data.pscj=parser.nextText();// 取得当前元素节点下的文本节点的值
                	values.put("pscj", data.pscj);
                }
                if ("qzcj".equals(name)) //期中成绩
                {
                	data.qzcj=parser.nextText();// 取得当前元素节点下的文本节点的值
                	values.put("qzcj", data.qzcj);
                }
                if ("qmcj".equals(name)) //期末成绩
                {
                	data.qmcj=parser.nextText();// 取得当前元素节点下的文本节点的值
                	values.put("qmcj", data.qmcj);
                }
                if ("x".equals(name)) //课程类别
                {
            		data.x=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("x", data.x);
                }
                if ("team".equals(name)) //课程年份
                {
            		team=parser.nextText();// 取得当前元素节点下的文本节点的值
                }
                break;
            case XmlPullParser.END_TAG:// 结束元素事件
                if ("psc".equals(parser.getName()))
                {	
                	if(team.equals(yat_longAndperfect))
                	{
                		db.insert(this.getTableName("sco"), null, values);
                    	values = new ContentValues();
                    	data = new Data();
                	}
                	else
                	{
                		//do nothing
                		values = new ContentValues();
                		data = new Data();
                	}
                }
             break;
            }      
            eventType = parser.next(); // 执行下一个事件
        }
        db.close();
        return true;
	}

}
