package com.low_kb;

import listening_function.Net_state_listening;
import com.low_kb.notice_table.InJavaScriptLocalObj;

import createdpac.NoticeHttpClient;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import util.MyWebViewClient;

public class order_food_table extends Activity{

	ConnectivityManager cwjManager=null;
	Net_state_listening nsl = null;
	private WebView webView = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.requestWindowFeature(Window.PROGRESS_START);
		this.getWindow().requestFeature(Window.FEATURE_PROGRESS);
		getWindow().setFeatureInt( Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_OFF);  
		setContentView(R.layout.gg);
		nsl = new Net_state_listening(this);
		//�ж��Ƿ�����
		if(!nsl.getInternet())
		{
			return;
		}	
		webView = (WebView)findViewById(R.id.notice_webView);	     
	    //webview�����趨
	    setWebView();	
		webView.loadUrl("http://waimai.meituan.com/home/wk3n9wfww4bs?utm_campaign=baidu&utm_source=4204");		
	}
		
	/*
	 * webview�����趨
	 */
	public void setWebView()
	{
		//����javaScript����	 
		webView.getSettings().setJavaScriptEnabled(true);
	    webView.getSettings().setSupportZoom(false);
	    webView.getSettings().setBuiltInZoomControls(false);    	
	    webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  
	    //������ʾ��ʽ
	    webView.setWebViewClient(new MyWebViewClient());
	    //webView.setWebChromeClient(new MyWebChromeClient());
	} 
        
	/**
     * ������Ӧ����WebView�в鿴��ҳʱ�������ؼ���ʱ�������ʷ�˻�,������������������WebView�����˳�
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            // ���ؼ��˻�
        	if(webView.canGoBack()){
        		webView.goBack();
        	}
        	else{
        		this.finish();
        	}
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up
        // to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
    
    /**
     * ��дwebclient��ʹ��ҳ���URL�����Լ���webview��
     */
    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
        	/**
        	 * url����loadData�������������в�ͬ
        	 * loadUrl�����Եõ�http��//
        	 * loadData��http://�ᱻʡ��
        	 */
        	view.loadUrl(url);          	         	
        	return true;
        }

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub				
			Log.e("WebView","onPageFinished ");
//				view.loadUrl("javascript:window.local_obj.showSource('<head>'+" 
//						+"document.getElementsByTagName('html')[0].innerHTML+'</head>');");			
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
		}         
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
//		//���webview����
//		webView.clearCache(true);
//		webView.clearHistory();
//		webView.clearFormData();
//		this.deleteDatabase("webview.db");
//		this.deleteDatabase("webviewCache.db");
//		//���cookie
//		CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(this);
//        CookieManager cookieManager = CookieManager.getInstance();
//        cookieManager.removeAllCookie();
	}
	
	
}
