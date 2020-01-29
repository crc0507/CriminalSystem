package bupt;
import bupt.DBConnection;
import oracle.net.aso.l;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.graphdb.schema.IndexDefinition;
import org.neo4j.graphdb.schema.Schema;

public class AddRelation extends DBConnection{

	private static Connection connection = null;
	static AddRelation neo4j = new AddRelation();

	private static PreparedStatement pre1 = null;
	private static PreparedStatement pre2 = null;
	private static PreparedStatement pre3 = null;
	static File file = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\databases\\\\data.db");
	static GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);
	public static void main(String[] args) throws SQLException{
		// TODO Auto-generated method stub
		Date date1 = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd:hh:mm:ss");
		
		try (Transaction tx = graphDB.beginTx()){

			connection = dbConn("scott", "123456");
			
			if(connection==null) {
				System.out.println("fail");
				System.exit(0);
			}

			Node node1 = graphDB.getNodeById(126);//小王
			Node node2 = graphDB.getNodeById(125);//小雨
			Node node3 = graphDB.getNodeById(128);//妈
			Node node4 = graphDB.getNodeById(127);//王有强
			Node node5 = graphDB.getNodeById(169);//徐杰名
			Node node6 = graphDB.getNodeById(149);//郑乐民
			Node node7 = graphDB.getNodeById(129);//彭素谷
			Node node8 = graphDB.getNodeById(34);//陈志凌
			Relationship r1 = node8.createRelationshipTo(node6, MyRelationshipTypes.HAS_CONTACT);
			Relationship r2 = node6.createRelationshipTo(node8, MyRelationshipTypes.HAS_CONTACT);
			r1.setProperty("2018-4-21 13:46:45", "120");
			r1.setProperty("2018-4-18  9:38:37", "20");
			r1.setProperty("2018-4-17  9:38:37", "55");
			r2.setProperty("2018-4-12  9:38:37", "54");
			r2.setProperty("2018-4-25 13:56:41", "41");
			
//			Relationship r7 = node3.createRelationshipTo(node5, MyRelationshipTypes.HAS_CONTACT);
//			Relationship r8 = node5.createRelationshipTo(node3, MyRelationshipTypes.HAS_CONTACT);
//			r7.setProperty("2018-1-29 13:49:49", "10");
//			r7.setProperty("2018-1-18 18:23:58", "18");
//			r7.setProperty("2018-1-2 19:23:38:", "20");
//			r7.setProperty("2018-3-27 15:52:40", "12");
//			r7.setProperty("2018-2-18 9:26:41", "8");
//			r7.setProperty("2018-1-29 14:37:33", "48");
//			r8.setProperty("2018-1-18  9:38:37", "30");
//			r8.setProperty("2018-2-18  9:38:37", "20");
//			r8.setProperty("2018-1-17  9:38:37", "11");
//			r8.setProperty("2018-1-16  9:38:37", "8");
//			r8.setProperty("2018-1-19  9:38:37", "12");
//			r8.setProperty("2018-1-15  9:38:37", "23");
//			r8.setProperty("2018-1-12  9:38:37", "54");
//			Relationship r9 = node5.createRelationshipTo(node7, MyRelationshipTypes.HAS_CONTACT);
//			Relationship r10 = node7.createRelationshipTo(node5, MyRelationshipTypes.HAS_CONTACT);
//			r9.setProperty("2018-1-27 13:49:49", "10");
//			r9.setProperty("2018-1-28 18:23:58", "18");
//			r9.setProperty("2018-1-3 19:23:38:", "20");
//			r9.setProperty("2018-1-27 15:52:40", "12");
//			r9.setProperty("2018-2-17 9:26:41", "8");
//			r9.setProperty("2018-1-28 14:37:33", "48");
//			r10.setProperty("2018-1-19  9:38:37", "30");
//			r10.setProperty("2018-1-18  9:38:37", "20");
//			r10.setProperty("2018-1-11  9:38:37", "11");
//			r10.setProperty("2018-1-23  9:38:37", "8");
//			r10.setProperty("2018-1-8  9:38:37", "12");
//			r10.setProperty("2018-1-5  9:38:37", "23");
//			r10.setProperty("2018-1-14  9:38:37", "54");
//			Relationship r1 = node1.createRelationshipTo(node2, MyRelationshipTypes.HAS_CONTACT);
//			Relationship r2 = node2.createRelationshipTo(node1, MyRelationshipTypes.HAS_CONTACT);
//			r1.setProperty("2018-1-29 13:49:49", "10");
//			r1.setProperty("2018-1-18 18:23:58", "18");
//			r1.setProperty("2018-1-2 19:23:38:", "20");
//			r1.setProperty("2018-3-27 15:52:40", "12");
//			r2.setProperty("2018-2-18 9:26:41", "8");
//			r2.setProperty("2018-1-29 14:37:33", "48");
//			Relationship r3 = node3.createRelationshipTo(node4, MyRelationshipTypes.HAS_CONTACT);
//			Relationship r4 = node4.createRelationshipTo(node3, MyRelationshipTypes.HAS_CONTACT);
//			r3.setProperty("2018-2-18 18:23:58", "18");
//			r3.setProperty("2018-2-2 19:23:38:", "20");
//			r3.setProperty("2018-1-27 15:52:40", "12");
//			r3.setProperty("2018-1-18 9:26:41", "8");
//			r4.setProperty("2018-1-25 13:56:41", "25");
//			r4.setProperty("2018-1-11 18:58:02", "45");
//			r4.setProperty("2018-1-29 13:49:49", "10");
//			r4.setProperty("2018-1-21 13:46:45", "120");
//			r4.setProperty("2018-1-14 9:10:04", "28");
//			r4.setProperty("2018-3-25 12:53:46", "56");
//			Relationship r5 = node1.createRelationshipTo(node3, MyRelationshipTypes.HAS_CONTACT);
//			Relationship r6 = node3.createRelationshipTo(node1, MyRelationshipTypes.HAS_CONTACT);
//			r5.setProperty("2018-1-29 13:49:49", "10");
//			r5.setProperty("2018-1-27 15:52:40", "12");
//			r5.setProperty("2018-1-24 12:52:40", "12");
//			r5.setProperty("2018-1-18 9:26:41", "8");
//			r5.setProperty("2018-1-31 14:02:31", "15");
//			r5.setProperty("2018-1-10 20:16:15", "44");
//			r5.setProperty("2018-2-1 4:45:46", "36");
//			r6.setProperty("2018-1-29 14:37:33", "48");
//			r6.setProperty("2018-2-10 6:54:32", "12");
//			r6.setProperty("2018-1-15 16:43:23", "24");
//			r6.setProperty("2018-2-18 13:00:57", "30");
//			r6.setProperty("2018-1-5 12:26:44", "34");
			

			System.out.println("success");
			Date date2 = new Date();
			System.out.println(dateFormat.format(date1));
			System.out.println(dateFormat.format(date2));
			tx.success();
			tx.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			connection.close();
			
		}
	}

	public enum MyRelationshipTypes implements RelationshipType{
		HAS_CONTACT;
	}
}

