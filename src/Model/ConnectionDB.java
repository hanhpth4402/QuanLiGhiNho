package Model;
import java.sql.*;
public class ConnectionDB {
    private static Connection conn;
    public ConnectionDB () {
        String conURL = "jdbc:sqlserver://DESKTOP-BM4SH04:1433;" +
                "user=sa;password=123;databaseName=Calendar;encrypt=false";
        try {
            conn = DriverManager.getConnection(conURL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static ResultSet ThucThiLechSelect (String CauLech) {
        try {
            if(conn != null) {
                Statement stm = conn.createStatement();
                return stm.executeQuery(CauLech);
            } else {
                return null;
            }

        } catch (SQLException e) {
            System.out.println("-Loi thu hien cau truy van SQL-");
            System.out.println("Cau lenh " + CauLech);
            System.out.println(e.getMessage());
        }

        return null;
    }

    public static int ThucThiCauLenhUpdate (String CauLech) {
        try {
            if(conn != null) {
                Statement stm = conn.createStatement();
                return stm.executeUpdate(CauLech);
            } else {
                return 0;
            }

        } catch (SQLException e) {
            System.out.println("-Loi thu hien cau truy van SQL-");
            System.out.println("Cau lenh " + CauLech);
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public void close () {
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void isClose () {
        try {
            if (conn.isClosed()) {
                System.out.print("Dong thanh cong");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
