package com.low_kb;

import java.io.IOException;
import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import service.WebService;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class submit_goods extends Activity  {
	public String name , price , user , tel;
	ConnectivityManager cwjManager=null;
	WebService ws = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.submit_goods);
		Intent intent = getIntent();// 获取intent对象
		Bundle bundle = intent.getExtras();// 获取传递的数据包
		TextView tv01 = (TextView) findViewById(R.id.tv01);
		ws = new WebService();
		tv01.setText("商品名: " + bundle.getString("name") + " \n" + "价格: "
				+ bundle.getString("price") + " \n" + "上传者: "
				+ bundle.getString("user") + " \n" + "联系电话: "
				+ bundle.getString("tel"));

		name = bundle.getString("name");
		price = bundle.getString("price");
		user = bundle.getString("user");
		tel = bundle.getString("tel");
		//取得上传按钮
		Button btn3 = (Button) findViewById(R.id.bt03);
		btn3.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//判断是否联网，然后开始上传
				if(!getInternet())
				{
					Toast.makeText(submit_goods.this, "无网络连接", 1000).show();
					return;
				}
				Toast.makeText(submit_goods.this, "开始上传", 1000).show();
				//开始联网上传
				ws.submit_goods_ws(name, price, user, tel);
				Toast.makeText(submit_goods.this, "上传成功", 1000).show();
			}
		});
	}

	public boolean getInternet()
    {
    	cwjManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
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
			  AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage("未连接网络").setCancelable(false)
	    		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						finish();
					}
				}).show();
		        return false; 
		  } 		 
    }
}

