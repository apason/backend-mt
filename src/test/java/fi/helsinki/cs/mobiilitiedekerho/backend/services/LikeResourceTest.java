// Test Commented out, because it is obligatory to pass a working appConfiguration for testing + S3 stuff.
// TODO: Fix tests?
//
// package fi.helsinki.cs.mobiilitiedekerho.backend.services;
// 
// import fi.helsinki.cs.mobiilitiedekerho.backend.models.*;
// 
// import spark.Request;
// import spark.Response;
// 
// import static junit.framework.Assert.assertTrue;
// import junit.framework.Test;
// import junit.framework.TestCase;
// import junit.framework.TestSuite;
// 
// import java.util.Optional;
// import java.util.Date;
// 
// import com.typesafe.config.ConfigFactory;
// import com.typesafe.config.Config;
// 
// import static org.mockito.Mockito.*;
// 
// public class LikeResourceTest extends TestCase {
//     
//     private UserService userService;
//     private LikeService likeService;
//     private AnswerService answerService;
//     private Config appConfiguration;
//     private LikeResource likeResource;
//     
//     private Request req;
//     private Response res;
//     
// 
//     public LikeResourceTest(String testName)
//     {
//         super(testName);
//     }
// 
//     public static Test suite()
//     {
//         return new TestSuite(LikeResourceTest.class);
//     }
//     
//     protected void setUp() {
//         likeService = mock(LikeService.class);
//         userService = mock(UserService.class);
//         answerService = mock(AnswerService.class);
//         appConfiguration = ConfigFactory.load();
//         
//         req = mock(Request.class);
//         res = mock(Response.class);       
// 
//         Like like = new Like();
//         like.setId(1);
//         like.setSubUserId(1);
//         like.setAnswer_id(1);
//         like.setCreated(new Date(0));
//         
//         //when(likeService.getLikeById(1)).thenReturn(Optional.of(like));
//         //when(likeService.getLikeById(2)).thenReturn(Optional.empty());
//         
//         likeResource = new LikeResource(likeService, userService, answerService, appConfiguration);
//     }
// 
//     public void testDescribeLikeSuccess() {         
//         when(req.queryParams("like_id")).thenReturn("1"); 
//         
//         String jsonResponse = likeResource.describeLike(req, res);
//         
//         String jsonExpected = "";
//                 
//         assertEquals(jsonResponse, jsonExpected);
//     }
// 
//     public void testDescribeLikeNotFound() {         
//         when(req.queryParams("like_id")).thenReturn("2"); 
//         
//         String jsonResponse = likeResource.describeLike(req, res);
//         
//         String jsonExpected = "{\"status\":\"LikeNotFoundError\"}";
//         
//         assertEquals(jsonResponse, jsonExpected);
//     }
// }
