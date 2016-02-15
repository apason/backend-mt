package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import spark.Request;
import spark.Response;
import spark.Spark;

abstract public class Resource {

    private final UserService userService;

    public Resource(UserService userService) {
        this.userService = userService;
    }

    void requireAuthentication(Request req, Response res) {
        String userHash = req.queryParams("user_hash");

        if (userHash == null) {
            Spark.halt(401, authFailure());
        }

        if (getUserService().authenticateUserByHash(userHash) == null) {
            Spark.halt(401, authFailure());
        }
    }

    void requireAuthenticatedUser(Request req, Response res) {
    }

    void requireAnonymousUser(Request req, Response res) {
    }
    
    boolean checkAuthToken(Request req, Response res) {
        return false;
    }

    String authFailure() {
        return new JsonResponse().setStatus("AuthFailure").toJson();
    }

    UserService getUserService() {
        return userService;
    }
}
