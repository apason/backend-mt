package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import spark.Request;
import spark.Response;
import spark.Spark;

import com.typesafe.config.Config;

public class UserResource extends Resource {


    public UserResource(UserService userService, Config appConfiguration) {
        super(userService, appConfiguration);

        defineRoutes();
    }

    // Defines routes for UserResource.
    private void defineRoutes() {
        Spark.get("/GetAuthToken", (req, res) -> {
            return getAuthToken(req, res);
        });

        Spark.get("/CheckTokenIntegrity", (req, res) -> {
            checkAuthToken(req, res);
            return checkTokenIntegrity(req, res);
        });

        Spark.get("/CreateUser", (req,res) -> {
            requireAnonymousUser(req, res);
            return createUser(req, res);
        });

        Spark.get("/DescribeUser", (req, res) -> {
            User user = requireAuthenticatedUser(req, res);
            return describeUser(req, res, user);
        });

        Spark.get("/SetPrivacyLevel", (req, res) -> {
            User user = requireAuthenticatedUser(req, res);
            return setPrivacyLevel(req, res, user); 
        });

        Spark.get("/SetPin", (req, res) -> {
            User user = requireAuthenticatedUser(req, res);
            return setPin(req, res, user); 
        });

        Spark.get("/CreateSubUser", (req, res) -> {
            User user = requireAuthenticatedUser(req, res);
           return createSubUser(req, res, user); 
        });

        Spark.get("/DescribeSubUser", (req, res) -> {
            User user = requireAuthenticatedUser(req, res);
            Subuser subUser = requireSubUser(req, res, user);
            return describeSubUser(req, res, user, subUser);
        });

        Spark.get("/DeleteSubUser", (req,res) -> {
            User user = requireAuthenticatedUser(req, res);
            Subuser subUser = requireSubUser(req, res, user);
            return deleteSubUser(req, res);
        });

        Spark.get("/DescribeSubUsers", (req,res) -> {
            User user = requireAuthenticatedUser(req, res);
            return describeSubUsers(req, res, user);
        });

    }

    // Generates a JSON Web Token for the client.
    // If email and password are set in GET, authenticates the user.
    // If they are not set, generates an anonymous token.
    // Returns status: AuthenticationFailure if authentication fails.
    String getAuthToken(Request req, Response res) {

        String auth_token = "";
        String email = req.queryParams("email");
        String password = req.queryParams(("password"));
        
        if ((email != null) && (password != null)) {
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
        // else if ((email != null) || (password != null)) {
        //    Error as one parameter is set but the other not.
        //    Should not happen, but anyways...
        // }
        // else
        auth_token = getUserService().generateAnonymousToken(req.ip());

        return new JsonResponse()
                .addPropery("auth_token", auth_token)
                .setStatus("Success")
                .toJson();
    }

    String checkTokenIntegrity(Request req, Response res){
        JsonResponse jsonResponse = new JsonResponse();

        String authToken = req.queryParams("auth_token");
        String userType = getUserType(authToken);

        return jsonResponse.setStatus(userType).toJson();
    }
    
    String createUser(Request req, Response res){
        JsonResponse jsonResponse = new JsonResponse();

        String user_email = req.queryParams("user_email");
        String user_pass  = req.queryParams("user_password");
        
        if(user_email == null || user_pass == null)
            return jsonResponse.setStatus("ParameterError").toJson();

        if(getUserService().userExists(user_email))
            return jsonResponse.setStatus("DuplicateUserError").toJson();

        if(getUserService().createUser(user_email, user_pass))
            return jsonResponse.setStatus("Success").toJson();

        return jsonResponse.setStatus("UnexpectedError").toJson();
    }

    // Describes the current user indicated by the auth token.
    String describeUser(Request req, Response res, User user) {
        return new JsonResponse().setObject(user).setStatus("Success").toJson();
    }


    //Sets the calling users privacy_level to the one given as a parameter.
    String setPrivacyLevel(Request req, Response res, User user) {
        JsonResponse jsonResponse = new JsonResponse();
        String privacyLevel = req.queryParams("privacy_level");
        int privacyLevelInt;
         
        if (privacyLevel == null) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        try {
            privacyLevelInt = Integer.parseInt(privacyLevel);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        
        if (privacyLevelInt > -1 && privacyLevelInt < 4) {
            return jsonResponse.setStatus(getUserService().setPrivacyLevel(user, privacyLevelInt)).toJson();
            
        } else {
            return jsonResponse.setStatus("InvalidPrivacyLevelNumber").toJson();
        }
    }


    //Sets the calling users pin to the one given as a parameter.
    String setPin(Request req, Response res, User user) {
        JsonResponse jsonResponse = new JsonResponse();
        String pin = req.queryParams("pin");
        
        if (pin == null) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        return jsonResponse.setStatus(getUserService().setPin(user, pin)).toJson();
    }


    String createSubUser (Request req, Response res, User user){
        JsonResponse jsonResponse = new JsonResponse();
        String subuserNick = req.queryParams("subuser_nick");

        if(subuserNick == null)
            return jsonResponse.setStatus("ParameterError").toJson();

        List<Subuser> users = getUserService().getSubUsers(user);
        //only 3 subusers allowed
        if(users.size() >=3)
            return jsonResponse.setStatus("SubuserQuantityError").toJson();

        //no duplicates allowed
        for(Subuser su : users) {
            if(su.getNick().equals(subuserNick)) {
                return jsonResponse.setStatus("SubuserDuplicateNickError").toJson();
            }
        }

        int subuserId = getUserService().createSubUser(user, subuserNick);
        
        ArrayList<Subuser> subUsers = new ArrayList<Subuser>();
        Optional<Subuser> subUser = getUserService().getSubUserById(subuserId);
        subUsers.add(modifyUriToSignedDownloadUrl(subUser.get()));
        //No need to check as must exist.
        return jsonResponse.setStatus("Success").setObject(subUsers).toJson();
    }

    //It can be assumed that the subuser exist (requireSubUser() in defineRoutes
    String deleteSubUser(Request req, Response res){
        JsonResponse jsonResponse = new JsonResponse();
        getUserService().deleteSubUser(req.queryParams("subuser_id"));
        return jsonResponse.setStatus("Success").toJson();
    }

    //parameter error not checked right, cannot read the subuser parameter before method!!!!
    String describeSubUser(Request req, Response res, User user, Subuser subUser){
        ArrayList<Subuser> subUsers = new ArrayList<Subuser>();
        JsonResponse jsonResponse = new JsonResponse();

        subUsers.add(modifyUriToSignedDownloadUrl(subUser));
        return jsonResponse.setStatus("Success")
            .setObject(subUsers).toJson();
    }

    String describeSubUsers(Request req, Response res, User user){
        JsonResponse jsonResponse = new JsonResponse();
        List<Subuser> subusers = getUserService().describeSubUsers(user);
        if(subusers == null || subusers.isEmpty())
            return jsonResponse.setStatus("NoSubUsersFound").toJson();
        
        for (Subuser su: subusers) {
            su = modifyUriToSignedDownloadUrl(su);
        }
        
        return jsonResponse.setStatus("Success")
            .setObject(subusers).toJson();
    }

    // Generate signed url for subuser avatar uri.
    Subuser modifyUriToSignedDownloadUrl(Subuser su) {
        String url = this.getS3Helper().generateSignedDownloadUrl(
                this.getAppConfiguration().getString("app.avatar_bucket"),
                su.getAvatar_url()
        );

        su.setAvatar_url(url);

        return su;
    }     
}
