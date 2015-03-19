package com.low_kb;



import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.weibo.net.AccessToken;
import com.weibo.net.Oauth2AccessTokenHeader;
import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboException;
import com.weibo.net.WeiboParameters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class send_weibo extends Activity
{
	Button send=null;
	EditText edittext=null;
	String token="" , result="" , jsondata="";
	private Editor editor=null;
	private Weibo weibo=null;
	ListView listview=null;
	List<Map<String ,Object>> weibos=null;
	ConnectivityManager cwjManager=null;
	public static String file = "happyforever";
	private SharedPreferences preferences;
	private String consumer_key = "2645266073";
	private String consumer_secret = "0f5752340de0d492059b451b6b88e65d";
	private String mRedirectUrl = "https://api.weibo.com/oauth2/default.html";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_weibo);
        send = (Button) findViewById(R.id.send);
        edittext = (EditText) findViewById(R.id.edittext);
        listview = (ListView) findViewById(R.id.listview);
        if(this.getInternet())
        {
        	//取得数据,确定登陆成功
            preferences = getSharedPreferences(file, 0);
    		token = preferences.getString("token", "");
    		//加载发送微博的功能
    		send.setOnClickListener(new OnClickListener()
			{
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					//发表微博
					Utility.setAuthorization(new Oauth2AccessTokenHeader()); // 这一步不能省.
					AccessToken accessToken = new AccessToken(token,consumer_secret);
					Weibo.getInstance().setAccessToken(accessToken);
					try {
						update(weibo.getInstance(), weibo.getAppKey(), edittext.getText().toString(), "", "");
					} catch (WeiboException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Toast.makeText(send_weibo.this, "发送失败", 1000).show();
					}
				}
			});
    		//开始获取微博数据
    		Thread get = new Thread(){
    			public void run()
    			{
    				try {
	    					result = send(token);
							jsondata = result;
							//向handler发消息  
	                        Message msg = new Message();
	                        msg.what=0;
	                        handler.sendMessage(msg);
					     } catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    			}
    		}; 		
    		get.start();
        }  
    }
	
	
	private String update(final Weibo weibo, String source, String status,String lon, String lat) throws WeiboException {
		final WeiboParameters bundle = new WeiboParameters();
		bundle.add("source", source);
		bundle.add("status", status);
		if (!TextUtils.isEmpty(lon)) {
			bundle.add("lon", lon);
		}
		if (!TextUtils.isEmpty(lat)) {
			bundle.add("lat", lat);
		}
		final String url = Weibo.SERVER + "statuses/update.json";
		new Thread(){
			@Override
			public void run() {
				String rlt = "";
				// TODO Auto-generated method stub
				try {
					rlt = weibo.request(send_weibo.this, url, bundle, Utility.HTTPMETHOD_POST,weibo.getAccessToken());
				} catch (WeiboException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mHandler.sendMessage(mHandler.obtainMessage());
			}
			
		}.start();
		return "";
	}
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Toast.makeText(send_weibo.this, "发表成功", 1000).show();
		}
	};
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(group1,1,1,"微博登陆界面");
    	menu.add(group1,2,2,"说明");
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
			   {  //跳转微博登陆界面
				   editor = preferences.edit();
					editor.putString("token", "");
					editor.commit();
				   File file = new File("/data/data/"+this.getPackageName()+ "/shared_prefs/happyforever.xml");
				   file.delete();
				   
				   Intent intent = new Intent(send_weibo.this , wb_table.class);
					startActivity(intent);
					finish();
					break;
			   }
			   case 2:
			   {
				   AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("微博版块说明").setMessage("现在软件还不支持微博的查看，给您带来不便，请谅解" +
							"，该功能将在之后尽快完善。").setCancelable(false)
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
    
    
    //发送微博的具体方法
    public String send(String token) throws JSONException
    {
    	
    	String target ="https://api.weibo.com/2/statuses/home_timeline.json" +
    			"?access_token="+token;
    	URL url;
    	String result = null;
    	HttpClient httpclient = new DefaultHttpClient();
    	HttpGet httpRequest = new HttpGet(target);
    	HttpResponse httpResponse;
    	
    	try {
			httpResponse = httpclient.execute(httpRequest);
			if(httpResponse.getStatusLine().getStatusCode()==HttpStatus.SC_OK)
	    	{
	    		result = EntityUtils.toString(httpResponse.getEntity());
	    	}
			else
			{
				result = "请求失败";
			}
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return result;
    }
    
    public List<Map<String ,Object>> getItem(String json) throws JSONException
    {
    	List<Map<String ,Object>> ListItem = new ArrayList<Map<String ,Object>>();
		Map<String,Object> map;
		
		//首先最外面的一层大括号{ ..... }，这个应该使用JSONObject()去获取对象
		JSONObject jsonObject = new JSONObject(json);
		//位置1的数据需要一个getJSONArray()方法去获取，因为他是一个数组，[ ]之间的每一个{ ..... }代表数组的一个元素
		JSONArray statusesArr = jsonObject.getJSONArray("statuses"); 
		for (int i = 0; i < statusesArr.length(); i++) 
		{
			//此时位置1的元素需要将其转化为JsonObject类
			JSONObject statusesObj = statusesArr.getJSONObject(i); 
			//这个时候我们就可以获取位置2的数据了
			String creat = statusesObj.getString("created_at"); 
			String text = statusesObj.getString("text");
			//位置3的数据
			String user = statusesObj.getString("user"); // 获取位置3的值
			JSONObject userObj = new JSONObject(user); // 将其转化为JSONObject
			String username = userObj.getString("name");
			String description = userObj.getString("description");// 使用get方法获取数据 			
		    map = new HashMap<String,Object>();
	        map.put("creat",creat);
	        map.put("text", text);
	        map.put("username", username);
	        map.put("description", description);
		    ListItem.add(map);
		}
		return ListItem;
    }
    //得到指令后开始加载列表信息
    private Handler handler = new Handler(){  
        @Override  
        public void handleMessage(Message msg) {  
        	super.handleMessage(msg);
        	try {
				weibos = getItem(jsondata);
				SimpleAdapter adapter=null;
				adapter = new SimpleAdapter(send_weibo.this,weibos,R.layout.weibo_item
		        		,new String[]{"description","text","username","creat"}
				        ,new int[]{R.id.mc,R.id.nr,R.id.mz,R.id.sj});
				listview.setAdapter(adapter);
				listview.setOnItemClickListener(new OnItemClickListener()
		        {
					public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(send_weibo.this,look_weibo.class);
						TextView mc = (TextView) arg1.findViewById(R.id.mc);
						TextView username = (TextView) arg1.findViewById(R.id.mz);
						TextView text = (TextView) arg1.findViewById(R.id.nr);
						Bundle bundle = new Bundle();
				        bundle.putString("mc", mc.getText().toString());
				        bundle.putString("mz", username.getText().toString());
				        bundle.putString("nr", text.getText().toString());
						intent.putExtra("bundle", bundle);
						startActivity(intent);
					}
		        	
		        });
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        }};  
    
}