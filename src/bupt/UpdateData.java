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

public class UpdateData extends DBConnection{

	private static Connection connection = null;
	private static Statement sql = null;
	private static ResultSet rSet1 = null;//data_person��
	private static ResultSet rSet2 = null;//callrecords��
	private static ResultSet rSet3 = null;//phone��
	private static ResultSet rSet4 = null;
	static Testdata neo4j = new Testdata();

	private static PreparedStatement pre1 = null;
	private static PreparedStatement pre2 = null;
	private static PreparedStatement pre3 = null;
	private static PreparedStatement pre4 = null;
	static File file = new File("D:\\\\neo4j-community-3.4.9\\\\data\\\\databases\\\\demo2.db");
	static GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase(file);
	static ArrayList usersCID = new ArrayList();//person���е�cid
	static ArrayList usersName = new ArrayList();//person���е�name
	static ArrayList usersID = new ArrayList();//person���е�id
	static ArrayList usersCheckType = new ArrayList();//person���е�checktype
	static ArrayList usersAge = new ArrayList();//person���е�age
	static ArrayList personTAG1 = new ArrayList();//��
	static ArrayList personTAG2 = new ArrayList();//��
	static ArrayList personTAG3 = new ArrayList();//��
	static ArrayList personTAG4 = new ArrayList();//��
	static ArrayList personTAG5 = new ArrayList();//��
	static ArrayList personTAG6 = new ArrayList();//��
	static ArrayList personTAG7 = new ArrayList();//ƭ
	static ArrayList personTAG8 = new ArrayList();//��
	static ArrayList personTAG9 = new ArrayList();//ǰ
	static ArrayList personTAG10 = new ArrayList();//��
	static ArrayList personTAG11 = new ArrayList();//��
	static ArrayList personTAG12 = new ArrayList();//��
	static ArrayList personTAG13 = new ArrayList();//��
	static ArrayList personTAG14 = new ArrayList();//��
	static ArrayList personCHECK1 = new ArrayList();//���ܻ�
	static ArrayList personCHECK2 = new ArrayList();//���ܻ�
	static ArrayList personCHECK3 = new ArrayList();//��׿��
	static ArrayList personCHECK4 = new ArrayList();//ƻ����
	static ArrayList personCHECK5 = new ArrayList();//sim��
	static ArrayList personCHECK6 = new ArrayList();//����
	static ArrayList personCHECK7 = new ArrayList();//��Ƭ
	static ArrayList personCHECK8 = new ArrayList();//���п�
	static ArrayList personCHECK9 = new ArrayList();//���֤��
	static ArrayList personCHECK10 = new ArrayList();// ����
	static ArrayList personCHECK11 = new ArrayList();// �ֻ���
	static ArrayList personCHECK12 = new ArrayList();//ͷ��
	static ArrayList personCHECK13 = new ArrayList();//����
	static ArrayList personCHECK14 = new ArrayList();//����
	static ArrayList personCHECK15 = new ArrayList();//��ϸ��Ϣ

	static ArrayList recordName = new ArrayList();//callrecords���е�name
	static ArrayList duration = new ArrayList();//callrecords���е�ͨ��ʱ��
	static ArrayList calltime_start = new ArrayList();//callrecords���е�ͨ������ʱ��
	static ArrayList record_CID = new ArrayList();//callrecords���е�cid
	static ArrayList record_phonenumber = new ArrayList();//callrecords���е��ֻ�����
	static ArrayList record_type = new ArrayList();//callrecords���еĺ������ͣ�����or����
	
	static ArrayList phone_number = new ArrayList();//phone���е�phonenumber
	static ArrayList phone_number1 = new ArrayList();//phone���е�phonenumber1
	static ArrayList phone_cid = new ArrayList();//phone���е�cid
	
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

			pre1 = connection.prepareStatement("select * from DATA_PERSON");
			rSet1 = pre1.executeQuery();
			pre2 = connection.prepareStatement("select * from TEMP_20180831");
			rSet2 = pre2.executeQuery();
			pre3 = connection.prepareStatement("select * from DATA_PHONE");
			rSet3 = pre3.executeQuery();
			
//*****************************************************************************************//
			int c=0;
			while(rSet1.next()) {
				usersCID.add(rSet1.getString(2));
				usersName.add(rSet1.getString(5));
				usersID.add(rSet1.getString(10));
				usersCheckType.add(rSet1.getString(4));
				usersAge.add(rSet1.getString(9));
				personTAG1.add(rSet1.getString(22));
				personTAG2.add(rSet1.getString(23));
				personTAG3.add(rSet1.getString(24));
				personTAG4.add(rSet1.getString(25));
				personTAG5.add(rSet1.getString(26));
				personTAG6.add(rSet1.getString(27));
				personTAG7.add(rSet1.getString(28));
				personTAG8.add(rSet1.getString(29));
				personTAG9.add(rSet1.getString(30));
				personTAG10.add(rSet1.getString(31));
				personTAG11.add(rSet1.getString(32));
				personTAG12.add(rSet1.getString(33));
				personTAG13.add(rSet1.getString(34));
				personTAG14.add(rSet1.getString(35));
				c++;
			}
			System.out.println("person��ע�����");
			////////////////////////////////////////////////////////////////////////////////
			while(rSet3.next()) {
				phone_number.add(rSet3.getString(23));
				phone_cid.add(rSet3.getString(2));
				phone_number1.add(rSet3.getString(24));
			}
			System.out.println("phone��ע�����");
			/////////////////////////////////////////////////////////////////////////////////////////
			int hotdog1 = 0;
			while(rSet2.next()) {
				record_CID.add(rSet2.getString(2));
				recordName.add(rSet2.getString(7));
				duration.add(rSet2.getString(8));
				calltime_start.add(rSet2.getString(9));
				//Ϊrecordname���ֻ���
				record_phonenumber.add(rSet2.getString(4));
				record_type.add(rSet2.getString(6));
				hotdog1++;
				//System.out.println(hotdog1);
				if(hotdog1==20000) {
					break;
				}
			}
			System.out.println("record��ע�����");
			/////////////////////////////////////////////////////////////////////////////////////////////////////
			//node������
			Index<Node> usersCIDIndex = graphDB.index().forNodes("USERSs");
			Index<Node> phonenumberIndex = graphDB.index().forNodes("USERSs");
//			Index<Node> phonenumber1Index = graphDB.index().forNodes("USERSs");
			//��ϵ������
			Index<Relationship> relationIndex = graphDB.index().forRelationships("HAS_CONTACTs");
			
/////////////////////////////////////////////////////////////////////////////////////////////////////
			//����record���е������ֻ���
			for(int i=0;i<20000;i++) {
				Node curNode = null;
				IndexHits<Node> allNodesHits = null;
				if(record_phonenumber.get(i)!=null) {
					curNode.getProperty("name");
					allNodesHits = phonenumberIndex.get("phonenumber", record_phonenumber.get(i));
					
					if(!(allNodesHits.hasNext())) {
						curNode = graphDB.createNode(MyLabels.USERS);
						curNode.setProperty("phonenumber", record_phonenumber.get(i));
						if(recordName.get(i)!=null) {
							curNode.setProperty("name", recordName.get(i));
						}
						phonenumberIndex.add(curNode, "phonenumber", record_phonenumber.get(i));
					}	
				}
						
			}
			//����phone���������ֻ���
			for(int i=0;i<phone_cid.size();i++) {
				Node curNode = null;
				IndexHits<Node> allNodesHits = null;
				if(phone_number.get(i)!=null) {
					allNodesHits = phonenumberIndex.get("phonenumber", phone_number.get(i));
					if(!(allNodesHits.hasNext())) {
						curNode = graphDB.createNode(MyLabels.USERS);
						curNode.setProperty("phonenumber", phone_number.get(i));
						phonenumberIndex.add(curNode, "phonenumber", phone_number.get(i));
						if(phone_cid.get(i)!=null) {
							curNode.setProperty("cid", phone_cid.get(i));
							usersCIDIndex.add(curNode, "cid", phone_cid.get(i));
						}
					}
				}
			}
			
			//�ٴα���record����ͨ����ϵ
			for(int i=0;i<20000;i++) {
				Node node1 = null;
				Node node2 = null;
				if(record_phonenumber.get(i)!=null) {
					IndexHits<Node> tempHits = phonenumberIndex.get("phonenumber", record_phonenumber.get(i));
					node1 = tempHits.getSingle();
					//����cid���ӹ�ϵ
					if(record_CID.get(i)!=null) {
						IndexHits<Node> tempNodeHits = usersCIDIndex.get("cid", record_CID.get(i));
						if(tempNodeHits.hasNext()) {
							node2 = tempNodeHits.next();
							tempNodeHits.close();
						}
					}
				}
				//ͨ��˫�����ҵ�֮��
				if(node1!=null&&node2!=null) {
					//node1����
					if(record_type.get(i).equals("1")) {
						//onlyID�ĸ�ʽΪ���к���+���к���
						IndexHits<Relationship> allRelations = relationIndex.get("onlyID", (String)node2.getProperty("phonenumber")+(String)node1.getProperty("phonenumber"));
						//��ϵ������
						if(!(allRelations.hasNext())) {
							Relationship curRelationship = node2.createRelationshipTo(node1, MyRelationshipTypes.HAS_CONTACT);
							if(calltime_start.get(i)!=null&&duration.get(i)!=null) {
								curRelationship.setProperty((String) calltime_start.get(i), duration.get(i));
							}
							curRelationship.setProperty("onlyID", (String)node2.getProperty("phonenumber")+(String)node1.getProperty("phonenumber"));
							relationIndex.add(curRelationship, "onlyID", (String)node2.getProperty("phonenumber")+(String)node1.getProperty("phonenumber"));
						}
						//��ϵ����
						if(allRelations.hasNext()) {
							Relationship curRelationship = allRelations.next();
							if(calltime_start.get(i)!=null&&duration.get(i)!=null) {
								curRelationship.setProperty((String) calltime_start.get(i), duration.get(i));
							}
						}
					}
					//node1Ϊ����
					if(record_type.get(i).equals("2")) {
						IndexHits<Relationship> allRelations = relationIndex.get("onlyID", (String)node1.getProperty("phonenumber")+(String)node2.getProperty("phonenumber"));
						//��ϵ������
						if(!(allRelations.hasNext())) {
							Relationship curRelationship = node1.createRelationshipTo(node2, MyRelationshipTypes.HAS_CONTACT);
							if(calltime_start.get(i)!=null&&duration.get(i)!=null) {
								curRelationship.setProperty((String) calltime_start.get(i), duration.get(i));
							}
							curRelationship.setProperty("onlyID", (String)node1.getProperty("phonenumber")+(String)node2.getProperty("phonenumber"));
							relationIndex.add(curRelationship, "onlyID", (String)node1.getProperty("phonenumber")+(String)node2.getProperty("phonenumber"));
						}
						//��ϵ����
						if(allRelations.hasNext()) {
							Relationship curRelationship = allRelations.next();
							if(calltime_start.get(i)!=null&&duration.get(i)!=null) {
								curRelationship.setProperty((String) calltime_start.get(i), duration.get(i));
							}
						}
					}
				}
				
			}
			
			////////////////////////////////
			for(int mm=0;mm<usersCID.size();mm++) {
				for(int nn=0;nn<phone_cid.size();nn++) {
					//cid相同的
					if(usersCID.get(mm).equals(phone_cid.get(nn))) {
						if(phone_number.get(nn)!=null) {
							String phonenumberString = (String) phone_number.get(nn);
							IndexHits<Node> findNodes = phonenumberIndex.get("phonenumber", phonenumberString);
							while(findNodes.hasNext()) {
								Node finNode = findNodes.next();
								if(usersName.get(mm)!=null) {
									finNode.setProperty("name", usersName.get(mm));
								}
								finNode.setProperty("tag1", personTAG1.get(mm));
								finNode.setProperty("tag2", personTAG2.get(mm));
								finNode.setProperty("tag3", personTAG3.get(mm));
								finNode.setProperty("tag4", personTAG4.get(mm));
								finNode.setProperty("tag5", personTAG5.get(mm));
								finNode.setProperty("tag6", personTAG6.get(mm));
								finNode.setProperty("tag7", personTAG7.get(mm));
								finNode.setProperty("tag8", personTAG8.get(mm));
								finNode.setProperty("tag9", personTAG9.get(mm));
								finNode.setProperty("tag10", personTAG10.get(mm));
								finNode.setProperty("tag11", personTAG11.get(mm));
								finNode.setProperty("tag12", personTAG12.get(mm));
								finNode.setProperty("tag13", personTAG13.get(mm));
								finNode.setProperty("tag14", personTAG14.get(mm));
							}
						}
						
					}
				}
			}
			
			
			System.out.println("success");
			Date date2 = new Date();
			System.out.println(dateFormat.format(date1));
			System.out.println(dateFormat.format(date2));
			tx.success();
			tx.close();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			rSet1.close();
			rSet2.close();
			rSet3.close();
			connection.close();
			
		}
	}

	public enum MyLabels implements Label{
		USERS;
	}
	public enum MyRelationshipTypes implements RelationshipType{
		HAS_CONTACT;
	}


}
