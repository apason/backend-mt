package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;

import spark.Spark;
import spark.Response;
import spark.Request;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;
import java.lang.Integer;

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
    }

    private String describeUser(Request req, Response res) {
        String userId = req.queryParams("user_id");
        JsonResponse jsonResponse = new JsonResponse();

        if (userId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        User user = getUserService().getUserById(Integer.parseInt(userId));

        if (user == null) {
            jsonResponse.setStatus("UserNotFoundError");
            return jsonResponse.toJson();
        }

        jsonResponse.setObject(user);

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

        User user = getUserService().authenticateUser(email, password);

        if (user == null) {
            return new JsonResponse().setStatus("AuthFailure").toJson();
        } else {
            getUserService().createAuthHashForUser(user.getId());
            user = getUserService().getUserById(user.getId());
            return new JsonResponse().setObject(user).setStatus("Success").toJson();
        }
    }

    private String authenticateUserByHash(Request req, Response res) {
        String userHash = req.queryParams("user_hash");
        JsonResponse jsonResponse = new JsonResponse();

        if (userHash == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        User user = getUserService().authenticateUserByHash(userHash);

        if (user == null) {
            return new JsonResponse().setStatus("AuthFailure").toJson();
        } else {
            return new JsonResponse().setObject(user).setStatus("Success").toJson();
        }
    }
}
