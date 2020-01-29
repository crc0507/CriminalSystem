package bupt;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.neo4j.graphdb.Node;

public class DirWeightedEdge extends DefaultWeightedEdge{
	//DefaultWeightedEdge edge;
	public DirWeightedEdge() {
		
	} 
	public Node getSource() {
		return (Node)super.getSource();
	}
	public Node getTarget() {
		return (Node)super.getTarget();
	}
}
