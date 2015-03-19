package com.low_kb;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import listening_function.Net_state_listening;


import operation.call;
import operation.message;

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
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class look_food extends Activity{

	ListView listview=null;
	TextView resname_text=null , resphone_text=null;
	SimpleAdapter adapter=null;
	List<Map<String ,Object>> ListItem=null;
	Vector classve=null , menuve=null;
	Bundle bundle=null;
	String resid="" , resname="" , resphone="";
	ConnectivityManager cwjManager=null;
	Net_state_listening nsl = null;
	WebService ws = null;
	LinearLayout respb = null;
	Button callphone = null , sendmessage = null;
	ArrayList menuList = null; 
	private MyAdapter mSimpleAdapter=null;
	message msg = null;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.look_rice);
		nsl = new Net_state_listening(this);
		//判断是否联网
		if(!nsl.getInternet())
		{
			return;
		}
		//取得控件
		respb =  (LinearLayout) findViewById(R.id.respb);
		listview = (ListView) findViewById(R.id.rice_listview);
		listview.setCacheColorHint(Color.TRANSPARENT);
		resname_text = (TextView) findViewById(R.id.resname);
		resphone_text = (TextView) findViewById(R.id.resphone);
		callphone = (Button) findViewById(R.id.callphone);
		sendmessage = (Button) findViewById(R.id.sendmessage);
		ws = new WebService();
		menuList = new ArrayList();
		msg = new message(resphone , look_food.this , "8");
		//获取传入的数据
		Intent intent = getIntent();
        bundle = intent.getBundleExtra("bundle");
        resid = bundle.getString("id");
        resname = bundle.getString("name");
        resphone = bundle.getString("phone");
        resname_text.setText(resname);
		//resphone_text.setText("订餐电话："+resphone+"\n\n");
		//根据传入的Id进行详细信息的下载
		Thread get = new Thread(){
			public void run()
			{
				classve = get_class(resid);
				menuve = get_menu(resid);
				Message msg = new Message();
                msg.what=0;
                handler.sendMessage(msg);
			}
		};
		get.start();
		//为两个按钮设定方法
		callphone.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) {
				// TODO Auto-generated method stub
				call call = new call(resphone , look_food.this);
				call.makecall();
			}
		});
		//发送短信的方法
		sendmessage.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String dl = "";
				for(int i=0;i<menuList.size();i++)
				{
					dl+=menuList.get(i);
				}
				Intent intent = new Intent(look_food.this , subMessage.class);
				Bundle bundle = new Bundle();
		        bundle.putString("dl", dl);
		        bundle.putString("resphone", resphone);
				intent.putExtra("bundle", bundle);
				startActivity(intent);	
			}
		});
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        int group1=1;
    	menu.add(1,1,1,"电话");
    	menu.add(2,2,2,"地址");
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
			   {
				    /*Intent intent = new Intent();// 创建Intent对象
	                intent.setAction(Intent.ACTION_CALL);// 为Intent设置动作
	                intent.setData(Uri.parse("tel:" + resphone));// 为Intent设置数据
	                startActivity(intent);// 将Intent传递给Activity*/
				   call call = new call(resphone , look_food.this);
				   call.makecall();
					break;
			   }
	    	   case 2:
			   {
				   LayoutInflater factory = LayoutInflater.from(look_food.this);
				   final View textEntryView = factory.inflate(R.layout.dialog, null);
				   final EditText adress = (EditText) textEntryView.findViewById(R.id.adress);  
			       final EditText myphone = (EditText)textEntryView.findViewById(R.id.myphone);
			       adress.setText(msg.getInfo().adress); 
				   myphone.setText(msg.getInfo().myphone);
				   AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setView(textEntryView).setCancelable(false)
					.setPositiveButton("设置地址", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							if(msg.setAdress(adress.getText().toString() , myphone.getText().toString()))
							{
								Toast.makeText(look_food.this,adress.getText()+"更新成功" , Toast.LENGTH_SHORT).show();
							}
							else
							{
								Toast.makeText(look_food.this,adress.getText()+"保存成功" , Toast.LENGTH_SHORT).show();
							}
							dialog.cancel();
						}
					}).show();
					break;
			   }
	    	}
			return true;
	    }
	
	
	public Vector get_class(String resid)
	{
		//开始下载并返回数据
		Vector ve= (Vector) ws.get_class_wp(resid);
		return ve;
	} 
	public Vector get_menu(String resid)
	{
		//开始下载并返回数据
		Vector ve= (Vector) ws.get_menu_ws(resid);
		return ve;
	} 
	
	//得到指令后开始解析，加载UI
	private Handler handler = new Handler(){  
	  	  
        @Override  
        public void handleMessage(Message msg) {  
        	super.handleMessage(msg);
        	//下载开始解析并且加载
        	List<Map<String ,Object>> ListItem = new ArrayList<Map<String ,Object>>();
    		Map<String,Object> map = null;
    		/*//得到的数据包含：菜类Id, 菜类名，菜Id，菜名,flag是标记
    		int flagi=0 , flagj=0;
    		String classid="" , classname="" , menu_flag="" , menu="";
    		//这里采用二重循环的方法加载菜谱，如：炒饭->xx炒饭、xx炒饭等
    		for(int i=0;i<classve.size()-2;i+=2)
    		{
    			flagi=i;
    			classid = classve.get(flagi).toString();
    			classname = classve.get(++flagi).toString();
    			map = new HashMap<String,Object>();
    			map.put("classname", classname);
    			menu="";
    			for(int j=0;j<menuve.size()-3;j+=3)
    			{
    				flagj=j;
    				if(menuve.get(flagj).toString().equals(classid))
    				{//判断菜单属性
    					menu_flag = menuve.get(flagj).toString();
    					menu+= menuve.get(++flagj).toString()+"    "+menuve.get(++flagj).toString()+"\n";
    				}
    				else
    				{//如果不等，执行下一操作
    					flagj+=2;
    					continue;
    				}
    			}
    			map.put("menu", menu);
    	        ListItem.add(map);
    		}
        	adapter = new SimpleAdapter(look_rice.this,ListItem,R.layout.rice_item
	        		,new String[]{ "classname" , "menu" },new int[]{R.id.classname , R.id.menu});
	        respb.setVisibility(View.GONE);
	        listview.setAdapter(adapter); */
        	//以上使用TextView的方式加载
        	/***********************************************************************************************/
        	//以下使用listview方式加载
    		//得到的数据包含：菜类Id, 菜类名，菜Id，菜名,flag是标记
    		int flagi=0 , flagj=0;
    		String classid="" , classname="" , menu_flag="" , menu="";
    		//这里采用二重循环的方法加载菜谱，如：炒饭->xx炒饭、xx炒饭等
    		for(int i=0;i<classve.size()-2;i+=2)
    		{
    			//盖饭类，，炒饭类，，煮品类
    			flagi=i;
    			classid = classve.get(flagi).toString();
    			classname = classve.get(++flagi).toString();
    			map = new HashMap<String,Object>();
    			map.put("classname1", classname);
    			map.put("menu1", " ");
    			map.put("flag1", "0");
    			/*********************************/
    			ListItem.add(map);
    			map = new HashMap<String,Object>();
    			/*********************************/
    			menu="";
    			for(int j=0;j<menuve.size()-3;j+=3)
    			{
    				//xxx炒饭，，xxx盖饭
    				flagj=j;
    				if(menuve.get(flagj).toString().equals(classid))
    				{//判断菜单属性
    					menu_flag = menuve.get(flagj).toString();
    					menu= menuve.get(++flagj).toString()+"    "+menuve.get(++flagj).toString()+"\n";
    					/**********************************/
    					map = new HashMap<String,Object>();
    					map.put("classname1", " ");
    					map.put("menu1", menu);
    					map.put("flag1", "1");
    	    	        ListItem.add(map);
    					menu="";
    	    	        /*********************************/
    				}
    				else
    				{//如果不等，执行下一操作
    					flagj+=2;
    					continue;
    				}
    			}
    		}
    		/*adapter = new SimpleAdapter(look_rice.this,ListItem,R.layout.rice_item1
	        		,new String[]{ "classname1" , "menu1" , "flag1"},new int[]{R.id.classname1 , R.id.menu1 , R.id.flag1});*/
    		mSimpleAdapter = new MyAdapter(look_food.this, ListItem, R.layout.rice_item1
    				, new String[]{ "classname1" , "menu1" , "flag1"},new int[]{R.id.classname1 , R.id.menu1 , R.id.flag1});
        	respb.setVisibility(View.GONE);
        	//listview.setAdapter(adapter);
	        listview.setAdapter(mSimpleAdapter); 
	        listview.setOnItemClickListener(new OnItemClickListener()
	        {
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
					// TODO Auto-generated method stub
					TextView classname1 = (TextView) arg1.findViewById(R.id.classname1);
					TextView menu1 = (TextView) arg1.findViewById(R.id.menu1);
					TextView flag1 = (TextView) arg1.findViewById(R.id.flag1);
					CheckBox ch = (CheckBox) arg1.findViewById(R.id.check1);
			        if(flag1.getText().toString().equals("1"))
			        {	
			        	//选择的是某个菜
			        	if(!ch.isChecked())
			        	{
			        		//确定
			        		ch.toggle();
			        		mSimpleAdapter.map.put(arg2, ch.isChecked());
			        		menuList.add(menu1.getText().toString());
			        	}
			        	else
			        	{
			        		//取消
			        		ch.toggle();
			        		mSimpleAdapter.map.put(arg2, ch.isChecked());
			        		menuList.remove(menu1.getText().toString());
			        	}
			        	//Toast.makeText(look_rice.this,ch.isChecked()+"" , Toast.LENGTH_SHORT).show();
			        }
				}
	        });
        }};  
	/**********************************适配器**********************************************/
        static class ViewHolder {
    		TextView classname1=null;
    		TextView menu1=null;
    		CheckBox check1=null;
    		TextView flag1=null;
        }
        public class MyAdapter extends SimpleAdapter {
    		Map<Integer, Boolean> map; 
    		LayoutInflater mInflater;
    		ViewHolder holder;
    		TextView classname1=null;
    		TextView menu1=null;
    		CheckBox check1=null;
    		TextView flag1=null;
    		private List<? extends Map<String, ?>> mList;
    		public MyAdapter( Context context, List<Map<String, Object>> data, int resource, String[] from, int[] to) {
    		  super(context, data, resource, from, to);
    		  map = new HashMap<Integer, Boolean>();
    		  mInflater = LayoutInflater.from(context);
    		  mList = data;
    		  for(int i = 0; i < data.size(); i++) {
    		  	map.put(i, false);
    		   }
    		}	
    		@Override
    		public int getCount() {
    			return mList.size();
    		}	
    		@Override
    		public Object getItem(int position) {
    			return position;
    		}	
    		@Override
    		public long getItemId(int position) {
    			return position;
    		}		
    		@Override
    		public View getView(int position, View convertView, ViewGroup parent) {			
    			if(convertView == null) {
    				convertView = mInflater.inflate(R.layout.rice_item1, null);
    				holder = new ViewHolder();
    				holder.classname1 = (TextView) convertView.findViewById(R.id.classname1);
    				holder.menu1 = (TextView) convertView.findViewById(R.id.menu1);
    				holder.check1 = (CheckBox) convertView.findViewById(R.id.check1);
    				holder.flag1 = (TextView) convertView.findViewById(R.id.flag1);
    				convertView.setTag(holder);
    			} else {
    				holder = (ViewHolder)convertView.getTag();
    			}					
    			holder.classname1.setText((String)mList.get(position).get("classname1"));
    			holder.menu1.setText((String)mList.get(position).get("menu1"));
    			holder.check1.setChecked(map.get(position));
    			holder.flag1.setText((String)mList.get(position).get("flag1"));
    			return convertView;
    		}		
    	}
}
