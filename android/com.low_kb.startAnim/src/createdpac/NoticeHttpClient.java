package createdpac;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import util.Regex;

import android.os.Environment;
import android.util.Log;
import android.webkit.WebView;

public class NoticeHttpClient {
	WebView webview;
	static HttpClient httpClient;
	String url;
	
	public HttpClient getHttpclient() {
		if(httpClient == null){
			httpClient = new DefaultHttpClient();
		}
		return httpClient;
	}

	public void setHttpclient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public NoticeHttpClient(){
	}
	
	private void saveFileToSDcard(String str){
		File saveFile=new File(Environment.getExternalStorageDirectory().toString()+"//caorui.txt");
    	try {
    		Log.e("Environment.getExternalStorageDirectory()",Environment.getExternalStorageDirectory().toString());
			BufferedWriter output = new BufferedWriter(new FileWriter(saveFile));
			output.write(str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
	}
	
	public String createHttpClient(String url){
		HttpClient httpClient = getHttpclient();
		HttpGet httpRequest = new HttpGet(url);
		String html = null;
		try {
			HttpResponse httpResponse = httpClient.execute(httpRequest);
			html = EntityUtils.toString(httpResponse.getEntity());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			Log.e("ClientProtocolException", e.getMessage());
			return "Client Error!";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("IOException", e.getMessage());
			return "Merrory Error or Server Error!";
		}         
		return html;
	}
	
	private String regexUrl(String url){
		return Regex.regexUrl(url);
	}
	
	private String regexHTML(String html){
		return Regex.regexHTML(html);
	}
	
	public void init()
	{
		Regex.init();	
	}
		
	public String startWork(String url)
	{
		//根据URL得到正确的URL
		url = regexUrl(url);
		//初始化HttpClient
		String html = createHttpClient(url);	
		//页面适应处理规则
		String regexedHTML = regexHTML(html);
		return regexedHTML;	
	}
}
