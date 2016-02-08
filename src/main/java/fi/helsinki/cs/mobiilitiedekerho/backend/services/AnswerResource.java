package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;

import spark.Spark;
import spark.Response;
import spark.Request;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
import java.util.Optional;

public class AnswerResource extends Resource{
    private final AnswerService answerService;

    public AnswerResource(UserService userService, AnswerService answerService) {
	super(userService);
        this.answerService = answerService;
    
	defineRoutes();
    }
  
    private void defineRoutes() {
	Spark.get("/DescribeAnswer", (req, res) -> {
                requireAuthentication(req, res);
		return this.describeAnswer(req, res);
	    });
	Spark.get("/StartAnswerUpload", (req, res) -> {
		requireAuthentication(req, res);
                return this.startAnswerUpload(req, res);
	    });
	Spark.get("/EndAnswerUpload", (req, res) -> {
		requireAuthentication(req, res);
                return this.endAnswerUpload(req, res);
	    });
    }

    private String endAnswerUpload(Request req, Response res){
	String uploadStatus = req.queryParams("upload_status");
	String answerIdString = req.queryParams("answer_id");
	String userHash = req.queryParams("user_hash");
	Integer answerId;

	Optional<User> user;

	JsonResponse jsonResponse = new JsonResponse();

	if(uploadStatus == null || answerIdString == null || userHash == null){
	    jsonResponse.setStatus("ParameterError");
	    return jsonResponse.toJson();
	}

	try{
	    answerId = Integer.parseInt(answerIdString);
	}
	catch(Exception e){
	    jsonResponse.setStatus("InvalidAnswerId");
	    return jsonResponse.toJson();
	}
	    

	user = getUserService().authenticateUserByHash(userHash);

	if(!user.isPresent()){
	    jsonResponse.setStatus("InvalidUserHash");
	    return jsonResponse.toJson();
	}


	if(uploadStatus.equals("success")){
	    getAnswerService().enableAnswer(answerId, user.get().getId());
	    jsonResponse.setStatus("Success");
	}
	else if(uploadStatus.equals("failure")){
	    getAnswerService().deleteAnswer(answerId, user.get().getId());
	    jsonResponse.setStatus("Success");
	}
	else
	    jsonResponse.setStatus("InvalidStatus");
	
	return jsonResponse.toJson();
	
    }

    private String startAnswerUpload(Request req, Response res){
	String userHash = req.queryParams("user_hash");
	String taskId   = req.queryParams("task_id");
	
	JsonResponse jsonResponse = new JsonResponse();
	
	Optional<User> user = getUserService().authenticateUserByHash(userHash);

	if(taskId == null){
	    jsonResponse.setStatus("ParameterError");
	    return jsonResponse.toJson();
	}

	Optional<Answer> answer = getAnswerService().setInitialAnswer(user.get().getId(), Integer.parseInt(taskId));

	if(!answer.isPresent()){
	    jsonResponse.setStatus("UnexpectedError");
	    return jsonResponse.toJson();
	}

	return jsonResponse
	    .addPropery("task_id", taskId)
	    .addPropery("answer_id", "" + answer.get().getId())
	    .addPropery("answer_uri", answer.get().getUri())
	    .setStatus("Success")
	    .toJson();

    }
    
    private String describeAnswer(Request req, Response res) {
	String answerId = req.queryParams("answer_id");
	JsonResponse jsonResponse = new JsonResponse();

	ArrayList<Answer> answers = new ArrayList<Answer>();
	if (answerId == null) {
	    jsonResponse.setStatus("ParameterError");
	    return jsonResponse.toJson();
	}

	Optional<Answer> answer = getAnswerService().getAnswerById(Integer.parseInt(answerId));
      
	if (!answer.isPresent()) {
	    jsonResponse.setStatus("AnswerNotFoundError");
	    return jsonResponse.toJson();
	}

	answers.add(answer);
	
	jsonResponse.setObject(answer.get());
      
	return jsonResponse.setStatus("Success").toJson();
    }

    public AnswerService getAnswerService() {
        return answerService;
    }
}
