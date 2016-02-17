package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;

import org.sql2o.*;

import java.util.List;
import java.util.Optional;

public class TaskService {

    private final Sql2o sql2o;

    public TaskService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    // Returns a task from the database by task_id.
    // If the task is found, returns Optional<Task> with the task object.
    // Otherwise, returns an empty Optional<Task>
    public Optional<Task> getTaskById(int taskId) {
        String sql
                = "SELECT *"
                + "FROM task "
                + "WHERE id = :id";

        try (Connection con = sql2o.open()) {
            List<Task> tasks = con.createQuery(sql)
                    .addParameter("id", taskId)
                    .executeAndFetch(Task.class);
            if (tasks.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(tasks.get(0));
            }
        }
    }
    
    // Saves the task to the database.
    public void saveTask(Task task) {
        String sql
                = "INSERT INTO task(uri, loaded) "
                + "VALUES (:uri, :loaded)";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(task).executeUpdate();
        }
    }

    // Lists all tasks from the dabase.
    public List<Task> getAllTasks() {
        String sql
                = "SELECT *"
                + "FROM task";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Task.class);
        }
    }
}
