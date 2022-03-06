package DAConcepts.MM;

import java.sql.*;

public class UserSchemaManager {
    final static String url = "jdbc:mysql://127.0.0.1:8889/";
    final static String schemaUsername = "GOD";
    final static String schemaPassword = "saint123";

    public static void addUser(String username, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(url+"Logins",schemaUsername,schemaPassword);
        Statement stmt = connection.createStatement();
        String sql = "INSERT INTO Accounts (username, password) " +
                "VALUES ('"+username.substring(0,Math.min(username.length(),45))+"', '"+password.substring(0,Math.min(password.length(),45))+"');";
        stmt.execute(sql);
    }

    public static boolean login(String attemptedUser, String attemptedPassword) throws SQLException {
        Connection connection = DriverManager.getConnection(url+"Logins",schemaUsername,schemaPassword);
        Statement stmt = connection.createStatement();
        String sql = "INSERT INTO LogIn (attemptedUser, attemptedPassword) " +
                "VALUES ('"+attemptedUser.substring(0,Math.min(attemptedUser.length(),45))
                +"', '"+attemptedPassword.substring(0,Math.min(attemptedPassword.length(),45))+"');";
        stmt.execute(sql);
        sql = "SELECT * FROM Accounts " +
                "WHERE username='"+attemptedUser+"';";
        ResultSet rs = stmt.executeQuery(sql);
        String pass = null;
        int id = -1;
        while (rs.next()) {
            id = rs.getInt("userID");
            pass = rs.getString("password");
        }
        sql = "SELECT * FROM LogIn " +
                "WHERE attemptedUser='"+attemptedUser+"';";
        rs = stmt.executeQuery(sql);
        int aId = -1;
        while (rs.next()) {
            aId = rs.getInt("attemptID");
        }
        if (pass == null) {
            return false;
        }
        boolean success = attemptedPassword.equals(pass);
        sql = "INSERT INTO LogInPerAccount (userID,attemptID,success) " +
                "VALUES ("+id+", "+aId+", "+((success)?1:0)+");";
        stmt.execute(sql);
        return success;
    }

    public static void changePassword(String username, String password, String newPass) throws SQLException {
        if (login(username,password)) {
            Connection connection = DriverManager.getConnection(url+"Logins",schemaUsername,schemaPassword);
            Statement stmt = connection.createStatement();
            String sql = "UPDATE Accounts " +
                    "SET password='"+newPass+"' "+
                    "WHERE username='"+username+"';";
            stmt.execute(sql);
        }
    }

    public static void deleteAccount(String username, String password) throws SQLException {
        if (login(username,password)) {
            Connection connection = DriverManager.getConnection(url+"Logins",schemaUsername,schemaPassword);
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM Accounts " +
                    "WHERE username='"+username+"';";
            ResultSet rs = stmt.executeQuery(sql);
            int userID = -1;
            while (rs.next()) {
                userID = rs.getInt("userID");
            }
            if (userID == -1) {
                System.out.println("ERROR: USER NOT FOUND");
                return;
            }
            sql = "DELETE FROM LogInPerAccount WHERE userID="+userID+";";
            stmt.execute(sql);
            sql = "DELETE FROM Accounts WHERE username='"+username+"';";
            stmt.execute(sql);
        }
    }

    public static String getTable(int ind) throws SQLException {//0 = Accounts, 1 = Login, 2 = LogInPerAccount
        Connection connection = DriverManager.getConnection(url+"Logins",schemaUsername,schemaPassword);
        Statement stmt = connection.createStatement();
        String[] tables = {"Accounts","LogIn","LogInPerAccount"};
        String sql = "SELECT * FROM "+tables[ind]+";";
        ResultSet rs = stmt.executeQuery(sql);
        String table;
        switch (ind) {
            case 0:
                table = "|userID|username|password|";
                break;
            case 1:
                table = "|attemptID|attemptedUsername|attemptedPassword";
                break;
            default:
                table = "|linkedAttemptID|userId|attemptId|success|";
        }
        while (rs.next()) {
            table += "\n|"+rs.getInt(1) + "|";
            if (ind < 2) {
                table += rs.getString(2) + "|" + rs.getString(3)+"|";
            }
            else {
                table += rs.getInt(2) + "|" + rs.getInt(3) + "|" + rs.getBoolean(4)+"|";
            }
        }
        return table;
    }
}
