package operation;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;

import com.low_kb.lesson_table;



public class kb_opreation {
	/*
	 * ���ǿα�Ĳ����࣬��������HTML���Ҳ�ȡ�������ǣ�ÿһ�ſα���ʵ���Կ���һ�Ŷ�ά���ַ������
	 * ��Ȼ����ÿһ���γ��ж����Ͽε�ʱ�䣬���������ܡ������ڡ�3��,4�ڣ���ʵҲ���ǿγ��ڶ�ά���еĶ�λ���꣬
	 * ��ά�����Ĭ�ϵ�ȫ�ֱ�����������ֻ��Ҫ��ÿ�δ���Ŀγ���ӽ������Ϳ��Եõ�һ�������Ŀγ̱�
	 */
	public boolean done = false ;
	String str = "";
	String kb[][] ;
	String date[] ;
	String date1[] ;
	ChangeDay cd ;
	/*HTMLͷ����Ϣ*/
	private String kbStr = "<table cellspacing='0px' style='width:1200px; height:300px;'>";
	//Ϊ�˱�֤���ݵ��ȶ������ǲ��Ķ�ԭʼֵ��������ʹ���м�ֵ��Ҳ�����±�����
	//setKB()�����С�
	String mid_time = null , endStr_time = null;
	String mid_address = null , endStr_address = null;
	int start_time = 0 , start_address = 0;
	int day = 0 , time = 0;
	public kb_opreation()
	{
		/*��ά�α�ĳ�ʼ��*/
		kb = new String[7][8];
		/*������ںͿ�ʱ���*/
		date = new String[]{"ʱ��/����","����һ","���ڶ�","������","������","������","������","������"};
		date1 = new String[]{" ","1","3","6","8","10","12"};
		cd = new ChangeDay();
		for(int i=0;i<8;i++)
		{
			kb[0][i] = date[i];
		}
		for(int i=1;i<7;i++)
		{
			kb[i][0] = date1[i];
		}
	}
	public void setKB(String lesson_time , String lesson_other , String lesson_address)
	{
		//Ϊ�˱�֤���ݵ��ȶ������ǲ��Ķ�ԭʼֵ��������ʹ���м�ֵ��Ҳ�����±�����
		
		if(lesson_time.contains("ͣ��"))		//���ⲻ�ɴ�������
		{
			return;
		}
		
		mid_time = lesson_time;
		endStr_time = lesson_time;
		mid_address = lesson_address;
		endStr_address = lesson_address;
		start_time = 0;
		start_address = 0;
		day = 0 ;
		time = 0;		
		//��ʼ�Կγ̽��н�ȡ�����
		while(endStr_time.contains("|"))
		{
			mid_time = lesson_time.substring(start_time).substring(0 , lesson_time.indexOf("|"));
			mid_address = lesson_address.substring(start_address).substring(0 , lesson_address.indexOf("|"));
			start_time+=mid_time.length()+1;
			start_address+=mid_address.length()+1;
			endStr_time = lesson_time.substring(start_time);
			endStr_address = lesson_address.substring(start_address);
			//���
			cd.setDay(mid_time.substring(mid_time.indexOf("��")+1, mid_time.indexOf("��")+2));//ת����
			day = cd.getDay();
			cd.setDay(mid_time.substring(mid_time.indexOf("��")+1, mid_time.indexOf(",")));//ת��ʱ��
			time = cd.getDay();
			if(time>=6)
			{
				time = time/2;
			}
			else
			{
				if(time==3)
					time = 2;
				//1����Ҫ���⴦��
			}
			if(kb[time][day]==null)
			{
				kb[time][day] = "";
				kb[time][day] = mid_time+"--"+mid_address+lesson_other;
			}
			else
			{
				kb[time][day] += mid_time+"--"+mid_address+lesson_other;
			}
		}
		mid_time = lesson_time.substring(lesson_time.lastIndexOf("|")+1);
		mid_address = lesson_address.substring(lesson_address.lastIndexOf("|")+1);
		//���
		cd.setDay(mid_time.substring(mid_time.indexOf("��")+1, mid_time.indexOf("��")+2));//ת����
		day = cd.getDay();
		cd.setDay(mid_time.substring(mid_time.indexOf("��")+1, mid_time.indexOf(",")));//ת��ʱ��
		time = cd.getDay();
		if(time>=6)
		{
			time = time/2;
		}
		else
		{
			if(time==3)
				time = 2;
			//1����Ҫ���⴦��
		}
		if(kb[time][day]==null)
		{
			kb[time][day] = "";
			kb[time][day] = mid_time+"--"+mid_address+lesson_other;
		}
		else
		{
			kb[time][day] += mid_time+"--"+mid_address+lesson_other;
		}
	}
	public String getKB()
	{
		/******************************************����****************************************************/
		//���α�����һ�飬��ȡ���ݣ�������HTML��װ����������Ʒ��
		for(int i=0;i<7;i++)
		{
			kbStr+="<tr>";
			for(int j=0;j<8;j++)
			{
				//��1�к͵�1������������װ���ں����ڵģ����Բ������ǡ�
				if(i>=1&&j>=1)
				{
					if(kb[i][j]!=null)
					{
						kbStr+="<td style='border:solid 1px #CCC;width:150px;'>"+kb[i][j]+"</td>";
					}
					else
					{
						kb[i][j]="";
						kbStr+="<td style='border:solid 1px #CCC;width:150px;'>"+kb[i][j]+"</td>";
					}
				}
				else
				{
					if(j==0)
					{
						kbStr+="<td align='center' style='border:solid 1px #CCC;width:150px;'>"+kb[i][j]+"</td>";
					}
					else
					{
						kbStr+="<td align='center' style='border:solid 1px #CCC;width:150px;'>"+kb[i][j]+"</td>";
					}
				}
				
			}
			kbStr+="</tr>";
		}
		kbStr+="</table>";
		this.str = kbStr;
		/*************************************************************************************************************/
		done = true;
		return str;
	}
	public boolean isDone()
	{
		return done;
	}
	
	public void getKB_1()
	{
		try 
		{
			WritableWorkbook book = Workbook.createWorkbook
					(new File(Environment.getExternalStorageDirectory() ,"�γ̱�.xls"));
			// ������Ϊ����һҳ���Ĺ���������0��ʾ���ǵ�һҳ
            WritableSheet sheet = book.createSheet("��һҳ", 0);
            // ��Label����Ĺ�������ָ����Ԫ��λ���ǵ�һ�е�һ��(0,0)
            // �Լ���Ԫ������Ϊtest
            
            for(int i=0;i<7;i++)
            {
            	for(int j=0;j<8;j++)
            	{
            		Label label = new Label(j, i, this.kb[i][j]);
            		// ������õĵ�Ԫ����ӵ���������
                    sheet.addCell(label);
            	}
            }
            
            
            // д�����ݲ��ر��ļ�
            book.write();
            book.close();
                       
		}
		catch (Exception e2) 
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}			

	}
}
