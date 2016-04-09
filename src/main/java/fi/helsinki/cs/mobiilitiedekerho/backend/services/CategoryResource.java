package fi.helsinki.cs.mobiilitiedekerho.backend.services;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.Category;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

public class CategoryResource extends Resource {

    private final CategoryService categoryService;
    
    
    public CategoryResource(UserService userService, CategoryService categoryService) {
        super(userService);
        this.categoryService = categoryService;

        defineRoutes();
    }

    // Defines routes for CategoryResource.
    private void defineRoutes() {
        Spark.get("/DescribeCategory", (req, res) -> {
	    requireAnonymousUser(req, res);
            return this.describeCategory(req, res);
        });
	Spark.get("/DescribeCategories", (req, res) -> {
	    requireAnonymousUser(req, res);
	    return this.DescribeCategories(req, res);
	});
    }
    
    // Describes an category indicated by category_id.
    // If the category is not found, returns status: CategoryNotFoundError.
    String describeCategory(Request req, Response res) {
    
        String categoryId = req.queryParams("category_id");
        int categoryIdInt;
        JsonResponse jsonResponse = new JsonResponse();

        ArrayList<Category> categories = new ArrayList<Category>();

        if (categoryId == null) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        try {
            categoryIdInt =  Integer.parseInt(categoryId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        

        Optional<Category> category = categoryService.getCategoryById(categoryIdInt);

        if (!category.isPresent()) {
            jsonResponse.setStatus("CategoryNotFoundError");
            return jsonResponse.toJson();
        }

        categories.add(category.get());

        jsonResponse.setObject(categories);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    String DescribeCategories(Request req, Response res){
        JsonResponse jsonResponse = new JsonResponse();
        List<Category> categories = categoryService.getAllCategories();
        return jsonResponse.setStatus("Success").setObject(categories).toJson();
    }
    
}
