
import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Writing_your_own_data_retrieval_application {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();

        String user = "";
        props.setProperty("user", "root");
        String password = "";
        props.setProperty("password", "1234");

        Connection connection =
                DriverManager.getConnection("jdbc:mysql://localhost:3306/diablo", props);

        PreparedStatement statement = connection
                .prepareStatement("SELECT \n" +
                        "   u.user_name, u.first_name, u.last_name, COUNT(g.id) AS count\n" +
                        "FROM\n" +
                        "    users AS u\n" +
                        "        LEFT JOIN\n" +
                        "    users_games AS ug ON u.id = ug.user_id\n" +
                        "        LEFT JOIN\n" +
                        "    games AS g ON g.id = ug.game_id\n" +
                        "WHERE\n" +
                        "    u.user_name LIKE (?);");
        Scanner scanner = new Scanner(System.in);

        statement.setString(1, (scanner.nextLine()));
        ResultSet results = statement.executeQuery();

        while (results.next()) {

            if(results.getString("first_name") == null) {

                System.out.println("No such user exists");
            }
            else
                System.out.println(String.format("User: %s \r\n%s %s has played %d games",

                        results.getString("user_name"),
                        results.getString("first_name"),
                        results.getString("last_name"),
                        results.getInt("count")

                ));

        }
    }
}

