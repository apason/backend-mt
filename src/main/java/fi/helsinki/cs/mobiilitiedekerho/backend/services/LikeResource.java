package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Like;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import spark.Request;
import spark.Response;
import spark.Spark;

public class LikeResource extends Resource {

    public enum Direction{
        TO, FROM
    }

    private final LikeService likeService;
    private final AnswerService answerService;

    public LikeResource(LikeService likeService, UserService userService, AnswerService answerService) {
        super(userService);
        this.likeService = likeService;
        this.answerService = answerService;
        defineRoutes();
    }

    // Defines routes for LikeResource
    private void defineRoutes() {
        Spark.get("/LikeAnswer", (req, res) -> {
                User user = requireAuthenticatedUser(req, res);
                Subuser subUser = requireSubUser(req, res, user);
            return likeAnswer(req, res, subUser);
        });
        
        Spark.get("/DescribeAnswerLikes", (req, res) -> {
            requireAnonymousUser(req, res);
            return describeAnswerLikes(req, res);
        });
        
        Spark.get("/DescribeLikesFromSubuser", (req, res) -> {
            User user = requireAuthenticatedUser(req, res);
            Subuser subUser = requireSubUser(req, res, user);
            return describeSubuserLikes(subUser, Direction.FROM);
        });

        Spark.get("/DescribeLikesToSubuser", (req, res) -> {
            User user = requireAuthenticatedUser(req, res);
            Subuser subUser = requireSubUser(req, res, user);
            return describeSubuserLikes(subUser, Direction.TO);
        });
    }


    String describeSubuserLikes(Subuser subUser, Direction direction){
        JsonResponse jsonResponse = new JsonResponse();

        List<Like> likes = direction == Direction.TO ?
            likeService.describeLikesToSubuser(subUser)
            :
            likeService.describeLikesFromSubuser(subUser);

        return !likes.isEmpty() ?
            jsonResponse.setStatus("Success")
            .setObject(likes)
            .toJson()
            :
            jsonResponse.setStatus("LikesNotFound")
            .toJson();
    }

    private String likeAnswer(Request req, Response res, Subuser subUser){
        JsonResponse jsonResponse = new JsonResponse();
        int answerIdInt; 

        String answerId = req.queryParams("answer_id");
        if(answerId == null)
            return jsonResponse.setStatus("ParameterError").toJson();
        
    try {
        answerIdInt =  Integer.parseInt(answerId);
    } catch (Exception e) {
        return jsonResponse.setStatus("ParameterError").toJson();
    }

        if(!answerService.getAnswerById(answerIdInt).isPresent())
            return jsonResponse.setStatus("AnswerNotFoundError").toJson();

        Integer newKey = likeService.likeAnswer(answerIdInt, subUser);
        
        if(newKey < 0)
            return jsonResponse.setStatus("AlreadyLiked").toJson();

        jsonResponse.addPropery("like_id", newKey.toString());

        return jsonResponse.setStatus("Success").toJson();
    }


    private String describeAnswerLikes(Request req, Response res){
        JsonResponse jsonResponse = new JsonResponse();
        int answerIdInt;

        String answerId = req.queryParams("answer_id");

        if(answerId == null)
            return jsonResponse.setStatus("ParameterError").toJson();
        
    try {
        answerIdInt =  Integer.parseInt(answerId);
    } catch (Exception e) {
        return jsonResponse.setStatus("ParameterError").toJson();
    }

        List<Like> likes = likeService.getLikesByAnswer(answerIdInt);
        
        if(likes.isEmpty())
            return jsonResponse.setStatus("LikesNotFound").toJson();
        
        jsonResponse.setObject(likes);

        return jsonResponse.setStatus("Success").toJson();
    }

}
