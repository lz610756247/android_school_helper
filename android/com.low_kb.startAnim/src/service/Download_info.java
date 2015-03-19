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
		/********************���ؿα���������������δ����*********************/
         
		VonService vonservice = new VonService(username,  password);
		HeaderProperty Header = vonservice.lgoin();
		if(vonservice.result.contains("ѧ��")||vonservice.result.contains("��ʦ"))
		{
			result = vonservice.result;
		}
		else
		{
			result = "break";
		}
		if (vonservice.result.contains("ѧ��")) {
			admin = "student";
			String kb = vonservice.getStudentKBAsStr(Header , yat);
			file_sr les_file = new file_sr();
			les_file.save("kb", kb);
		} else {
			//ֻ��Ҫ�޸�������������ܷ���ѧ���ͽ�ʦ����Ϣ���·���������ֻ��Ҫ���ص�ǰѧ�ڵĿα���ѧ���������������ѧ�ھ�����
			admin = "teacher";
			String kb = vonservice.getTeacherKB(Header , yat);
			file_sr les_file = new file_sr();
			les_file.save("kb", kb);
		}			

		/********************���ؿα���������������δ����*********************/
		
		
		
		/********************���سɼ�,,,,,,,,,,,,,,,δ����*********************/
			String score = vonservice.getStudentScoreCard(Header);
			file_sr sco_file = new file_sr();
			sco_file.save("score", score);				
			
		/********************���سɼ�,,,,,,,,,,,,,,,δ����*********************/
		//���سɹ�,����ȷ��
		return true;
	}
	
	public boolean getKB()
	{
		VonService vonservice = new VonService(username,  password);
		HeaderProperty les_Header = vonservice.lgoin();
		if(vonservice.result.contains("ѧ��")||vonservice.result.contains("��ʦ"))
		{
			result = vonservice.result;
		}
		else
		{
			result = "break";
			return false;
		}
		if (vonservice.result.contains("ѧ��")) {
			admin = "student";
			String kb = vonservice.getStudentKBAsStr(les_Header , yat);
			file_sr les_file = new file_sr();
			les_file.save("kb", kb);
			return true;
		} else {
			//ֻ��Ҫ�޸�������������ܷ���ѧ���ͽ�ʦ����Ϣ���·���������ֻ��Ҫ���ص�ǰѧ�ڵĿα���ѧ���������������ѧ�ھ�����
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
		if(vonservice.result.contains("ѧ��"))
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
