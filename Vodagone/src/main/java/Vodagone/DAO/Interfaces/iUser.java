package Vodagone.DAO.Interfaces;

import Vodagone.Domain.User;

public interface iUser {
    /**
     * @param username = username
     * @param password = password
     * @return = Found user
     */
    User login(String username, String password);
}