package utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static String url;
    private static String user;
    private static String password;
    private static String driver;

    static {
        try {
            initializeParams();
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    private static void initializeParams() throws IOException {
        Properties props = new Properties();
        InputStream inStr = DBUtil.class.getResourceAsStream("/db.config");
        if (inStr == null) {
            throw new IOException("❌ db.config not found in resources.");
        }
        props.load(inStr);
        driver = props.getProperty("DRIVER");
        url = props.getProperty("URL");
        user = props.getProperty("USER");
        password = props.getProperty("PASSWORD");
    }
}
