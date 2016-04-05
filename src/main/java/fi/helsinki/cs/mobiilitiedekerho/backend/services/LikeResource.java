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
        Spark.get("/DescribeAnswerLikes", (req, res) -> {
            requireAnonymousUser(req, res);
            return describeAnswerLikes(req, res);
        });

        Spark.get("/LikeAnswer", (req, res) -> {
	    User user = requireAuthenticatedUser(req, res);
	    Subuser subUser = requireSubUser(req, res, user);
            return likeAnswer(req, res, subUser);
        });
	

    }

    String likeAnswer(Request req, Response res, Subuser subUser){
	JsonResponse jsonResponse = new JsonResponse();

	String answerId = req.queryParams("answer_id");
	if(answerId == null || (Integer) Integer.parseInt(answerId) == null)
	    return jsonResponse.setStatus("ParameterError").toJson();

	int answer = Integer.parseInt(answerId);

	if(!answerService.getAnswerById(answer).isPresent())
	    return jsonResponse.setStatus("AnswerNotFoundError").toJson();

	Integer newKey = likeService.likeAnswer(answer, subUser);
	
	if(newKey < 0)
	    return jsonResponse.setStatus("AlreadyLiked").toJson();

	jsonResponse.addPropery("like_id", newKey.toString());

	return jsonResponse.setStatus("Success").toJson();
    }


    String describeAnswerLikes(Request req, Response res){
	JsonResponse jsonResponse = new JsonResponse();

	String answerId = req.queryParams("answer_id");

	if(answerId == null || (Integer)Integer.parseInt(answerId) == null)
	    return jsonResponse.setStatus("ParameterError").toJson();

	List<Like> likes = likeService.describeAnswerLikes(answerId);
	
	if(likes.isEmpty())
	    return jsonResponse.setStatus("LikesNotFound").toJson();
	
	jsonResponse.setObject(likes);

	return jsonResponse.setStatus("Success").toJson();
    }

}
