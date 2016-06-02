package securedlogin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import securedlogin.util.ConnectionManager;

public class UserDAO {	
	public static UserBean login(UserBean bean) {
		try {
			String userName=bean.getUserName(), passwd=bean.getPassWord();
			Connection connection=new ConnectionManager().connect();
			String searchQuery="select * from users where uname=? and passwd=?";
			PreparedStatement preparedStatement=connection.prepareStatement(searchQuery);
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, passwd);
			ResultSet rs=preparedStatement.executeQuery();
			if(!rs.next()) {
				System.out.println("Invalid username/password!");
				bean.setValid(false);
			}
			else {
				searchQuery="select * from profile where uname=?";
				preparedStatement=connection.prepareStatement(searchQuery);
				preparedStatement.setString(1, rs.getString(1));
				rs=preparedStatement.executeQuery();
				System.out.println("Logged in");
				if(rs.next()) {
					bean.setName(rs.getString(2));
					bean.setAddress(rs.getString(3));
				}
				bean.setValid(true);
				connection.close();
			}
		}catch(ClassNotFoundException|SQLException e) {
			e.printStackTrace();
		}
		return bean;
		
	}
}
