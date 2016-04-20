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

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import static org.mockito.Mockito.*;

public class TaskResourceTest extends TestCase {
    
    private UserService userService;
    private TaskService taskService;
    private TaskResource taskResource;
    private Config appConfiguration;
    
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
        appConfiguration = ConfigFactory.load();
        
        req = mock(Request.class);
        res = mock(Response.class);       

        Task task = new Task();
        task.setId(1);
        task.setCategory_id(1);
        task.setCreated(new Date(0));
        task.setUploaded(true);
        task.setEnabled(true);
        task.setName("Jään sulaminen");
        task.setInfo("Tehtävässä tutkitaan, kuinka jää sulaa.");
        task.setUri("task_id_1.webm");
        task.setIcon_uri("task_icon_id_1.png");
        
        when(taskService.getTaskById(1)).thenReturn(Optional.of(task));
        when(taskService.getTaskById(2)).thenReturn(Optional.empty());
        
        taskResource = new TaskResource(userService, taskService, appConfiguration);
    }

    public void testDescribeTaskSuccess() {         
        when(req.queryParams("task_id")).thenReturn("1"); 
        
        String jsonResponse = taskResource.describeTask(req, res);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"category_id\":1,\"created\":\"Jan 1, 1970 2:00:00 AM\",\"uploaded\":true,\"enabled\":true,\"name\":\"Jään sulaminen\",\"info\":\"Tehtävässä tutkitaan, kuinka jää sulaa.\",\"uri\":\"task_id_1.webm\",\"icon_uri\":\"task_icon_id_1.png\"}],\"status\":\"Success\"}";
        
        assertEquals(jsonResponse, jsonExpected);
    }

    public void testDescribeTaskNotFound() {         
        when(req.queryParams("task_id")).thenReturn("2"); 
        
        String jsonResponse = taskResource.describeTask(req, res);
        
        String jsonExpected = "{\"status\":\"TaskNotFoundError\"}";
        
        assertEquals(jsonResponse, jsonExpected);
    }
}
