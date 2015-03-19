package com.SQLite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Xml;

public class Score {

	Context context=null;
	String username="" , yat_shortAndpart="" , yat_longAndperfect="" , tablename="";
	TabName tan = null;
	public Score(Context context , String username , String yat_shortAndpart , String yat_longAndperfect)
	{
		//short为短格式201120131
		//long为长格式2012-2013学年第一学期
		this.context = context;
		this.username = username;
		this.yat_shortAndpart = yat_shortAndpart;
		this.yat_longAndperfect = yat_longAndperfect;
		tan = new  TabName();
		tablename = tan.getTableName(username, "sco" , yat_shortAndpart);
	}
	public boolean start()
	{
		//找到下载信息，随后进行解析
		File sco_file = new File("/data/data/com.low_kb/","score.txt");
		//如果文件存在就进行解析，解析的方法使用动态建立表格，如果重复建相同的表格，程序就停止运行
		if(sco_file.exists())
		{
			if(this.getSCO())
			{
				//无论是否编译成功，都删除下载个TXT文本，防止意外错误
				sco_file.delete();
				return true;
			}
			else
			{
				//编译失败时，如果建立的错误的表格，将其删除，防止数据库重复建表错误
				SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		    	db.execSQL("DROP TABLE IF EXISTS "+tablename);
				sco_file.delete();
				db.close();
				return false;
			}
		}
		return true;
	}
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
    	TabName tan = new  TabName();	    	
    	String CREATE_TABLE = "create table "+tablename+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
    			+XKBH+" text not null, "+KCMC+" text not null , "+KKJS+" text not null, "+CJ+" DEFAULT '0', "
    			+XH+" text not null, "+XM+" text not null, "+PSCJ+" DEFAULT '0', "+QZCJ+" DEFAULT '0', "
    			+QMCJ+" DEFAULT '0', "+X+" text not null "+")";
    	SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
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
    	SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/", "score.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
        TabName tan = new  TabName();
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
                		db.insert(tablename, null, values);
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
