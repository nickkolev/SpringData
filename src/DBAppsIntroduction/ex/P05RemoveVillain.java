package DBAppsIntroduction.ex;

import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class P05RemoveVillain {

    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "");

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        Scanner scanner = new Scanner(System.in);
        int villainID = Integer.parseInt(scanner.nextLine());

        PreparedStatement villainStatement = connection
                .prepareStatement("SELECT name FROM villains WHERE id = ?;");

        villainStatement.setInt(1, villainID);
        ResultSet villainSet = villainStatement.executeQuery();

        if(!villainSet.next()) {
            System.out.println("No such villain was found");
            return;
        }

        String villainName = villainSet.getString("name");

        PreparedStatement selectAllVillainMinions = connection
                .prepareStatement("SELECT COUNT(DISTINCT minion_id) as m_count" +
                        " FROM minions_villains WHERE villain_id = ?");

        selectAllVillainMinions.setInt(1, villainID);

        ResultSet minionsCountSet = selectAllVillainMinions.executeQuery();
        minionsCountSet.next();
        int countMinionsDeleted = minionsCountSet.getInt("m_count");

        connection.setAutoCommit(false);

        try {
            PreparedStatement deleteMinionsVillains = connection
                    .prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?");
            deleteMinionsVillains.setInt(1, villainID);
            deleteMinionsVillains.executeUpdate();

            PreparedStatement deleteVillain = connection
                    .prepareStatement("DELETE FROM villains WHERE id = ?");
            deleteVillain.setInt(1, villainID);
            deleteVillain.executeUpdate();

            connection.commit();

            System.out.println(villainName + " was deleted");
            System.out.println(countMinionsDeleted + " minions released");
        } catch (SQLException e) {
            e.printStackTrace();

            connection.rollback();
        }

        connection.close();
    }
}
