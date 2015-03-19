package com.low_kb;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import listening_function.Net_state_listening;

import operation.JWGG_detail;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import service.*;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * ����֪ͨ������鿴
 */

public class look_notice extends Activity{
	Bundle bundle=null;
	String news_detail="" , href="";
	ConnectivityManager cwjManager;
	Net_state_listening nsl = null;
	ProgressDialog progressDialog = null;
	WebService ws = null;
	private WebView webView = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_gg);
		nsl = new Net_state_listening(this);
		//�ж��Ƿ�����
		if(!nsl.getInternet())
		{
			return;
		}
		//�ڲ����ļ����ҵ���Ӧ�Ŀؼ�
		ws = new WebService();
		progressDialog = ProgressDialog.show(look_notice.this, "���Ե�", "���ڼ���", true, false);
		webView = (WebView)findViewById(R.id.notice_detail_webView);
		webView.getSettings().setDefaultTextEncodingName("UTF-8") ;
	    webView.getSettings().setJavaScriptEnabled(true);
	    webView.getSettings().setSupportZoom(false);
	    webView.getSettings().setBuiltInZoomControls(false);
	    //����javaScript����
	    //window.open()
	    webView.addJavascriptInterface(look_notice.this, "contact");	    
		//ȡ�ô��������
		Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        href = bundle.getString("href");
		//���ݴ����Idֵ���ؾ���Ĺ�����Ϣ
        Thread get = new Thread(){
			public void run()
			{
				news_detail = ws.getJWGG_detail(href);
				Message msg = new Message();
                msg.what=0;
                handler.sendMessage(msg);
			}
		};
		get.start();
	}
		
	
	
	
	
	//�õ�ָ������UI
	private Handler handler = new Handler(){  
  	  
        @Override  
        public void handleMessage(Message msg) {  
        	super.handleMessage(msg);
        	switch(msg.what)
        	{
        		case 0:
        		{      
        			//������ɺ󣬽������ݲ������б���
        			webView.loadDataWithBaseURL(null, JWGG_detail.start+news_detail+JWGG_detail.end, "text/html", "UTF-8", null);
        			progressDialog.dismiss();
        			break;
        		}
        		case 1:
        		{     			
        			break;
        		}
        	}
        }}; 

}
