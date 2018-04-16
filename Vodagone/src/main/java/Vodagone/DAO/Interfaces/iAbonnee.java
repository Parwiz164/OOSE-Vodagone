package Vodagone.DAO.Interfaces;

import Vodagone.Domain.Abonnee;

import java.util.List;

public interface iAbonnee {
    List <Abonnee> getAllAbonnees(int userId);

    void shareAbonnementWithAbonnee(int userId, int abonnementId);
}
