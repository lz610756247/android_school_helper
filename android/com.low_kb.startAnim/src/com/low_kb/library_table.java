package com.low_kb;

import listening_function.Net_state_listening;

import com.SQLite.User;
import com.low_kb.notice_table.InJavaScriptLocalObj;

import createdpac.NoticeHttpClient;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class library_table extends Activity {

	String url;
	User user;
	WebView webView;
	ConnectivityManager cwjManager;
	Net_state_listening nsl = null;
	String username="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book);
        nsl = new Net_state_listening(this);
        url = "http://202.203.193.2/default.aspx";
        webView = (WebView) findViewById(R.id.book);
        //这个界面是一个WebView，采用GET方法进入
        //先判断网络是否连接，无连接则给出提示结束进程
        if(!nsl.getInternet())
        {
        	return;
        }
        //webview参数设定
	    setWebView();
	    webView.loadUrl(url);
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
	    //设置显示方式
	    webView.setWebViewClient(new MyWebViewClient());
	    webView.setWebChromeClient(new MyWebChromeClient());
	} 
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
    // 返回前一个页面
    	webView.goBack();
    return true;
    }
    return super.onKeyDown(keyCode, event);
    }  
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(group1,1,1,"板块说明");
    	menu.setGroupVisible(1, true);
        return true;
    }
    
    /*------------------------------------------*/
    //为菜单指定动作
    public void openOptionsMenu() {  
        // TODO Auto-generated method stub  
        super.openOptionsMenu(); 
	 }
	public boolean onOptionsItemSelected(MenuItem item) {
	    	switch (item.getItemId()) {
			   case 1:
			   {  //为软件说明添加功能
				   AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("借书查看说明").setMessage("此时使用的是图书馆。").setCancelable(false)
					.setPositiveButton("关闭", new DialogInterface.OnClickListener() {
						
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
	
	/**
     * 重写webclient，使得页面的URL都用自己的webview打开
     */
    private class MyWebViewClient extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
        	view.loadUrl(url);          	         	
        	return true;
        }

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub				
			Log.e("WebView","onPageFinished ");	
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			// TODO Auto-generated method stub
		}         
    }
    
    private class MyWebChromeClient extends WebChromeClient{}
}
