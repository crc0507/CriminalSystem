package bupt;

public class TimeDetail {
	private String startTime;
	private String endTime;
	
	public TimeDetail(String start, String end){
		this.startTime = start;
		this.endTime = end;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
