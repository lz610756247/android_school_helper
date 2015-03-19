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
	 * 这是课表的操作类，用于生成HTML，我采取的做法是：每一张课表其实可以看做一张二维的字符串表格
	 * ，然后在每一个课程中都有上课的时间，有两个，周“三”第“3”,4节，其实也就是课程在二维表中的定位坐标，
	 * 二维表格是默认的全局变量，则我们只需要把每次处理的课程添加进来，就可以得到一张完整的课程表。
	 */
	public boolean done = false ;
	String str = "";
	String kb[][] ;
	String date[] ;
	String date1[] ;
	ChangeDay cd ;
	/*HTML头部信息*/
	private String kbStr = "<table cellspacing='0px' style='width:1200px; height:300px;'>";
	//为了保证数据的稳定，我们不改动原始值，操作中使用中间值，也就是新变量。
	//setKB()方法中。
	String mid_time = null , endStr_time = null;
	String mid_address = null , endStr_address = null;
	int start_time = 0 , start_address = 0;
	int day = 0 , time = 0;
	public kb_opreation()
	{
		/*二维课表的初始化*/
		kb = new String[7][8];
		/*填充星期和课时标记*/
		date = new String[]{"时间/星期","星期一","星期二","星期三","星期四","星期五","星期六","星期天"};
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
		//为了保证数据的稳定，我们不改动原始值，操作中使用中间值，也就是新变量。
		
		if(lesson_time.contains("停开"))		//特殊不可处理数据
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
		//开始对课程进行截取并填充
		while(endStr_time.contains("|"))
		{
			mid_time = lesson_time.substring(start_time).substring(0 , lesson_time.indexOf("|"));
			mid_address = lesson_address.substring(start_address).substring(0 , lesson_address.indexOf("|"));
			start_time+=mid_time.length()+1;
			start_address+=mid_address.length()+1;
			endStr_time = lesson_time.substring(start_time);
			endStr_address = lesson_address.substring(start_address);
			//填充
			cd.setDay(mid_time.substring(mid_time.indexOf("周")+1, mid_time.indexOf("周")+2));//转换天
			day = cd.getDay();
			cd.setDay(mid_time.substring(mid_time.indexOf("第")+1, mid_time.indexOf(",")));//转换时间
			time = cd.getDay();
			if(time>=6)
			{
				time = time/2;
			}
			else
			{
				if(time==3)
					time = 2;
				//1不需要特殊处理
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
		//填充
		cd.setDay(mid_time.substring(mid_time.indexOf("周")+1, mid_time.indexOf("周")+2));//转换天
		day = cd.getDay();
		cd.setDay(mid_time.substring(mid_time.indexOf("第")+1, mid_time.indexOf(",")));//转换时间
		time = cd.getDay();
		if(time>=6)
		{
			time = time/2;
		}
		else
		{
			if(time==3)
				time = 2;
			//1不需要特殊处理
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
		/******************************************测试****************************************************/
		//将课表表遍历一遍，获取内容，并进行HTML组装，最后输出成品。
		for(int i=0;i<7;i++)
		{
			kbStr+="<tr>";
			for(int j=0;j<8;j++)
			{
				//第1行和第1列我们是用来装星期和日期的，所以不动它们。
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
					(new File(Environment.getExternalStorageDirectory() ,"课程表.xls"));
			// 生成名为“第一页”的工作表，参数0表示这是第一页
            WritableSheet sheet = book.createSheet("第一页", 0);
            // 在Label对象的构造子中指名单元格位置是第一列第一行(0,0)
            // 以及单元格内容为test
            
            for(int i=0;i<7;i++)
            {
            	for(int j=0;j<8;j++)
            	{
            		Label label = new Label(j, i, this.kb[i][j]);
            		// 将定义好的单元格添加到工作表中
                    sheet.addCell(label);
            	}
            }
            
            
            // 写入数据并关闭文件
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
