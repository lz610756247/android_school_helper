package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.os.Environment;

/**
 * @author zhq �ļ��Ĵ洢�ڶ�ȡ��
 */
public class file_sr {

	/**
	 * �洢�ļ�
	 * 
	 * @param name
	 *            �ļ�����
	 * @param resource
	 *            ��Ҫ�洢������
	 */
	public void save(String name, String resource) {

		String fileName = name + ".txt";

		try {
			File file = new File("/data/data/com.low_kb/",fileName);
			if(file.exists())
			{
				file.delete();
			}
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(resource.getBytes());
			fos.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}	

}
