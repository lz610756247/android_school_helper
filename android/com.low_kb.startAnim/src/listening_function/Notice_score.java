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
	//定义变量
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
		Log.e("Notice_score--", "成绩推送开始onStart");
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
		Log.e("Notice_score--", "成绩推送开始onStartCommand");
        return super.onStartCommand(intent, flags, startId);  
    }  
	
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.e("Notice_score--", "开始更新成绩onHandleIntent");
		
	}
	
	
	//数据库提取与网络对比
	public String get_score_table_name()
	{
		
		//在这里读入需要查询的成绩表名
		user_info_db = openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);
		/*****取得成绩表名******/
		Cursor c = user_info_db.rawQuery("SELECT * FROM userinfo ", null);
		if(!c.moveToNext())
		{
			//如果整张表是空的，就停止推送
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
		/*****取得成绩表名******/
		return table_name;	
	}
	
	//成绩联网查询
	public void get_score_state_update() throws XmlPullParserException, IOException
	{
		get_score_table_name();
		user_data_info_db = openOrCreateDatabase("userdatainfo.db", Context.MODE_PRIVATE, null);
		/*****取得成绩,只需要更新成绩为0的分数******/
		Cursor c = user_data_info_db.rawQuery("SELECT * FROM "+table_name, null);
		ArrayList score_xkbh = new ArrayList();
		if(!c.moveToNext())
		{
			//如果整张表是空的，那么直接下载成绩来更新
			if(update.update_all_score())
			{
				Log.i("update_down_score--3", "全部成绩更新成功");
				this.stopSelf();
				Log.i("update_down_score--8", "更新成功，退出服务");
			}
			else
			{
				Log.i("update_down_score--4", "全部成绩更新失败");
				this.stopSelf();
			}
		}
		else
		{
			//如果表不为空在进行推送
			c = user_data_info_db.rawQuery("SELECT xkbh FROM "+table_name+" where cj = '0' ", null);
			while (c.moveToNext()) 
			{  	 
				score_xkbh.add(c.getString(c.getColumnIndex("xkbh")));
		    }
			//使用webservice进行判断
			score_xkbh = ws.update_score(score_xkbh);
			//经过上面一步，最后只剩下老师已经提交成绩的(刚刚提交)课程了
			if(score_xkbh.size()!=0)
			{
				//如果成绩有变动，先下载成绩信息，在更新成绩
				if(update.update_item_score(score_xkbh))
				{
					Log.i("update_down_score--6", "成绩推送成功");
					this.stopSelf();
				}
				else
				{
					Log.i("update_down_score--7", "成绩推送失败");
					this.stopSelf();
				}
				Log.i("update_down_score--5", "成绩更新");
			}
			else
			{
				//如果成绩都没有更新，就停止服务
				Log.i("update_down_score--0", "成绩都没有更新");
				this.stopSelf();
			}
		}		  
		c.close();
		user_info_db.close();
		/*****取得成绩表名******/
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
