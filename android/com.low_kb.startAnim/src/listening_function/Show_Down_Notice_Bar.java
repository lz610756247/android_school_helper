package listening_function;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class Show_Down_Notice_Bar extends Activity
{
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		Bundle bundle = intent.getBundleExtra("bundle");
        if(bundle.getInt("id")>=0)
        {
        	NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        	mNotificationManager.cancel(bundle.getInt("id"));
        	finish();
        }
	}

}
