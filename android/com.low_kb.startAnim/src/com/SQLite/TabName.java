package com.SQLite;

public class TabName 
{
	public String getTableName(String username , String mod , String yat)
    {
    	//mod的形式为"les",或者"sco"
		//返回的表名为，例：les201105002361201120122  或者  sco201105002361201120122
		String tablename="";
    	tablename = mod+username+yat;
    	return tablename;
    }

}
