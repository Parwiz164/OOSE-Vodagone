package Vodagone.DAO;

import Vodagone.DAO.Interfaces.iAbonnee;
import Vodagone.Domain.Abonnee;
import Vodagone.Domain.Abonnement;
import Vodagone.JDBC.DatabaseProperties;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbonneeDao implements iAbonnee {
    private Logger logger;
    private Connection connection;

    public AbonneeDao() {
        Logger.getLogger(getClass().getName());
        DatabaseProperties.tryLoadJdbcDriver(DatabaseProperties.getInstance());
        try {
            this.connection = DriverManager.getConnection(DatabaseProperties.getInstance().connectionString());
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    public AbonneeDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List <Abonnee> getAllAbonnees(int userId) {
        List <Abonnee> abonneeList = new ArrayList <Abonnee>();
        String sql = "SELECT  id, naam, email FROM Users WHERE NOT id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                abonneeList.add(new Abonnee(
                        resultSet.getInt("id"),
                        resultSet.getString("naam"),
                        resultSet.getString("email")
                ));
            }
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
        return abonneeList;
    }

    @Override
    public void shareAbonnementWithAbonnee(int userId, int abonnementId) {
        String sql = "SELECT * FROM Abonnees WHERE NOT user_id = ? AND abonnement_id = ?";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, abonnementId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sql = "INSERT INTO Abonnees(user_id, abonnement_id, status, verdubbeling, gedeeldMet, startDatum) " +
                        "VALUES (?,?,?,?,?,?)";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, userId);
                statement.setInt(2, abonnementId);
                statement.setString(3, resultSet.getString("status"));
                statement.setString(4, resultSet.getString("verdubbeling"));
                statement.setInt(5, resultSet.getInt("user_id"));
                statement.setString(6, dateTimeFormatter.format(localDate));
                statement.executeUpdate();
            }
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
    }
}
