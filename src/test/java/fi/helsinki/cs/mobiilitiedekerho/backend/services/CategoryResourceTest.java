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

public class CategoryResourceTest extends TestCase {
    
    private UserService userService;
    private CategoryService categoryService;
    private CategoryResource categoryResource;
    
    private Request req;
    private Response res;
    

    public CategoryResourceTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(CategoryResourceTest.class);
    }
    
    protected void setUp() {
        taskService = mock(TaskService.class);
        userService = mock(UserService.class);
        
        req = mock(Request.class);
        res = mock(Response.class);       

        Category category = new Category();
        category.setId(1);
        category.setName("testia");
        category.setLoaded(new Date(0));
        
        when(categoryService.getTaskById(1)).thenReturn(Optional.of(category));
        when(categoryService.getTaskById(2)).thenReturn(Optional.empty());
        
        categoryResource = new CategoryResource(userService, categoryService);
    }

    public void testDescribeCategorySuccess() {         
        when(req.queryParams("category_id")).thenReturn("1"); 
        
        String jsonResponse = categoryResource.describeCategory(req, res);
        
        String jsonExpected = "{\"objects\":[{\"id\":1,\"name\":\"testia\","
                + "\"loaded\":\"Jan 1, 1970 2:00:00 AM\"}],\"status\":\"Success\"}";
        assertEquals(jsonResponse, jsonExpected);
        
        jsonResponse = categoryResource.describeCategory(req, res);
    }

    public void testDescribeCategoryNotFound() {         
        when(req.queryParams("category_id")).thenReturn("2"); 
        
        String jsonResponse = categoryResource.describeCategory(req, res);
        
        String jsonExpected = "{\"status\":\"CategoryNotFoundError\"}";
        assertEquals(jsonResponse, jsonExpected);
        
        jsonResponse = categoryResource.describeCategory(req, res);
    }
}
