package com.SQLite;

public class TabName 
{
	public String getTableName(String username , String mod , String yat)
    {
    	//mod����ʽΪ"les",����"sco"
		//���صı���Ϊ������les201105002361201120122  ����  sco201105002361201120122
		String tablename="";
    	tablename = mod+username+yat;
    	return tablename;
    }

}
