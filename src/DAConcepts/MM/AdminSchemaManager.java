package DAConcepts.MM;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class AdminSchemaManager {
    public static boolean writeToTable(Connection con, int table, String[] input) throws SQLException {
        if (table < 2) {
            return writeToTable1or2(con,table,input[0],input[1]);
        }
        return writeToTable3(con,Integer.parseInt(input[0]),Integer.parseInt(input[1]),input[2].equalsIgnoreCase("true"));
    }
    public static boolean writeToTable1or2(Connection con, int table, String user, String pass) throws SQLException {
        Statement stmt = con.createStatement();
        String tableHead = (new String[]{"Accounts (username, password)","LogIn (attemptedUser, attemptedPassword)"})[table];
        String sql = "INSERT INTO "+tableHead+" VALUES ('"+user+"', '"+pass+"');";
        return stmt.execute(sql);
    }
    public static boolean writeToTable3(Connection con, int id, int aId, boolean success) throws SQLException {
        Statement stmt = con.createStatement();
        String sql = "INSERT INTO LogInPerAccount (userID,attemptID,success) " +
                "VALUES ("+id+", "+aId+", "+((success)?1:0)+");";
        return stmt.execute(sql);
    }
    public static boolean modifyRow(Connection con, int table, int row, int col, String newVal) throws SQLException {
        Statement stmt = con.createStatement();
        String tableHead = (new String[]{"Accounts","LogIn","LogInPerAccount"})[table];
        String[][] columnNames = new String[][]{
                new String[]{"userID","username","password"},
                new String[]{"attemptID","attemptedUser","attemptedPassword"},
                new String[]{"linkedAttemptID","userID","attemptID","success"}
        };
        if (row != 2) {
            newVal = "'"+newVal+"'";
        }
        else if (col == 2) {
            newVal = (newVal.equalsIgnoreCase("true") ? "1":"0");
        }
        String sql = "UPDATE "+tableHead+" " +
                "SET "+columnNames[table][col+1]+"="+newVal+" "+
                "WHERE "+columnNames[table][0]+"="+row+";";
        return stmt.execute(sql);
    }
    public static boolean deleteRow(Connection con, int table, int row) throws SQLException {
        Statement stmt = con.createStatement();
        String tableHead = (new String[]{"Accounts","LogIn","LogInPerAccount"})[table];
        String[] idNames = new String[]{"userID","attemptID","linkedAttemptID"};
        String sql = "DELETE FROM "+tableHead+" WHERE "+idNames[row]+"="+row+";";
        return stmt.execute(sql);
    }
}
