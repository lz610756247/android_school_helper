package service;

import java.io.InputStream;
import java.util.LinkedList;

import java.util.List;
import java.util.Stack;

import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.util.Log;

public class VonService {
	String name="";
	String password="";
	public String result="";
	public String nameSpace = "http://www.vontao.com/ynufe/jw";
	public String serviceURL = "http://202.203.194.10/scorem/vonservice.asmx";

	public VonService() {

	}

	public VonService(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	@SuppressWarnings("unchecked")
	public HeaderProperty lgoin() {

		String methodName = "userLogin";
		HeaderProperty mSessionHeader = null;

		SoapObject request = new SoapObject(nameSpace, methodName);
		request.addProperty("name", name);
		request.addProperty("password", password);
		SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		ssEnvelope.dotNet = true;
		ssEnvelope.bodyOut = request;
		ssEnvelope.setOutputSoapObject(request);
		HttpTransportSE httpSE = new HttpTransportSE(serviceURL);
		httpSE.debug = true;

		// Build request header
		List<HeaderProperty> reqHeaders = new LinkedList<HeaderProperty>();

		try {
			reqHeaders = httpSE.call(null, ssEnvelope, reqHeaders);

			// See note after code
			for (HeaderProperty headProperty : reqHeaders) {
				if (headProperty.getKey() != null
						&& headProperty.getKey().equalsIgnoreCase("set-cookie")) {
					mSessionHeader = headProperty;

					Object object = ssEnvelope.getResponse();
					result = object.toString();

				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return mSessionHeader;
	}

	// -----------------------
	public String getTeacherKB(HeaderProperty mSessionHeader , String yat) {

		String TCKBresource = "";
		String methodName = "getTeacherKBAsStr";

		SoapObject request2 = new SoapObject(nameSpace, methodName);
		request2.addProperty("term", yat);
		//request2.addProperty("term", null);
		SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		ssEnvelope.bodyOut = request2;
		ssEnvelope.dotNet = true;
		ssEnvelope.setOutputSoapObject(request2);
		HttpTransportSE httpSE = new HttpTransportSE(serviceURL);
		List<HeaderProperty> reqHeaders2 = new LinkedList<HeaderProperty>();
		if (mSessionHeader != null) {
			reqHeaders2.add(new HeaderProperty("Cookie", mSessionHeader
					.getValue()));
		}

		try {
			httpSE.call("http://www.vontao.com/ynufe/jw/getTeacherKBAsStr",
					ssEnvelope, reqHeaders2);
			Object object2 = ssEnvelope.getResponse();
			TCKBresource = object2.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return TCKBresource;
	}

	// -------------------------------
	public String getStudentKBAsStr(HeaderProperty mSessionHeader , String yat) {

		String KBresource = "";
		String methodName = "getStudentKBAsStr";

		SoapObject request2 = new SoapObject(nameSpace, methodName);
		request2.addProperty("term", yat);
		request2.addProperty("term", null);
		SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		ssEnvelope.bodyOut = request2;
		ssEnvelope.dotNet = true;
		ssEnvelope.setOutputSoapObject(request2);
		HttpTransportSE httpSE = new HttpTransportSE(serviceURL);
		List<HeaderProperty> reqHeaders2 = new LinkedList<HeaderProperty>();
		if (mSessionHeader != null) {
			reqHeaders2.add(new HeaderProperty("Cookie", mSessionHeader
					.getValue()));
		}

		try {
			httpSE.call("http://www.vontao.com/ynufe/jw/getStudentKBAsStr",
					ssEnvelope, reqHeaders2);
			Object object2 = ssEnvelope.getResponse();
			KBresource = object2.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return KBresource;
	}

	public String getStudentScoreCard(HeaderProperty mSessionHeader) {
		String methodName = "getStuScoreCard";
		String scoreResource = "";

		SoapObject request3 = new SoapObject(nameSpace, methodName);
		SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		ssEnvelope.bodyOut = request3;
		ssEnvelope.dotNet = true;
		ssEnvelope.setOutputSoapObject(request3);
		HttpTransportSE httpSE = new HttpTransportSE(serviceURL);

		List<HeaderProperty> reqHeaders3 = new LinkedList<HeaderProperty>();
		if (mSessionHeader != null) {
			reqHeaders3.add(new HeaderProperty("Cookie", mSessionHeader
					.getValue()));

		} else
			Log.i("mSessionHeader", "mSessionHeader为空！");
		try {
			httpSE.call("http://www.vontao.com/ynufe/jw/getStuScoreCard",
					ssEnvelope, reqHeaders3);

			Object object3 = ssEnvelope.getResponse();
			scoreResource = object3.toString();
			Log.i("成绩：", scoreResource);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return scoreResource;
	}

	public String getAPKUrl(HeaderProperty mSessionHeader) {
		String methodName = "getAPKUrl";
		String APKUrlResource = null;

		SoapObject request4 = new SoapObject(nameSpace, methodName);
		SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		ssEnvelope.bodyOut = request4;
		ssEnvelope.dotNet = true;
		ssEnvelope.setOutputSoapObject(request4);
		HttpTransportSE httpSE = new HttpTransportSE(serviceURL);

		List<HeaderProperty> reqHeaders4 = new LinkedList<HeaderProperty>();
		if (mSessionHeader != null) {
			reqHeaders4.add(new HeaderProperty("Cookie", mSessionHeader
					.getValue()));
		} else
			Log.i("mSessionHeader", "mSessionHeader为空！");
		try {
			httpSE.call("http://www.vontao.com/ynufe/jw/getAPKUrl", ssEnvelope,
					reqHeaders4);

			Object object4 = ssEnvelope.getResponse();
			APKUrlResource = object4.toString();
			Log.i("url:", APKUrlResource);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return APKUrlResource;
	}

	public String getVersion(HeaderProperty mSessionHeader) {
		String methodName = "getVersion";
		String VersionResource = null;

		SoapObject request5 = new SoapObject("http://www.ckpark.net/jw", methodName);
		request5.addProperty("platform", "android");
		SoapSerializationEnvelope ssEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		ssEnvelope.bodyOut = request5;
		ssEnvelope.dotNet = true;
		ssEnvelope.setOutputSoapObject(request5);
		HttpTransportSE httpSE = new HttpTransportSE("http://116.55.243.13/vonservice.asmx");

		List<HeaderProperty> reqHeaders5 = new LinkedList<HeaderProperty>();
		if (mSessionHeader != null) {
			reqHeaders5.add(new HeaderProperty("Cookie", mSessionHeader
					.getValue()));
		} else
			Log.i("mSessionHeader", "mSessionHeader为空！");
		try {
			httpSE.call("http://www.ckpark.net/jw/getVersion",
					ssEnvelope, reqHeaders5);

			Object object5 = ssEnvelope.getResponse();
			VersionResource = object5.toString();
			Log.i("VersionResource:", VersionResource);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return VersionResource;
	}

}
