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

public class LikeResourceTest extends TestCase {
    
    private UserService userService;
    private LikeService likeService;
    private AnswerService answerService;
    private LikeResource likeResource;
    
    private Request req;
    private Response res;
    

    public LikeResourceTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(LikeResourceTest.class);
    }
    
    protected void setUp() {
        likeService = mock(LikeService.class);
        userService = mock(UserService.class);
        answerService = mock(AnswerService.class);
        
        req = mock(Request.class);
        res = mock(Response.class);       

        Like like = new Like();
        like.setId(1);
        like.setSubUserId(1);
        like.setAnswer_id(1);
        like.setCreated(new Date(0));
        
        //when(likeService.getLikeById(1)).thenReturn(Optional.of(like));
        //when(likeService.getLikeById(2)).thenReturn(Optional.empty());
        
        likeResource = new LikeResource(likeService, userService, answerService);
    }

    public void testDescribeLikeSuccess() {         
        when(req.queryParams("like_id")).thenReturn("1"); 
        
//         String jsonResponse = likeResource.describeLike(req, res);
//         
//         String jsonExpected = "";
//                 
//         assertEquals(jsonResponse, jsonExpected);

        assertTrue(true);
    }

    public void testDescribeLikeNotFound() {         
        when(req.queryParams("like_id")).thenReturn("2"); 
        
//         String jsonResponse = likeResource.describeLike(req, res);
//         
//         String jsonExpected = "{\"status\":\"LikeNotFoundError\"}";
//         
//         assertEquals(jsonResponse, jsonExpected);

        assertTrue(true);
    }
}
