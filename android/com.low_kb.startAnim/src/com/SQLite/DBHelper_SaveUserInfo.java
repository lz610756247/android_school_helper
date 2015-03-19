package com.SQLite;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper_SaveUserInfo 
{
	
	private static final String DATABASE_NAME = "userinfo.db" ;
	private static final int DATABASE_VERSION = 1 ;
	//userinfo���ݿ����������Ҫ��ס������û�
	private static final String TABLE_NAME = "userinfo" ;
	//haskogin���ݿ�����������е��û���Ϣ
	private static final String HASKOGIN = "haslogin" ;
	//adress_table��������û�����ĵ�ַ����
	private static final String ADRESS_TABLE = "adresstable" ;
	
	//chat���ݿ�������������¼
	private static final String CHAT = "chattable" ;
	private static final String XKBH = "xkbh" ;
	private static final String NAME = "name" ;
	private static final String MSG = "msg" ;
	private static final String TIME = "time" ;
	
	private static final String ID = "_id" ;
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String YAT = "yat";
	private static final String ISNUM = "isnum";
	private static final String ADRESS = "adress";
	private static final String MYPHONE = "myphone";
	private static final String TABLENUM = "tablenum";
	private static final String LAYOUT_TABLE = "layouttable" ;
	private static final String ACT_NAME = "actname" ;
	private static final String LAYOUT = "layout" ;
	
	
	
	private DBOpenHelper helper;
	private SQLiteDatabase db;
	//�û���Ϣ����ס������û���Ϣ
	private static final String CREATE_TABLE1 = "create table "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ISNUM+" text not null, "+USERNAME+" text not null, "+PASSWORD+" text not null , "+YAT+" text not null  "+")";
	//��ǰ��½������Ϣ
	private static final String CREATE_TABLE2 = "create table "+HASKOGIN+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
			+USERNAME+" text not null, "+PASSWORD+" text not null , "+YAT+" text not null  "+")";
	//�����û���Ϣ
	private static final String CREATE_TABLE3 = "create table "+ADRESS_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ADRESS+" text not null, "+MYPHONE+" text not null , "+TABLENUM+" text not null  "+")";
	//��鲼����Ϣ
	private static final String CREATE_TABLE4 = "create table "+LAYOUT_TABLE+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ACT_NAME+" text not null, "+LAYOUT+" DEFAULT 'list_mod'  "+")";
	//�����¼
	private static final String CREATE_TABLE5 = "create table "+CHAT+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
			+XKBH+" DEFAULT 'xkbh', "+NAME+" DEFAULT 'name', "+MSG+" DEFAULT 'msg', "+TIME+" DEFAULT 'time'"+")";
	private static class DBOpenHelper extends SQLiteOpenHelper
	{

		public DBOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db)   //�������
		{
			// TODO Auto-generated method stub
			db.execSQL(CREATE_TABLE1);
			db.execSQL(CREATE_TABLE2);
			db.execSQL(CREATE_TABLE3);
			db.execSQL(CREATE_TABLE4);
			db.execSQL(CREATE_TABLE5);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)  //�������
		{
			// TODO Auto-generated method stub
			db.execSQL("drop table if exists "+CREATE_TABLE1);
			db.execSQL("drop table if exists "+CREATE_TABLE2);
			db.execSQL("drop table if exists "+CREATE_TABLE3);
			db.execSQL("drop table if exists "+CREATE_TABLE4);
			db.execSQL("drop table if exists "+CREATE_TABLE5);
			onCreate(db);
			
		}
		
	}
    
	public DBHelper_SaveUserInfo(Context context)
    {
    	helper = new DBOpenHelper(context);
    	db = helper.getWritableDatabase();
    	db = helper.getReadableDatabase();
    }
	
	public void insert_item(String username , String password , String isnum , String yat)
	{
		ContentValues values = new ContentValues();
		//1�Ǽ�ס����,0�ǲ���ס
		values.put("isnum", isnum);
        values.put("username", username);
        values.put("password", password); 
        values.put("yat", yat);
        db.insert(TABLE_NAME, null, values);
	}
	public void insert(String username , String password , String yat)
	{
		ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("yat", yat);
        db.insert(HASKOGIN, null, values);
	}
	
	public User query_userinfo(String username , String yat)
	{
		User user = new User();
		user.username = "";
		user.password = "";
		user.isnum = "";
		user.yat = "";
		Cursor c = db.rawQuery("SELECT * FROM "+HASKOGIN, null);
		 while (c.moveToNext()) 
		 {    
	         user.username = c.getString(c.getColumnIndex("username"));  
	         user.password = c.getString(c.getColumnIndex("password"));
	         user.yat = c.getString(c.getColumnIndex("yat"));
	         if(user.username.equals(username))
	        	 if(user.yat.equals(yat))
	        	 {
	        		 break;
	        	 }          
	        }  
	        c.close(); 
		return user;
	}
	public User query_userinfo_item()
	{
		User user = new User();
		user.username = "";
		user.password = "";
		user.isnum = "";
		user.yat = "";
		Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
		 while (c.moveToNext()) 
		 {    
			 user.isnum = c.getString(c.getColumnIndex("isnum"));
	         user.username = c.getString(c.getColumnIndex("username"));  
	         user.password = c.getString(c.getColumnIndex("password")); 
	         user.yat = c.getString(c.getColumnIndex("yat"));
	        }  
	        c.close(); 
		return user;
	}
	
	public void del_userinfo_item(String username)
	{
		User user = new User();
		Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME, null);
		 while (c.moveToNext()) 
		 {    
	         user.username = c.getString(c.getColumnIndex("username"));  
	         user.password = c.getString(c.getColumnIndex("password"));
	         if(user.username.equals(username))
	           break;
	     }  
		 //��ɾ����ʱ��ֻҪ�ҵ��˹ؼ��֣����£��Ϳ���ɾ��һ��
		 db.delete(TABLE_NAME, "username = ?", new String[]{user.username });  
	     c.close();
	}
	public void del_userinfo(String username)
	{
		User user = new User();
		Cursor c = db.rawQuery("SELECT * FROM "+HASKOGIN, null);
		 while (c.moveToNext()) 
		 {    
	            user.username = c.getString(c.getColumnIndex("username"));  
	            user.password = c.getString(c.getColumnIndex("password"));
	            if(user.username.equals(username))
	            	break;
	     }  
		 //��ɾ����ʱ��ֻҪ�ҵ��˹ؼ��֣����£��Ϳ���ɾ��һ��
		 db.delete(HASKOGIN, "username = ?", new String[]{user.username });  
	     c.close();
	}
	
	
	public void close()
	{
		db.close();
	}
	
	

}
