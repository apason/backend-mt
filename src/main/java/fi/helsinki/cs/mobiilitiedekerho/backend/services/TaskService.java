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
    // If the task is found, it returns Optional<Task> with the task object.
    // Otherwise it returns an empty Optional<Task>
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

    // Returns a list of tasks that belongs to a category.
    public List<Task> getTasksByCategory(int categoryId) {
        String sql
                = "SELECT *"
                + "FROM task "
                + "WHERE category_id = :category_id";

        try (Connection con = sql2o.open()) {
            List<Task> tasks = con.createQuery(sql)
                    .addParameter("category_id", categoryId)
                    .executeAndFetch(Task.class);
            return tasks;
        }        
    }

    // Lists all tasks from the database.
    public List<Task> getAllTasks() {
        String sql
                = "SELECT *"
                + "FROM task";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Task.class);
        }
    }


    // Saves the task to the database.
    // TODO: Is this ever needed?
    public void saveTask(Task task) {
        String sql
                = "INSERT INTO task(uri, loaded, enabled, info, category_id) "
                + "VALUES (:uri, :loaded, :enabled, :info, :category_id)";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(task).executeUpdate();
        }
    }

}
