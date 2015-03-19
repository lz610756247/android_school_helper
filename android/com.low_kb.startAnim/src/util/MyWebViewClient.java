package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * ��дwebclient��ʹ��ҳ���URL�����Լ���webview��
 */
public class MyWebViewClient extends WebViewClient
{
	ProgressDialog progressDialog;
	Context context;
	public MyWebViewClient(Context context){
		this.context = context;
	}
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url)
    {
    	/**
    	 * url����loadData�������������в�ͬ
    	 * loadUrl�����Եõ�http��//
    	 * loadData��http://�ᱻʡ��
    	 */
    	view.loadUrl(url);          	         	
    	return true;
    }

	@Override
	public void onPageFinished(WebView view, String url) {
		// TODO Auto-generated method stub				
		Log.e("WebView","onPageFinished ");
		progressDialog.dismiss();			
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(this.context, "���Ե�", "���ڼ���", true, false);
	}         
}
