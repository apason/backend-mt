package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;

import org.sql2o.*;

import java.util.List;

public class TaskService {

  private final Sql2o sql2o;

  public TaskService(Sql2o sql2o) {
    this.sql2o = sql2o;
  }

  public List<Task> getTaskById(int task_id) {
    String sql =
    "SELECT *" +
    "FROM task " +
    "WHERE id = :id";

    try(Connection con = sql2o.open()) {
      List<Task> tasks = con.createQuery(sql)
          .addParameter("id", task_id)
          .executeAndFetch(Task.class);
      return tasks;
    }
  }
  
  public void saveTask(Task task) {
      try (Connection con = sql2o.open()) {
	  con.createQuery(insertSql)
          .addParameter("id", task_id)
          .executeUpdate();
      }
  }
  
  public List<Task> getAllTasks(int task_id) {
      try(Connection con = sql2o.open()) {
	  return con.createQuery(sql).executeAndFetch(Task.class);
      }
  }
}
