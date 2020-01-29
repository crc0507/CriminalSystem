package bupt;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	public static Connection dbConn(String name, String pass) {
		// TODO Auto-generated method stub
		Connection c=null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			c = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "scott", "123456");
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return c;
	}

}
