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
		//��ȡ������Ϊ2011-2012�ڶ�ѧ��
	    //�������ݿ����ϢΪ201120122��ͨ������ת���õ�
    	String s = yat;
    	if(flag.equals("1"))
    	{
    		//ʲô������
    	}
    	else
    	{
    		s = yat.substring(0, 4);
	    	s+=yat.substring(5, 9);
	    	if(yat.substring(12, 13).equals("һ"))
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
