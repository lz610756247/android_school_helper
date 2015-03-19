package com.low_kb;

import java.io.File;

import service.MyWeiboManager;


import com.weibo.net.AccessToken;
import com.weibo.net.DialogError;
import com.weibo.net.Oauth2AccessTokenHeader;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboDialogListener;
import com.weibo.net.WeiboException;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

public class wb_table extends Activity implements WeiboDialogListener{
	
	private Weibo weibo;
	private String key;
	private final String TAG = "jjhappyforever";
	private WebView mWebView;
	private Editor editor;
	private WeiboDialogListener mListener;
	private LinearLayout linearLayout;
	ConnectivityManager cwjManager;
	private SharedPreferences preferences;
	public static String file = "happyforever";
	private String consumer_key = "2645266073";
	private String consumer_secret = "0f5752340de0d492059b451b6b88e65d";
	private String mRedirectUrl = "https://api.weibo.com/oauth2/default.html";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListener = this;
        setContentView(R.layout.wb);
        //ȡ����Ӧ�����ݣ��ж��û��Ƿ��ס���û���Ϣ
        weibo = Weibo.getInstance();
		weibo.setupConsumerConfig(consumer_key, consumer_secret);
		weibo.setRedirectUrl(mRedirectUrl);
		Utility.setAuthorization(new Oauth2AccessTokenHeader());
		preferences = getSharedPreferences(file, 0);
		key = preferences.getString("token", "");
		//�ж��Ƿ��������������ӹ���
		if(this.getInternet())
		{
			InitWebView();
		}
    }


	public void onCancel() {
		// TODO Auto-generated method stub
		
	}

	public void onComplete(Bundle values) {
		// TODO Auto-generated method stub
		/***
		 * ������Ҫsave the access_token
		 */
		String token = values.getString("access_token");

		SharedPreferences preferences = getSharedPreferences(wb_table.file,0);
		Editor editor = preferences.edit();
		editor.putString("token", token);
		editor.commit();

		AccessToken accessToken = new AccessToken(token, Weibo.getAppSecret());
		Weibo.getInstance().setAccessToken(accessToken);
		setResult(RESULT_OK);
		Toast.makeText(wb_table.this, "��½�ɹ�", 5000).show();
		//��½����Ժ����ת������,��ת��΢���б�ͷ���΢������
		Intent intent = new Intent(wb_table.this , send_weibo.class);
		startActivity(intent);
		finish();
	}

	public void onError(DialogError arg0) {
		// TODO Auto-generated method stub
		
	}

	public void onWeiboException(WeiboException arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void InitWebView() {
		//�ڲ����ļ����ҵ���Ӧ�Ŀؼ������ҽ�����Ӧ�ļ���
		linearLayout = (LinearLayout) findViewById(R.id.ll_webview);
		mWebView = (WebView) findViewById(R.id.mywebview);
		mWebView.setVerticalScrollBarEnabled(false);
		mWebView.setHorizontalScrollBarEnabled(false);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WeiboWebViewClient());
		mWebView.loadUrl(MyWeiboManager.getOauthURL(this));

	}
	
	/***
	 * WebViewClient
	 * 
	 * @author zhangjia
	 * 
	 */
	private class WeiboWebViewClient extends WebViewClient {

		/***
		 * ��ַ�ı䶼�����
		 */
		//΢���ͻ��˴���
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.d(TAG, "Redirect URL: " + url);
			if (url.startsWith(Weibo.getInstance().getRedirectUrl())) {
				handleRedirectUrl(view, url);
				return true;
			}
			// launch non-dialog URLs in a full browser
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
			return true;
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			mListener.onError(new DialogError(description, errorCode,
					failingUrl));
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			Log.d(TAG, "onPageStarted URL: " + url);
			// google issue. shouldOverrideUrlLoading not executed
			/**
			 * �����Ȩ��url��ȷ
			 */
			if (url.startsWith(Weibo.getInstance().getRedirectUrl())) {
				handleRedirectUrl(view, url);
				view.stopLoading();

				return;
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			Log.d(TAG, "onPageFinished URL: " + url);
			super.onPageFinished(view, url);
			linearLayout.setVisibility(View.GONE);
		}

		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			handler.proceed();
		}

	}
	
	private void handleRedirectUrl(WebView view, String url) {
		Bundle values = Utility.parseUrl(url);
		String error = values.getString("error");
		String error_code = values.getString("error_code");
		// ��Ȩ�ɹ�
		if (error == null && error_code == null) {
			mListener.onComplete(values);
			// �ܾ�ʧ�ܵ�
		} else if (error.equals("access_denied")) {
			mListener.onCancel();
		} else {
			// �쳣
			mListener.onWeiboException(new WeiboException(error, Integer
					.parseInt(error_code)));
		}
	}
	
	public boolean getInternet()
    {
    	cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cwjManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable())
		{ 
		       //do something 
		       //������ 
	    	   return true; 
		  }else
		  { 
		       //do something 
		       //�������� 
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage("δ��������").setCancelable(false)
	    		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).show();
		        return false; 
		  } 		 
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(group1,1,1,"���˵��");
    	menu.setGroupVisible(1, true);
        return true;
    }
    
    /*------------------------------------------*/
    //Ϊ�˵�ָ������
    public void openOptionsMenu() {  
        // TODO Auto-generated method stub  
        super.openOptionsMenu(); 
	 }
	public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
			   case 1:
			   {  //Ϊ���˵����ӹ���
				   AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("΢�����˵��").setMessage("�����������֧��΢���Ĳ鿴�������������㣬���½�" +
							"���ù��ܽ���֮�󾡿����ơ�").setCancelable(false)
					.setPositiveButton("�ر�", new DialogInterface.OnClickListener() {
						
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					}).show();
					break;
			   }
	    	}
			return true;
	    }



}
