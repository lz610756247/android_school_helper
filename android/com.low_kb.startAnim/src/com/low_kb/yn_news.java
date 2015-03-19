package com.low_kb;

import com.SQLite.User;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class yn_news extends Activity {

	String url;
	WebView yn_news_web;
	ConnectivityManager cwjManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.yn_news);
        //url = "http://www.ynufe.edu.cn/xwzx/index.htm";
        url = "http://www.ynufe.edu.cn/xwzx/index.htm";
        yn_news_web = (WebView) findViewById(R.id.yn_news);
        //���������һ��WebView������GET��������
        //���ж������Ƿ����ӣ��������������ʾ��������
        if(getInternet())
        {
        	yn_news_web.getSettings().setSupportZoom(true);
        	yn_news_web.getSettings().setBuiltInZoomControls(true);
        	yn_news_web.getSettings().setJavaScriptEnabled(true);
            //����url
            //���Ӳ�������ҳ
        	yn_news_web.loadUrl(url);
        }
        
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    // TODO Auto-generated method stub
    if (keyCode == KeyEvent.KEYCODE_BACK && yn_news_web.canGoBack()) {
    // ����ǰһ��ҳ��
    yn_news_web.goBack();
    return true;
    }
    return super.onKeyDown(keyCode, event);
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
					builder.setMessage("�������Ʋ����ţ�ϣ���ܴ�����������Ѷ.").setCancelable(false)
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
