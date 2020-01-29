package bupt;

public class NodeFeature {
	private int totalOuttime;
	private int totalIntime;

	public NodeFeature(int out, int in, int totalOuttime, int totalIntime, int numberOut, int numberIn) {
		this.totalOuttime = totalOuttime;
		this.totalIntime = totalIntime;
	}

	public int getTotalOuttime() {
		return totalOuttime;
	}

	public void setTotalOuttime(int totalOuttime) {
		this.totalOuttime = totalOuttime;
	}

	public int getTotalIntime() {
		return totalIntime;
	}

	public void setTotalIntime(int totalIntime) {
		this.totalIntime = totalIntime;
	}

}
