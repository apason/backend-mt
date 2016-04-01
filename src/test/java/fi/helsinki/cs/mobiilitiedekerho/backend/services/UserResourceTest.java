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
        userService = mock(UserService.class);
        
        req = mock(Request.class);
        res = mock(Response.class);       

        User user = new User();
        user.setId(1);
        user.setCreated(new Date(0));
        user.setEnabled(true);
        user.setEmail("testi@testika.test");
        user.setPassword("password123");
        
        
        // We mock userService, which sets password to an empty string
        // before returning the object.
        user.setPassword("");
        
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        when(userService.getUserById(2)).thenReturn(Optional.empty());
        
        userResource = new UserResource(userService);
    }

    public void testDescribeUserSuccess() {         
        when(req.queryParams("user_id")).thenReturn("1"); 
        
        String jsonResponse = userResource.describeUser(req, res);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"create_time\":\"Jan 1, 1970 2:00:00 AM\",\"enabled\":true,\"email\":\"testi@testika.test\",\"password\":\"password123\"}],\"status\":\"Success\"}";
        
        assertEquals(jsonResponse, jsonExpected);
        
    }

    public void testDescribeUserNotFound() {         
        when(req.queryParams("user_id")).thenReturn("2"); 
        
        String jsonResponse = userResource.describeUser(req, res);
        
        String jsonExpected = "{\"status\":\"UserNotFoundError\"}";
        
        assertEquals(jsonResponse, jsonExpected);
    }
}
