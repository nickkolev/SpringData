package DBAppsIntroduction.ex;

import java.sql.*;
import java.util.Properties;

public class P01GetVillainsNames {
    public static void main(String[] args) throws SQLException {

        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement statement = connection.prepareStatement(
                "SELECT name, COUNT(distinct mv.minion_id) as minion_count from villains as v" +
                        " JOIN minions_villains as mv on mv.villain_id = v.id" +
                        " GROUP BY mv.villain_id" +
                        " HAVING minion_count > ?" +
                        " ORDER BY minion_count desc;");

        statement.setInt(1, 15);

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            String villainName = resultSet.getString("name");
            int minionsCount = resultSet.getInt("minion_count");

            System.out.println(villainName + " " + minionsCount);
        }

        connection.close();
    }
}
