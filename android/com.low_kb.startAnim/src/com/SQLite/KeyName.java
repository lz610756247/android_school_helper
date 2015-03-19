package com.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class KeyName {

	Context context=null;
	String username="" , yat="";
	public KeyName(Context context , String username , String yat)
	{
		this.context = context;
		this.username = username;
		this.yat = yat;
	}
	//����Key���е�ǰ�û�����ϸ��Ϣ,�Ա�������������ȡ,�������˺ź�ѧ��ѧ��
	public void insertName()
    {
    	String ID = "_id" ;
    	String KEY = "key";
    	String YAT = "yat";
    	SQLiteDatabase db = context.openOrCreateDatabase("key.db", Context.MODE_PRIVATE, null);
    	String CREATE_TABLE = "create table "+"key"+"("+ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
				+KEY+" text not null , "+YAT+" text not null "+")";
    	db.execSQL("DROP TABLE IF EXISTS key");
	    db.execSQL(CREATE_TABLE);
	    ContentValues values = new ContentValues() ;
	    values.put("key", username);
	    values.put("yat", yat);
	    db.insert("key", null, values);
	    db.close();
    }
	//ȡ��Key���е�ǰ�û�����ϸ��Ϣ���������˺ź�ѧ��ѧ��
		public String getName()
		{
			String s = "";
			SQLiteDatabase db = context.openOrCreateDatabase("key.db", Context.MODE_PRIVATE, null);
			Cursor c = db.rawQuery("SELECT * FROM "+"key", null);
			while (c.moveToNext()) 
			{
				s = c.getString(c.getColumnIndex("key"))+c.getString(c.getColumnIndex("yat"));
			}
			c.close();
			db.close();
			//this.deleteDatabase("key");
			return s;
		}
}
