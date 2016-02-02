package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;

import spark.Spark;
import spark.Response;
import spark.Request;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.List;
import java.lang.Integer;

public class AnswerResource {
  private final AnswerService answerService;

  public AnswerResource(AnswerService answerService) {
    this.answerService = answerService;
    
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
  }
  
  private String describeAnswer(Request req, Response res) {
      String answer_id = req.queryParams("answer_id");
      JsonResponse jsonResponse = new JsonResponse();
      
      if (answer_id == null) {
          jsonResponse.setStatus("ParameterError");
          return jsonResponse.toJson();
      }

      List<Answer> answers = answerService.getAnswerById(Integer.parseInt(answer_id));
      
      if (answers.isEmpty()) {
          jsonResponse.setStatus("AnswerNotFoundError");
          return jsonResponse.toJson();
      }
      
      jsonResponse.setObject(answers.get(0));
      
      return jsonResponse.setStatus("Success").toJson();
  }
}
