package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class TaskResource extends Resource {

    private final TaskService taskService;

    public TaskResource(UserService userService, TaskService taskService) {
        super(userService);
        this.taskService = taskService;
        
        defineRoutes();
    }

    // Defines routes for TaskResource.
    private void defineRoutes() {
        Spark.get("/DescribeTask", (req, res) -> {
            requireAuthenticatedUser(req, res);
            return describeTask(req, res);
        });
    }

    // Describes the task indicated by task_id.
    // If the task is not found, returns status: TaskNotFoundError.
    String describeTask(Request req, Response res) {
        String taskId = req.queryParams("task_id");
        JsonResponse jsonResponse = new JsonResponse();

	ArrayList<Task> tasks = new ArrayList<Task>();

        if (taskId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        Optional<Task> task = getTaskService().getTaskById(Integer.parseInt(taskId));

        if (!task.isPresent()) {
            jsonResponse.setStatus("TaskNotFoundError");
            return jsonResponse.toJson();
        }

	tasks.add(task.get());
	
        jsonResponse.setObject(tasks);

        return jsonResponse.setStatus("Success").toJson();
    }

    public TaskService getTaskService() {
        return taskService;
    }
}
