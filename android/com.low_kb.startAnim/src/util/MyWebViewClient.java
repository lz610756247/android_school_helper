package util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * 重写webclient，使得页面的URL都用自己的webview打开
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
    	 * url由于loadData方法，解析会有不同
    	 * loadUrl，可以得到http：//
    	 * loadData，http://会被省略
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
		progressDialog = ProgressDialog.show(this.context, "请稍等", "正在加载", true, false);
	}         
}
