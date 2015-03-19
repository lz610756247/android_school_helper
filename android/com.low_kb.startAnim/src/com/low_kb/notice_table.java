package com.low_kb;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import listening_function.Net_state_listening;

import operation.JWGG;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import createdpac.NoticeHttpClient;

import service.*;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/*
 * 教务通知
 */
public class notice_table extends Activity{
	
	ConnectivityManager cwjManager=null;
	Net_state_listening nsl = null;
	private WebView webView = null;
	private boolean isloading = true;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.requestWindowFeature(Window.PROGRESS_START);
		setContentView(R.layout.gg);
		nsl = new Net_state_listening(this);
		//判断是否联网
		if(!nsl.getInternet())
		{
			return;
		}	
		webView = (WebView)findViewById(R.id.notice_webView);	     
	    //webview参数设定
	    setWebView();	
		webView.loadUrl("http://202.203.194.10/jwnews/%28S%28g1bee33d2u5olu2n1j1bvd45%29%29/list.aspx?classname=%BD%CC%CE%F1%D0%C5%CF%A2");		
	}
		
	/*
	 * webview参数设定
	 */
	public void setWebView()
	{
		//设置javaScript可用	 
		webView.getSettings().setJavaScriptEnabled(true);
	    webView.getSettings().setSupportZoom(false);
	    webView.getSettings().setBuiltInZoomControls(false);    	
	    webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
	    //设置显示方式
	    webView.setWebViewClient(new MyWebViewClient());
	    webView.setWebChromeClient(new MyWebChromeClient());
	} 
        
    /**
     * 按键响应，在WebView中查看网页时，按返回键的时候按浏览历史退回,如果不做此项处理则整个WebView返回退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            // 返回键退回
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
     * 重写webclient，使得页面的URL都用自己的webview打开
     */
    private class MyWebViewClient extends WebViewClient
    {
    	NoticeHttpClient noticeHttpClient;
    	public MyWebViewClient(){
    		this.noticeHttpClient = new NoticeHttpClient();
    		noticeHttpClient.init();
    	}
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
        	/**
        	 * url由于loadData方法，解析会有不同
        	 * loadUrl，可以得到http：//
        	 * loadData，http://会被省略
        	 */
        	isloading = true;
        	String html = noticeHttpClient.startWork(url);
        	view.loadData(html, "text/html; charset=UTF-8", null);           	         	
        	return true;
        }

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub				
			Log.e("WebView","onPageFinished ");
			isloading = false;
//				view.loadUrl("javascript:window.local_obj.showSource('<head>'+" 
//						+"document.getElementsByTagName('html')[0].innerHTML+'</head>');");			
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
			isloading = true;
		}         
    }
    
    private class MyWebChromeClient extends WebChromeClient{}
    
    /**
     * 本地Javascript接口
     */	
	final class InJavaScriptLocalObj {
	    public void showSource(String html){
	        Log.e("HTML", html);		        
	    }
	}
}
