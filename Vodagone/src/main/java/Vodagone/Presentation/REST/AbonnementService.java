package Vodagone.Presentation.REST;

import Vodagone.DAO.Interfaces.iAbonnement;
import Vodagone.DAO.Interfaces.iToken;
import Vodagone.Domain.Abonnement;
import Vodagone.Domain.User;
import Vodagone.Presentation.REST.DTO.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/api")
public class AbonnementService {

    @Inject
    private iToken tokenDAO;

    @Inject
    private iAbonnement abonnementDao;

    @GET
    @Path("/abonnementen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAbonnementenFromUser(@QueryParam("token") String token) {
        User user = tokenDAO.checkUserToken(token);

        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        } else {
            List <Abonnement> abonnementList = abonnementDao.getAbonnementenFromUser(user.getId());
            List <AbonnementUser> abonnementUserList = new ArrayList <>();
            double totalPrice = 0.00;
            for (Abonnement abonnement : abonnementList) {
                abonnementUserList.add(new AbonnementUser(abonnement.getId(), abonnement.getAanbieder(), abonnement.getDienst()));
                totalPrice += abonnement.getPrijs();
            }
            AbonnementResponse abonnementResponse = new AbonnementResponse(abonnementUserList, totalPrice);
            return Response.status(200).entity(abonnementResponse).build();
        }
    }

    @GET
    @Path("/abonnementen/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAvailableAbonnementen(@QueryParam("token") String token, @QueryParam("filter") String filter) {
        User user = tokenDAO.checkUserToken(token);

        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        } else {
            List <Abonnement> abonnementList = abonnementDao.getAllAvailableAbonnementen(user.getId(), filter);
            List <AbonnementUser> abonnementUserList = new ArrayList <>();

            for (Abonnement abonnement : abonnementList) {
                abonnementUserList.add(new AbonnementUser(abonnement.getId(), abonnement.getAanbieder(), abonnement.getDienst()));
            }
            return Response.status(200).entity(abonnementUserList).build();
        }
    }

    @POST
    @Path("/abonnementen")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addAbonnement(@QueryParam("token") String token, addAbonnementRequest request) {
        User user = tokenDAO.checkUserToken(token);
        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        } else {
            abonnementDao.addAbonnement(user.getId(), request.getId());
            List <Abonnement> abonnementList = abonnementDao.getAbonnementenFromUser(user.getId());
            List <AbonnementUser> abonnementenUser = new ArrayList <>();
            double totalPrice = 0.00;

            for (Abonnement abonnee : abonnementList) {
                abonnementenUser.add(new AbonnementUser(abonnee.getId(), abonnee.getAanbieder(), abonnee.getDienst()));
                totalPrice += abonnee.getPrijs();
            }

            AbonnementResponse response = new AbonnementResponse(abonnementenUser, totalPrice);
            return Response.status(201).entity(response).build();
        }
    }

    @GET
    @Path("/abonnementen/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSpecificAbonnementFromLoggedUser(@QueryParam("token") String token, @PathParam("id") String id) {
        User user = tokenDAO.checkUserToken(token);
        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        } else {
            Abonnement abonnement = abonnementDao.getSpecificAbonnementFromLoggedUser(Integer.parseInt(id));
            SpecificAbonnementResponse specificAbonnement = new SpecificAbonnementResponse(abonnement.getId(), abonnement.getAanbieder(),
                    abonnement.getDienst(), abonnement.getPrijs(), abonnement.getStartDatum(), abonnement.getVerdubbeling(),
                    abonnement.isDeelbaar(), abonnement.getStatus());
            return Response.status(200).entity(specificAbonnement).build();
        }
    }

    @DELETE
    @Path("/abonnementen/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response terminateAbonnement(@QueryParam("token") String token, @PathParam("id") String id) {
        User user = tokenDAO.checkUserToken(token);
        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        } else {
            abonnementDao.terminateAbonnement(Integer.parseInt(id));
            return getSpecificAbonnementFromLoggedUser(token, id);
        }
    }

    @POST
    @Path("/abonnementen/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response upgradeAbonnement(@QueryParam("token") String token, @PathParam("id") String id) {
        User user = tokenDAO.checkUserToken(token);
        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        } else {
            abonnementDao.upgradeAbonnement(Integer.parseInt(id));
            Abonnement abonnement = abonnementDao.getSpecificAbonnementFromLoggedUser(Integer.parseInt(id));

            SpecificAbonnementResponse specificAbonnement = new SpecificAbonnementResponse(abonnement.getId(), abonnement.getAanbieder(),
                    abonnement.getDienst(), abonnement.getPrijs(), abonnement.getStartDatum(), abonnement.getVerdubbeling(),
                    abonnement.isDeelbaar(), abonnement.getStatus());

            return Response.status(201).entity(specificAbonnement).build();
        }
    }

}