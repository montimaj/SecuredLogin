package securedlogin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
	private static Connection connect() throws ClassNotFoundException, SQLException {
		Connection con=null;
		Class.forName("com.mysql.jdbc.Driver");
		con=DriverManager.getConnection("jdbc:mysql://localhost:3306/securedlogin","sayantan","sm1234");		
		return con;
	}
	public static UserBean login(UserBean bean) {
		try {
			String userName=bean.getUserName(), passwd=bean.getPassWord();
			String searchQuery="select * from users where uname="+"\""+userName+"\""+" and passwd="+"\""+passwd+"\"";
			PreparedStatement preparedStatement=connect().prepareStatement(searchQuery);
			ResultSet rs=preparedStatement.executeQuery();
			if(!rs.next()) {
				System.out.println("Invalid username/password!");
				bean.setValid(false);
			}
			else {
				searchQuery="select * from profile where uname="+"\""+userName+"\"";
				preparedStatement=connect().prepareStatement(searchQuery);
				rs=preparedStatement.executeQuery();
				System.out.println("Logged in");
				if(rs.next()) {
					bean.setName(rs.getString(2));
					bean.setAddress(rs.getString(3));
				}
				bean.setValid(true);
			}
		}catch(ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		}
		return bean;
		
	}
}
