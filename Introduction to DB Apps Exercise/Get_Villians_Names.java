
import java.sql.*;
import java.util.Properties;

public class Get_Villians_Names {
    public static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    public static final String DATABASE_NAME = "minions_db";

    public static void main(String[] args) throws SQLException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection =
                DriverManager.getConnection(
                        CONNECTION_URL + DATABASE_NAME, properties
                );

        String query = "select v.name, count(mv.minion_id) as count\n" +
                "from villains as v\n" +
                "         left join minions_villains as mv\n" +
                "                   on v.id = mv.villain_id\n" +
                "\n" +
                "group by v.name\n" +
                "having count > 15\n" +
                "order by count desc;";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println(String.format("%s %d",
                    resultSet.getString("name"),
                    resultSet.getInt("count")
            ));
        }
    }
}
