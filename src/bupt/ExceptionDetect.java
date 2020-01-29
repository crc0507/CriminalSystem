package bupt;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

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

public class ExceptionDetect {
	static File file2 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\databases\\\\demo2.db");
	static GraphDatabaseService graphDB2 = new GraphDatabaseFactory().newEmbeddedDatabase(file2);
//	static File file201801 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201801.db");
//	static GraphDatabaseService graphDB201801 = new GraphDatabaseFactory().newEmbeddedDatabase(file201801);
//	static File file201802 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201802.db");
//	static GraphDatabaseService graphDB201802 = new GraphDatabaseFactory().newEmbeddedDatabase(file201802);
//	static File file201803 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201803.db");
//	static GraphDatabaseService graphDB201803 = new GraphDatabaseFactory().newEmbeddedDatabase(file201803);
//	static File file201804 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201804.db");
//	static GraphDatabaseService graphDB201804 = new GraphDatabaseFactory().newEmbeddedDatabase(file201804);
//	static ArrayList<GraphDatabaseService> allGraphDBsArrayList = new ArrayList<>();

	public static EffectiveDate effectiveDate = new EffectiveDate();
	public static ArrayList<TimeDetail> timesArrayList = new ArrayList<>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		double closeRate2 = 0;
		Node curNode= null;
		ArrayList<Node> neighborNodes = null;
		try (Transaction tx = graphDB2.beginTx()){
//			allGraphDBsArrayList.add(graphDB201801);
//			allGraphDBsArrayList.add(graphDB201802);
//			allGraphDBsArrayList.add(graphDB201803);
//			allGraphDBsArrayList.add(graphDB201804);
//			timesArrayList.add(new TimeDetail("2018-1-1 00:00:00", "2018-1-31 23:59:59"));
//			timesArrayList.add(new TimeDetail("2018-2-1 00:00:00", "2018-2-28 23:59:59"));
//			timesArrayList.add(new TimeDetail("2018-3-1 00:00:00", "2018-3-31 23:59:59"));
			timesArrayList.add(new TimeDetail("2018-4-1 00:00:00", "2018-4-30 23:59:59"));
			for(int timePart=0;timePart<timesArrayList.size();timePart++) {
				//计算某个时间段的嫌疑指数
				curNode= graphDB2.getNodeById(664);
			    neighborNodes = getAllNeighbors(curNode);
				for(int i=0;i<neighborNodes.size();i++) {
					Node curNeighNode = neighborNodes.get(i);
					//如果邻居节点为属性确认人员
					if(curNeighNode.hasProperty("tag1")) {
						for(int tagNum=1;tagNum<11;tagNum++) {
							//合并出入度的总时间
							double totalDuration = 0;
							double totalduration2 = 0;
							double totalduration3 = 0;
							String number=Integer.toString(tagNum);
//							System.out.println("xxxxxxxxxxx"+tagNum);
							String property = "tag"+number;
							String tag1 = (String) curNeighNode.getProperty(property);
//							System.out.println(tag1);
							if(!(tag1.equals("0 "))){
//								System.out.println(property);
								//计算第一层
								//计算入度关系的总时间
								Relationship inRelationship = getRelationshipByTwoNodes(curNode, curNeighNode, Direction.INCOMING);
								if(inRelationship!=null) {
									Map<String, Object> inproperties = inRelationship.getAllProperties();
									for(Map.Entry<String, Object> entry:inproperties.entrySet()) {
										if(isValidDate(entry.getKey())) {
											String call_startTime = entry.getKey();
											double duration = Double.parseDouble((String)entry.getValue());
//											System.out.println("duration=========="+duration);
											Date curDate = effectiveDate.strToDate(call_startTime);
											Date startDate = effectiveDate.strToDate(timesArrayList.get(timePart).getStartTime());
											Date endDate = effectiveDate.strToDate(timesArrayList.get(timePart).getEndTime());
											if(effectiveDate.isBelongCalendar(curDate, startDate, endDate)) {
												totalDuration = totalDuration+duration;
											}
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
//											System.out.println("duration=========="+duration);
											Date curDate = effectiveDate.strToDate(call_startTime);
											Date startDate = effectiveDate.strToDate(timesArrayList.get(timePart).getStartTime());
											Date endDate = effectiveDate.strToDate(timesArrayList.get(timePart).getEndTime());
											if(effectiveDate.isBelongCalendar(curDate, startDate, endDate)) {
												totalDuration = totalDuration+duration;
												
											}
										}
									}
								}
								double closeRate = computeCloseRate(totalDuration, totalduration2, totalduration3, 1);
//								System.out.println(totalDuration);
								System.out.println("time:"+(timePart+1)+"----"+curNode.getId()+"--------tag"+tagNum+"嫌疑:"+closeRate);
								if(tagNum==2) {
									curNode.setProperty("tag2_rate", Math.abs(closeRate2-closeRate1));
								}
								if(tagNum==9) {
									curNode.setProperty("tag9_rate", Math.abs(closeRate2-1.652345763876955));
								}
								//计算第二层的亲密度
								ArrayList<Node> neighborNodes2 = getAllNeighbors(curNeighNode);
								for(int j=0;j<neighborNodes2.size();j++) {
									Node curNeighNode2 = neighborNodes2.get(j);
									if(curNeighNode2.hasProperty("tag1")&&curNeighNode2.getId()!=curNeighNode.getId()) {
										String tag2 = (String) curNeighNode2.getProperty(property);
										if(!(tag2.equals("0 "))) {
//											System.out.println(tagNum);
											//计算入度时间
											Relationship inRelationship2 = getRelationshipByTwoNodes(curNeighNode, curNeighNode2, Direction.INCOMING);
											if(inRelationship2!=null) {
												Map<String, Object> inProperties2 = inRelationship2.getAllProperties();
												for(Map.Entry<String, Object> entry:inProperties2.entrySet()) {
													if(isValidDate(entry.getKey())) {
														String call_startTime2 = entry.getKey();
														double duration2 = Double.parseDouble((String)entry.getValue());
														Date curDate2 = effectiveDate.strToDate(call_startTime2);
														Date startDate2 = effectiveDate.strToDate(timesArrayList.get(timePart).getStartTime());
														Date endDate2 = effectiveDate.strToDate(timesArrayList.get(timePart).getEndTime());
														if(effectiveDate.isBelongCalendar(curDate2, startDate2, endDate2)) {
															totalduration2 = totalduration2+duration2;
														}
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
														Date curDate2 = effectiveDate.strToDate(call_startTime2);
														Date startDate2 = effectiveDate.strToDate(timesArrayList.get(timePart).getStartTime());
														Date endDate2 = effectiveDate.strToDate(timesArrayList.get(timePart).getEndTime());
														if(effectiveDate.isBelongCalendar(curDate2, startDate2, endDate2)) {
															totalduration2 = totalduration2+duration2;
														}
													}
												}
											}
											closeRate2 = computeCloseRate(totalDuration, totalduration2, totalduration3, 2);
//											if(curNode.getId()==664) {
//												System.out.println(totalDuration);
//												System.out.println(totalduration2);
//												System.out.println("time:"+(timePart+1)+"----"+curNode.getId()+"--------tag"+tagNum+"嫌疑:"+closeRate2);
//											}
//											System.out.println(totalDuration);
//											System.out.println(totalduration2);
											System.out.println("time:"+(timePart+1)+"----"+curNode.getId()+"--------tag"+tagNum+"嫌疑:"+closeRate2);
											curNode.setProperty("time:"+timesArrayList.get(timePart).getStartTime()+"-tag"+tagNum, closeRate2);
											if(tagNum==2) {
												curNode.setProperty("tag2_rate", Math.abs(closeRate2-1.6295140436914848));
											}
											if(tagNum==9) {
												curNode.setProperty("tag9_rate", Math.abs(closeRate2-1.652345763876955));
											}
										}
									}
								}
							}
						}
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

