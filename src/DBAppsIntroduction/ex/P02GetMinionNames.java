package DBAppsIntroduction.ex;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class P02GetMinionNames {
    public static void main(String[] args) throws SQLException {

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        Scanner scanner = new Scanner(System.in);
        int villainID = Integer.parseInt(scanner.nextLine());

        PreparedStatement villainStatement = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        villainStatement.setInt(1, villainID);

        ResultSet villainResultSet = villainStatement.executeQuery();

        if(!villainResultSet.next()) {
            System.out.printf("No villain with ID %d exists in the database.%n", villainID);
            return;
        }

        String villainName = villainResultSet.getString("name");
        System.out.println("Villain: " + villainName);

        PreparedStatement minionStatement = connection.prepareStatement(
                "SELECT name, age" +
                        " FROM minions AS m" +
                        " JOIN minions_villains as mv ON mv.minion_id = m.id" +
                        " WHERE mv.villain_id = ?;");

        minionStatement.setInt(1, villainID);

        ResultSet minionsResultSet = minionStatement.executeQuery();

        int counter = 1;
        while (minionsResultSet.next()) {
            String minionName = minionsResultSet.getString("name");
            int minionAge = minionsResultSet.getInt("age");

            System.out.printf("%d. %s %d%n", counter, minionName, minionAge);

            counter++;
        }
    }
}
