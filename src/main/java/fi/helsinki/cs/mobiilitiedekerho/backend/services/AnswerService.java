package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;

import org.sql2o.*;

import java.util.List;

public class AnswerService {

  private final Sql2o sql2o;

  public AnswerService(Sql2o sql2o) {
    this.sql2o = sql2o;
  }

  public List<Answer> getAnswerById(int answer_id) {
    String sql =
    "SELECT *" +
    "FROM answer " +
    "WHERE id = :id";

    try(Connection con = sql2o.open()) {
      List<Answer> answers = con.createQuery(sql)
          .addParameter("id", answer_id)
          .executeAndFetch(Answer.class);
      return answers;
    }
  }
}
