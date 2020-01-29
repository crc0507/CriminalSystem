package bupt;

import java.util.ArrayList;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.neo4j.graphdb.Node;
 
public class VertexName {
	ArrayList<Node> nameList;
	public VertexName() {
		nameList = new ArrayList<Node>();
	}
	public void add(Node node) {
		nameList.add(node);
	}
	public int length() {
		return nameList.size();
	}
	public Node get(int i) {
		return nameList.get(i);
	}
	public void removeAll(ArrayList<Node> list) {
		nameList.removeAll(list);
	}
	public ArrayList<Node> clone() {
		ArrayList<Node> copy = (ArrayList<Node>) nameList.clone();
		return copy;
	}
	

	public ArrayList<Node> getNameList() {
		return nameList;
	}

	public void setNameList(ArrayList<Node> nameList) {
		this.nameList = nameList;
	}

	
//	ArrayList<String> nameList;
//	public VertexName() {
//		nameList = new ArrayList<String>();
//	}
//	public void add(String name) {
//		nameList.add(name);
//	}
//	public int length() {
//		return nameList.size();
//	}
//	public String get(int i) {
//		return nameList.get(i);
//	}
//	public void removeAll(ArrayList<String> list) {
//		nameList.removeAll(list);
//	}
//	public ArrayList<String> clone() {
//		ArrayList<String> copy = (ArrayList<String>) nameList.clone();
//		return copy;
//	}
//	
//
//	public ArrayList<String> getNameList() {
//		return nameList;
//	}
//
//	public void setNameList(ArrayList<String> nameList) {
//		this.nameList = nameList;
//	}
//
//	
}
