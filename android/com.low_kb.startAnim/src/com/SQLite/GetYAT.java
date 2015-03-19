package com.SQLite;

public class GetYAT 
{
	String yat="" , flag="";
	public GetYAT(String yat , String flag)
	{
		this.yat = yat;
		this.flag = flag;
	}
	public String getYAT()
    {
		//获取的数据为2011-2012第二学期
	    //存入数据库的信息为201120122，通过以下转换得到
    	String s = yat;
    	if(flag.equals("1"))
    	{
    		//什么都不做
    	}
    	else
    	{
    		s = yat.substring(0, 4);
	    	s+=yat.substring(5, 9);
	    	if(yat.substring(12, 13).equals("一"))
	    	{
	    		s+=1;
	    	}
	    	else
	    	{
	    		s+=2;
	    	}
    	}
    	
		return s;
    	
    }

}
