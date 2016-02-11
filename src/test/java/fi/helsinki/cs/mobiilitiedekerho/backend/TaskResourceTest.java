package fi.helsinki.cs.mobiilitiedekerho.backend;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.*;
import fi.helsinki.cs.mobiilitiedekerho.backend.services.TaskResource;
import fi.helsinki.cs.mobiilitiedekerho.backend.services.TaskService;
import fi.helsinki.cs.mobiilitiedekerho.backend.services.UserService;

import spark.Request;
import spark.Response;

import static junit.framework.Assert.assertTrue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Optional;
import java.util.Date;

import static org.mockito.Mockito.*;

public class TaskResourceTest extends TestCase {

    public TaskResourceTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(TaskResourceTest.class);
    }

    public void testDescribeTask()
    {
        TaskService taskService = mock(TaskService.class);
        UserService userService = mock(UserService.class);
        
        Request req = mock(Request.class);
        Response res = mock(Response.class);
        
        when(req.queryParams("task_id")).thenReturn("1");
        
        Task task = new Task();
        task.setId(1);
        task.setUri("uri.mp4");
        task.setLoaded(new Date(0));
        
        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));
        
        TaskResource taskResource = new TaskResource(userService, taskService);
        
        String jsonResponse = taskResource.describeTask(req, res);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"uri\":\"uri.mp4\","
                + "\"loaded\":\"Jan 1, 1970 2:00:00 AM\"}],\"status\":\"Success\"}";
        assertEquals(jsonResponse, jsonExpected);
    }    
}
