package com.low_kb;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class set_goods extends Activity  {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_goods);
		Button bt1 = (Button) findViewById(R.id.btn01);
		bt1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				//在布局文件中取得相应的控件
				String name=((EditText)findViewById(R.id.edt01)).getText().toString();
				String price=((EditText)findViewById(R.id.edt02)).getText().toString();
				String user=((EditText)findViewById(R.id.edt03)).getText().toString();
				String tel=((EditText)findViewById(R.id.edt04)).getText().toString();
				//取得输入的数据，传入下一个界面
				Intent intent = new Intent(set_goods.this,
						submit_goods.class);
				Bundle budle=new Bundle();
				budle.putString("name",name);
				budle.putString("price",price);
				budle.putString("user",user);
				budle.putString("tel",tel);
				intent.putExtras(budle);//将bundle 对象添加到intent对象 中
				startActivity(intent);

			}
		});

	}

}
