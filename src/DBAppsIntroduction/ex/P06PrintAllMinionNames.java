package DBAppsIntroduction.ex;

import java.sql.*;
import java.util.*;

public class P06PrintAllMinionNames {

    public static void main(String[] args) throws SQLException {

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement allMinionsStatement = connection
                .prepareStatement("SELECT name FROM minions");

        ResultSet minionsNames = allMinionsStatement.executeQuery();

        ArrayDeque<String> names = new ArrayDeque<>();

        while (minionsNames.next()) {
            names.offer(minionsNames.getString("name"));
        }

        while (names.size() > 2){
            System.out.println(names.pollFirst());
            System.out.println(names.pollLast());
        }

        while(!names.isEmpty()) {
            System.out.println(names.poll());
        }

        connection.close();
    }
}
