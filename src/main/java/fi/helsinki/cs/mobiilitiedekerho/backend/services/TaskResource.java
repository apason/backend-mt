package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.List;

public class TaskResource extends Resource {

    private final TaskService taskService;

    public TaskResource(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;

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

        List<Task> tasks = taskService.getTaskById(Integer.parseInt(taskId));

        if (tasks.isEmpty()) {
            jsonResponse.setStatus("TaskNotFoundError");
            return jsonResponse.toJson();
        }

        jsonResponse.setObject(tasks.get(0));

        return jsonResponse.setStatus("Success").toJson();
    }
}
