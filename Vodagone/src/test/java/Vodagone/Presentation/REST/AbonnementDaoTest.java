package Vodagone.Presentation.REST;

import Vodagone.DAO.AbonnementDao;
import Vodagone.Domain.Abonnement;
import Vodagone.Domain.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

public class AbonnementDaoTest {

    @Test
    public void getAbonnementenFromUserTest() throws SQLException {
        String sql = "SELECT Abonnement.id, aanbieder, dienst, prijsPerMaand, Abonnee.startDatum, Abonnee.verdubbeling, " +
                "deelbaar, Abonnee.status, Abonnee.gedeeldMet FROM Abonnementen Abonnement INNER JOIN " +
                "Abonnees Abonnee ON Abonnement.id = Abonnee.abonnement_id WHERE user_id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getString("verdubbeling")).thenReturn("verdubbeld");
        when(resultSet.getInt("gedeeldMet")).thenReturn(1);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        abonnementDao.getAbonnementenFromUser(user.getId());

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setInt(1, user.getId());

        verify(resultSet, times(1)).getInt("id");
        verify(resultSet, times(1)).getString("aanbieder");
        verify(resultSet, times(1)).getString("dienst");
        verify(resultSet, times(1)).getDouble("prijsPerMaand");
        verify(resultSet, times(1)).getString("startDatum");
//        verify(resultSet, times(1)).getString("verdubbeling");
        verify(resultSet, times(1)).getBoolean("deelbaar");
        verify(resultSet, times(1)).getString("status");
    }

    @Test
    public void getAbonnementenFromUserTestFails() throws SQLException {
        String sql = "SELECT Abonnement.id, aanbieder, dienst, prijsPerMaand, Abonnee.startDatum, Abonnee.verdubbeling, " +
                "deelbaar, Abonnee.status, Abonnee.gedeeldMet FROM Abonnementen Abonnement INNER JOIN " +
                "Abonnees Abonnee ON Abonnement.id = Abonnee.abonnement_id WHERE user_id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        when(resultSet.getString("verdubbeling")).thenReturn("verdubbeld");
        when(resultSet.getInt("gedeeldMet")).thenReturn(1);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        abonnementDao.getAbonnementenFromUser(user.getId());

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setInt(1, user.getId());

        verify(resultSet, times(0)).getInt("id");
        verify(resultSet, times(0)).getString("aanbieder");
        verify(resultSet, times(0)).getString("dienst");
        verify(resultSet, times(0)).getDouble("prijsPerMaand");
        verify(resultSet, times(0)).getString("startDatum");
//        verify(resultSet, times(1)).getString("verdubbeling");
        verify(resultSet, times(0)).getBoolean("deelbaar");
        verify(resultSet, times(0)).getString("status");
    }

    @Test
    public void getAllAvailableAbonnementenTest() throws SQLException {
        String sql = "SELECT * FROM Abonnementen WHERE (aanbieder LIKE ? OR dienst LIKE ?) " +
                "AND id NOT IN (SELECT abonnement_id FROM Abonnees Abonnee WHERE user_id = ?)";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        String filter = "";
        abonnementDao.getAllAvailableAbonnementen(user.getId(), filter);

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setString(1, "%" + filter + "%");
        verify(statement, times(1)).setString(2, "%" + filter + "%");
        verify(statement, times(1)).setInt(3, user.getId());

        verify(resultSet, times(1)).getInt("id");
        verify(resultSet, times(1)).getString("aanbieder");
        verify(resultSet, times(1)).getString("dienst");
    }

    @Test
    public void getAllAvailableAbonnementenTestFails() throws SQLException {
        String sql = "SELECT * FROM Abonnementen WHERE (aanbieder LIKE ? OR dienst LIKE ?) " +
                "AND id NOT IN (SELECT abonnement_id FROM Abonnees Abonnee WHERE user_id = ?)";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        String filter = "";
        abonnementDao.getAllAvailableAbonnementen(user.getId(), filter);

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setString(1, "%" + filter + "%");
        verify(statement, times(1)).setString(2, "%" + filter + "%");
        verify(statement, times(1)).setInt(3, user.getId());

        verify(resultSet, times(0)).getInt("id");
        verify(resultSet, times(0)).getString("aanbieder");
        verify(resultSet, times(0)).getString("dienst");
    }

    @Test
    public void addAbonnementTest() throws SQLException {
        String sql1 = "SELECT * FROM Abonnementen WHERE id = ?";
        String sql2 = "INSERT INTO Abonnees(user_id, abonnement_id, status, verdubbeling, gedeeldMet, startDatum) VALUES (?,?,?,?,?,?)";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        // SQL 1
        when(connection.prepareStatement(sql1)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        // SQL 2
        when(connection.prepareStatement(sql2)).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        abonnementDao.addAbonnement(user.getId(), 1);

        // 3. Verify that correct calls where made
        // SQL 1
        verify(connection, times(1)).prepareStatement(sql1);
        verify(statement, times(2)).setInt(1, 1);

        // SQL 2
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        verify(connection, times(1)).prepareStatement(sql2);
        verify(statement, times(2)).setInt(1, user.getId());
        verify(statement, times(1)).setInt(2, 1);
        verify(statement, times(1)).setString(3, "actief");
        verify(statement, times(1)).setString(4, resultSet.getString("verdubbeling"));
        verify(statement, times(1)).setInt(5, resultSet.getInt("user_id"));
        verify(statement, times(1)).setString(6, dateTimeFormatter.format(localDate));
    }

    @Test
    public void addAbonnementTestFails() throws SQLException {
        String sql1 = "SELECT * FROM Abonnementen WHERE id = ?";
        String sql2 = "INSERT INTO Abonnees(user_id, abonnement_id, status, verdubbeling, gedeeldMet, startDatum) VALUES (?,?,?,?,?,?)";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        // SQL 1
        when(connection.prepareStatement(sql1)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // SQL 2
        when(connection.prepareStatement(sql2)).thenReturn(statement);
        when(statement.executeUpdate()).thenReturn(0);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        abonnementDao.addAbonnement(user.getId(), 1);

        // 3. Verify that correct calls where made
        // SQL 1
        verify(connection, times(1)).prepareStatement(sql1);
        verify(statement, times(1)).setInt(1, 1);

        // SQL 2
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        verify(connection, times(0)).prepareStatement(sql2);
//        verify(statement, times(0)).setInt(1, user.getId());
        verify(statement, times(0)).setInt(2, 1);
        verify(statement, times(0)).setString(3, "actief");
        verify(statement, times(0)).setString(4, resultSet.getString("verdubbeling"));
        verify(statement, times(0)).setInt(5, resultSet.getInt("user_id"));
        verify(statement, times(0)).setString(6, dateTimeFormatter.format(localDate));
    }

    @Test
    public void getSpecificAbonnementFromLoggedUserTest() throws SQLException {
        String sql = "SELECT Abonnement.id, aanbieder, dienst, prijsPerMaand, Abonnee.startDatum, Abonnee.verdubbeling, " +
                "deelbaar, Abonnee.status, Abonnee.gedeeldMet FROM Abonnementen Abonnement INNER JOIN " +
                "Abonnees Abonnee ON Abonnement.id = Abonnee.abonnement_id WHERE Abonnement.id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        when(resultSet.getString("verdubbeling")).thenReturn("verdubbeld");
        when(resultSet.getInt("gedeeldMet")).thenReturn(1);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        abonnementDao.getSpecificAbonnementFromLoggedUser(user.getId());

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setInt(1, user.getId());

        verify(resultSet, times(1)).getInt("id");
        verify(resultSet, times(1)).getString("aanbieder");
        verify(resultSet, times(1)).getString("dienst");
        verify(resultSet, times(1)).getDouble("prijsPerMaand");
        verify(resultSet, times(1)).getString("startDatum");
//        verify(resultSet, times(1)).getString("verdubbeling");
        verify(resultSet, times(1)).getBoolean("deelbaar");
        verify(resultSet, times(1)).getString("status");
    }

    @Test
    public void getSpecificAbonnementFromLoggedUserTestFails() throws SQLException {
        String sql = "SELECT Abonnement.id, aanbieder, dienst, prijsPerMaand, Abonnee.startDatum, Abonnee.verdubbeling, " +
                "deelbaar, Abonnee.status, Abonnee.gedeeldMet FROM Abonnementen Abonnement INNER JOIN " +
                "Abonnees Abonnee ON Abonnement.id = Abonnee.abonnement_id WHERE Abonnement.id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        when(resultSet.getString("verdubbeling")).thenReturn("verdubbeld");
        when(resultSet.getInt("gedeeldMet")).thenReturn(1);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        User user = new User(1, "Parwiz");
        abonnementDao.getSpecificAbonnementFromLoggedUser(user.getId());

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setInt(1, user.getId());

        verify(resultSet, times(0)).getInt("id");
        verify(resultSet, times(0)).getString("aanbieder");
        verify(resultSet, times(0)).getString("dienst");
        verify(resultSet, times(0)).getDouble("prijsPerMaand");
        verify(resultSet, times(0)).getString("startDatum");
//        verify(resultSet, times(1)).getString("verdubbeling");
        verify(resultSet, times(0)).getBoolean("deelbaar");
        verify(resultSet, times(0)).getString("status");
    }

    @Test
    public void terminateAbonnementTest() throws SQLException {
        String sql = "UPDATE Abonnees SET status = ? WHERE abonnement_id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        abonnementDao.terminateAbonnement(1);

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setString(1, "opgezegd");
        verify(statement, times(1)).setInt(2, 1);
    }

    @Test
    public void upgradeAbonnementTest() throws SQLException {
        String sql = "UPDATE Abonnees SET verdubbeling = ? WHERE abonnement_id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);

        // 2. Run test
        AbonnementDao abonnementDao = new AbonnementDao(connection);
        abonnementDao.upgradeAbonnement(1);

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setString(1, "verdubbeld");
        verify(statement, times(1)).setInt(2, 1);
    }
}


