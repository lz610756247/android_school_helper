package com.low_kb;







import listening_function.Net_state_listening;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.webkit.WebView;

public class map extends Activity{

	WebView map;
	Net_state_listening nsl = null;
	ProgressDialog progressDialog = null;
	Message msg = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.map);
        
        nsl = new Net_state_listening(this);
        //判断是否联网
        if(!nsl.getInternet())
        {
    	    return;
        }
        //初始化提示框
        msg = handler.obtainMessage();
        
        //获取map的webview
        map = (WebView) this.findViewById(R.id.map);
        //支持javascript
        map.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        map.getSettings().setSupportZoom(true);
        map.getSettings().setBuiltInZoomControls(true);      

        
        msg.what=1;
        //开始加载UI
        handler.sendMessage(msg);
        
	}
	
	
	//加载网页
    public void load_map()
    {
    	
    	map.loadUrl("file:///android_asset/map.html");
    	
    }
    
  //得到指令后加载网页
  	private Handler handler = new Handler(){  
    	  
          @Override  
          public void handleMessage(Message msg) {  
          	super.handleMessage(msg);
          	switch(msg.what)
          	{
          		case 0:
          		{              			
          			//get_map.setVisibility(View.GONE);
          			progressDialog.dismiss();
          			break;
          		}
          		case 1:
          		{       
          			progressDialog = ProgressDialog.show(map.this, "请稍等", "正在加载", true, false);
          			//加载网页
          			load_map();
          			progressDialog.dismiss();
          			break;
          		}
          	}       	 
          }};
	
}
