package listening_function;


import java.io.IOException;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParserException;

import com.SQLite.UpDate;
import com.low_kb.R;



import service.WebService;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Notice_score extends IntentService
{
	//�������
	int i=0;
	WebService ws = new WebService();
	SQLiteDatabase user_info_db = null;
	SQLiteDatabase user_data_info_db = null;
	UpDate update = null;
	String table_name = "";
	public Notice_score() {
		super("Notice_score");
		// TODO Auto-generated constructor stub
	}

	
	@Override  
    public void onStart(Intent intent, int startId) {  
		Log.e("Notice_score--", "�ɼ����Ϳ�ʼonStart");
        super.onStart(intent, startId); 
        try {
        	get_score_state_update();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    } 
	
	
	@Override  
    public int onStartCommand(Intent intent, int flags, int startId) {  
		Log.e("Notice_score--", "�ɼ����Ϳ�ʼonStartCommand");
        return super.onStartCommand(intent, flags, startId);  
    }  
	
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.e("Notice_score--", "��ʼ���³ɼ�onHandleIntent");
		
	}
	
	
	//���ݿ���ȡ������Ա�
	public String get_score_table_name()
	{
		
		//�����������Ҫ��ѯ�ĳɼ�����
		user_info_db = openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);
		/*****ȡ�óɼ�����******/
		Cursor c = user_info_db.rawQuery("SELECT * FROM userinfo ", null);
		if(!c.moveToNext())
		{
			//������ű��ǿյģ���ֹͣ����
			this.stopSelf();
		}
		else
		{
			c = user_info_db.rawQuery("SELECT * FROM userinfo ", null);
			while (c.moveToNext()) 
			{  	 
				update = new UpDate(c.getString(c.getColumnIndex("username")) , c.getString(c.getColumnIndex("password"))
						,c.getString(c.getColumnIndex("yat")) , this);
				table_name = update.getTableName("sco");
		    }  
		}		
		c.close();
		user_info_db.close();
		/*****ȡ�óɼ�����******/
		return table_name;	
	}
	
	//�ɼ�������ѯ
	public void get_score_state_update() throws XmlPullParserException, IOException
	{
		get_score_table_name();
		user_data_info_db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		/*****ȡ�óɼ�,ֻ��Ҫ���³ɼ�Ϊ0�ķ���******/
		Cursor c = user_data_info_db.rawQuery("SELECT * FROM "+table_name, null);
		ArrayList score_xkbh = new ArrayList();
		if(!c.moveToNext())
		{
			//������ű��ǿյģ���ôֱ�����سɼ�������
			if(update.update_all_score())
			{
				Log.i("update_down_score--3", "ȫ���ɼ����³ɹ�");
				this.stopSelf();
				Log.i("update_down_score--8", "���³ɹ����˳�����");
			}
			else
			{
				Log.i("update_down_score--4", "ȫ���ɼ�����ʧ��");
				this.stopSelf();
			}
		}
		else
		{
			//�����Ϊ���ڽ�������
			c = user_data_info_db.rawQuery("SELECT xkbh FROM "+table_name+" where cj = '0' ", null);
			while (c.moveToNext()) 
			{  	 
				score_xkbh.add(c.getString(c.getColumnIndex("xkbh")));
		    }
			//ʹ��webservice�����ж�
			score_xkbh = ws.update_score(score_xkbh);
			//��������һ�������ֻʣ����ʦ�Ѿ��ύ�ɼ���(�ո��ύ)�γ���
			if(score_xkbh.size()!=0)
			{
				//����ɼ��б䶯�������سɼ���Ϣ���ڸ��³ɼ�
				if(update.update_item_score(score_xkbh))
				{
					Log.i("update_down_score--6", "�ɼ����ͳɹ�");
					this.stopSelf();
				}
				else
				{
					Log.i("update_down_score--7", "�ɼ�����ʧ��");
					this.stopSelf();
				}
				Log.i("update_down_score--5", "�ɼ�����");
			}
			else
			{
				//����ɼ���û�и��£���ֹͣ����
				Log.i("update_down_score--0", "�ɼ���û�и���");
				this.stopSelf();
			}
		}		  
		c.close();
		user_info_db.close();
		/*****ȡ�óɼ�����******/
	}


	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub		
		super.onDestroy();
		user_info_db.close();
		user_data_info_db.close();
		System.exit(0);
	}

}
