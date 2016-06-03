package securedlogin.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class LogDatabase {
	void generateLogDB(String attackClass, String attackType, String sig) throws ClassNotFoundException, SQLException {
		Timestamp timestamp=new Timestamp(Calendar.getInstance().getTime().getTime());
		String insertQuery="insert into log values(?,?,?,?)";
		Connection connection=new ConnectionManager().connect();
		PreparedStatement preparedStatement=connection.prepareStatement(insertQuery);
		preparedStatement.setString(1, attackClass);
		preparedStatement.setString(2, attackType);
		preparedStatement.setTimestamp(3, timestamp);
		preparedStatement.setString(4, sig);
		preparedStatement.executeUpdate();
		connection.close();
	}
}
