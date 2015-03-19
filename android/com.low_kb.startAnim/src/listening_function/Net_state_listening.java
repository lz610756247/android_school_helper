package listening_function;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.Activity;

public class Net_state_listening 
{
	public Context context;
	public Net_state_listening(Context context)
	{
		this.context = context;
	}
	
	public boolean getInternet()
	{		
		ConnectivityManager cwjManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
		{ 
		       //do something 
		       //能联网 
	    	   return true; 
		  }else
		  { 
		       //do something 
		       //不能联网 
			  AlertDialog.Builder builder = new AlertDialog.Builder(context);
	    		builder.setMessage("未连接网络").setCancelable(false)
	    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						((Activity)context).finish();
					}
				}).show();
		        return false; 
		  } 		 		
	}

}
