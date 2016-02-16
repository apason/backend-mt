package fi.helsinki.cs.mobiilitiedekerho.backend.services;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.Category;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.ArrayList;
import java.util.Optional;


public class CategoryResource extends Resource {

    private final CategoryService categoryService;
    
    
    public AnswerResource(UserService userService, CategoryService categoryService) {
        super(userService);
        this.categoryService = categoryService;

        defineRoutes();
    }

    // Defines routes for CategoryResource.
    private void defineRoutes() {
        Spark.get("/DescribeCategory", (req, res) -> {
            requireAnonymousUser(req, res);
            return this.describeCategory(req, res);
    }
    
    // Describes an category indicated by caetgory_id.
    // If the category is not found, returns status: CategoryNotFoundError.
    private String describeCategory {
    
        String categoryId = req.queryParams("category_id");
        JsonResponse jsonResponse = new JsonResponse();

        ArrayList<Category> categories = new ArrayList<Category>();

        if (categoryId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
        
        try {
            taskIdInt = Integer.parseInt(categoryId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        

        Optional<Category> category = getCategoryService().getCategoryById(Integer.parseInt(categoryId));

        if (!category.isPresent()) {
            jsonResponse.setStatus("CategoryNotFoundError");
            return jsonResponse.toJson();
        }

        categories.add(category.get());

        jsonResponse.setObject(categories);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    
    public CategoryService getCategoryService() {
        return categoryService;
    }
    
}