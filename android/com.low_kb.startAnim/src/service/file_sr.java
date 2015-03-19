package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import android.os.Environment;

/**
 * @author zhq 文件的存储于读取类
 */
public class file_sr {

	/**
	 * 存储文件
	 * 
	 * @param name
	 *            文件名称
	 * @param resource
	 *            需要存储的数据
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
