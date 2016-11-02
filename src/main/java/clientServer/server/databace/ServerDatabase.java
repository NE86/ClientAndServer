package clientServer.server.databace;

import java.sql.*;
import java.util.Locale;

public class ServerDatabase {
    private static Connection connection;

    public ServerDatabase() {
        System.out.println("Подключение к базе данных ServerDatabase");
        Locale.setDefault(Locale.ENGLISH);
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("База данных не найдена ");
            throw new RuntimeException(e);
        }
        String username = "ServerDB";
        String password = "123";
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        try {
            connection = DriverManager.getConnection(url, username, password);
            if (connection != null) System.out.println("Подключение прошло успешно");
            else System.exit(0);
            Statement st = connection.createStatement();
            try {
                st.executeQuery("Drop table clients");
            } catch (SQLSyntaxErrorException e) {
                //Если во время удаления таблицы возникает ошибка, это означает что таблица уже удалена
            }
            st.executeQuery("create table clients (\n" +
                    "  client_name varchar(40),\n" +
                    "  client_status varchar(1)\n" +
                    ")");
        } catch (SQLException e) {
            System.out.println("При подключении базы данных произошла ошибка");
            throw new RuntimeException(e);
        }
    }

    public static void addClientDB(String name) throws SQLException {
        Statement st = connection.createStatement();
        st.executeQuery("insert into clients values ('" + name + "', 'y')");
    }

    public static int checkClientOnlineDB(String name) throws SQLException {
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery("select client_status from clients where client_name='" + name + "'");
        if (rs.next()) {
            String s = rs.getString(1);
            if (s.equals("y")) {
                if (rs != null) rs.close();
                return 1;
            } else {
                if (rs != null) rs.close();
                return 0;
            }
        }
        if (rs != null) rs.close();
        return -1;
    }

    public static void updateClientOnlineDB(String name) throws SQLException {
        Statement st = connection.createStatement();
        st.executeQuery("update clients set client_status='y' where client_name='" + name + "'");
    }

    public static void updateClientOfflineDB(String name) throws SQLException {
        Statement st = connection.createStatement();
        st.executeQuery("update clients set client_status='n' where client_name='" + name + "'");
    }

    public static ResultSet onlineListClientDB() throws SQLException {
        Statement st = connection.createStatement();
        return st.executeQuery("select client_name from clients where client_status='y'");
    }

    public static void closeServerDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Удаление ServerDatabase не удалось");
        }
    }
}
