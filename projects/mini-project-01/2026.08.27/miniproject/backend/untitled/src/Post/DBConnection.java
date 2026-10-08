package Post;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String USER = "miniproject";
    private static final String PASSWORD = "1234";

    public static Connection getConnection()
            throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

