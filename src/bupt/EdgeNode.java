package bupt;

public class EdgeNode {
	Object adjvex;
	int weight;
	Object nextNode;
	
	//��ͨ��ʱ���ܺ���ΪȨ��
	public EdgeNode(Object adjvex, int weight, Object nextNode) {
		this.adjvex = adjvex;
		this.weight = weight;
		this.nextNode = nextNode;
	}
	
	public Object getObject() {
		return adjvex;
	}
	public void setObject(Object object) {
		adjvex = object;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public Object getNextNode() {
		return nextNode;
	}

	public void setNextNode(Object nextNode) {
		this.nextNode = nextNode;
	}
	
}
