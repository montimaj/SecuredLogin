package securedlogin.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	public Connection connect() throws ClassNotFoundException, SQLException {
		Connection con=null;
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/securedlogin","sayantan","sm1234");		
		return con;
	}
}
