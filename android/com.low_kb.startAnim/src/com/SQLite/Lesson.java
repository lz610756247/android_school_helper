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
		//�ڽ����¶���ʱ�����ݴ���Ĳ���������Ӧ�ı�񣬾������TabName��
		tan = new  TabName();
		tablename = tan.getTableName(username, "les" , yat);
	}
	public boolean start()
	{
		//�ҵ�������Ϣ�������н���
		File les_file = new File( "/data/data/com.low_kb/","kb.txt");
		//����ļ����ھͽ��н����������ķ���ʹ�ö�̬�����������ظ�����ͬ�ı�񣬳����ֹͣ����
		if(les_file.exists())
		{
			if(this.getKB())
			{
				//�����Ƿ����ɹ�����ɾ�����ظ�TXT�ı�����ֹ�������
				les_file.delete();
				return true;
			}
			else
			{
				//����ʧ��ʱ����������Ĵ���ı�񣬽���ɾ������ֹ���ݿ��ظ��������
				SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		    	db.execSQL("DROP TABLE IF EXISTS "+tablename);
				les_file.delete();
				db.close();
				return false;
			}
		}
		return true;
	}
	private boolean getKB()  //�����α��
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
				//��������ɹ�
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
    //�����α�
	private boolean saveLesData() throws XmlPullParserException, IOException
	{
		//�÷�������ֵ��true�������ұ���ɹ�
    	SQLiteDatabase db = context.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/" , "kb.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(fis , "UTF-8");
        int eventType = parser.getEventType();// ������һ���¼�
        String name="";
        Data data = null;  
        ContentValues values = new ContentValues();
        while (eventType != XmlPullParser.END_DOCUMENT) 
        {
            switch (eventType) {
            case XmlPullParser.START_DOCUMENT://��ʼ�ĵ��¼�
            	data = new Data();	
                break;
            case XmlPullParser.START_TAG://��ʼԪ���¼�        	
                name = parser.getName();// ��ȡ��������ǰָ���Ԫ�ص�����
                if ("kb".equals(name)) 
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
            	if ("jsgh".equals(name)) //��ʦ����
                {
            		data.jsgh=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("jsgh", data.jsgh);
                }
            	if ("x".equals(name)) //�γ����
                {
            		data.x=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("x", data.x);
                }
            	if ("kclb".equals(name)) //�γ����
                {
            		data.kclb=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("kclb", data.kclb);
                }
                if ("k1".equals(name)) //�Ͽ�ʱ��
                {
                	data.k1=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                	values.put("k1", data.k1);
                }
                if ("s1".equals(name)) //�Ͽεص�
                {
                	data.s1=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                	values.put("s1", data.s1);
                }
                if ("qzz".equals(name)) //�Ͽ�ʱ��
                {
                	data.qzz=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                	values.put("qzz", data.qzz);
                }
                break;
            case XmlPullParser.END_TAG:// ����Ԫ���¼�
                if ("kb".equals(parser.getName()))
                {	
                	db.insert(tablename, null, values);
                }
             break;
            }      
            eventType = parser.next(); // ִ����һ���¼�
        }	
        db.close();
        return true;
	}

}
