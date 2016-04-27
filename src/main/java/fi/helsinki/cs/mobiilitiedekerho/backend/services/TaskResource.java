package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import com.typesafe.config.Config;

public class TaskResource extends Resource {

    private final TaskService taskService;


    public TaskResource(UserService userService, TaskService taskService, Config appConfiguration) {
        super(userService, appConfiguration);
        this.taskService = taskService;
        
        defineRoutes();
    }

    // Defines routes for TaskResource.
    private void defineRoutes() {
        Spark.get("/DescribeTask", (req, res) -> {
            requireAnonymousUser(req, res);
            return describeTask(req, res);
        });
        
        Spark.get("/DescribeCategoryTasks", (res, req) -> {
            requireAnonymousUser(res, req);
            return describeGategoryTasks(res, req);
        });
    }

    // Describes the task indicated by task_id.
    // If the task is not found, returns status: TaskNotFoundError.
    String describeTask(Request req, Response res) {
        String taskId = req.queryParams("task_id");
        int taskIdInt;
        JsonResponse jsonResponse = new JsonResponse();
        ArrayList<Task> tasks = new ArrayList<Task>();

        if (taskId == null) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        try {
            taskIdInt = Integer.parseInt(taskId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        

        Optional<Task> task = taskService.getTaskById(taskIdInt);

        if (!task.isPresent()) {
            jsonResponse.setStatus("TaskNotFoundError");
            return jsonResponse.toJson();
        }

        tasks.add(modifyUrisToSignedDownloadUrls(task.get()));

        jsonResponse.setObject(tasks);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Describes tasks of the category indicated by category_id.
    // If no tasks are not found, returns status: TaskNotFoundError.
    String describeGategoryTasks(Request req, Response res) {
        String categoryId = req.queryParams("category_id");
        int categoryIdInt;
        JsonResponse jsonResponse = new JsonResponse();

        if (categoryId == null) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        try {
            categoryIdInt = Integer.parseInt(categoryId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        ArrayList<Task> tasks = (ArrayList<Task>) taskService.getTasksByCategory(categoryIdInt);

        if (tasks.isEmpty()) {
            jsonResponse.setStatus("TaskNotFoundError");
            return jsonResponse.toJson();
        }
        
        for (Task t: tasks) {
            t = modifyUrisToSignedDownloadUrls(t);
        }
	
        jsonResponse.setObject(tasks);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Generate signed urls for task video and icon uri.
    Task modifyUrisToSignedDownloadUrls(Task t) {
        String videoUri = this.getS3Helper().generateSignedDownloadUrl(
                this.getAppConfiguration().getString("app.task_bucket"),
                t.getUri()
        );
        
        String iconUri = this.getS3Helper().generateSignedDownloadUrl(
                this.getAppConfiguration().getString("app.graphics_bucket"),
                t.getIcon_uri()
        );        

        t.setUri(videoUri);
        t.setIcon_uri(iconUri);

        return t;
    }
}
