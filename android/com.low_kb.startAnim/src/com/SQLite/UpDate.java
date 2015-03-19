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
	//������ʹ�ã��ȵ���login(),�ٵ�����������
	String username = "" , yat_shortAndpart = "" , yat_longAndperfect = "" , password = "";
	SQLiteDatabase db = null;
	IntentService content = null;
	VonService vonservice = null;
	HeaderProperty header = null;
	public UpDate(String username , String password , String yat , IntentService content)
	{
		//shortΪ�̸�ʽ201120131
		//longΪ����ʽ2012-2013ѧ���һѧ��
		this.username = username;
		this.password = password;
		this.yat_shortAndpart = yat;		
		this.content = content;
		if(db==null)
		{
			db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		}
	}
	
	
	//��½�û�
	public void login()
	{
		vonservice = new VonService(username,  password);
		header = vonservice.lgoin();
	}
	
	
	//���سɼ�
	public boolean Download_score()
	{
		this.login();
		String score = vonservice.getStudentScoreCard(header);
		file_sr sco_file = new file_sr();
		sco_file.save("score", score);	
		return true;
	}
	
	
	//�������гɼ�
	public boolean update_all_score()
	{
		if(!Download_score())
		{
			Log.i("update_down_score", "ʧ��");
			content.stopSelf();
		}
		this.yat_longAndperfect = Change_yat(this.yat_shortAndpart);
		db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		//������ǰ�ı�����½�������������
    	db.execSQL("DROP TABLE IF EXISTS "+this.getTableName("sco"));
    	//�ҵ�������Ϣ�������н���
    	File sco_file = new File("/data/data/com.low_kb/","score.txt");
		//����ļ����ھͽ��н����������ķ���ʹ�ö�̬�����������ظ�����ͬ�ı�񣬳����ֹͣ����
		if(sco_file.exists())
		{
			if(this.getSCO())
			{
				//�����Ƿ����ɹ�����ɾ�����ظ�TXT�ı�����ֹ�������
				sco_file.delete();
				Log.i("update_down_score--1", "�ɹ�");
				return true;
			}
			else
			{
				//����ʧ��ʱ����������Ĵ���ı�񣬽���ɾ������ֹ���ݿ��ظ��������
		    	db.execSQL("DROP TABLE IF EXISTS "+this.getTableName("sco"));
				sco_file.delete();
				db.close();
				Log.i("update_down_score--2", "ʧ��");
				content.stopSelf();
				return false;
			}
		}
		return true;		
	}
	
	
	//���³ɼ�����
	public boolean update_item_score(ArrayList score_xkbh) throws XmlPullParserException, IOException
	{
		//�÷�������ֵ��true�������ұ���ɹ�
		int length = 0;
    	db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/", "score.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
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
            	
                name = parser.getName();//��ȡ��������ǰָ���Ԫ�ص�����
                if ("psc".equals(name)) 
                {
                	data.head = name;
                }               
                if ("xkbh".equals(name)) //ѡ�ΰ��
                {
            		data.xkbh=parser.nextText();//ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		if(!data.xkbh.equals(score_xkbh.get(length)))
            			continue;
                }   
                if ("kcmc".equals(name)) //�γ�����
                {
            		data.kcmc=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
                }
            	if ("cj".equals(name)) //�ܳɼ�
                {
            		data.cj=parser.nextText();// ȡ�õ�ǰԪ�ؽڵ��µ��ı��ڵ��ֵ
            		values.put("cj", data.cj);
                }            	                
                break;
            case XmlPullParser.END_TAG:// ����Ԫ���¼�
                if ("psc".equals(parser.getName()))
                {	
                	//���³ɼ�������֪ͨ����ʾ
                	/*****************************֪ͨ������*********************************/
            		//��Ϣ֪ͨ��
                    //����NotificationManager
                    NotificationManager mNotificationManager = (NotificationManager) content.getSystemService(Context.NOTIFICATION_SERVICE);
                    //����֪ͨ��չ�ֵ�������Ϣ
                    Notification notification = new Notification(R.drawable.notice_score, "���гɼ�������", System.currentTimeMillis());
                    //��������֪ͨ��ʱҪչ�ֵ�������Ϣ
                    Context context = content.getApplicationContext();
                    Intent notificationIntent = new Intent(content, Show_Down_Notice_Bar.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", length);
                    notificationIntent.putExtra("bundle", bundle);
                    PendingIntent contentIntent = PendingIntent.getActivity(content, 0,notificationIntent, 0);
                    notification.setLatestEventInfo(context, "�γ�����"+data.kcmc, "�ɼ�"+data.cj,contentIntent);  
                    //��mNotificationManager��notify����֪ͨ�û����ɱ�������Ϣ֪ͨ
                    mNotificationManager.notify(length, notification);
                    /*****************************֪ͨ������*********************************/
                	db.update(this.getTableName("sco"), values, "xkbh = ?", new String[]{data.xkbh});
                	length++;
                	values = new ContentValues();
                	data = new Data();
                }
             break;
            }      
            eventType = parser.next(); // ִ����һ���¼�
        }
        db.close();
        score_xkbh.clear();
        length = 0;
        return true;		
	}
	
	
	//ѧ��ѧ�ڵķ���ת��
	public String Change_yat(String yat)
	{
		String b = "";
		yat = yat.substring(yat.length()-9, yat.length());
		b += yat.substring(0, 4)+"-";
		b += yat.substring(4, 8)+"ѧ��";
		if(yat.substring(8).equals("1"))
		{
			b += "��һѧ��";
		}
		else
		{
			if(yat.substring(8).equals("2"))
				b += "�ڶ�ѧ��";
		}
		return b;
	}
	
	
	//��ȡ����
	public String getTableName(String mod)
	{
		mod = mod + username + yat_shortAndpart;
		return mod;
	}
	
	
	//�ɼ��Ĵ���
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
		//�÷�������ֵ��true�������ұ���ɹ�
    	db = content.openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		File file = new File("/data/data/com.low_kb/", "score.txt");		
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis);
        XmlPullParser parser = Xml.newPullParser();
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
            eventType = parser.next(); // ִ����һ���¼�
        }
        db.close();
        return true;
	}

}
