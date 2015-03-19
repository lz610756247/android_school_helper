package service;

import android.content.Context;
import android.webkit.CookieSyncManager;

import com.weibo.net.Utility;
import com.weibo.net.Weibo;
import com.weibo.net.WeiboParameters;

/***
 * 
 * @author
 * 
 */
public class MyWeiboManager {

	/***
	 * 获取OauthURl
	 * 
	 * @return
	 */
	public static String getOauthURL(Context context) {

		CookieSyncManager.createInstance(context);

		WeiboParameters parameters = new WeiboParameters();
		parameters.add("client_id", Weibo.getInstance().getAppKey());
		parameters.add("response_type", "token");
		parameters.add("redirect_uri", Weibo.getInstance().getRedirectUrl());
		parameters.add("display", "mobile");

		return Weibo.URL_OAUTH2_ACCESS_AUTHORIZE + "?"
				+ Utility.encodeUrl(parameters);

	}

}
