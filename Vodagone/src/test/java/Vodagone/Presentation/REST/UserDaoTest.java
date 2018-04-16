package Vodagone.Presentation.REST;

import Vodagone.DAO.UserDao;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class UserDaoTest {

    @Test
    public void loginTest() throws SQLException {
        String sql = "SELECT id, username, naam, email FROM Users WHERE username = ? AND password = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        // 2. Run test
        UserDao userDao = new UserDao(connection);
        userDao.login("Parwiz", "admin");

        // 3. Verify that correct calls where made
        verify(connection,times(1)).prepareStatement(sql);
        verify(statement,times(1)).setString(1, "Parwiz");
        verify(statement,times(1)).setString(2, "admin");

        verify(resultSet, times(1)).getInt("id");
        verify(resultSet, times(1)).getString("username");
        verify(resultSet, times(1)).getString("naam");
        verify(resultSet, times(1)).getString("email");
    }

    @Test
    public void loginTestFails() throws SQLException {
        String sql = "SELECT id, username, naam, email FROM Users WHERE username = ? AND password = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // 2. Run test
        UserDao userDao = new UserDao(connection);
        userDao.login("Parwiz", "admin");

        // 3. Verify that correct calls where made
        verify(connection,times(1)).prepareStatement(sql);
        verify(statement,times(1)).setString(1, "Parwiz");
        verify(statement,times(1)).setString(2, "admin");

        verify(resultSet, times(0)).getInt("id");
        verify(resultSet, times(0)).getString("username");
        verify(resultSet, times(0)).getString("naam");
        verify(resultSet, times(0)).getString("email");
    }

}