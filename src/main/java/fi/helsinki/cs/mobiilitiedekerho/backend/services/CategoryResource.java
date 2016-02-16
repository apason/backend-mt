package fi.helsinki.cs.mobiilitiedekerho.backend.services;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.Category;

import spark.Spark;
import spark.Response;
import spark.Request;



public class CategoryResource extends Resource {

    private final CategoryService categoryService;
    
    
    public AnswerResource(CategoryService categoryService) {
        super(userService);
        this.categoryService = categoryService;

        defineRoutes();
    }

    // Defines routes for CategoryResource.
    private void defineRoutes() {
        
    }
    
}