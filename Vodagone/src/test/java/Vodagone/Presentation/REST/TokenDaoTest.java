package Vodagone.Presentation.REST;

import Vodagone.DAO.TokenDao;
import Vodagone.Domain.User;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class TokenDaoTest {

    @Test
    public void insertUserTokenTest() throws SQLException {
        String sql = "UPDATE Users SET token = ? WHERE id = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);

        // 2. Run test
        User user = new User(1, "Parwiz");
        TokenDao tokenDao = new TokenDao(connection);
        String token = tokenDao.generateToken();
        tokenDao.insertUserToken(user, token);

        // 3. Verify that correct calls where made
        verify(connection,times(1)).prepareStatement(sql);
        verify(statement,times(1)).setString(1, token);
        verify(statement,times(1)).setInt(2, user.getId());
    }

    @Test
    public void checkUserToken() throws SQLException {
        String sql = "SELECT id, naam FROM Users WHERE token = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        // 2. Run test
        TokenDao tokenDao = new TokenDao(connection);
        String token = tokenDao.generateToken();
        tokenDao.checkUserToken(token);

        // 3. Verify that correct calls where made
        verify(connection,times(1)).prepareStatement(sql);
        verify(statement, times(1)).setString(1, token);

        verify(resultSet, times(1)).getInt("id");
        verify(resultSet, times(1)).getString("naam");
    }

    @Test
    public void checkUserTokenFails() throws SQLException {
        String sql = "SELECT id, naam FROM Users WHERE token = ?";

        // 1. Setup test
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(sql)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // 2. Run test
        TokenDao tokenDao = new TokenDao(connection);
        String token = tokenDao.generateToken();
        tokenDao.checkUserToken(token);

        // 3. Verify that correct calls where made
        verify(connection,times(1)).prepareStatement(sql);
        verify(statement, times(1)).setString(1, token);

        verify(resultSet, times(0)).getInt("id");
        verify(resultSet, times(0)).getString("naam");
    }

}
