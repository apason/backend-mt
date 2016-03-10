package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.SubUser;
import java.util.ArrayList;
import java.util.Optional;
import spark.Request;
import spark.Response;
import spark.Spark;

public class SubUserResource extends Resource {

    private final SubUserService subUserService;

    public SubUserResource(UserService userService, SubUserService subUserService) {
        super(userService);
        this.subUserService = subUserService;

        defineRoutes();
    }

    // Defines routes for UserResource.
    private void defineRoutes() {
        Spark.get("/DescribeSubUsers", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
            return describeSubUsers(req, res, u);
        });

        Spark.get("/CreateSubUser", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
            return createSubUser(req, res, u);
        });
        
        Spark.get("/DeleteSubUSer", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
           return deleteSubUSer(req, res, u); 
        });
    }

    // Describes all the SubUsers pointing to the user in question.
    String describeSubUser(Request req, Response res, User user) {
        int userIdInt = user.getId();
        JsonResponse jsonResponse = new JsonResponse();
        
        //TODO
        

        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Creates a new user pointing to the user in question.
    String CreateSubUser(Request req, Response res, User user) {
        int userIdInt = user.getId();

        //TODO
        
        
        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Deletes the wanted SubUser pointing to the user in question.
    String deleteSubUSer(Request req, Response res, User user) {
        int userIdInt = user.getId();
        String subUserId = req.queryParams("subuser_id");
        int subUserIDInt;
        
        JsonResponse jsonResponse = new JsonResponse();
        
        
        if (subUserID== null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
              
        try {
           subUserIDInt = Integer.parseInt(subUserID);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        //TODO
        
        
        return jsonResponse.setStatus("Success").toJson();
    }
}
