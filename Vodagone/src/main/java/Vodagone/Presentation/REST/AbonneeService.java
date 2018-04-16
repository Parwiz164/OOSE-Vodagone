package Vodagone.Presentation.REST;

import Vodagone.DAO.Interfaces.iAbonnee;
import Vodagone.DAO.Interfaces.iToken;
import Vodagone.Domain.Abonnee;
import Vodagone.Domain.User;
import Vodagone.Presentation.REST.DTO.AbonneeResponse;
import Vodagone.Presentation.REST.DTO.ShareWithAbonneeRequest;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/api")
public class AbonneeService {
    @Inject
    private iToken tokenDAO;

    @Inject
    private iAbonnee abonneeDao;

    @GET
    @Path("/abonnees")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAbonnees(@QueryParam("token") String token) {
        User user = tokenDAO.checkUserToken(token);

        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        }

        List <Abonnee> abonneeList = abonneeDao.getAllAbonnees(user.getId());
        List <AbonneeResponse> abonneeResponseList = new ArrayList <>();

        for (Abonnee abonnee : abonneeList) {
            abonneeResponseList.add(new AbonneeResponse(abonnee.getId(), abonnee.getName(), abonnee.getEmail()));
        }
        return Response.status(200).entity(abonneeResponseList).build();
    }

    @POST
    @Path("/abonnees/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response shareAbonnementWithAbonnee(@QueryParam("token") String token, @PathParam("id") String abonnee, ShareWithAbonneeRequest request) {
        User user = tokenDAO.checkUserToken(token);
        if (user == null) {
            // Incorrect token
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "User not found!");
            return Response.status(403).build();
        } else {
            abonneeDao.shareAbonnementWithAbonnee(Integer.parseInt(abonnee), request.getId());
            return Response.status(200).build();
        }
    }
}
