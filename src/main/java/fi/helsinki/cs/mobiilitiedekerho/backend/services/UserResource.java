package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;

import spark.Spark;
import spark.Response;
import spark.Request;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
import java.util.Optional;

public class UserResource extends Resource {

    public UserResource(UserService userService) {
        super(userService);

        defineRoutes();
    }

    private void defineRoutes() {
        Spark.get("/DescribeUser", (req, res) -> {
            requireAuthentication(req, res);
            return describeUser(req, res);
        });

        Spark.get("/AuthenticateUser", (req, res) -> {
            return authenticateUser(req, res);
        });

        Spark.get("/AuthenticateUserByHash", (req, res) -> {
            return authenticateUserByHash(req, res);
        });

        Spark.get("/GetAuthToken", (req, res) -> {
            return getAuthToken(req, res);
        });

    }

    private String describeUser(Request req, Response res) {
        String userId = req.queryParams("user_id");
        JsonResponse jsonResponse = new JsonResponse();
        ArrayList<User> users = new ArrayList<User>();

        if (userId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        Optional<User> user = getUserService().getUserById(Integer.parseInt(userId));

        if (!user.isPresent()) {
            jsonResponse.setStatus("UserNotFoundError");
            return jsonResponse.toJson();
        }

        users.add(user.get());
        jsonResponse.setObject(users);

        return jsonResponse.setStatus("Success").toJson();
    }

    private String authenticateUser(Request req, Response res) {
        String email = req.queryParams("email");
        String password = req.queryParams("password");
        JsonResponse jsonResponse = new JsonResponse();

        if (email == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        if (password == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        Optional<User> user = getUserService().authenticateUser(email, password);

        if (!user.isPresent()) {
            return new JsonResponse().setStatus("AuthFailure").toJson();
        } else {
            getUserService().createHashForUser(user.get().getId());
            user = getUserService().getUserById(user.get().getId());
            return new JsonResponse().setObject(user.get()).setStatus("Success").toJson();
        }
    }

    private String authenticateUserByHash(Request req, Response res) {
        String userHash = req.queryParams("user_hash");
        JsonResponse jsonResponse = new JsonResponse();

        if (userHash == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        Optional<User> user = getUserService().authenticateUserByHash(userHash);

        if (!user.isPresent()) {
            return new JsonResponse().setStatus("AuthFailure").toJson();
        } else {
            return new JsonResponse().setObject(user.get()).setStatus("Success").toJson();
        }
    }

    private String getAuthToken(Request req, Response res) {
        // Generates a JSON Web Token for the client.
        // If email and password are set in GET, authenticates the user.
        
        return "";
    }


    private String generateAnonymousToken(String client_ip) {
        // Generates an anonymous auth token. Stores client ip
        // in token.
        return "";
    }
    
    private String generateAuthenticatedToken(String client_ip) {
        return "";
    }    
}
