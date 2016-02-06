package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.List;
import java.util.Optional;

public class TaskResource extends Resource {

    private final TaskService taskService;

    public TaskResource(UserService userService, TaskService taskService) {
        super(userService);
        this.taskService = taskService;
        
        defineRoutes();
    }

    private void defineRoutes() {
        Spark.get("/DescribeTask", (req, res) -> {
            requireAuthentication(req, res);
            return describeTask(req, res);
        });
    }

    private String describeTask(Request req, Response res) {
        String taskId = req.queryParams("task_id");
        JsonResponse jsonResponse = new JsonResponse();

        if (taskId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        Optional<Task> task = getTaskService().getTaskById(Integer.parseInt(taskId));

        if (!task.isPresent()) {
            jsonResponse.setStatus("TaskNotFoundError");
            return jsonResponse.toJson();
        }

        jsonResponse.setObject(task.get());

        return jsonResponse.setStatus("Success").toJson();
    }

    public TaskService getTaskService() {
        return taskService;
    }
}
