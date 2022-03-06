package DAConcepts.MM;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class SetterUpper {
    final static String url = "jdbc:mysql://127.0.0.1:8889/";
    final static String username = "GOD";
    final static String password = "saint123";

    public static void createDatabase() throws SQLException {
        Connection connection = DriverManager.getConnection(url,username,password);
        Statement stmt = connection.createStatement();
        String sql = "CREATE SCHEMA IF NOT EXISTS Logins";
        stmt.execute(sql);
    }

    public static void createTables() throws SQLException {
        Connection connection = DriverManager.getConnection(url+"Logins",username,password);
        Statement stmt = connection.createStatement();
        String sql = "CREATE TABLE IF NOT EXISTS Accounts (\n" +
                "  userID INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  username VARCHAR(45) NOT NULL,\n" +
                "  password VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (userID),\n" +
                "  UNIQUE INDEX userID_UNIQUE (userID ASC),\n" +
                "  UNIQUE INDEX username_UNIQUE (username ASC)\n" +
                ") ENGINE = InnoDB;\n";
        stmt.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS LogIn (\n" +
                "  attemptID INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  attemptedUser VARCHAR(45) NOT NULL,\n" +
                "  attemptedPassword VARCHAR(45) NOT NULL,\n" +
                "  PRIMARY KEY (attemptID),\n" +
                "  UNIQUE INDEX attemptID_UNIQUE (attemptID ASC)\n" +
                ")ENGINE = InnoDB";
        stmt.execute(sql);
        sql = "CREATE TABLE IF NOT EXISTS LogInPerAccount (\n" +
                "  linkedAttemptID INT UNSIGNED NOT NULL AUTO_INCREMENT,\n" +
                "  userId INT UNSIGNED NOT NULL,\n" +
                "  attemptId INT UNSIGNED NOT NULL,\n" +
                "  success TINYINT NOT NULL,\n" +
                "  PRIMARY KEY (linkedAttemptID),\n" +
                "  UNIQUE INDEX linkedAttemptID_UNIQUE (linkedAttemptID ASC),\n" +
                "  INDEX FK_Account_LogInPerAccount_idx (userId ASC),\n" +
                "  INDEX FK_LoginPerAccount_Login_idx (attemptId ASC),\n" +
                "  CONSTRAINT FK_LogInPerAccount_Account\n" +
                "    FOREIGN KEY (userId)\n" +
                "    REFERENCES Accounts (userID)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION,\n" +
                "  CONSTRAINT FK_LoginPerAccount_Login\n" +
                "    FOREIGN KEY (attemptId)\n" +
                "    REFERENCES LogIn (attemptID)\n" +
                "    ON DELETE NO ACTION\n" +
                "    ON UPDATE NO ACTION\n" +
                ")ENGINE = InnoDB";
        stmt.execute(sql);
    }
}
