package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.*;

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
    
    private UserService userService;
    private TaskService taskService;
    private TaskResource taskResource;
    
    private Request req;
    private Response res;
    

    public TaskResourceTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(TaskResourceTest.class);
    }
    
    protected void setUp() {
        taskService = mock(TaskService.class);
        userService = mock(UserService.class);
        
        req = mock(Request.class);
        res = mock(Response.class);       

        Task task = new Task();
        task.setId(1);
        task.setLoaded(new Date(0));
        task.setInfo("tehtävä");
        task.setCategory_id(1);
        
        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));
        when(taskService.getTaskById(2)).thenReturn(Optional.empty());
        
        taskResource = new TaskResource(userService, taskService);
    }

    public void testDescribeTaskSuccess() {         
        when(req.queryParams("task_id")).thenReturn("1"); 
        
        String jsonResponse = taskResource.describeTask(req, res);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"loaded\":\"Jan 1, 1970 2:00:00 AM\",\"enabled\":false,\"info\":\"tehtävä\",\"category_id\":1}],\"status\":\"Success\"}";
        assertEquals(jsonResponse, jsonExpected);
        
        jsonResponse = taskResource.describeTask(req, res);
    }

    public void testDescribeTaskNotFound() {         
        when(req.queryParams("task_id")).thenReturn("2"); 
        
        String jsonResponse = taskResource.describeTask(req, res);
        
        String jsonExpected = "{\"status\":\"TaskNotFoundError\"}";
        assertEquals(jsonResponse, jsonExpected);
        
        jsonResponse = taskResource.describeTask(req, res);
    }
}
