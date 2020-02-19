import java.sql.*;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

public class GetMinionNames {

    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "minions_db";

    public static void main(String[] args) throws SQLException {

        Properties properties = new Properties();

        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection(
                CONNECTION_URL + DATABASE_NAME, properties
        );
        Scanner scanner = new Scanner(System.in);
        int villain_id = Integer.parseInt(scanner.nextLine());

        PreparedStatement preparedStatementName = connection.prepareStatement("select name from villains where id = ?;");
        preparedStatementName.setInt(1, villain_id);
        PreparedStatement preparedStatement = connection.prepareStatement(
                "select m.name, m.age\n" +
                        "from villains as v\n" +
                        "         left join minions_villains as mv\n" +
                        "                   on v.id = mv.villain_id\n" +
                        "         left join minions as m on mv.minion_id = m.id\n" +
                        "where v.id = ?;"
        );
        preparedStatement.setInt(1,villain_id);
        ResultSet resultSet = preparedStatement.executeQuery();
        ResultSet resultSetName = preparedStatementName.executeQuery();
        if (resultSet.next() == Boolean.parseBoolean(null)){
            System.out.println(String.format("No villain with ID %d exists in the database.", villain_id));
        }
        while (resultSetName.next()) {
            System.out.println(String.format("Villain: %s", resultSetName.getString("name")));
        }
        int counter  = 1;
        while (resultSet.next()){
            System.out.println(String.format("%d. %s %d",
                    counter++,
                    resultSet.getString("m.name"),
                    resultSet.getInt("m.age")

            ));
        }

    }
}
