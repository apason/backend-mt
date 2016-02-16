package fi.helsinki.cs.mobiilitiedekerho.backend.services;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.Category;

import org.sql2o.*;



public class CategoryService {
    
    private final Sql2o sql2o;

    
    public CategoryService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
}