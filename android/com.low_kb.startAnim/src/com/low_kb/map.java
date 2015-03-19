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
        //�ж��Ƿ�����
        if(!nsl.getInternet())
        {
    	    return;
        }
        //��ʼ����ʾ��
        msg = handler.obtainMessage();
        
        //��ȡmap��webview
        map = (WebView) this.findViewById(R.id.map);
        //֧��javascript
        map.getSettings().setJavaScriptEnabled(true);
        //֧������
        map.getSettings().setSupportZoom(true);
        map.getSettings().setBuiltInZoomControls(true);      

        
        msg.what=1;
        //��ʼ����UI
        handler.sendMessage(msg);
        
	}
	
	
	//������ҳ
    public void load_map()
    {
    	
    	map.loadUrl("file:///android_asset/map.html");
    	
    }
    
  //�õ�ָ��������ҳ
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
          			progressDialog = ProgressDialog.show(map.this, "���Ե�", "���ڼ���", true, false);
          			//������ҳ
          			load_map();
          			progressDialog.dismiss();
          			break;
          		}
          	}       	 
          }};
	
}
