package bupt;

import bupt.EffectiveDate;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jgrapht.Graph;
import org.jgrapht.graph.AsUndirectedGraph;
import org.jgrapht.graph.DefaultUndirectedWeightedGraph;
//import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.E;
import org.neo4j.cypher.internal.frontend.v2_3.perty.recipe.Pretty.nest;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import oracle.net.nt.NTAdapter;

public class CommunityPageRank extends DirWeightedEdge{
	static File file = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\databases\\\\data.db");
	static GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);
	
	public static ArrayList<TimeDetail> timesArrayList = new ArrayList<>();
	public static EffectiveDate effectiveDate = new EffectiveDate();
	public static ArrayList<VertexName> vertexNameArrayList = new ArrayList<>();
	//�����нڵ㽨��һ��arraylist�洢��ͬʱ��ε�ͨ��ʱ��
	ArrayList<OneNodeFeatures> nodesFeatures = new ArrayList<>();
	
	public static void main(String[] args) {
		//ͼ�ļ���
		ArrayList<SimpleDirectedWeightedGraph<Node, DirWeightedEdge>> graphs = new ArrayList<>();
		//SimpleDirectedWeightedGraph<Node, DirWeightedEdge> graph = new SimpleDirectedWeightedGraph<>(DirWeightedEdge.class);
		Map<Node, Double> scores;
		
		try(Transaction tx = graphDB.beginTx()){
			timesArrayList.add(new TimeDetail("2018-1-1 00:00:00", "2018-1-31 23:59:59"));
			timesArrayList.add(new TimeDetail("2018-2-1 00:00:00", "2018-2-28 23:59:59"));
			timesArrayList.add(new TimeDetail("2018-3-1 00:00:00", "2018-3-31 23:59:59"));
			timesArrayList.add(new TimeDetail("2018-4-1 00:00:00", "2018-4-30 23:59:59"));
//			timesArrayList.add(new TimeDetail("2018-5-1 00:00:00", "2018-5-31 23:59:59"));
			
			//׷�ٽڵ�ı仯
			for(int i=0;i<timesArrayList.size();i++) {
				System.out.println("���ǵ�"+(i+1)+"����:");
				//ÿ����һ����ÿ��Сʱ��ͨ��ʱ��
				ArrayList<Map<Node, List<Integer>>> hoursDuration = new ArrayList<>();
//				int mmm=0;
				ResourceIterable<Node> nodesIterable = graphDB.getAllNodes();
				ResourceIterator<Node> allNodes = nodesIterable.iterator();
				while(allNodes.hasNext()) {
					Node curNode = allNodes.next();
					List<Integer> durationList = new ArrayList<>();
					//��ʼ��list
					for(int add=0;add<24;add++) {
						durationList.add(0);
					}
					Map<Node, List<Integer>> map = new HashMap<>();
					map.put(curNode, durationList);
					hoursDuration.add(map);
				}
				ResourceIterable<Node> nodesIterable1 = graphDB.getAllNodes();
				ResourceIterator<Node> allNodes1 = nodesIterable1.iterator();
				while(allNodes1.hasNext()) {
					Node curNode = allNodes1.next();
//					Map<Node, List<Integer>> tempMap = new HashMap<>();
//					List<Integer> durationList = new ArrayList<>();
//					//��ʼ��list
//					for(int add=0;add<24;add++) {
//						durationList.add(0);
//					}
//					tempMap.put(curNode, durationList);
//					//��Ӹ����ڵ��map
//					hoursDuration.add(tempMap);
					Iterable<Relationship> relationIterable = curNode.getRelationships(Direction.INCOMING);
					Iterator<Relationship> allRelationshipsIterator = relationIterable.iterator();
					while(allRelationshipsIterator.hasNext()) {
						Relationship curRelationship = allRelationshipsIterator.next();
						Map<String, Object> properties = curRelationship.getAllProperties();
						for(Map.Entry<String, Object> entry:properties.entrySet()) {
							//Ҫ������֤���Ե�key�Ƿ�Ϊʱ���ʽ
							if(isValidDate(entry.getKey())) {
								String call_startTime = entry.getKey();
								int duration = Integer.parseInt((String) entry.getValue()) ;
								Date curDate = effectiveDate.strToDate(call_startTime);
								Date startDate = effectiveDate.strToDate(timesArrayList.get(i).getStartTime());
								Date endDate = effectiveDate.strToDate(timesArrayList.get(i).getEndTime());
								//���ͨ����ʼʱ���ڹ涨ʱ����
								if(effectiveDate.isBelongCalendar(curDate, startDate, endDate)) {									
									//startnode
									Node startNode = curRelationship.getStartNode();
									for(int k=0;k<hoursDuration.size();k++) {
										//Ѱ��ƥ���node
										if(hoursDuration.get(k).containsKey(startNode)) {
											//��÷��������ĵ�ǰ�ڵ�map
											Map<Node, List<Integer>> curMap = hoursDuration.get(k);
											//��ȡ��ǰmap������list
											List<Integer> curList = getFirstList(curMap);
											int hour = curDate.getHours();
											int oldDuration = curList.get(hour);
											int newDuration = oldDuration+duration;
											curList.set(hour, newDuration);
											curMap.replace(startNode, curList);
											hoursDuration.set(k, curMap);
										}
									}
									//endnode
									Node endNode = curRelationship.getEndNode();
									for(int m=0;m<hoursDuration.size();m++) {
										//Ѱ��ƥ���node
										if(hoursDuration.get(m).containsKey(endNode)) {
											//��õ�ǰmap������list������
											Map<Node, List<Integer>> curMap = hoursDuration.get(m);
											List<Integer> curList = getFirstList(curMap);
											int hour = curDate.getHours();
											int oldDuration = curList.get(hour);
											int newDuration = oldDuration+duration;
											curList.set(hour,newDuration);
											curMap.replace(endNode, curList);
											hoursDuration.set(m, curMap);
										}
									}
								}
							}
						}
//						mmm++;
						
					}
					
				}
				for(int b=0;b<hoursDuration.size();b++) {
					System.out.println(hoursDuration.get(b));
				}
//				System.out.println(mmm);
			}
			
			//׷�����������ı仯
			for(int i=0;i<timesArrayList.size();i++) {
				System.out.println("********************************");
				System.out.println("��"+(i+1)+"���½����");
				ResourceIterable<Node> allNodesIterable = graphDB.getAllNodes();
				ResourceIterator<Node> allNodesIterator = allNodesIterable.iterator();
				graphs.add(new SimpleDirectedWeightedGraph<>(DirWeightedEdge.class));
				//���μ���ڵ�
				while(allNodesIterator.hasNext()) {
					graphs.get(i).addVertex(allNodesIterator.next());
				}
				//���ӹ�ϵ
				ResourceIterable<Relationship> allRelationshipsIterable = graphDB.getAllRelationships();
				ResourceIterator<Relationship> allRelationshipsIterator = allRelationshipsIterable.iterator();
				while(allRelationshipsIterator.hasNext()) {
					Relationship curRelationship = allRelationshipsIterator.next();
					Map<String, Object> allPoperties = curRelationship.getAllProperties();
					int totalDuration = 0;
					//�������Զ�
					DirWeightedEdge edge = null;
					boolean isHascontact = false;
					Node startNode = null;
					Node endNode = null;
					int duration = 0;
					for(Map.Entry<String, Object> entry:allPoperties.entrySet()) {
						if(isValidDate(entry.getKey())) {
							String call_startTime = entry.getKey();
							duration = Integer.parseInt((String) entry.getValue()) ;
							Date curDate = effectiveDate.strToDate(call_startTime);
							Date startDate = effectiveDate.strToDate(timesArrayList.get(i).getStartTime());
							Date endDate = effectiveDate.strToDate(timesArrayList.get(i).getEndTime());
							//���ͨ����ʼʱ���ڹ涨ʱ����
							if(effectiveDate.isBelongCalendar(curDate, startDate, endDate)) {
								//��ȡ��ʼ�ڵ�
								startNode = curRelationship.getStartNode();
								endNode = curRelationship.getEndNode();
								totalDuration = totalDuration+duration;
								isHascontact = true;
							}
						}
					}
					
					if(isHascontact==true) {
						//�ӱ�
						edge = graphs.get(i).addEdge(startNode, endNode);
//						System.out.println(startNode.getProperty("name")+"--->"+endNode.getProperty("name")+":"+totalDuration);
						//�����ߵ�Ȩ��
						graphs.get(i).setEdgeWeight(edge, totalDuration);
					}
//					System.out.println(totalDuration);
//					graph.setEdgeWeight(edge, (double)totalDuration);
				}
				
				//����pagerank
				Map<String, Double> result = new HashMap<String, Double>();
				PageRank<Node, DirWeightedEdge> pg = new PageRank<>(graphs.get(i));
				pg.run(0.85, 100, 0.0001);
				BreadthFirstIterator<Node, DirWeightedEdge> bfi = new BreadthFirstIterator<>(graphs.get(i));
				while(bfi.hasNext()) {
					Node foundNode = bfi.next();
					Double score = pg.getVertexScore(foundNode);
					result.put((String) foundNode.getProperty("name"), score);
					
				}
				//����prֵ
				SortByValueDescending st = new SortByValueDescending();
				result = st.sortByValueDescending(result);
				//����쵼�ڵ�
				System.out.println("�쵼�ڵ㣺"+getFirstObject(result));
				//���������prֵ
				for(Map.Entry<String, Double> entry:result.entrySet()) {
					System.out.println(entry.getKey()+":"+entry.getValue());
				}
				
				Set<Node> nodesSet = graphs.get(i).vertexSet();
				Iterator<Node> nodesIterator = nodesSet.iterator();
				VertexName nameList = new VertexName();
				//nameList = null;
				while(nodesIterator.hasNext()) {
					Node tempNode = nodesIterator.next();
					int outdegree = graphs.get(i).outDegreeOf(tempNode);
					int indegree = graphs.get(i).inDegreeOf(tempNode);
					if((outdegree+indegree)!=0) {
						nameList.add(tempNode);
					
					}
				}
				vertexNameArrayList.add(nameList);

				if(i>0) {
					System.out.println("��ʧ�Ľڵ㣺");
					ArrayList<Node> list1 = new ArrayList<>();
					ArrayList<Node> list2 = new ArrayList<>();
					ArrayList<Node> list11 = new ArrayList<>();
					list1 = vertexNameArrayList.get(i-1).clone();
					list11 = vertexNameArrayList.get(i-1).clone();
					list2 = nameList.clone();
					list1.removeAll(list2);
					for(int j=0;j<list1.size();j++) {
//						System.out.println("��ʧ�Ľڵ㣺");
						System.out.println("["+list1.get(j).getProperty("name")+"]");
						//�ж���ʧ�ڵ����ϵ��
						Set<DirWeightedEdge> inEdges = graphs.get(i-1).incomingEdgesOf(list1.get(j));
						Iterator<DirWeightedEdge> inEdgesIterator = inEdges.iterator();
						Set<DirWeightedEdge> outEdges = graphs.get(i-1).outgoingEdgesOf(list1.get(j));
						Iterator<DirWeightedEdge> outEdgesIterator = outEdges.iterator();
						while(inEdgesIterator.hasNext()) {
							DirWeightedEdge edge = inEdgesIterator.next();
//							Node n1 = edge.getSource();
							System.out.println(edge.getSource().getProperty("name")+"--->"+edge.getTarget().getProperty("name"));
						}
						while(outEdgesIterator.hasNext()) {
							DirWeightedEdge edge = outEdgesIterator.next();
							System.out.println(edge.getSource().getProperty("name")+"--->"+edge.getTarget().getProperty("name"));
//							System.out.println(edge);
						}
						
					}
					list2.removeAll(list11);
					for(int k=0;k<list2.size();k++) {
						System.out.println("�³��ֵĽڵ㣺");
						System.out.println("["+list2.get(k).getProperty("name")+"]");
						//�жϳ��ֽڵ����ϵ��
						Set<DirWeightedEdge> inEdges = graphs.get(i).incomingEdgesOf(list2.get(k));
						Iterator<DirWeightedEdge> inEdgesIterator = inEdges.iterator();
						Set<DirWeightedEdge> outEdges = graphs.get(i).outgoingEdgesOf(list2.get(k));
						Iterator<DirWeightedEdge> outEdgesIterator = outEdges.iterator();
						while(inEdgesIterator.hasNext()) {
							DirWeightedEdge edge = inEdgesIterator.next();
							System.out.println(edge.getSource().getProperty("name")+"--->"+edge.getTarget().getProperty("name"));
						}
						while(outEdgesIterator.hasNext()) {
							DirWeightedEdge edge = outEdgesIterator.next();
							System.out.println(edge.getSource().getProperty("name")+"--->"+edge.getTarget().getProperty("name"));
						}
						
					}
					
				}
			}
			//����ͼ����
			ArrayList<DefaultUndirectedWeightedGraph<Node, DirWeightedEdge>> newGraphs = new ArrayList<>();
			//����ڵ㵽����ͼ��
			for(int i=0;i<graphs.size();i++) {
				newGraphs.add(new DefaultUndirectedWeightedGraph<>(DirWeightedEdge.class));
				SimpleDirectedWeightedGraph<Node, DirWeightedEdge> curGraph = graphs.get(i);
				
				Set<Node> nodeSet = curGraph.vertexSet();
				Iterator<Node> nodesIterator = nodeSet.iterator();
				int size=0;
				while(nodesIterator.hasNext()) {
					size++;
				}

			}
			//����ͼת����ͼ,�������ͨ��ʱ��ϲ�
			for(int i=0;i<graphs.size();i++) {
				System.out.println("month"+(i+1)+":");
				SimpleDirectedWeightedGraph<Node, DirWeightedEdge> curGraph = graphs.get(i);
				Set<DirWeightedEdge> edgesSet = curGraph.edgeSet();
				Iterator<DirWeightedEdge> edgesIterator = edgesSet.iterator();
				while(edgesIterator.hasNext()) {
					DirWeightedEdge curEdge = edgesIterator.next();
					Node node1 = curEdge.getSource();
					Node node2 = curEdge.getTarget();
					double curWeight = curGraph.getEdgeWeight(curEdge);
					boolean flag = newGraphs.get(i).addEdge(node1, node2, curEdge);
					if(!flag) {
						DirWeightedEdge newCurEdge = newGraphs.get(i).getEdge(node1, node2);
						newGraphs.get(i).setEdgeWeight(newCurEdge, newGraphs.get(i).getEdgeWeight(newCurEdge)+curWeight);
						
					}
				}
				//�ڵ�����ܶ�list
				ArrayList<Double> closingRateArrayList = new ArrayList<>();
				for(int t=0;t<newGraphs.size();t++) {
					DefaultUndirectedWeightedGraph<Node, DirWeightedEdge> closeGraph = newGraphs.get(t);
					Set<Node> nodes = closeGraph.vertexSet();
					Iterator<Node> nodesIterator = nodes.iterator();
					while(nodesIterator.hasNext()) {
						Node midNode = nodesIterator.next();
					}
				}
				
			}
			
			tx.success();
			tx.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}finally {

		}
	}

	
	public enum MyRelationshipTypes implements RelationshipType{
		HAS_CONTACT;
	}
	public enum MyLabels implements Label{
		USERS;
	}
	public static String getFirstObject(Map<String, Double> map) {
		Object value = null;
		String nameString = null;
		for(Entry<String, Double> entry:map.entrySet()) {
			value = entry.getValue();
			nameString = entry.getKey();
			if(value!=null) {
				break;
			}
		}
		return nameString;
	}
	public static List<Integer> getFirstList(Map<Node, List<Integer>> map){
		List<Integer> list = null;
		for(Entry<Node, List<Integer>> entry:map.entrySet()) {
			list = entry.getValue();
			if(list!=null) {
				break;
			}
		}
		return list;
	}
	public static boolean isValidDate(String date) {
		boolean convertSuccess = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			//����lenientΪfalse������simpledateformat��ȽϿ��ɵ���֤����
			sdf.setLenient(false);
			sdf.parse(date);
		}
		catch(ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}
}


