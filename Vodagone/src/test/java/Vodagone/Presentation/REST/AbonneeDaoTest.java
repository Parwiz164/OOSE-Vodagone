package Vodagone.Presentation.REST;

import Vodagone.DAO.AbonneeDao;
import Vodagone.Domain.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.*;

public class AbonneeDaoTest {

    @Test
    public void getAllAbonneesTest() throws SQLException {
        String sql = "SELECT  id, naam, email FROM Users WHERE NOT id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        // 2. Run test
        AbonneeDao abonneeDao = new AbonneeDao(connection);
        User user = new User(1, "Parwiz");
        abonneeDao.getAllAbonnees(user.getId());

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setInt(1, user.getId());

        verify(resultSet, times(1)).getInt("id");
        verify(resultSet, times(1)).getString("naam");
        verify(resultSet, times(1)).getString("email");

    }

    @Test
    public void getAllAbonneesTestFails() throws SQLException {
        String sql = "SELECT  id, naam, email FROM Users WHERE NOT id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // 2. Run test
        AbonneeDao abonneeDao = new AbonneeDao(connection);
        User user = new User(1, "Parwiz");
        abonneeDao.getAllAbonnees(user.getId());

        // 3. Verify that correct calls where made
        verify(connection, times(1)).prepareStatement(sql);
        verify(statement, times(1)).setInt(1, user.getId());

        verify(resultSet, times(0)).getInt("id");
        verify(resultSet, times(0)).getString("naam");
        verify(resultSet, times(0)).getString("email");

    }

    @Test
    public void shareAbonnementWithAbonneeTest() throws SQLException {
        String sql1 = "SELECT * FROM Abonnees WHERE NOT user_id = ? AND abonnement_id = ?";
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
        AbonneeDao abonneeDao = new AbonneeDao(connection);
        User user = new User(1, "Parwiz");
        abonneeDao.shareAbonnementWithAbonnee(user.getId(), 1);

        // 3. Verify that correct calls where made
        // SQL 1
        verify(connection, times(1)).prepareStatement(sql1);
        verify(statement, times(2)).setInt(1, user.getId());
        verify(statement, times(2)).setInt(2, 1);

        // SQL 2
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();

        verify(connection,times(1)).prepareStatement(sql2);
        verify(statement, times(2)).setInt(1, user.getId());
        verify(statement, times(2)).setInt(2, 1);
        verify(statement, times(1)).setString(3, resultSet.getString("status"));
        verify(statement, times(1)).setString(4, resultSet.getString("verdubbeling"));
        verify(statement, times(1)).setInt(5, resultSet.getInt("user_id"));
        verify(statement, times(1)).setString(6, dateTimeFormatter.format(localDate));
    }

    @Test
    public void shareAbonnementWithAbonneeTestFails() throws SQLException {
        String sql1 = "SELECT * FROM Abonnees WHERE NOT user_id = ? AND abonnement_id = ?";
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
        AbonneeDao abonneeDao = new AbonneeDao(connection);
        User user = new User(1, "Parwiz");
        abonneeDao.shareAbonnementWithAbonnee(user.getId(), 1);

        // 3. Verify that correct calls where made
        // SQL 1
        verify(connection, times(1)).prepareStatement(sql1);
        verify(statement, times(1)).setInt(1, user.getId());
        verify(statement, times(1)).setInt(2, 1);

        // SQL 2
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();

        verify(connection,times(0)).prepareStatement(sql2);
//        verify(statement, times(0)).setInt(1, user.getId());
//        verify(statement, times(0)).setInt(2, 1);
        verify(statement, times(0)).setString(3, "atief");
        verify(statement, times(0)).setString(4, "verdubbeling");
        verify(statement, times(0)).setInt(5, 2);
        verify(statement, times(0)).setString(6, dateTimeFormatter.format(localDate));

        verify(resultSet, times(0)).getInt("user_id");
        verify(resultSet, times(0)).getInt("abonnement_id");
        verify(resultSet, times(0)).getString("status");
        verify(resultSet, times(0)).getString("verdubbeling");
        verify(resultSet, times(0)).getInt("user_id");
        verify(resultSet, times(0)).getString("startDatum");
    }

}
