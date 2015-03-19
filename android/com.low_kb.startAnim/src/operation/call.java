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
		Intent intent = new Intent();// ����Intent����
        intent.setAction(Intent.ACTION_CALL);// ΪIntent���ö���
        intent.setData(Uri.parse("tel:" + phone));// ΪIntent��������
        context.startActivity(intent);// ��Intent���ݸ�Activity
	}

}
