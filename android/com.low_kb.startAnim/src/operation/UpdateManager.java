package operation;

import java.io.File;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.MalformedURLException;  
import java.net.URL;  

import service.VonService;

import com.low_kb.R;
  
  
import android.app.AlertDialog;  
import android.app.Dialog;  
import android.app.AlertDialog.Builder;  
import android.content.Context;  
import android.content.DialogInterface;  
import android.content.Intent;  
import android.content.DialogInterface.OnClickListener;  
import android.net.Uri;  
import android.os.Handler;  
import android.os.Message;  
import android.view.LayoutInflater;  
import android.view.View;  
import android.widget.ProgressBar;

public class UpdateManager {  
	  
    private Context mContext;  
    
    //��ǰ�汾
    private static final String version = "1.0.1";
    VonService vonservice = new VonService("201105002361",  "19930423");
      
    //��ʾ��  
    private String updateMsg = "������°汾��~";  
      
    //���صİ�װ��url  
    private String apkUrl = "http://www.ckpark.net/ychz/Download_files/Android/YE_BOX_1.0.2.apk";  
      
      
    private Dialog noticeDialog;  
      
    private Dialog downloadDialog;  
     /* ���ذ���װ·�� */  
    private static final String savePath = "/sdcard/�Ʋƺ���/";  
      
    private static String saveFileName = savePath + "YE_BOX_1.0.1.apk";  
  
    /* ��������֪ͨuiˢ�µ�handler��msg���� */  
    private ProgressBar mProgress;  
  
      
    private static final int DOWN_UPDATE = 1;  
      
    private static final int DOWN_OVER = 2;  
      
    private int progress;  
      
    private Thread downLoadThread;  
      
    private boolean interceptFlag = false;  
      
    private Handler mHandler = new Handler(){  
        public void handleMessage(Message msg) {  
            switch (msg.what) {  
            case DOWN_UPDATE:  
                mProgress.setProgress(progress);  
                break;  
            case DOWN_OVER:  
                  
                installApk();  
                break;  
            default:  
                break;  
            }  
        };  
    };  
      
    public UpdateManager(Context context) {  
        this.mContext = context;  
    }  
      
    //�ⲿ�ӿ�����Activity����  
    public boolean checkUpdateInfo()
    {  
    	String temp_ver = vonservice.getVersion(vonservice.lgoin());
    	if(!version.equals(temp_ver))
    	{    		
    		this.apkUrl = "http://www.ckpark.net/ychz/Download_files/Android/YE_BOX_"+temp_ver+".apk";
    		this.saveFileName = savePath + "YE_BOX_"+temp_ver+".apk";
    		showNoticeDialog();
    		return true;
    	}
        return false;        
    }  
      
      
    private void showNoticeDialog(){  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("����汾����");  
        builder.setMessage(updateMsg);  
        builder.setPositiveButton("����", new OnClickListener() {           
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                showDownloadDialog();             
            }  
        });  
        builder.setNegativeButton("�Ժ���˵", new OnClickListener() {             
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();                 
            }  
        });  
        noticeDialog = builder.create();  
        noticeDialog.show();  
    }  
      
    private void showDownloadDialog(){  
        AlertDialog.Builder builder = new Builder(mContext);  
        builder.setTitle("����汾����");  
          
        final LayoutInflater inflater = LayoutInflater.from(mContext);  
        View v = inflater.inflate(R.layout.progress, null);  
        mProgress = (ProgressBar)v.findViewById(R.id.progress);  
          
        builder.setView(v);  
        builder.setNegativeButton("ȡ��", new OnClickListener() {   
            public void onClick(DialogInterface dialog, int which) {  
                dialog.dismiss();  
                interceptFlag = true;  
            }  
        });  
        downloadDialog = builder.create();  
        downloadDialog.show();  
          
        downloadApk();  
    }  
      
    private Runnable mdownApkRunnable = new Runnable() {      
        public void run() {  
            try {  
                URL url = new URL(apkUrl);  
              
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();  
                conn.connect();  
                int length = conn.getContentLength();  
                InputStream is = conn.getInputStream();  
                  
                File file = new File(savePath);  
                if(!file.exists()){  
                    file.mkdir();  
                }  
                String apkFile = saveFileName;  
                File ApkFile = new File(apkFile);  
                FileOutputStream fos = new FileOutputStream(ApkFile);  
                  
                int count = 0;  
                byte buf[] = new byte[1024];  
                  
                do{                   
                    int numread = is.read(buf);  
                    count += numread;  
                    progress =(int)(((float)count / length) * 100);  
                    //���½���  
                    mHandler.sendEmptyMessage(DOWN_UPDATE);  
                    if(numread <= 0){      
                        //�������֪ͨ��װ  
                        mHandler.sendEmptyMessage(DOWN_OVER);  
                        break;  
                    }  
                    fos.write(buf,0,numread);  
                }while(!interceptFlag);//���ȡ����ֹͣ����.  
                  
                fos.close();  
                is.close();  
            } catch (MalformedURLException e) {  
                e.printStackTrace();  
            } catch(IOException e){  
                e.printStackTrace();  
            }  
              
        }  
    };  
      
     /** 
     * ����apk 
     * @param url 
     */  
      
    private void downloadApk(){  
        downLoadThread = new Thread(mdownApkRunnable);  
        downLoadThread.start();  
    }  
     /** 
     * ��װapk 
     * @param url 
     */  
    private void installApk(){  
        File apkfile = new File(saveFileName);  
        if (!apkfile.exists()) {  
            return;  
        }      
        Intent i = new Intent(Intent.ACTION_VIEW);  
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");   
        mContext.startActivity(i);        
    }  
}  
