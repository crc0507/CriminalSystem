package bupt;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EffectiveDate {
	//�ַ�ת����
	public static Date strToDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = (Date) sdf.parse(str);
		}catch (ParseException e) {
			// TODO: handle exception
		}
		return date;
	}
	
	//�жϵ�ǰʱ���Ƿ��ڸ���ʱ�����
	public static boolean isBelongCalendar(Date curTime, Date startTime, Date endTime) {
		Calendar date = Calendar.getInstance();
		date.setTime(curTime);
		Calendar start = Calendar.getInstance();
		start.setTime(startTime);
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		//��ǰʱ���ڸ���ʱ��ε��ڼ���
		if(date.after(start)&&date.before(end)) {
			return true;
		}
		//��ǰʱ�����ʼʱ������һ��Ϊͬһʱ���
		else if(curTime.compareTo(startTime)==0||curTime.compareTo(endTime)==0) {
			return true;
		}
		else {
			return false;
		}
	}
}
