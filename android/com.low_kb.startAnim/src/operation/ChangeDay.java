package operation;


public class ChangeDay {
	/**
	 * 这个类是用来处理日期的转换，方便对课表单元格的位置定位。
	 * 将字符串转换为整形数字。（坐标值是整形数）
	 */
	int result=0;
	public int setDay(String Str)
	{
		if(Str.equals("1"))
			this.result = 1;
		if(Str.equals("2"))
			this.result = 2;
		if(Str.equals("3"))
			this.result = 3;
		if(Str.equals("4"))
			this.result = 4;
		if(Str.equals("5"))
			this.result = 5;
		if(Str.equals("6"))
			this.result = 6;
		if(Str.equals("7"))
			this.result = 7;
		if(Str.equals("8"))
			this.result = 8;
		if(Str.equals("9"))
			this.result = 9;
		if(Str.equals("10"))
			this.result = 10;
		if(Str.equals("11"))
			this.result = 11;
		if(Str.equals("12"))
			this.result = 12;
			
		return result;
	}
	public int getDay()
	{
		return this.result;
	}
}
