package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import java.util.ArrayList;
import java.util.Optional;
import spark.Request;
import spark.Response;
import spark.Spark;

public class UserResource extends Resource {

    public UserResource(UserService userService) {
        super(userService);

        defineRoutes();
    }

    // Defines routes for UserResource.
    private void defineRoutes() {
        Spark.get("/DescribeUser", (req, res) -> {
            requireAuthenticatedUser(req, res);
            return describeUser(req, res);
        });

        Spark.get("/GetAuthToken", (req, res) -> {
            return getAuthToken(req, res);
        });
        
        Spark.get("/DescribeCurrentUser", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
           return describeCurrentUser(req, res, u); 
        });
    }

    // Describes the user indicated by
    // user_id parameter.
    String describeUser(Request req, Response res) {
        String userId = req.queryParams("user_id");
        int userIdInt;
        JsonResponse jsonResponse = new JsonResponse();
        ArrayList<User> users = new ArrayList<User>();

        if (userId == null) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        try {
            userIdInt = Integer.parseInt(userId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        Optional<User> user = getUserService().getUserById(userIdInt);

        if (!user.isPresent()) {
            jsonResponse.setStatus("UserNotFoundError");
            return jsonResponse.toJson();
        }

        users.add(user.get());
        jsonResponse.setObject(users);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Generates a JSON Web Token for the client.
    // If email and password are set in GET, authenticates the user.
    // If they are not set, generates an anonymous token.
    // Returns status: AuthenticationFailure if authentication fails.
    String getAuthToken(Request req, Response res) {

        String auth_token = "";
        String email = req.queryParams("email");
        String password = req.queryParams(("password"));

        if (email != null && password != null) {

            Optional<User> u = getUserService()
                    .authenticateUser(email, password);

            if (!u.isPresent()) {
                return authenticationFailure();
            } else {
                auth_token = this.getUserService().generateAuthenticatedToken(u.get(), req.ip());
                return new JsonResponse()
                        .addPropery("auth_token", auth_token)
                        .setStatus("Success")
                        .toJson();
            }
        }
        
        auth_token = getUserService().generateAnonymousToken(req.ip());

        return new JsonResponse()
                .addPropery("auth_token", auth_token)
                .setStatus("Success")
                .toJson();
    }
    
    // Describes the current user indicated by the auth token.
    String describeCurrentUser(Request req, Response res, User user) {
        return new JsonResponse().setObject(user).setStatus("Success").toJson();
    }
}
