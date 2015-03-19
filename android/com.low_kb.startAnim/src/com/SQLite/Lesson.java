package com.SQLite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Xml;

public class Lesson {
	Context context=null;
	String username="" , yat="" , tablename="";
	TabName tan = null;
	public Lesson(Context context , String username , String yat)
	{
		this.context = context;
		this.username = username;
		this.yat = yat;
		//在建立新对象时，根据传入的参数建立相应的表格，具体规则看TabName类
		tan = new  TabName();
		tablename = tan.getTableName(username, "les" , yat);
	}
	public boolean start()
	{
		//找到下载信息，随后进行解析
		File les_file = new File( "/data/data/com.low_kb/","kb.txt");
		//如果文件存在就进行解析，解析的方法使用动态建立表格，如果重复建相同的表格，程序就停止运行
		if(les_file.exists())
		{
			if(this.getKB())
			{
				//无论是否编译成功，都删除下载个TXT文本，防止意外错误
				les_file.delete();
				return true;
			}
			else
			{
				//编译失败时，如果建立的错误的表格，将其删除，防止数据库重复建表错误
				SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		    	db.execSQL("DROP TABLE IF EXISTS "+tablename);
				les_file.delete();
				db.close();
				return false;
			}
		}
		return true;
	}
	private boolean getKB()  //建立课表表
    {
		 String DATABASE_NAME = "userdatainfo.db" ;
    	 int DATABASE_VERSION = 1 ;
    	 String ID = "_id" ;
    	 String USERNAME="username";
    	 String XKBH = "xkbh" ;
    	 String KCMC = "kcmc" ;
    	 String KKJS = "kkjs" ;
    	 String JSGH = "jsgh" ; 
    	 String X= "x" ;
    	 String KCLB = "kclb" ;
    	 String K1 = "k1" ;
    	 String T = "t" ;
    	 String T1 = "t1" ;
    	 String S1 = "s1" ;
    	 String QZZ = "qzz" ;

    	 String CREATE_TABLE = "create table "+tablename+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
    				+XKBH+" text not null, "+KCMC+" text not null , "+KKJS+" text not null, "+JSGH+" text not null, "
    				+X+" text not null, "+KCLB+" text not null, "+K1+" text not null, "+S1+" text not null ,"+QZZ+" text not null "+")";
    	SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
    	db.execSQL(CREATE_TABLE);
    	try {
			if(this.saveLesData())
			{
				//解析保存成功
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
    //解析课表
	private boolean saveLesData() throws XmlPullParserException, IOException
	{
		//该方法返回值，true解析并且保存成功
    	SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/" , "kb.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(fis , "UTF-8");
        int eventType = parser.getEventType();// 产生第一个事件
        String name="";
        Data data = null;  
        ContentValues values = new ContentValues();
        while (eventType != XmlPullParser.END_DOCUMENT) 
        {
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT://开始文档事件
            	data = new Data();	
                break;
            case XmlPullParser.START_TAG://开始元素事件        	
                name = parser.getName();// 获取解析器当前指向的元素的名称
                if ("kb".equals(name)) 
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
            	if ("jsgh".equals(name)) //教师工号
                {
            		data.jsgh=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("jsgh", data.jsgh);
                }
            	if ("x".equals(name)) //课程类别
                {
            		data.x=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("x", data.x);
                }
            	if ("kclb".equals(name)) //课程类别
                {
            		data.kclb=parser.nextText();// 取得当前元素节点下的文本节点的值
            		values.put("kclb", data.kclb);
                }
                if ("k1".equals(name)) //上课时间
                {
                	data.k1=parser.nextText();// 取得当前元素节点下的文本节点的值
                	values.put("k1", data.k1);
                }
                if ("s1".equals(name)) //上课地点
                {
                	data.s1=parser.nextText();// 取得当前元素节点下的文本节点的值
                	values.put("s1", data.s1);
                }
                if ("qzz".equals(name)) //上课时间
                {
                	data.qzz=parser.nextText();// 取得当前元素节点下的文本节点的值
                	values.put("qzz", data.qzz);
                }
                break;
            case XmlPullParser.END_TAG:// 结束元素事件
                if ("kb".equals(parser.getName()))
                {	
                	db.insert(tablename, null, values);
                }
             break;
            }      
            eventType = parser.next(); // 执行下一个事件
        }	
        db.close();
        return true;
	}

}
