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

public class UserResourceTest extends TestCase {
    
    private UserService userService;
    private UserService userService;
    private UserResource userResource;
    
    private Request req;
    private Response res;
    

    public UserResourceTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(UserResourceTest.class);
    }
    
    protected void setUp() {
        taskService = mock(TaskService.class);
        userService = mock(UserService.class);
        
        req = mock(Request.class);
        res = mock(Response.class);       

        User user = new User();
        user.setId(1);
        user.setEmail("test@testistania.test");
        user.setPassword("passu");
        user.setEnabled(true);
        user.setCreate_time(new Date(0));
        
        when(userService.getTaskById(1)).thenReturn(Optional.of(user));
        when(userService.getTaskById(2)).thenReturn(Optional.empty());
        
        userResource = new UserResource(userService, userService);
    }

    public void testDescribeUserSuccess() {         
        when(req.queryParams("user_id")).thenReturn("1"); 
        
        String jsonResponse = userResource.describeUser(req, res);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"email\":\"test@testistania.test\",\"password\"\":passu\",\"enabled\":true,"
                + "\"create_time\":\"Jan 1, 1970 2:00:00 AM\"}],\"status\":\"Success\"}";
        assertEquals(jsonResponse, jsonExpected);
        
        jsonResponse = userResource.describeUser(req, res);
    }

    public void testDescribeUserNotFound() {         
        when(req.queryParams("user_id")).thenReturn("2"); 
        
        String jsonResponse = userResource.describeUser(req, res);
        
        String jsonExpected = "{\"status\":\"UserNotFoundError\"}";
        assertEquals(jsonResponse, jsonExpected);
        
        jsonResponse = userResource.describeUser(req, res);
    }
}
