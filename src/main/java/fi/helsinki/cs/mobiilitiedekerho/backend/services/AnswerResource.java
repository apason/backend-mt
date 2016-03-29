package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.ArrayList;
import java.util.Optional;

import static java.lang.System.out;

public class AnswerResource extends Resource {

    private final AnswerService answerService;

    public AnswerResource(UserService userService, AnswerService answerService) {
        super(userService);
        this.answerService = answerService;

        defineRoutes();
    }

    // Defines routes for AnswerResource.
    private void defineRoutes() {
        Spark.get("/DescribeAnswer", (req, res) -> {
            requireAnonymousUser(req, res);
            return this.describeAnswer(req, res);
        });
        
        Spark.get("/DescribeTaskAnswers", (req, res) -> {
            requireAnonymousUser(req, res);
            return this.describeTaskAnswers(req, res);
        });
        
        Spark.get("/StartAnswerUpload", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
	    Subuser subUser = requireSubUser(req, res, u);
            return this.startAnswerUpload(req, res, subUser);
        });
        
	Spark.get("/EndAnswerUpload", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
            return this.endAnswerUpload(req, res, u);
        });
        
        Spark.get("/DescribeSubUserAnswers", (req, res) -> {
            requireAnonymousUser(req, res); //TODO: Depends actually of the "privacy-level" of the SubUser's (parent) user.
            return this.describeSubUserAnswers(req, res);
        });
    }
    
    // Starts answer upload.
    // Creates the answer in the database.
    // Returns the new answer id and an URI for the client to upload to.
    String startAnswerUpload(Request req, Response res, Subuser subUser) {
        String taskId = req.queryParams("task_id");
        int taskIdInt;

        JsonResponse jsonResponse = new JsonResponse();

        if (taskId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
	}
              
        try {
           taskIdInt = Integer.parseInt(taskId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        Optional<Answer> answer = getAnswerService().setInitialAnswer(subUser, taskIdInt);

        if (!answer.isPresent()) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        return jsonResponse
	    .addPropery("task_id", taskId)
	    .addPropery("answer_id", "" + answer.get().getId())
	    .addPropery("answer_uri", answer.get().getUri())
	    .setStatus("Success")
	    .toJson();
    }
    
    // Ends the answer upload.
    // If upload was successful, updates the database.
    // If upload failed, removes the row from the database.
    String endAnswerUpload(Request req, Response res, User user) {
        String uploadStatus = req.queryParams("upload_status");
        String answerIdString = req.queryParams("answer_id");
        Integer answerId;

        JsonResponse jsonResponse = new JsonResponse();

        if (uploadStatus == null || answerIdString == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        try {
            answerId = Integer.parseInt(answerIdString);
        } catch (Exception e) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        if (uploadStatus.equals("success")) {
            jsonResponse
		.setStatus(getAnswerService()
			   .enableAnswer(answerId, user));
	    
        } else if (uploadStatus.equals("failure")) {
            jsonResponse
		.setStatus(getAnswerService()
			   .deleteAnswer(answerId, user));
        } else {
            jsonResponse.setStatus("InvalidStatus");
        }

        return jsonResponse.toJson();

    }

    // Describes an answer indicated by answer_id.
    // If the answer is not found, returns status: AnswerNotFoundError.
    String describeAnswer(Request req, Response res) {
        String answerId = req.queryParams("answer_id");
        Integer answerIdInt;
        JsonResponse jsonResponse = new JsonResponse();

        ArrayList<Answer> answers = new ArrayList<Answer>();

        if (answerId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
        
        try {
           answerIdInt = Integer.parseInt(answerId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        Optional<Answer> answer = getAnswerService().getAnswerById(answerIdInt);

        if (!answer.isPresent()) {
            jsonResponse.setStatus("AnswerNotFoundError");
            return jsonResponse.toJson();
        }

        answers.add(answer.get());

        jsonResponse.setObject(answers);

        return jsonResponse.setStatus("Success").toJson();
    }

    // Describes all answers associated with the task indicated by task_id.
    // If no answers are found, returns status: AnswerNotFoundError.
    String describeTaskAnswers(Request req, Response res) {
        String taskId = req.queryParams("task_id");
        Integer taskIdInt;
        JsonResponse jsonResponse = new JsonResponse();

        ArrayList<Answer> answers = new ArrayList<Answer>();

        if (taskId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
        
        try {
           taskIdInt = Integer.parseInt(taskId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        answers = (ArrayList<Answer>) getAnswerService().getAnswersByTask(taskIdInt);

        if (answers.isEmpty()) {
            jsonResponse.setStatus("AnswerNotFoundError");
            return jsonResponse.toJson();
        }

        jsonResponse.setObject(answers);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Describes all answers associated with the SubUser indicated by subuser_id.
    // If no answers are found, returns status: AnswerNotFoundError.
    String describeSubUserAnswers(Request req, Response res) {
        String subUserId = req.queryParams("subuser_id");
        Integer subUserIdInt;
        JsonResponse jsonResponse = new JsonResponse();
        
        if (subUserId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
        
        try {
           subUserIdInt = Integer.parseInt(subUserId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        ArrayList<Answer> answers = new ArrayList<Answer>();
        answers = (ArrayList<Answer>) getAnswerService().getAnswersBySubUser(subUserIdInt);
        
        if (answers.isEmpty()) {
            jsonResponse.setStatus("AnswerNotFoundError");
            return jsonResponse.toJson();
        }
        
        jsonResponse.setObject(answers);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    public AnswerService getAnswerService() {
        return answerService;
    }
}
