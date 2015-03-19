package listening_function;

import java.io.IOException;

import operation.UpdateManager;

import org.xmlpull.v1.XmlPullParserException;

import com.low_kb.login_table;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class Notice_version extends IntentService
{

	public Notice_version() {
		super("Notice_version");
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override  
    public void onStart(Intent intent, int startId) {
		Toast.makeText(this, "有新版本", Toast.LENGTH_SHORT).show();
		UpdateManager mUpdateManager = new UpdateManager(this.getApplicationContext());  
	       if(mUpdateManager.checkUpdateInfo())
	       {
	    	  //开始下载 
	    	   Toast.makeText(this, "有新版本", Toast.LENGTH_SHORT).show();
	       }
	       else
	       {
	    	   this.stopSelf();
	       }
    } 
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub		
		super.onDestroy();
	}

}
