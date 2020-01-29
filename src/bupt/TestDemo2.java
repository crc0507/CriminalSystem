package bupt;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.internal.compiler.v2_3.commands.indexQuery;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterable;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import bupt.Testdata.MyLabels;
import bupt.Testdata.MyRelationshipTypes;
import oracle.net.aso.i;
import oracle.net.aso.l;

public class TestDemo2 {
	static File file = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\databases\\\\demo.db");
	static GraphDatabaseService graphDB_c = new GraphDatabaseFactory().newEmbeddedDatabase(file);

	public static EffectiveDate effectiveDate = new EffectiveDate();
	public static ArrayList<TimeDetail> timesArrayList = new ArrayList<>();
	public static ArrayList<Node> tag2crime_nodeSet = new ArrayList<>();
	public static ArrayList<Node> tag2not_nodeSet = new ArrayList<>();
	public static ArrayList<Node> crimeChoseNodes = new ArrayList<>();
	public static ArrayList<Node> notChosenNodes = new ArrayList<>();
	public static ArrayList<Double> tag1Rates = new ArrayList<>();
	public static ArrayList<Double> tag2Rates = new ArrayList<>();
	public static ArrayList<Double> tag3Rates = new ArrayList<>();
	public static ArrayList<Double> tag4Rates = new ArrayList<>();
	public static ArrayList<Double> tag5Rates = new ArrayList<>();
	public static ArrayList<Double> tag6Rates = new ArrayList<>();
	public static ArrayList<Double> tag7Rates = new ArrayList<>();
	public static ArrayList<Double> tag8Rates = new ArrayList<>();
	public static ArrayList<Double> tag9Rates = new ArrayList<>();
	public static ArrayList<Double> tag10Rates = new ArrayList<>();
	public static ArrayList<Double> tag2notRates = new ArrayList<>();
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try (Transaction tx = graphDB_c.beginTx()){
			ResourceIterable<Node> allNodesIterable = graphDB_c.getAllNodes();
			ResourceIterator<Node> allNodesIterator = allNodesIterable.iterator();
			
//			timesArrayList.add(new TimeDetail("2018-1-1 00:00:00", "2018-1-31 23:59:59"));
//			timesArrayList.add(new TimeDetail("2018-2-1 00:00:00", "2018-2-28 23:59:59"));
//			timesArrayList.add(new TimeDetail("2018-3-1 00:00:00", "2018-3-31 23:59:59"));
//			timesArrayList.add(new TimeDetail("2018-4-1 00:00:00", "2018-4-30 23:59:59"));
			
			//tag2犯罪类型的全样本集
			while(allNodesIterator.hasNext()) {
				Node curNode = allNodesIterator.next();
				int numOfNeigh = getAllNeighbors(curNode).size();
				if(curNode.hasProperty("tag9")&&numOfNeigh>=2) {
					//犯罪样本集
					if(curNode.getProperty("tag9").equals("1 ")) {
						tag2crime_nodeSet.add(curNode);
					}
					//未犯罪样本集
					if(curNode.getProperty("tag9").equals("0 ")) {
						tag2not_nodeSet.add(curNode);
					}
				}
			}
			int sizeofCrime = tag2crime_nodeSet.size();
			System.out.println(sizeofCrime);
			int sizeofNot = tag2not_nodeSet.size();
			System.out.println(sizeofNot);
			System.out.println("犯罪：");
			//确实犯罪置0后预测
			for(int kkkk=0;kkkk<tag2crime_nodeSet.size();kkkk++) {
				Node curNode = tag2crime_nodeSet.get(kkkk);
				System.out.println(curNode.getId());
				System.out.println(curNode.getProperty("phonenumber"));
				String tempProperty = (String) curNode.getProperty("tag9");
				curNode.setProperty("tag9", "0 ");
				//求取嫌疑指数
				ArrayList<Node> neighborNodes = getAllNeighbors(curNode);
				//遍历所有邻居节点，看是否有犯罪人员
				for(int i=0;i<neighborNodes.size();i++) {
					Node curNeighNode = neighborNodes.get(i);
					//如果邻居节点为属性确认人员
					if(curNeighNode.hasProperty("tag9")) {
						System.out.println("有");
//						for(int tagNum=1;tagNum<11;tagNum++) {
							//合并出入度的总时间
							double totalDuration = 0;
							double totalduration2 = 0;
							double totalduration3 = 0;
							String tag1 = (String) curNeighNode.getProperty("tag9");
							if(tag1.equals("1 ")){
								//计算第一层
								//计算入度关系的总时间
								Relationship inRelationship = getRelationshipByTwoNodes(curNode, curNeighNode, Direction.INCOMING);
								if(inRelationship!=null) {
									Map<String, Object> inproperties = inRelationship.getAllProperties();
									for(Map.Entry<String, Object> entry:inproperties.entrySet()) {
										if(isValidDate(entry.getKey())) {
											String call_startTime = entry.getKey();
											double duration = Double.parseDouble((String)entry.getValue());
											totalDuration = totalDuration+duration;
										}
									}
								}
								//计算出度关系的总时间
								Relationship outRelationship = getRelationshipByTwoNodes(curNode, curNeighNode, Direction.OUTGOING);
								if(outRelationship!=null) {
									Map<String, Object> outproperties = outRelationship.getAllProperties();
									for(Map.Entry<String, Object> entry:outproperties.entrySet()) {
										if(isValidDate(entry.getKey())) {
											String call_startTime = entry.getKey();
											double duration = Double.parseDouble((String)entry.getValue());
											totalDuration = totalDuration+duration;
										}
									}
								}
								
								//计算第二层的亲密度
								ArrayList<Node> neighborNodes2 = getAllNeighbors(curNeighNode);
								for(int j=0;j<neighborNodes2.size();j++) {
									Node curNeighNode2 = neighborNodes2.get(j);
									if(curNeighNode2.hasProperty("tag9")&&curNeighNode2.getId()!=curNeighNode.getId()) {
										String tag2 = (String) curNeighNode2.getProperty("tag9");
										if(!(tag2.equals("1 "))) {
											//计算入度时间
											Relationship inRelationship2 = getRelationshipByTwoNodes(curNeighNode, curNeighNode2, Direction.INCOMING);
											if(inRelationship2!=null) {
												Map<String, Object> inProperties2 = inRelationship2.getAllProperties();
												for(Map.Entry<String, Object> entry:inProperties2.entrySet()) {
													if(isValidDate(entry.getKey())) {
														String call_startTime2 = entry.getKey();
														double duration2 = Double.parseDouble((String)entry.getValue());
														totalduration2 = totalduration2+duration2;
													}
												}
											}
											//计算出度时间
											Relationship outRelationship2 = getRelationshipByTwoNodes(curNeighNode, curNeighNode2, Direction.OUTGOING);
											if(outRelationship2!=null) {
												Map<String, Object> outProperties2 = outRelationship2.getAllProperties();
												for(Map.Entry<String, Object> entry:outProperties2.entrySet()) {
													if(isValidDate(entry.getKey())) {
														String call_startTime2 = entry.getKey();
														double duration2 = Double.parseDouble((String)entry.getValue());
														totalduration2 = totalduration2+duration2;
													}
												}
											}
											double closeRate2 = computeCloseRate(totalDuration, totalduration2, totalduration3, 2);
											System.out.println(+curNode.getId()+"--------嫌疑:"+closeRate2);
											tag2Rates.add(closeRate2);
											
										}
									}
								}
							}

//						}
					}						
				}
				curNode.setProperty("tag9", tempProperty);
				
			}
			System.out.println("非犯罪：");
			//非犯罪预测
			for(int not=0;not<tag2not_nodeSet.size();not++) {
				Node curNode = tag2not_nodeSet.get(not);
				System.out.println(curNode.getId());
				System.out.println(curNode.getProperty("phonenumber"));
				//求取嫌疑指数
				ArrayList<Node> neighborNodes = getAllNeighbors(curNode);
				//遍历所有邻居节点，看是否有犯罪人员
				for(int i=0;i<neighborNodes.size();i++) {
					Node curNeighNode = neighborNodes.get(i);
					//如果邻居节点为属性确认人员
					if(curNeighNode.hasProperty("tag9")) {
						System.out.println("有");
							//合并出入度的总时间
							double totalDuration = 0;
							double totalduration2 = 0;
							double totalduration3 = 0;
							String tag1 = (String) curNeighNode.getProperty("tag9");
//							System.out.println(tag1);
							if(!(tag1.equals("0 "))){
								//计算第一层
								//计算入度关系的总时间
								Relationship inRelationship = getRelationshipByTwoNodes(curNode, curNeighNode, Direction.INCOMING);
								if(inRelationship!=null) {
									Map<String, Object> inproperties = inRelationship.getAllProperties();
									for(Map.Entry<String, Object> entry:inproperties.entrySet()) {
										if(isValidDate(entry.getKey())) {
											String call_startTime = entry.getKey();
											double duration = Double.parseDouble((String)entry.getValue());
											totalDuration = totalDuration+duration;
										}
									}
								}
								//计算出度关系的总时间
								Relationship outRelationship = getRelationshipByTwoNodes(curNode, curNeighNode, Direction.OUTGOING);
								if(outRelationship!=null) {
									Map<String, Object> outproperties = outRelationship.getAllProperties();
									for(Map.Entry<String, Object> entry:outproperties.entrySet()) {
										if(isValidDate(entry.getKey())) {
											String call_startTime = entry.getKey();
											double duration = Double.parseDouble((String)entry.getValue());
											totalDuration = totalDuration+duration;
										}
									}
								}
								//计算第二层的亲密度
								ArrayList<Node> neighborNodes2 = getAllNeighbors(curNeighNode);
								for(int j=0;j<neighborNodes2.size();j++) {
									Node curNeighNode2 = neighborNodes2.get(j);
									if(curNeighNode2.hasProperty("tag9")&&curNeighNode2.getId()!=curNeighNode.getId()) {
										String tag2 = (String) curNeighNode2.getProperty("tag9");
										if(!(tag2.equals("1 "))) {
											//计算入度时间
											Relationship inRelationship2 = getRelationshipByTwoNodes(curNeighNode, curNeighNode2, Direction.INCOMING);
											if(inRelationship2!=null) {
												Map<String, Object> inProperties2 = inRelationship2.getAllProperties();
												for(Map.Entry<String, Object> entry:inProperties2.entrySet()) {
													if(isValidDate(entry.getKey())) {
														String call_startTime2 = entry.getKey();
														double duration2 = Double.parseDouble((String)entry.getValue());
														totalduration2 = totalduration2+duration2;
													}
												}
											}
											//计算出度时间
											Relationship outRelationship2 = getRelationshipByTwoNodes(curNeighNode, curNeighNode2, Direction.OUTGOING);
											if(outRelationship2!=null) {
												Map<String, Object> outProperties2 = outRelationship2.getAllProperties();
												for(Map.Entry<String, Object> entry:outProperties2.entrySet()) {
													if(isValidDate(entry.getKey())) {
														String call_startTime2 = entry.getKey();
														double duration2 = Double.parseDouble((String)entry.getValue());
														totalduration2 = totalduration2+duration2;
													}
												}
											}
											double closeRate2 = computeCloseRate(totalDuration, totalduration2, totalduration3, 2);
											tag2notRates.add(closeRate2);
											System.out.println(+curNode.getId()+"--------嫌疑:"+closeRate2);
										}
									}
								}
							}
//						}
					}						
				}
			}
			
			
			tx.success();
			tx.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			
		}
	}
	
	//寻找数组最大值
	public double GetMax(ArrayList<Double> array) {
		double max = 0;
		double temp = 0;
		for(int i=0;i<array.size();i++) {
			temp = array.get(i);
			if(temp >= max) {
				max = temp;
			}
			if(temp < max) {
				
			}
		}
		
		return max;
	}
	//寻找数组最小值
	
	//计算节点亲密度,r1:第一跳的系数，r2:第二跳的系数，r3：第三跳的系数，r：跳数的系数
	public static double computeCloseRate(double duration1, double duration2, double duration3, double step) {
		double r1 = 0.5;
		double r2 = 0.3;
		double r3 = 0.1;
		double r = 0.3;
		double result = 0;
		if(step==1) {
			result = 1/(1/(r1*duration1) + r*step);
		}
		if(step==2) {
			result = 1/(1/(r1*duration1 + r2*duration2) + r*step);
		}
		if(step==3){
			result = 1/(1/(r1*duration1 + r2*duration2 + r3*duration3) + r*step);
		}
		return result;
	}
	
	//根据两个节点找关系
	public static Relationship getRelationshipByTwoNodes(Node node1, Node node2, Direction dir) {
		Relationship returnRelationship = null;
		if(dir==Direction.INCOMING) {
			Iterable<Relationship> allIterable = node1.getRelationships(dir);
			Iterator<Relationship> allRelations = allIterable.iterator();
			while(allRelations.hasNext()) {
				Relationship curRelationship = allRelations.next();
				//找出node1与node2之间的关系
				if(curRelationship.getStartNodeId()==node2.getId()) {
					returnRelationship = curRelationship;
				}
			}
		}
		if(dir==Direction.OUTGOING) {
			Iterable<Relationship> allIterable = node1.getRelationships(dir);
			Iterator<Relationship> allRelations = allIterable.iterator();
			while(allRelations.hasNext()) {
				Relationship curRelationship = allRelations.next();
				//找出node1与node2之间的关系
				if(curRelationship.getEndNodeId()==node2.getId()) {
					returnRelationship = curRelationship;
				}
			}
		}
		return returnRelationship;
	}
	
	//寻找某个node的全部邻居
	public static ArrayList<Node> getAllNeighbors(Node node) {
		ArrayList<Node> neighborNodes = new ArrayList<>();
		//寻找出度邻居
		Iterable<Relationship> outRelationIterable = node.getRelationships(Direction.OUTGOING);
		Iterator<Relationship> outRelationIterator = outRelationIterable.iterator();
		while(outRelationIterator.hasNext()) {
			Iterator<Node> neighborsIterator = neighborNodes.iterator();
			boolean isExist = false;
			Relationship curRelationship = outRelationIterator.next();
			Node otherNode = curRelationship.getEndNode();
			//判断该点是否已经存在
			while(neighborsIterator.hasNext()) {
				Node curcurNeigh = neighborsIterator.next();
				if(curcurNeigh.getId()==otherNode.getId()) {
					isExist = true;
				}
			}
			if(isExist==false) {
				neighborNodes.add(otherNode);
			}
			if(isExist==true) {
			}
		}
		//寻找入度邻居
		Iterable<Relationship> inRelationIterable = node.getRelationships(Direction.INCOMING);
		Iterator<Relationship> inRelationIterator = inRelationIterable.iterator();
		while(inRelationIterator.hasNext()) {
			Iterator<Node> neighborsIterator = neighborNodes.iterator();
			boolean isExist = false;
			Relationship curRelationship = inRelationIterator.next();
			Node otherNode = curRelationship.getStartNode();
			//判断该点是否已经存在
			while(neighborsIterator.hasNext()) {
				Node curcurNeigh = neighborsIterator.next();
				if(curcurNeigh.getId()==otherNode.getId()) {
					isExist = true;
				}
			}
			if(isExist==false) {
				neighborNodes.add(otherNode);
			}
			if(isExist==true) {
			}

		}

		return neighborNodes;
	}
	//时间格式是否合法
	public static boolean isValidDate(String date) {
		boolean convertSuccess = true;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			sdf.setLenient(false);
			sdf.parse(date);
		}
		catch(ParseException e) {
			convertSuccess = false;
		}
		return convertSuccess;
	}

}

