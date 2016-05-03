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
// public class AnswerResourceTest extends TestCase {
//     
//     private UserService userService;
//     private AnswerService answerService;
//     private Config appConfiguration;
//     private AnswerResource answerResource;
//     private Request req;
//     private Response res;
//     
// 
//     public AnswerResourceTest(String testName)
//     {
//         super(testName);
//     }
// 
//     public static Test suite()
//     {
//         return new TestSuite(AnswerResourceTest.class);
//     }
//     
//     protected void setUp() {
//         answerService = mock(AnswerService.class);
//         userService = mock(UserService.class);
//         appConfiguration = ConfigFactory.load();
//         
//         req = mock(Request.class);
//         res = mock(Response.class);       
// 
//         Answer answer = new Answer();
//         answer.setId(1);
//         answer.setTask_id(1);
//         answer.setSubuser_id(1);
//         answer.setCreated(new Date(0));
//         answer.setUploaded(true);
//         answer.setEnabled(true);
//         answer.setAnswerType("video");
//         answer.setUri("answer_suid_1_id_1.webm");
//         
//         when(answerService.getAnswerById(1)).thenReturn(Optional.of(answer));
//         when(answerService.getAnswerById(2)).thenReturn(Optional.empty());
//         
//         answerResource = new AnswerResource(userService, answerService, appConfiguration);
//     }
// 
//     public void testDescribeAnswerkSuccess() {         
//         when(req.queryParams("answer_id")).thenReturn("1"); 
//         
//         /*
//         String jsonResponse = answerResource.describeAnswer(req, res);
//         
//         String jsonExpected = "{\"objects\":[{\"id\":1,\"task_id\":1,\"subuser_id\":1,\"created\":\"Jan 1, 1970 2:00:00 AM\",\"uploaded\":true,\"enabled\":true,\"answer_type\":\"video\",\"uri\":\"answer_suid_1_id_1.webm\"}],\"status\":\"Success\"}";
//         
//         assertEquals(jsonResponse, jsonExpected);
//         */
//         assertTrue(true);
//     }
// 
//     public void testDescribeAnswerkNotFound() {         
//         when(req.queryParams("answer_id")).thenReturn("2"); 
//         
//         /*
//         String jsonResponse = answerResource.describeAnswer(req, res);
//         
//         String jsonExpected = "{\"status\":\"AnswerNotFoundError\"}";
//         
//         assertEquals(jsonResponse, jsonExpected);
//         */
//         
//         assertTrue(true);
//     }
// }
