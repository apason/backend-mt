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

public class AnswerResource {

    private final AnswerService answerService;
    private final UserService userService;

    public AnswerResource(AnswerService answerService, UserService userService) {
        this.answerService = answerService;
        this.userService = userService;

        setContentType();

        defineRoutes();
    }

    private void setContentType() {
        Spark.after((req, res) -> {
            res.type("application/json");
        });
    }

    private void defineRoutes() {
        Spark.get("/DescribeAnswer", (req, res) -> {
            return this.describeAnswer(req, res);
        });
        Spark.get("/StartAnswerUpload", (req, res) -> {
            return this.startAnswerUpload(req, res);
        });
    }

    private String startAnswerUpload(Request req, Response res) {
        String userHash = req.queryParams("user_hash");
        String taskId = req.queryParams("task_id");
        User user = userService.authenticateUserByHash(userHash);

        JsonResponse jsonResponse = new JsonResponse();

        if (user == null || taskId == null) {
            jsonResponse.setStatus("ParameterErrorOrInvalidUserHash");
            return jsonResponse.toJson();
        }

        List<Answer> answer = answerService.setInitialAnswer(user.getId(), Integer.parseInt(taskId));

        if (answer == null || answer.isEmpty()) {
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

        List<Answer> answers = answerService.getAnswerById(Integer.parseInt(answerId));

        if (answers.isEmpty()) {
            jsonResponse.setStatus("AnswerNotFoundError");
            return jsonResponse.toJson();
        }

        jsonResponse.setObject(answers.get(0));

        return jsonResponse.setStatus("Success").toJson();
    }
}
