package DAConcepts.MM;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    final static String url = "jdbc:mysql://127.0.0.1:8889/Logins";

    public static void main(String[] args) {
        try {
            SetterUpper.createDatabase();
            SetterUpper.createTables();
        } catch (SQLException e) {
            System.out.println("Error initializing database/tables!");
            System.exit(2);// 2 will be that :p
        }
        Scanner s = new Scanner(System.in);
        System.out.print("Welcome traveler you have reached the login database!" +
                "\nwould you like to sign in as an admin (0) or as a user (1): ");
        if (Integer.parseInt(s.nextLine()) == 0) {
            Connection userCon;
            while (true) {
                System.out.print("Username: ");
                String user = s.nextLine();
                System.out.print("Password: ");
                String pass = s.nextLine();
                try {
                    userCon = DriverManager.getConnection(url, user, pass);
                    System.out.println("Success!");
                    break;
                } catch (SQLException e) {
                    System.out.println("Invalid Credentials/Connection authorities are being contacted.");
                    try {//TODO: yes this is a try catch in a try catch i guess you could call it an in/ex-ception handler
                        FileWriter myWriter = new FileWriter("call.911");
                        myWriter.write("weewoo");
                        myWriter.close();
                    } catch (IOException e2) {
                        System.out.println("Lucky duck the authorities could not be contacted.");
                    }
                    System.out.println("Try again");
                }
            }
            while (true) {
                System.out.print("What would you like to do now-" +
                        "\n\t0) see tables" +
                        "\n\t1) insert into table" +
                        "\n\t2) modify table" +
                        "\n\t3) delete from table" +
                        "\n\t4) e s c a p e." +
                        "\n(0-4): ");
                int ind = Integer.parseInt(s.nextLine());
                if (ind == 0) {
                    try {
                        System.out.println("TABLE 1\n"+UserSchemaManager.getTable(0) + "\n\n" +
                                "TABLE 2\n" + UserSchemaManager.getTable(1) + "\n\n" +
                                "TABLE 3\n" + UserSchemaManager.getTable(2));
                        System.out.println("\n");
                    } catch (SQLException e) {
                        System.out.println("Connection lost!");
                        System.exit(1);//1 will mean connection lost
                    }
                }
                else if (ind == 1) {
                    System.out.print("What table would you like to write to (0-2): ");
                    int tableNum = Math.min(Math.max(0, Integer.parseInt(s.nextLine())), 2);
                    System.out.print("Now fill in the row excluding the primary ID and" +
                            "\nusing commas to separate (without spaces)." +
                            "\nFor example \"1,2,true\"" +
                            "\nYour turn: ");
                    try {
                        AdminSchemaManager.writeToTable(userCon, tableNum, s.nextLine().split(","));
                        System.out.println("Success!");
                    } catch (SQLException e) {
                        System.out.println("Connection or Input invalid!");
                        System.exit(-1);//-1 will be for an unspecified error
                    }
                }
                else if (ind == 2) {
                    System.out.print("What table would you like to modify (0-2): ");
                    int tableNum = Math.min(Math.max(0, Integer.parseInt(s.nextLine())), 2);
                    System.out.print("What primary ID would you like to modify: ");
                    int row = Integer.parseInt(s.nextLine());
                    System.out.print("What column would you like to modify(0-"+(tableNum==2?2:1)+"): ");
                    int col = Integer.parseInt(s.nextLine());
                    System.out.print("What would you like to change this to: ");
                    try {
                        AdminSchemaManager.modifyRow(userCon, tableNum, row, col, s.nextLine());
                        System.out.println("Success!");
                    } catch (SQLException e) {
                        System.out.println("Connection or Input invalid!");
                        System.exit(-1);//-1 will be for an unspecified error
                    }
                }
                else if (ind == 3) {
                    System.out.print("What table would you like to delete from (0-2): ");
                    int tableNum = Math.min(Math.max(0, Integer.parseInt(s.nextLine())), 2);
                    System.out.print("What primary ID would you like to delete: ");
                    try {
                        AdminSchemaManager.deleteRow(userCon, tableNum, Integer.parseInt(s.nextLine()));
                        System.out.println("Success!");
                    } catch (SQLException e) {
                        System.out.println("Connection or Input invalid!");
                        System.exit(-1);//-1 will be for an unspecified error
                    }
                }
                else {
                    System.out.print("NO...");
                    int count = 0;
                    long lastTime = System.nanoTime();
                    while (count < 5) {
                        if ((System.nanoTime()-lastTime)/Math.pow(10,9) >= 1) {
                            lastTime = System.nanoTime();
                            count++;
                            System.out.print(".");
                        }
                    }
                    System.out.println("\nFINE-");
                    System.exit(0);//0 is everything went well
                }
            }
        }
        try {
            System.out.println("Are you a returning user (do you have a login)? (y/n)");
            char answer = s.nextLine().charAt(0);
            if (answer == 'n' || answer == 'N') {
                System.out.println("Create a new account!");
                System.out.print("Username: ");
                String user = s.nextLine();
                System.out.print("Password: ");
                String pass = s.nextLine();
                UserSchemaManager.addUser(user,pass);
                System.out.println("Success! Now go ahead and login to your account.");
            }
            String user = null;
            String pass;
            do {
                if (user != null) {
                    System.out.println("Log in invalid! Try Again.");
                }
                System.out.print("Username: ");
                user = s.nextLine();
                System.out.print("Password: ");
                pass = s.nextLine();
            } while (!UserSchemaManager.login(user, pass));
        } catch (SQLException e) {
            System.out.println("Connection lost/invalid!");
            System.exit(1);//1 will mean connection lost
        }
        while (true) {
            System.out.print("What would you like to do now-" +
                    "\n\t0) see login attempts" +
                    "\n\t\t(accounts and linked logins are protected and require administrator permissions)" +
                    "\n\t1) change password" +
                    "\n\t2) delete account" +
                    "\n\t3) log out" +
                    "\n(0-3): ");
            int ind = Integer.parseInt(s.nextLine());
            if (ind == 0) {
                try {
                    System.out.println(UserSchemaManager.getTable(1) + "\n\n");
                } catch (SQLException e) {
                    System.out.println("Connection lost!");
                    System.exit(1);//1 will mean connection lost
                }
            }
            else if (ind == 1) {
                System.out.println("You will have to verify your identity.");
                try {
                    String user = null;
                    String pass;
                    do {
                        if (user != null) {
                            System.out.println("Log in invalid! Try Again.");
                        }
                        System.out.print("Username: ");
                        user = s.nextLine();
                        System.out.print("Password: ");
                        pass = s.nextLine();
                    } while (!UserSchemaManager.login(user, pass));
                    System.out.print("What will your new password be: ");
                    UserSchemaManager.changePassword(user,pass,s.nextLine());
                    System.out.println("Success!");
                } catch (SQLException e) {
                    System.out.println("Connection lost/invalid!");
                    System.exit(1);//1 will mean connection lost
                }
            }
            else if (ind == 2) {
                System.out.println("You will have to verify your identity.");
                try {
                    String user = null;
                    String pass;
                    do {
                        if (user != null) {
                            System.out.println("Log in invalid! Try Again.");
                        }
                        System.out.print("Username: ");
                        user = s.nextLine();
                        System.out.print("Password: ");
                        pass = s.nextLine();
                    } while (!UserSchemaManager.login(user, pass));
                    UserSchemaManager.deleteAccount(user,pass);
                    System.out.println("Success!");
                    System.exit(0);
                } catch (SQLException e) {
                    System.out.println("Connection lost/invalid!");
                    System.exit(1);//1 will mean connection lost
                }
            }
            else {
                System.exit(0);
            }
        }
    }
}
