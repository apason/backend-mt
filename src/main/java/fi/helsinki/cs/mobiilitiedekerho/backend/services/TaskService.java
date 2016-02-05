package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;

import org.sql2o.*;

import java.util.List;

public class TaskService {

    private final Sql2o sql2o;

    public TaskService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public Task getTaskById(int taskId) {
        String sql
                = "SELECT *"
                + "FROM task "
                + "WHERE id = :id";

        try (Connection con = sql2o.open()) {
            List<Task> tasks = con.createQuery(sql)
                    .addParameter("id", taskId)
                    .executeAndFetch(Task.class);
            if (tasks.isEmpty()) {
                return null;
            } else {
                return tasks.get(0);
            }
        }
    }

    public void saveTask(Task task) {
        String sql
                = "INSERT INTO task(uri, loaded) "
                + "VALUES (:uri, :loaded)";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(task).executeUpdate();
        }
    }

    public List<Task> getAllTasks() {
        String sql
                = "SELECT *"
                + "FROM task";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Task.class);
        }
    }
}
