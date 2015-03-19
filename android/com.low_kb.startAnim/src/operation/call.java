package operation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class call {
	private String phone = "";
	private Context context = null;
	public call(String phone , Context context)
	{
		this.context = context;
		this.phone = phone;
	}
	public void makecall()
	{
		Intent intent = new Intent();// 创建Intent对象
        intent.setAction(Intent.ACTION_CALL);// 为Intent设置动作
        intent.setData(Uri.parse("tel:" + phone));// 为Intent设置数据
        context.startActivity(intent);// 将Intent传递给Activity
	}

}
