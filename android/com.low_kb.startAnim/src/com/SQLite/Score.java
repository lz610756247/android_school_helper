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
		//shortΪ�̸�ʽ201120131
		//longΪ����ʽ2012-2013ѧ���һѧ��
		this.context = context;
		this.username = username;
		this.yat_shortAndpart = yat_shortAndpart;
		this.yat_longAndperfect = yat_longAndperfect;
		tan = new  TabName();
		tablename = tan.getTableName(username, "sco" , yat_shortAndpart);
	}
	public boolean start()
	{
		//�ҵ�������Ϣ�������н���
		File sco_file = new File("/data/data/com.low_kb/","score.txt");
		//����ļ����ھͽ��н����������ķ���ʹ�ö�̬�����������ظ�����ͬ�ı�񣬳����ֹͣ����
		if(sco_file.exists())
		{
			if(this.getSCO())
			{
				//�����Ƿ����ɹ�����ɾ�����ظ�TXT�ı�����ֹ�������
				sco_file.delete();
				return true;
			}
			else
			{
				//����ʧ��ʱ����������Ĵ���ı�񣬽���ɾ������ֹ���ݿ��ظ��������
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
		//�÷�������ֵ��true�������ұ���ɹ�
    	SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/", "score.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
        TabName tan = new  TabName();
        parser.setInput(fis , "UTF-8");
        int eventType = parser.getEventType();// ������һ���¼�
        String name="" , team = "";
        Data data = data = new Data();  
        ContentValues values = new ContentValues();
        while (eventType != XmlPullParser.END_DOCUMENT) 
        {
        	
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT://��ʼ�ĵ��¼�
            	data = new Data();	
                break;
            case XmlPullParser.START_TAG://��ʼԪ���¼�    
            	
                name = parser.getName();// ��ȡ��������ǰָ���Ԫ�ص�����
                if ("psc".equals(name)) 
                {
                	data.head = name;
                }               
                if ("xkbh".equals(name)) //ѡ�ΰ��
                {
            		data.xkbh=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("xkbh", data.xkbh);
                }
            	if ("kcmc".equals(name)) //�γ�����
                {
            		data.kcmc=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("kcmc", data.kcmc);
                }
            	if ("kkjs".equals(name)) //�γ̽�ʦ
                {
            		data.kkjs=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("kkjs", data.kkjs);
                }
            	if ("cj".equals(name)) //�ܳɼ�
                {
            		data.cj=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("cj", data.cj);
                }
            	if ("xh".equals(name)) //ѧ������
                {
            		data.xh=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("xh", data.xh);
                }
            	if ("xm".equals(name)) //ѧ������
                {
            		data.xm=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("xm", data.xm);
                }
                if ("pscj".equals(name)) //ƽʱ�ɼ�
                {
                	data.pscj=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                	values.put("pscj", data.pscj);
                }
                if ("qzcj".equals(name)) //���гɼ�
                {
                	data.qzcj=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                	values.put("qzcj", data.qzcj);
                }
                if ("qmcj".equals(name)) //��ĩ�ɼ�
                {
                	data.qmcj=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                	values.put("qmcj", data.qmcj);
                }
                if ("x".equals(name)) //�γ����
                {
            		data.x=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("x", data.x);
                }
                if ("team".equals(name)) //�γ����
                {
            		team=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                }
                break;
            case XmlPullParser.END_TAG:// ����Ԫ���¼�
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
            eventType = parser.next(); // ִ����һ���¼�
        }
        db.close();
        return true;
	}
}
