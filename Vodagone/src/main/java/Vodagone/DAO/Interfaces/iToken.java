package Vodagone.DAO.Interfaces;

import Vodagone.Domain.User;

import java.util.ArrayList;

public interface iToken {

    String generateToken();

    String insertUserToken(User user, String token);

    User checkUserToken(String token);

}
