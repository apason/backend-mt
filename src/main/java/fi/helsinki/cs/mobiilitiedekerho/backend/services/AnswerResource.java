package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;

import spark.Spark;
import spark.Response;
import spark.Request;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;
import java.lang.Integer;

public class AnswerResource extends Resource{
    private final AnswerService answerService;

    public AnswerResource(UserService userService, AnswerService answerService) {
	super(userService);
        this.answerService = answerService;
    
	defineRoutes();
    }
  
    private void defineRoutes() {
	Spark.get("/DescribeAnswer", (req, res) -> {
		return this.describeAnswer(req, res);
	    });
	Spark.get("/StartAnswerUpload", (req, res) -> {
		return this.startAnswerUpload(req, res);
	    });
	Spark.get("/EndAnswerUpload", (req, res) -> {
		return this.endAnswerUpload(req, res);
	    });
    }

    private String endAnswerUpload(Request req, Response res){
	String uploadStatus = req.queryParams("upload_status");
	String answerIdString = req.queryParams("answer_id");
	String userHash = req.queryParams("user_hash");
	Integer answerId;

	User user;

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

	if(user == null){
	    jsonResponse.setStatus("InvalidUserHash");
	    return jsonResponse.toJson();
	}


	if(uploadStatus.equals("success")){
	    getAnswerService().enableAnswer(answerId, user.getId());
	    jsonResponse.setStatus("success");
	}
	else if(uploadStatus.equals("failure")){
	    getAnswerService().deleteAnswer(answerId, user.getId());
	    jsonResponse.setStatus("success");
	}
	else
	    jsonResponse.setStatus("InvalidStatus");
	
	return jsonResponse.toJson();
	
    }

    private String startAnswerUpload(Request req, Response res){
	String userHash = req.queryParams("user_hash");
	String taskId   = req.queryParams("task_id");
	
	JsonResponse jsonResponse = new JsonResponse();

	this.requireAuthentication(req, res);
	
	User user = getUserService().authenticateUserByHash(userHash);

	if(taskId == null){
	    jsonResponse.setStatus("ParameterError");
	    return jsonResponse.toJson();
	}

	List<Answer> answer = getAnswerService().setInitialAnswer(user.getId(), Integer.parseInt(taskId));

	if(answer == null || answer.isEmpty()){
	    jsonResponse.setStatus("UnexpectedError");
	    return jsonResponse.toJson();
	}

	return jsonResponse
	    .addPropery("task_id", taskId)
	    .addPropery("answer_id", "" + answer.get(0).getId())
	    .addPropery("answer_uri", answer.get(0).getUri())
	    .setStatus("Success")
	    .toJson();

    }


    
    private String describeAnswer(Request req, Response res) {
	String answerId = req.queryParams("answer_id");
	JsonResponse jsonResponse = new JsonResponse();
	
	if (answerId == null) {
	    jsonResponse.setStatus("ParameterError");
	    return jsonResponse.toJson();
	}

	List<Answer> answers = getAnswerService().getAnswerById(Integer.parseInt(answerId));
      
	if (answers.isEmpty()) {
	    jsonResponse.setStatus("AnswerNotFoundError");
	    return jsonResponse.toJson();
	}
      
	jsonResponse.setObject(answers.get(0));
      
	return jsonResponse.setStatus("Success").toJson();
    }

    public AnswerService getAnswerService() {
        return answerService;
    }
}
