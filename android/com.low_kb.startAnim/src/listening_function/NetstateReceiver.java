package listening_function;

import com.low_kb.main_function;
import com.low_kb.startAnim;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class NetstateReceiver extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
		// TODO Auto-generated method stub
		ConnectivityManager manager = (ConnectivityManager) context 
                .getSystemService(Context.CONNECTIVITY_SERVICE); 
        NetworkInfo gprs = manager 
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE); 
        NetworkInfo wifi = manager 
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI); 
        if (!gprs.isConnected() && !wifi.isConnected()) { 
        	
            // network closed  
        } else 
        { 
            // network opend 
        	//Intent startServiceIntent = new Intent(context,Notice_score.class);  
        	//context.startService(startServiceIntent);
        	//Intent startServiceIntent1 = new Intent(context,Notice_version.class);  
        	//context.startService(startServiceIntent1);
        	//Toast.makeText(context, "NetstateReceiver", 100).show();
        	Log.i("NetstateReceiver", "start");
        }  		
	}

}
