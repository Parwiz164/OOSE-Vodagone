package Vodagone.Presentation.REST;

import Vodagone.DAO.Interfaces.iToken;
import Vodagone.DAO.Interfaces.iUser;
import Vodagone.Domain.User;
import Vodagone.Presentation.REST.DTO.LoginRequest;
import Vodagone.Presentation.REST.DTO.LoginResponse;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

@Path("/api")
public class LoginService {
    @Inject
    private iToken tokenDAO;

    @Inject
    private iUser userDAO;

    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(LoginRequest login) {
        User user = userDAO.login(login.getUser(), login.getPassword());
        if (user == null) {
            return Response.status(401).build();
        } else {
            String token = tokenDAO.generateToken();
            tokenDAO.insertUserToken(user, token);
            LoginResponse loginResponse = new LoginResponse(token, user.getNaam());
            return Response.status(201).entity(loginResponse).build();
        }
    }
}

