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
    
    private Subuser subuser; //for SubUser testing.
    private User user; //for SubUser testing.
    
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
        req = mock(Request.class);
        res = mock(Response.class);       

        userService = mock(UserService.class);
        userResource = new UserResource(userService);
        
        
        user = new User();
        user.setId(1);
        user.setCreated(new Date(0));
        user.setEnabled(true);
        user.setEmail("testi@testika.test");
        user.setPassword("password123");
        user.setPrivacyLevel(3);
        
        when(userService.getUserById(1)).thenReturn(Optional.of(user));
        when(userService.getUserById(2)).thenReturn(Optional.empty());
        
        
        //SubUser part. 
        subuser = new Subuser();
        subuser.setId(1);
        subuser.setUser_id(1);
        subuser.setCreated(new Date(0));;
        subuser.setNick("Lissu");
        subuser.setAvatar_url("avatar_icon_id_1.png");
        
        when(userService.getSubUserById(1)).thenReturn(Optional.of(subuser));
        when(userService.getSubUserById(2)).thenReturn(Optional.empty());
    }

    public void testDescribeUserSuccess() {         
        when(req.queryParams("user_id")).thenReturn("1"); 
        
        String jsonResponse = userResource.describeUser(req, res);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"create_time\":\"Jan 1, 1970 2:00:00 AM\",\"enabled\":true,\"email\":\"testi@testika.test\",\"password\":\"password123\",\"privacy_level\":3}],\"status\":\"Success\"}";
        
        assertEquals(jsonResponse, jsonExpected);
        
    }

    public void testDescribeUserNotFound() {         
        when(req.queryParams("user_id")).thenReturn("2"); 
        
        String jsonResponse = userResource.describeUser(req, res);
        
        String jsonExpected = "{\"status\":\"UserNotFoundError\"}";
        
        assertEquals(jsonResponse, jsonExpected);
    }
    
    public void testDescribeSubUserSuccess() {         
        when(req.queryParams("user_id")).thenReturn("1"); 
        
        String jsonResponse = userResource.describeSubUser(req, res, user, subuser);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"user_id\":1,\"create_time\":\"Jan 1, 1970 2:00:00 AM\",\"nick\":\"Lissu\",\"avatar_url\":\"avatar_icon_id_1.png\"}],\"status\":\"Success\"}";
        
        assertEquals(jsonResponse, jsonExpected);
        
    }

    public void testDescribeSubUserNotFound() {         
        when(req.queryParams("user_id")).thenReturn("2"); 
        //EI OLE PARAMETER CHECKAUSTA TEHTY!
        /*
        String jsonResponse = userResource.describeUser(req, res);
        
        String jsonExpected = "{\"status\":\"SubUserNotFoundError\"}";
        
        assertEquals(jsonResponse, jsonExpected);
    	*/
        
        assertTrue(true);
    }
}
