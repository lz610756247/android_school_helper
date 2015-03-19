package com.low_kb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class look_weibo extends Activity {

	Bundle bundle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_weibo);
        //取得传入的信息，进行微博详细信息的加载，包括名字，名称，内容
        TextView mz = (TextView) findViewById(R.id.mz);
        TextView mc = (TextView) findViewById(R.id.mc);
        TextView nr = (TextView) findViewById(R.id.nr);
        Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        String mz_text = bundle.getString("mz");
        String mc_text = bundle.getString("mc");
        String nr_text = bundle.getString("nr");
        mz.setText(mz_text);
        nr.setText(nr_text);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
}
