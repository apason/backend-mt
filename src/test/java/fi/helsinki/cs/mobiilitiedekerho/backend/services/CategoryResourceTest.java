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
// public class CategoryResourceTest extends TestCase {
//     
//     private UserService userService;
//     private CategoryService categoryService;
//     private Config appConfiguration;
//     private CategoryResource categoryResource;
//     
//     private Request req;
//     private Response res;
//     
// 
//     public CategoryResourceTest(String testName)
//     {
//         super(testName);
//     }
// 
//     public static Test suite()
//     {
//         return new TestSuite(CategoryResourceTest.class);
//     }
//     
//     protected void setUp() {
//         categoryService = mock(CategoryService.class);
//         userService = mock(UserService.class);
//         appConfiguration = ConfigFactory.load();
//         req = mock(Request.class);
//         res = mock(Response.class);       
// 
//         Category category = new Category();
//         category.setId(1);
//         category.setCreated(new Date(0));
//         category.setUploaded(true);
//         category.setEnabled(true);
//         category.setName("Fysiikka");
//         category.setCordinateX(100);
//         category.setCordinateY(200);
//         category.setBg_uri("category_bg_id_1.png");
//         category.setIcon_uri("category_icon_id_1.png");
//         
//         when(categoryService.getCategoryById(1)).thenReturn(Optional.of(category));
//         when(categoryService.getCategoryById(2)).thenReturn(Optional.empty());
//         
//         categoryResource = new CategoryResource(userService, categoryService, appConfiguration);
//     }
// 
//     public void testDescribeCategorySuccess() {         
//         when(req.queryParams("category_id")).thenReturn("1"); 
//         
//         String jsonResponse = categoryResource.describeCategory(req, res);
//         
//         String jsonExpected = "{\"objects\":[{\"id\":1,\"created\":\"Jan 1, 1970 2:00:00 AM\",\"uploaded\":true,\"enabled\":true,\"name\":\"Fysiikka\",\"coordinate_x\":100,\"coordinate_y\":200,\"bg_uri\":\"category_bg_id_1.png\",\"icon_uri\":\"category_icon_id_1.png\"}],\"status\":\"Success\"}";        
//         
//         assertEquals(jsonResponse, jsonExpected);
//     }
// 
//     public void testDescribeCategoryNotFound() {         
//         when(req.queryParams("category_id")).thenReturn("2"); 
//
//         String jsonResponse = categoryResource.describeCategory(req, res);
//         
//         String jsonExpected = "{\"status\":\"CategoryNotFoundError\"}";
//         
//         assertEquals(jsonResponse, jsonExpected);
//     }
// }
