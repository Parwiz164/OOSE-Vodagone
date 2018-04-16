package Vodagone.DAO;

import Vodagone.DAO.Interfaces.iUser;
import Vodagone.JDBC.DatabaseProperties;
import Vodagone.Domain.User;

import java.sql.*;
import java.util.logging.*;

public class UserDao implements iUser {
    private Logger logger;
    private Connection connection;

    public UserDao() {
        Logger.getLogger(getClass().getName());
        DatabaseProperties.tryLoadJdbcDriver(DatabaseProperties.getInstance());
        try {
            this.connection = DriverManager.getConnection(DatabaseProperties.getInstance().connectionString());
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    public UserDao(Connection connection) {
        this.connection = connection;
    }



    @Override
    public User login(String username, String password) {
        User user = null;
        String sql = "SELECT id, username, naam, email FROM Users WHERE username = ? AND password = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("username"),
                        resultSet.getString("naam"), resultSet.getString("email"));
            }

            statement.close();
        } catch ( SQLException e ) {
            logger.log(Level.SEVERE, "Error communicating with database " +
                    DatabaseProperties.getInstance().connectionString(), e);
        }
        return user;
    }
}
