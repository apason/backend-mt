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

    String authFailure() {
        return new JsonResponse().setStatus("AuthFailure").toJson();
    }

    UserService getUserService() {
        return userService;
    }
}
