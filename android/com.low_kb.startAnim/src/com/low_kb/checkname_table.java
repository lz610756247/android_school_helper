package com.low_kb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class checkname_table extends Activity {

	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkname);
        String name[] = new String[]{"曹瑞剑雄","常明远","李明","林赛姬","刘大海","张强","a","s","d","f","g","z","x","c","v","q","w","e","r"};
        ListView name_listview = (ListView) findViewById(R.id.checkname_listview);
       
        List<Map<String ,Object>> ListItem = new ArrayList<Map<String ,Object>>();;
        Map<String,Object> map;
        int i=0;
        while(i<name.length)
        {
        	map = new HashMap<String,Object>();
	        map.put("name",name[i]);
	        ListItem.add(map);
        	i++;
        }
        SimpleAdapter adapter = new SimpleAdapter(this,ListItem,R.layout.checkname_item
        		,new String[]{"name"},new int[]{R.id.name});
        name_listview.setAdapter(adapter);
        
        
        
        
        
        name_listview.setOnItemClickListener(new OnItemClickListener()
        {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Toast.makeText(checkname_table.this,"aaaaaaaas" , Toast.LENGTH_SHORT).show();
				String res = arg0.getItemAtPosition(arg2).toString();
				final TextView content=(TextView)arg1.findViewById(R.id.nocome);
				CheckBox cb = (CheckBox) arg1.findViewById(R.id.check);
				boolean str = cb.isChecked();
				Toast.makeText(checkname_table.this,str+"" , Toast.LENGTH_SHORT).show();
				int i=0;
				if(!str)
				{
					content.setText("缺席"+(++i)+"次");
				}
				else
				{
					
				}
				cb.setOnCheckedChangeListener(new OnCheckedChangeListener()
		        {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub				
						
						if(!isChecked)
						{
							int i=0;
							//content.setText("缺席"+(++i)+"次");
							Toast.makeText(checkname_table.this,"aaaaaaaas" , Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(checkname_table.this,"xxxxxxx" , Toast.LENGTH_SHORT).show();
						}
						
					}
		        	
		        });
				 
				
			}
        	
        });
        
        
        BaseAdapter adapter1 = new BaseAdapter()
        {

			public int getCount() {
				// TODO Auto-generated method stub
				return 0;
			}

			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return null;
			}

			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return 0;
			}

			public View getView(int position, View convertView, ViewGroup parent) 
			{				
				// TODO Auto-generated method stub
				if(convertView==null){}
				else
				{
					
				}
				return null;
			}
        	
        };
        	
       
    }

   
}
