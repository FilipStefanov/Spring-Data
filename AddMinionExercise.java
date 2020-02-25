import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
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
        // 4. Add Minion
//        addMinionExercise();

        // 5. Change Town Names Casing
//        changeTownNameCasing();
        // 6. Remove Villain*
//        System.out.println("Enter villainID:");
//        removeVillainByID(Integer.parseInt(reader.readLine()));


    }

    private static void removeVillainByID(int villainID) throws SQLException {

        String tableName = "villains";
        String villainsName = getEntityNameByID(villainID, tableName);

        query = "select count(minion_id)\n" +
                "from minions_villains\n" +
                "where villain_id = ?\n" +
                "group by villain_id;";

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, villainID);
        ResultSet resultSet = preparedStatement.executeQuery();
        int countOfMinions = 0;

        while (resultSet.next()) {
            countOfMinions = resultSet.getInt(1);
        }
        query = "DELETE from minions_villains where villain_id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, villainID);
        preparedStatement.executeUpdate();

        query = "DELETE from villains where id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, villainID);
        preparedStatement.executeUpdate();

        if (villainsName.length() == 0) {
            System.out.println("No such villain was found");
        } else {
            System.out.printf("%s was deleted\r\n", villainsName);
            System.out.printf("%d minions released", countOfMinions);
        }

    }

    private static String getEntityNameByID(int id, String tableName) throws SQLException {
        String entityName = "";
        query = "SELECT name from " + tableName + " WHERE id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            entityName = resultSet.getString(1);
        }

        return entityName;
    }

    private static void changeTownNameCasing() throws IOException, SQLException {
        String countryName = reader.readLine();
        changeTownNameCasingToUpperCase(countryName);
    }

    private static void changeTownNameCasingToUpperCase(String countryName) throws SQLException {
        query = "UPDATE towns set towns.name = UPPER(towns.name) WHERE country like (?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, countryName);
        int queryExecuted = preparedStatement.executeUpdate();
        if (queryExecuted > 0) {
            printUpdatedTowns(countryName);
        } else System.out.println("No town names were affected.");

    }

    private static void printUpdatedTowns(String countryName) throws SQLException {
        query = "SELECT name FROM towns WHERE country like (?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, countryName);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> updatedTowns = new ArrayList<>();
        while (resultSet.next()) {
            updatedTowns.add(resultSet.getString(1));
        }
        System.out.println(String.format("%d towns were affected", updatedTowns.size()));
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (String updatedTown : updatedTowns) {
            builder
                    .append(updatedTown)
                    .append(", ");
        }
        builder.setLength(builder.length() - 2);
        builder.append("]");
        System.out.println(builder);
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
