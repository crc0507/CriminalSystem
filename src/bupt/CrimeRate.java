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
import bupt.EffectiveDate;
import oracle.net.aso.i;
import oracle.net.aso.l;
import scala.reflect.internal.Trees.New;

public class CrimeRate {
	static File file = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\databases\\\\newest.db");
	static GraphDatabaseService graphDB_c = new GraphDatabaseFactory().newEmbeddedDatabase(file);
//	static File file201801 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201801.db");
//	static GraphDatabaseService graphDB201801 = new GraphDatabaseFactory().newEmbeddedDatabase(file201801);
//	static File file201802 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201802.db");
//	static GraphDatabaseService graphDB201802 = new GraphDatabaseFactory().newEmbeddedDatabase(file201802);
//	static File file201803 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201803.db");
//	static GraphDatabaseService graphDB201803 = new GraphDatabaseFactory().newEmbeddedDatabase(file201803);
//	static File file201804 = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\database\\\\201804.db");
//	static GraphDatabaseService graphDB201804 = new GraphDatabaseFactory().newEmbeddedDatabase(file201804);
	static ArrayList<GraphDatabaseService> allGraphDBsArrayList = new ArrayList<>();

	public static EffectiveDate effectiveDate = new EffectiveDate();
	public static ArrayList<TimeDetail> timesArrayList = new ArrayList<>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		try (Transaction tx = graphDB_c.beginTx()){
//			allGraphDBsArrayList.add(graphDB201801);
//			allGraphDBsArrayList.add(graphDB201802);
//			allGraphDBsArrayList.add(graphDB201803);
//			allGraphDBsArrayList.add(graphDB201804);
			timesArrayList.add(new TimeDetail("2018-1-1 00:00:00", "2018-1-31 23:59:59"));
			timesArrayList.add(new TimeDetail("2018-2-1 00:00:00", "2018-2-28 23:59:59"));
			timesArrayList.add(new TimeDetail("2018-3-1 00:00:00", "2018-3-31 23:59:59"));
			timesArrayList.add(new TimeDetail("2018-4-1 00:00:00", "2018-4-30 23:59:59"));
			for(int timePart=0;timePart<timesArrayList.size();timePart++) {
				ResourceIterable<Node> nodesIterable = graphDB_c.getAllNodes();
				ResourceIterator<Node> nodesIterator = nodesIterable.iterator();
				while(nodesIterator.hasNext()) {
					Node curNode = nodesIterator.next();
					//����ýڵ�δ����עtag������Ҫ��������ָ��
					if(!(curNode.hasProperty("tag1"))) {
						ArrayList<Node> neighborNodes = getAllNeighbors(curNode);
						//���������ھӽڵ㣬���Ƿ��з�����Ա
						for(int i=0;i<neighborNodes.size();i++) {
							Node curNeighNode = neighborNodes.get(i);
							
							if(curNeighNode.hasProperty("tag1")) {
								for(int tagNum=1;tagNum<11;tagNum++) {
									
									double totalDuration = 0;
									double totalduration2 = 0;
									double totalduration3 = 0;
									String number=Integer.toString(tagNum);
//									System.out.println("xxxxxxxxxxx"+tagNum);
									String property = "tag"+number;
									String tag1 = (String) curNeighNode.getProperty(property);
//									System.out.println(tag1);
									if(!(tag1.equals("0 "))){
//										System.out.println(property);
										
										
										Relationship inRelationship = getRelationshipByTwoNodes(curNode, curNeighNode, Direction.INCOMING);
										if(inRelationship!=null) {
											Map<String, Object> inproperties = inRelationship.getAllProperties();
											for(Map.Entry<String, Object> entry:inproperties.entrySet()) {
												if(isValidDate(entry.getKey())) {
													String call_startTime = entry.getKey();
													double duration = Double.parseDouble((String)entry.getValue());
//													System.out.println("duration=========="+duration);
													Date curDate = effectiveDate.strToDate(call_startTime);
													Date startDate = effectiveDate.strToDate(timesArrayList.get(timePart).getStartTime());
													Date endDate = effectiveDate.strToDate(timesArrayList.get(timePart).getEndTime());
													if(effectiveDate.isBelongCalendar(curDate, startDate, endDate)) {
														totalDuration = totalDuration+duration;
													}
												}
											}
										}
										
										Relationship outRelationship = getRelationshipByTwoNodes(curNode, curNeighNode, Direction.OUTGOING);
										if(outRelationship!=null) {
											Map<String, Object> outproperties = outRelationship.getAllProperties();
											for(Map.Entry<String, Object> entry:outproperties.entrySet()) {
												if(isValidDate(entry.getKey())) {
													String call_startTime = entry.getKey();
													double duration = Double.parseDouble((String)entry.getValue());
//													System.out.println("duration=========="+duration);
													Date curDate = effectiveDate.strToDate(call_startTime);
													Date startDate = effectiveDate.strToDate(timesArrayList.get(timePart).getStartTime());
													Date endDate = effectiveDate.strToDate(timesArrayList.get(timePart).getEndTime());
													if(effectiveDate.isBelongCalendar(curDate, startDate, endDate)) {
														totalDuration = totalDuration+duration;
														
													}
												}
											}
										}
//										double closeRate = computeCloseRate(totalDuration, totalduration2, totalduration3, 1);
//										System.out.println(totalDuration);
//										System.out.println("time:"+(timePart+1)+"----"+curNode.getId()+"--------tag"+tagNum+"嫌疑指数:"+closeRate);
										//����ڶ�������ܶ�
										ArrayList<Node> neighborNodes2 = getAllNeighbors(curNeighNode);
										for(int j=0;j<neighborNodes2.size();j++) {
											Node curNeighNode2 = neighborNodes2.get(j);
											if(curNeighNode2.hasProperty("tag1")&&curNeighNode2.getId()!=curNeighNode.getId()) {
												String tag2 = (String) curNeighNode2.getProperty(property);
												if(!(tag2.equals("0 "))) {
//													System.out.println(tagNum);
													
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
													double closeRate2 = computeCloseRate(totalDuration, totalduration2, totalduration3, 2);
													System.out.println("time:"+(timePart+1)+"----"+curNode.getId()+"--------tag"+tagNum+"嫌疑指数:"+closeRate2);
													
												}
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
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			System.out.println("start:"+df1.format(new Date()));
			System.out.println("end:"+df2.format(new Date()));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally {
			
		}
	}
	
	
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
	
	//���������ڵ��ҹ�ϵ
	public static Relationship getRelationshipByTwoNodes(Node node1, Node node2, Direction dir) {
		Relationship returnRelationship = null;
		if(dir==Direction.INCOMING) {
			Iterable<Relationship> allIterable = node1.getRelationships(dir);
			Iterator<Relationship> allRelations = allIterable.iterator();
			while(allRelations.hasNext()) {
				Relationship curRelationship = allRelations.next();

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
				
				if(curRelationship.getEndNodeId()==node2.getId()) {
					returnRelationship = curRelationship;
				}
			}
		}
		return returnRelationship;
	}
	
	//Ѱ��ĳ��node��ȫ���ھ�
	public static ArrayList<Node> getAllNeighbors(Node node) {
		ArrayList<Node> neighborNodes = new ArrayList<>();
		//Ѱ�ҳ����ھ�
		Iterable<Relationship> outRelationIterable = node.getRelationships(Direction.OUTGOING);
		Iterator<Relationship> outRelationIterator = outRelationIterable.iterator();
		while(outRelationIterator.hasNext()) {
			Iterator<Node> neighborsIterator = neighborNodes.iterator();
			boolean isExist = false;
			Relationship curRelationship = outRelationIterator.next();
			Node otherNode = curRelationship.getEndNode();
			//�жϸõ��Ƿ��Ѿ�����
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
		
		Iterable<Relationship> inRelationIterable = node.getRelationships(Direction.INCOMING);
		Iterator<Relationship> inRelationIterator = inRelationIterable.iterator();
		while(inRelationIterator.hasNext()) {
			Iterator<Node> neighborsIterator = neighborNodes.iterator();
			boolean isExist = false;
			Relationship curRelationship = inRelationIterator.next();
			Node otherNode = curRelationship.getStartNode();
			//�жϸõ��Ƿ��Ѿ�����
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
	//ʱ���ʽ�Ƿ�Ϸ�
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
