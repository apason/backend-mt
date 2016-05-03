package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import spark.Request;
import spark.Response;
import spark.Spark;

import io.jsonwebtoken.Jwts;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;
import java.util.Optional;

import com.typesafe.config.Config;

abstract public class Resource {

    private final UserService userService;
    private S3Helper s3Helper;
    private final Config appConfiguration;


    public Resource(UserService userService, Config appConfiguration) {
        this.userService = userService;
        this.appConfiguration = appConfiguration;
        this.s3Helper = new S3Helper(
                appConfiguration.getString("s3.access_key"),
                appConfiguration.getString("s3.secret_access_key"));
    }
    
    /* This method is for setting a mock class in testing. */
    public void setS3Helper(S3Helper h) {
        this.s3Helper = h;
    }

    /*
     * This function checks whether the given (req) subuser_id
     * belongs to the given user. If it does not, spart.halt()
     * is used. Otherwise Subuser object of that id is returned.
     */
    Subuser requireSubUser(Request req, Response res, User u){
        String subUserIdString = req.queryParams("subuser_id");
        if(subUserIdString == null)
            Spark.halt(401, parameterError());
        Integer subUserId = Integer.parseInt(subUserIdString);
        if(subUserId == null)
            Spark.halt(401, parameterError());
        
        Subuser subUser = userService.requireSubUser(u, subUserId);
        if(subUser == null)
            Spark.halt(401, subUserError());

        return subUser;
    }

    String getUserType(String authToken){
        return Jwts.parser()
            .setSigningKey(getUserService().getSecretKey())
            .parseClaimsJws(authToken)
            .getBody()
            .get("user_type", String.class);
    }

    // Checks if the user is authenticated with an auth token.
    // First validates the tokens signature.
    // On errors (invalid signature, invalid token format),
    // halts the request with an error message.
    // If auth token corresponds to a registered user, returns the user object.
    User requireAuthenticatedUser(Request req, Response res) {
        checkAuthToken(req, res);
        
        String authToken = req.queryParams("auth_token");
        
        String userType = getUserType(authToken);
        
        if (!userType.equals("authenticated")) {
            Spark.halt(401, authorizationFailure());
        }
        
        int userId = Jwts.parser().setSigningKey(getUserService().getSecretKey()).parseClaimsJws(authToken).getBody().get("user_id", Integer.class);
        
        Optional<User> u = getUserService().getUserById(userId);
        
        if (!u.isPresent()) {
            Spark.halt(401, authorizationFailure());
        }
        
        return u.get();
    }

    // Checks if the auth token is a valid token.
    void requireAnonymousUser(Request req, Response res) {
        checkAuthToken(req, res);
    }
    
    // Validates the token signature.
    // On errors, halts the request with an error message.
    void checkAuthToken(Request req, Response res) {
        String authToken = req.queryParams("auth_token");
        
        if (authToken == null || authToken.equals("")) {
            Spark.halt(401, parameterError());
        }
        
        if (!getUserService().validateToken(authToken)) {
            Spark.halt(401, tokenError());
        }
    }

    String subUserError(){
        return new JsonResponse().setStatus("SubuserError").toJson();
    }

    String authenticationFailure() {
        return new JsonResponse().setStatus("AuthenticationFailure").toJson();
    }
    
    String authorizationFailure() {
        return new JsonResponse().setStatus("AuthorizationFailure").toJson();
    }    
    
    String tokenError() {
        return new JsonResponse().setStatus("TokenError").toJson();
    }
    
    String parameterError() {
        return new JsonResponse().setStatus("ParameterError").toJson();
    }
    

    UserService getUserService() {
        return userService;
    }
    
    S3Helper getS3Helper() {
        return s3Helper;
    }
    
    Config getAppConfiguration() {
        return this.appConfiguration;
    }

}
