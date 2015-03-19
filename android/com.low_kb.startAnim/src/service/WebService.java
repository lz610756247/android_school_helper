package service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.os.Message;
import android.util.Log;

public class WebService {
	
	public WebService()
	{
		
	}
	
	
	
	//����ϵͳ����
	public String getJWGG()
	{
		String getJWGG = "";
		String wsdlurl = "http://116.55.243.13:80/Notice/services/Result";
		String webmethod = "getJWGG";
		String namespace = "http://notice";
		String soapaction = namespace + webmethod;
		SoapObject request= new SoapObject(namespace,webmethod);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				getJWGG = envelope.getResponse().toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getJWGG;
	}
	
	//�õ�����ϵͳ��������
	public String getJWGG_detail(String url)
	{
		String getJWGG_detail = "";
		String wsdlurl = "http://116.55.243.13:80/Notice/services/Result";
		String webmethod = "getJWGG_detail";
		String namespace = "http://notice";
		String soapaction = namespace + webmethod;
		SoapObject request= new SoapObject(namespace,webmethod);
		request.addProperty("url",url);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				getJWGG_detail = envelope.getResponse().toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getJWGG_detail;
	}
	
	
	//�õ�����������
	public String getXWWGG()
	{
		String getXWWGG = "";
		String wsdlurl = "http://116.55.243.13:80/Notice/services/Result";
		String webmethod = "getXWWGG";
		String namespace = "http://notice";
		String soapaction = namespace + webmethod;
		SoapObject request= new SoapObject(namespace,webmethod);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				getXWWGG = envelope.getResponse().toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getXWWGG;
	}
	
	
	//�õ���������������
	public String getXWWGG_detail(String url)
	{
		String getXWWGG_detail = "";
		String wsdlurl = "http://116.55.243.13:80/Notice/services/Result";
		String webmethod = "getXWWGG_detail";
		String namespace = "http://notice";
		String soapaction = namespace + webmethod;
		SoapObject request= new SoapObject(namespace,webmethod);
		request.addProperty("url",url);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				getXWWGG_detail = envelope.getResponse().toString();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return getXWWGG_detail;
	}
	
	
	//�����г��ĵõ���Ʒ����
	public Vector getWp_ws()
	{
		Vector result=null;
		String wsdlurl = "http://116.55.243.13:80/Mywebservice/services/Object";
		String webmethod = "getob";
		String namespace = "http://test.com";
		String soapaction = namespace + webmethod;
		SoapObject request = new SoapObject(namespace, webmethod);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;

		HttpTransportSE se = new HttpTransportSE(wsdlurl);
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse() != null) {
				result = (Vector) envelope.getResponse();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	//�ϴ���Ʒ��Ϣ
	public boolean submit_goods_ws(String name , String price , String user , String tel)
	{
		String wsdlurl = "http://116.55.243.13:80/Mywebservice/services/Object";
		String webmethod = "postnew";
		String namespace = "http://test.com";
		String soapaction = namespace + webmethod;
		SoapObject request = new SoapObject(namespace, webmethod);
		// String name,int price,String user,String tel
		request.addProperty("name", name);
		request.addProperty("price", price);
		request.addProperty("user", user);
		request.addProperty("tel", tel);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.bodyOut = request;
		HttpTransportSE se = new HttpTransportSE(wsdlurl);
		Vector result = new Vector();
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse() != null) {
				result = (Vector) envelope.getResponse();
				System.out.print(result);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
	
	//�õ��͹ݵķ���
	public Vector get_res_ws(int lflag)
	{
		String wsdlurl = "http://116.55.243.13:80/school_res/services/restest";
		String webmethod = "getRes";
		String namespace = "http://res";
		String soapaction = namespace + webmethod;
		SoapObject request= new SoapObject(namespace,webmethod);
        request.addProperty("location",lflag);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				 ob = envelope.getResponse();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector ve= (Vector) ob;
		return ve;
	}
	//�õ�����ķ���
	public Vector get_class_wp(String resid)
	{
		String wsdlurl = "http://116.55.243.13:80/school_res/services/restest";
		String webmethod_class = "getClass";
		String namespace = "http://res";
		String soapaction = namespace + webmethod_class;
		SoapObject request= new SoapObject(namespace,webmethod_class);
        request.addProperty("resid",resid);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				 ob = envelope.getResponse();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector ve= (Vector) ob;
		return ve;
	}
	//�õ��˵��ķ���
	public Vector get_menu_ws(String resid)
	{
		String wsdlurl = "http://116.55.243.13:80/school_res/services/restest";
		String webmethod_menu = "getMenu";
		String namespace = "http://res";
		String soapaction = namespace + webmethod_menu;
		SoapObject request= new SoapObject(namespace,webmethod_menu);
        request.addProperty("resid",resid);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				 ob = envelope.getResponse();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Vector ve= (Vector) ob;
		return ve;
	}
	
	//�ɼ�����
	public ArrayList update_score(ArrayList score_xkbh)
	{
		String state = "";
		int length = 0;		
		//������ñ������������гɼ���Ϣ
		while(length <score_xkbh.size())
		{
			try 
			{
				SoapObject request2 = new SoapObject("http://www.ckpark.net/jw", "getScoreState");
				request2.addProperty("xkbh", score_xkbh.get(length));
				SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				ssEnvelope.bodyOut = request2;
				ssEnvelope.dotNet = true;
				ssEnvelope.setOutputSoapObject(request2);
				HttpTransportSE httpSE = new HttpTransportSE("http://116.55.243.13/VonService.asmx");
				List<HeaderProperty> reqHeaders2 = new LinkedList<HeaderProperty>();
				httpSE.call("http://www.ckpark.net/jw/getScoreState",
						ssEnvelope, reqHeaders2);
				Object object2 = ssEnvelope.getResponse();
				state = object2.toString();
				Log.d("http_update_score", "score_update xkbh "+score_xkbh.get(length) + state);
				/*��ȡ����Ϣ��������ʾ,¼����,���ύ,���������ύ*/
				//����ɼ��Ѿ���û���ύ
				Log.i("score_state", state);
				if(state.contains("���ύ"))
				{
					length++;
					continue;					
				}
				else
				{
					score_xkbh.remove(length);
				}				
				
			} catch (Exception e)
			{
				e.printStackTrace();
			}
			length=0;
		}
		return score_xkbh;
	}
	
	//��ȡ����
	public String getName(String username , String password)
	{
		String str = "";
		String wsdlurl = "http://116.55.243.13:80/c/services/Com?wsdl";
		String webmethod = "login";
		String namespace = "http://com";
		String soapaction = namespace + webmethod;
		SoapObject request= new SoapObject(namespace,webmethod);
		request.addProperty("username", username);
		request.addProperty("password", password);
		SoapSerializationEnvelope envelope= new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.bodyOut=request;
		HttpTransportSE se= new HttpTransportSE(wsdlurl);
		Object ob =null;
		try {
			se.call(soapaction, envelope);
			if (envelope.getResponse()!=null){
				ob = envelope.getResponse();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(ob.toString());
		return ob.toString();
	}

}
