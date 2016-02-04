package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import spark.Request;
import spark.Response;
import spark.Spark;

abstract public class Resource {

    UserService userService;

    void requireAuthentication(Request req, Response res) {
        String authHash = req.queryParams("auth_hash");

        if (authHash == null) {
            Spark.halt(401, authFailure());
        }

        if (userService.authenticateUserByHash(authHash) == null) {
            Spark.halt(401, authFailure());
        }
    }

    String authFailure() {
        return new JsonResponse().setStatus("AuthFailure").toJson();
    }
}
