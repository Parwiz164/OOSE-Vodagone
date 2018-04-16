package Vodagone.DAO.Interfaces;

import Vodagone.Domain.Abonnement;

import java.util.List;

public interface iAbonnement {

    List <Abonnement> getAbonnementenFromUser(int userId);

    List <Abonnement> getAllAvailableAbonnementen(int userId, String filter);

    void addAbonnement(int userId, int abonnementId);

    Abonnement getSpecificAbonnementFromLoggedUser(int userId);

    void terminateAbonnement(int abonnementId);

    void upgradeAbonnement(int abonnementId);

}
