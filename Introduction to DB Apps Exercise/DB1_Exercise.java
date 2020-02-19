

import java.sql.*;
import java.util.Properties;

public class DB1_Exercise {
    private static final String CONNECTION_STRING = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "minions_db";

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection =
                DriverManager.getConnection(CONNECTION_STRING + DATABASE_NAME, properties);

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM minions ");
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(String.format("%s %s %s %s",
                    resultSet.getString("id"),
                    resultSet.getString("name"),
                    resultSet.getString("age"),
                    resultSet.getString("town_id")
            ));
        }


    }
}
