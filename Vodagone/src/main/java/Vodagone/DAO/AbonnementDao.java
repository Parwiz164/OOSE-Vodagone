package Vodagone.DAO;

import Vodagone.DAO.Interfaces.iAbonnement;
import Vodagone.Domain.Abonnement;
import Vodagone.JDBC.DatabaseProperties;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbonnementDao implements iAbonnement {
    private Logger logger;
    private Connection connection;

    public AbonnementDao() {
        Logger.getLogger(getClass().getName());
        DatabaseProperties.tryLoadJdbcDriver(DatabaseProperties.getInstance());
        try {
            this.connection = DriverManager.getConnection(DatabaseProperties.getInstance().connectionString());
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    public AbonnementDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List <Abonnement> getAbonnementenFromUser(int userId) {
        List <Abonnement> abonnementList = new ArrayList <Abonnement>();
        String sql = "SELECT Abonnement.id, aanbieder, dienst, prijsPerMaand, Abonnee.startDatum, Abonnee.verdubbeling, " +
                "deelbaar, Abonnee.status, Abonnee.gedeeldMet FROM Abonnementen Abonnement INNER JOIN " +
                "Abonnees Abonnee ON Abonnement.id = Abonnee.abonnement_id WHERE user_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double price = resultSet.getDouble("prijsPerMaand");
                Boolean deelbaar = resultSet.getBoolean("deelbaar");

                if (resultSet.getString("verdubbeling").equals("verdubbeld")) {
                    price = price * 1.5;
                }

                if (resultSet.getInt("gedeeldMet") != 0) {
                    deelbaar = false;
                    price = 0.0;
                }

                abonnementList.add(new Abonnement(
                        resultSet.getInt("id"),
                        resultSet.getString("aanbieder"),
                        resultSet.getString("dienst"),
                        price,
                        resultSet.getString("startDatum"),
                        resultSet.getString("verdubbeling"),
                        deelbaar,
                        resultSet.getString("status")
                ));
            }

            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
        return abonnementList;
    }

    @Override
    public List <Abonnement> getAllAvailableAbonnementen(int userId, String filter) {
        List <Abonnement> abonnementList = new ArrayList <Abonnement>();
        String sql = "SELECT * FROM Abonnementen WHERE (aanbieder LIKE ? OR dienst LIKE ?) " +
                "AND id NOT IN (SELECT abonnement_id FROM Abonnees Abonnee WHERE user_id = ?)";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + filter + "%");
            statement.setString(2, "%" + filter + "%");
            statement.setInt(3, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                abonnementList.add(new Abonnement(
                        resultSet.getInt("id"),
                        resultSet.getString("aanbieder"),
                        resultSet.getString("dienst")
                ));
            }

            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
        return abonnementList;
    }

    @Override
    public void addAbonnement(int userId, int abonnementId) {
        String sql = "SELECT * FROM Abonnementen WHERE id = ?";
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, abonnementId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                sql = "INSERT INTO Abonnees(user_id, abonnement_id, status, verdubbeling, gedeeldMet, startDatum) " +
                        "VALUES (?,?,?,?,?,?)";

                statement = connection.prepareStatement(sql);
                statement.setInt(1, userId);
                statement.setInt(2, abonnementId);
                statement.setString(3, "actief");
                statement.setString(4, resultSet.getString("verdubbeling"));
                statement.setInt(5, 0);
                statement.setString(6, dateTimeFormatter.format(localDate));
                statement.executeUpdate();
            }
            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
    }

    @Override
    public Abonnement getSpecificAbonnementFromLoggedUser(int userId) {
        Abonnement abonnement = null;
        String sql = "SELECT Abonnement.id, aanbieder, dienst, prijsPerMaand, Abonnee.startDatum, " +
                "Abonnee.verdubbeling, deelbaar, Abonnee.status, Abonnee.gedeeldMet FROM Abonnementen Abonnement INNER JOIN " +
                "Abonnees Abonnee ON Abonnement.id = Abonnee.abonnement_id WHERE Abonnement.id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double price = resultSet.getDouble("prijsPerMaand");
                Boolean deelbaar = resultSet.getBoolean("deelbaar");

                if (resultSet.getString("verdubbeling").equals("verdubbeld")) {
                    price = price * 1.5;
                }

                if (resultSet.getInt("gedeeldMet") != 0) {
                    deelbaar = false;
                    price = 0.0;
                }

                abonnement = new Abonnement(
                        resultSet.getInt("id"),
                        resultSet.getString("aanbieder"),
                        resultSet.getString("dienst"),
                        price,
                        resultSet.getString("startDatum"),
                        resultSet.getString("verdubbeling"),
                        deelbaar,
                        resultSet.getString("status")
                );
            }

            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
        return abonnement;
    }

    @Override
    public void terminateAbonnement(int abonnementId) {
        String sql = "UPDATE Abonnees SET status = ? WHERE abonnement_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "opgezegd");
            statement.setInt(2, abonnementId);

            statement.execute();
            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
    }

    @Override
    public void upgradeAbonnement(int abonnementId) {
        String sql = "UPDATE Abonnees SET verdubbeling = ? WHERE abonnement_id = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "verdubbeld");
            statement.setInt(2, abonnementId);

            statement.executeUpdate();
            statement.close();
        } catch ( SQLException e ) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error communicating with database "
                    + DatabaseProperties.getInstance().connectionString(), e);
        }
    }
}
