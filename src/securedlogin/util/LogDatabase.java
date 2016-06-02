package securedlogin.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

public class LogDatabase {
	void generateLogDB(String attackClass, String attackType) throws ClassNotFoundException, SQLException {
		Timestamp timeStamp=new Timestamp(Calendar.getInstance().getTime().getTime());
		String insertQuery="insert into log values(?,?,?)";
		Connection connection=new ConnectionManager().connect();
		PreparedStatement preparedStatement=connection.prepareStatement(insertQuery);
		preparedStatement.setString(1, attackClass);
		preparedStatement.setString(2, attackType);
		preparedStatement.setTimestamp(3, timeStamp);
		preparedStatement.executeUpdate();
		connection.close();
	}
}
