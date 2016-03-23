package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;
import java.util.ArrayList;
import java.util.List;
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
            User user = requireAuthenticatedUser(req, res);
           return describeCurrentUser(req, res, user); 
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

	Spark.get("/CreateUser", (req,res) -> {
	    requireAnonymousUser(req, res);
	    return createUser(req, res);
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

    //It can be assumed that the subuser exist (requireSubUser() in defineRoutes
    String deleteSubUser(Request req, Response res){
	JsonResponse jsonResponse = new JsonResponse();
	getUserService().deleteSubUser(req.queryParams("subuser_id"));
	return jsonResponse.setStatus("Success").toJson();
    }

    //parameter error ????
    String describeSubUser(Request req, Response res, User user, Subuser subUser){
	ArrayList<Subuser> subUsers = new ArrayList<Subuser>();
	JsonResponse jsonResponse = new JsonResponse();
	
	subUsers.add(subUser);	
	return jsonResponse.setStatus("Success")
	    .setObject(subUsers).toJson();
    }

    String describeSubUsers(Request req, Response res, User user){
	JsonResponse jsonResponse = new JsonResponse();
	List<Subuser> users = getUserService().describeSubUsers(user);
	if(users == null || users.isEmpty())
	    return jsonResponse.setStatus("NoSubUsersFound").toJson();

	return jsonResponse.setStatus("Success")
	    .setObject(users).toJson();
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

    String createSubUser (Request req, Response res, User user){
	JsonResponse jsonResponse = new JsonResponse();
	String subuserNick = req.queryParams("subuser_nick");
	int newKey;

	if(subuserNick == null)
	    return jsonResponse.setStatus("ParameterError").toJson();

	List<Subuser> users = getUserService().getSubUsers(user);
	//only 3 subusers allowed
	if(users.size() >=3)
	    return jsonResponse.setStatus("SubuserQuantityError").toJson();

	//no duplicates allowed
	for(Subuser su : users)
	    if(su.getNick().equals(subuserNick))
		return jsonResponse.setStatus("SubuserDuplicateNickError").toJson();

	newKey = getUserService().createSubUser(user, subuserNick);

	return jsonResponse.setStatus("Success")
	    .setObject(getUserService().getSubUserById(newKey)).toJson();
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
