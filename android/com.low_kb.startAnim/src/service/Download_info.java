package service;

import org.ksoap2.HeaderProperty;

import service.VonService;
import service.file_sr;



public class Download_info {
	public String admin="";
	public String username ="",password="",yat="";
	public String result="";
	
	public Download_info(String username ,String password,String yat)
	{
		this.username = username;
		this.password = password;
		this.yat = yat;
	}
	
	public boolean run() 
	{
		// TODO Auto-generated method stub
		/********************下载课表，，，，，，，，未解析*********************/
         
		VonService vonservice = new VonService(username,  password);
		HeaderProperty Header = vonservice.lgoin();
		if(vonservice.result.contains("学生")||vonservice.result.contains("教师"))
		{
			result = vonservice.result;
		}
		else
		{
			result = "break";
		}
		if (vonservice.result.contains("学生")) {
			admin = "student";
			String kb = vonservice.getStudentKBAsStr(Header , yat);
			file_sr les_file = new file_sr();
			les_file.save("kb", kb);
		} else {
			//只需要修改这个方法，就能分离学生和教师的信息，新方法在这里只需要返回当前学期的课表，在学生方法参数里加上学期就行了
			admin = "teacher";
			String kb = vonservice.getTeacherKB(Header , yat);
			file_sr les_file = new file_sr();
			les_file.save("kb", kb);
		}			

		/********************下载课表，，，，，，，，未解析*********************/
		
		
		
		/********************下载成绩,,,,,,,,,,,,,,,未解析*********************/
			String score = vonservice.getStudentScoreCard(Header);
			file_sr sco_file = new file_sr();
			sco_file.save("score", score);				
			
		/********************下载成绩,,,,,,,,,,,,,,,未解析*********************/
		//下载成功,返回确定
		return true;
	}
	
	public boolean getKB()
	{
		VonService vonservice = new VonService(username,  password);
		HeaderProperty les_Header = vonservice.lgoin();
		if(vonservice.result.contains("学生")||vonservice.result.contains("教师"))
		{
			result = vonservice.result;
		}
		else
		{
			result = "break";
			return false;
		}
		if (vonservice.result.contains("学生")) {
			admin = "student";
			String kb = vonservice.getStudentKBAsStr(les_Header , yat);
			file_sr les_file = new file_sr();
			les_file.save("kb", kb);
			return true;
		} else {
			//只需要修改这个方法，就能分离学生和教师的信息，新方法在这里只需要返回当前学期的课表，在学生方法参数里加上学期就行了
			admin = "teacher";
			String kb = vonservice.getTeacherKB(les_Header , yat);
			file_sr les_file = new file_sr();
			les_file.save("kb", kb);
			return true;
		}	
	}
	public boolean getSCO()
	{
		VonService vonservice = new VonService(username,  password);
		HeaderProperty sco_Header = vonservice.lgoin();
		String score = vonservice.getStudentScoreCard(sco_Header);
		if(vonservice.result.contains("学生"))
		{
			result = vonservice.result;
			file_sr sco_file = new file_sr();
			sco_file.save("score", score);
			return true;
		}
		else
		{
			result = "break";
			return false;
		}
	}
	
	public String getAdmin()
	{
		return admin;
	}
	
	public String getResult()
	{
		return result;
	}
	
}
