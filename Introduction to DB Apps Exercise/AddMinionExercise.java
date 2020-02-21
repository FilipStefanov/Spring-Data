import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.Properties;

public class AddMinionExercise {
    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
    private static final String DATABASE_NAME = "minions_db";
    private static Connection connection;
    private static String query;
    private static PreparedStatement preparedStatement;
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws SQLException, IOException {
        Properties properties = new Properties();
        properties.setProperty("user", "root");
        properties.setProperty("password", "1234");
        connection = DriverManager.getConnection(
                CONNECTION_URL + DATABASE_NAME, properties
        );
        addMinionExercise();
    }

    private static void addMinionExercise() throws IOException, SQLException {
        System.out.println("Enter MINION's details:");
        String[] minionParameters = reader.readLine().split("\\s+");
        String minionName = minionParameters[0];
        int minionAge = Integer.parseInt(minionParameters[1]);
        String minionTown = minionParameters[2];
        if (!checkIfEntityExistByName(minionTown, "towns")) {
            insertEntityInTowns(minionTown);
            System.out.printf("Town %s was added to the database.\r\n", minionTown);
        }
        if (!checkIfEntityExistByName(minionName, "minions")) {
            insertEntityInMinions(minionName, minionAge, minionTown);
            System.out.printf("Minion %s was added to the database.\r\n", minionName);
        }
        System.out.println("Enter VILLAIN's name:");
        String villainName = reader.readLine();
        if (!checkIfEntityExistByName(villainName, "villains")) {
            insertEntityInVillains(villainName);
            System.out.printf("Villain %s was added to the database.\r\n", villainName);
        }
        int minionID = getEntityIdByNameAndTable(minionName, "minions");
        int villainID = getEntityIdByNameAndTable(villainName, "villains");
        addMinionToVillain(minionID, villainID);
        System.out.printf("Successfully added %s to be minion of %s.\r\n", minionName, villainName);
    }

    private static void insertEntityInMinions(String minionName, int minionAge, String minionTown) throws SQLException {
        query = "insert into minions (name, age, town_id)\n" +
                "values (?, ?, (select id\n" +
                "               from towns\n" +
                "               where name like (?)))";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, minionName);
        preparedStatement.setInt(2, minionAge);
        preparedStatement.setString(3, minionTown);
        preparedStatement.executeUpdate();
    }

    private static void addMinionToVillain(int minionID, int villainID) throws SQLException {
        query = "INSERT INTO minions_villains (minion_id, villain_id) VALUES (?,?);";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(minionID));
        preparedStatement.setString(2, String.valueOf(villainID));
        preparedStatement.executeUpdate();
    }
    private static int getEntityIdByNameAndTable(String entityName, String tableName) throws SQLException {
        query = "SELECT id, name from " + tableName + " where name = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, entityName);
        ResultSet resultSet = preparedStatement.executeQuery();
        int id = 0;
        while (resultSet.next()) {
            id = resultSet.getInt("id");
        }
        return id;
    }


    private static void addVillainExercise(String villainName) throws IOException, SQLException {

    }

    private static void insertEntityInVillains(String villainName) throws SQLException {
        query = "INSERT INTO villains (name, evilness_factor) value (? , ?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, villainName);
        preparedStatement.setString(2, "evil");
        preparedStatement.executeUpdate();
    }

    private static void insertEntityInTowns(String minionTown) throws SQLException {
        query = "INSERT INTO towns (name, country) value(?, ?) ";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, minionTown);
        preparedStatement.setString(2, "NULL");
        preparedStatement.executeUpdate();

    }

    private static boolean checkIfEntityExistByName(String paramName, String tableName) throws SQLException {
        query = String.format("SELECT * FROM %s WHERE name = ?", tableName);
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, paramName);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }
}
