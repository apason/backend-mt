package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.ArrayList;
import java.util.Optional;

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
        Spark.get("/StartAnswerUpload", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
            return this.startAnswerUpload(req, res, u);
        });
        Spark.get("/EndAnswerUpload", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
            return this.endAnswerUpload(req, res, u);
        });
    }
    
    // Starts answer upload.
    // Creates the answer in the database.
    // Returns the new answer id and an URI for the client to upload to.
    String startAnswerUpload(Request req, Response res, User user) {
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

        Optional<Answer> answer = getAnswerService().setInitialAnswer(user.getId(), taskIdInt);

        if (!answer.isPresent()) {
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
            jsonResponse.setStatus("InvalidAnswerId");
            return jsonResponse.toJson();
        }

        if (uploadStatus.equals("success")) {
            getAnswerService().enableAnswer(answerId, user.getId());
            jsonResponse.setStatus("Success");
        } else if (uploadStatus.equals("failure")) {
            getAnswerService().deleteAnswer(answerId, user.getId());
            jsonResponse.setStatus("Success");
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
           answerIdInt = Integer.parseInt(answersId);
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

    public AnswerService getAnswerService() {
        return answerService;
    }
}
