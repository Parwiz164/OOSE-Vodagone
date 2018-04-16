package Vodagone.JDBC;

import javax.ejb.Singleton;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class DatabaseProperties {
    private static DatabaseProperties databaseProperties;
    private Properties properties;

    public static DatabaseProperties getInstance() {
        if (databaseProperties == null) {
            databaseProperties = new DatabaseProperties();
        }
        return databaseProperties;
    }

    private DatabaseProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch ( IOException e ) {

            Logger logger = Logger.getLogger(getClass().getName());
            logger.log(Level.SEVERE, "Can't access property file database.properties");
        }
    }

    public static void tryLoadJdbcDriver(DatabaseProperties databaseProperties) {
        try {
            Class.forName(DatabaseProperties.getInstance().driver());
        } catch ( ClassNotFoundException e ) {
            System.out.println("Can't load JDBC Driver " + databaseProperties.driver() + e);
        }
    }

    private String driver() {
        return properties.getProperty("driver");
    }

    public String connectionString() {
        return properties.getProperty("connectionString");
    }
}
