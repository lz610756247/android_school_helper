package operation;



import com.SQLite.DBHelper_SaveUserInfo;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.SmsManager;
import android.widget.Toast;

public class message {
	private String phone = "" , table_num="" , adress = "",myphone="";
	private Context context = null;
	private SQLiteDatabase db = null;
	public message(String phone , Context context , String table_num)
	{
		//获得电话等待进一步操作
		this.context = context;
		this.phone = phone;
		this.table_num = table_num;
	}
	public boolean setAdress(String adress , String myphone)
	{
		//首先查询是否有记录，如果有就更新，如果没有就插入
		db = context.openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM adresstable", null);
		 while (c.moveToNext()) 
		 {  	 
			 if(c.getString(c.getColumnIndex("tablenum")).equals(table_num))
			 {
				 ContentValues values = new ContentValues();
				 values.put("adress", adress);
				 values.put("myphone", myphone);
			     db.update("adresstable", values, "tablenum = ?", new String[]{table_num});
				 return true;
			 }
	     }  
		 c.close();
		//ContentValues以键值对的形式存放数据  
	    ContentValues cv = new ContentValues();  
	    cv.put("adress", adress);  
	    cv.put("myphone", myphone); 
	    cv.put("tablenum", table_num);  
	    //插入ContentValues中的数据  
	    db.insert("adresstable", null, cv);  
		db.close();
		return false;
	}
	public Info getInfo()
	{
		//得到地址
		Info info = new Info();
		db = context.openOrCreateDatabase("userinfo.db", Context.MODE_PRIVATE, null);
		Cursor c = db.rawQuery("SELECT * FROM adresstable", null);
		 while (c.moveToNext()) 
		 {  	 
			 if(c.getString(c.getColumnIndex("tablenum")).equals(table_num))
			 {
				 info.adress = c.getString(c.getColumnIndex("adress"));
				 info.myphone = c.getString(c.getColumnIndex("myphone"));
				 break;
			 }
	     }  
		 c.close();
		db.close();
		return info;
	}
	public void sendMessage(String msg)
	{
		String SENT = "SMS_SENT" ; 
        String DELIVERED = "SMS_DELIVERED" ; 

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,  new Intent( SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,  new Intent( SENT), 0) ; 

        //---when the SMS has been sent--- 
        context.registerReceiver( new BroadcastReceiver( ) { 
                @Override
                public void onReceive( Context arg0, Intent arg1) { 
                        switch ( getResultCode( ) ) 
                        { 
                        case Activity.RESULT_OK :
                                Toast.makeText ( context , "短信已经发送" , 
                                                Toast.LENGTH_SHORT ) .show ( ) ; 
                                break ; 
                        case SmsManager.RESULT_ERROR_GENERIC_FAILURE :
                                Toast.makeText ( context , "Generic failure" , 
                                                Toast.LENGTH_SHORT ) .show ( ) ; 
                                break ; 
                        case SmsManager.RESULT_ERROR_NO_SERVICE :
                                Toast.makeText ( context , "No service" , 
                                                Toast.LENGTH_SHORT ) .show ( ) ; 
                                break ; 
                        case SmsManager.RESULT_ERROR_NULL_PDU :
                                Toast.makeText ( context , "Null PDU" , 
                                                Toast.LENGTH_SHORT ) .show ( ) ; 
                                break ; 
                        case SmsManager.RESULT_ERROR_RADIO_OFF :
                                Toast.makeText (context , "Radio off" , 
                                                Toast.LENGTH_SHORT ) .show ( ) ; 
                                break ; 
                        }
                }
        }, new IntentFilter( SENT) ) ;

        //---when the SMS has been delivered--- 
        context.registerReceiver( new BroadcastReceiver( ) { 
                @Override
                public void onReceive( Context arg0, Intent arg1) { 
                        switch ( getResultCode( ) ) 
                        { 
                        case Activity.RESULT_OK :
                                Toast.makeText ( context , "SMS delivered" , 
                                                Toast.LENGTH_SHORT ) .show ( ) ; 
                                break ; 
                        case Activity.RESULT_CANCELED :
                                Toast.makeText ( context, "SMS not delivered" , 
                                                Toast.LENGTH_SHORT ) .show ( ) ; 
                                break ; 
                        } 
                } 
        } , new IntentFilter( DELIVERED) ) ; 

        SmsManager sms = SmsManager.getDefault () ; 
        sms.sendTextMessage ( phone, null , msg, sentPI, deliveredPI) ;
	}

}
