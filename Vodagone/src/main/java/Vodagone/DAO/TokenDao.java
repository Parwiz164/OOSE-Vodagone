package Vodagone.DAO;

import Vodagone.DAO.Interfaces.iToken;
import Vodagone.JDBC.DatabaseProperties;
import Vodagone.Domain.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;

public class TokenDao implements iToken {
    private ArrayList <Integer> numbers = new ArrayList <Integer>();
    private Logger logger;
    private Connection connection;

    public TokenDao() {
        Logger.getLogger(getClass().getName());
        DatabaseProperties.tryLoadJdbcDriver(DatabaseProperties.getInstance());
        try {
            this.connection = DriverManager.getConnection(DatabaseProperties.getInstance().connectionString());
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    public TokenDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public String insertUserToken(User user, String token) {
        String sql = "UPDATE Users SET token = ? WHERE id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, token);
            statement.setInt(2, user.getId());

            statement.executeUpdate();
            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Erro communicating with database " +
                    DatabaseProperties.getInstance().connectionString(), e);
        }

        return token;
    }

    @Override
    public User checkUserToken(String token) {
        User user = null;
        String sql = "SELECT id, naam FROM Users WHERE token = ?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, token);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new User(resultSet.getInt("id"), resultSet.getString("naam"));
            }
            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database " +
                    DatabaseProperties.getInstance().connectionString(), e);
        }
        return user;
    }

    @Override
    public String generateToken() {
        String tokenToString = "";
        String token;
        for (int i = 0; i <= 2; i++) {
            numbers.add((int) Math.floor(Math.random() * (9999 - 1000) + 1000));
        }

        for (Integer number : numbers) {
            tokenToString += "-" + Integer.toString(number);
        }
        token = tokenToString.substring(1);
        return token;
    }
}
