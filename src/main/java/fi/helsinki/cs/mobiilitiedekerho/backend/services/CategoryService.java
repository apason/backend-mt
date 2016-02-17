package fi.helsinki.cs.mobiilitiedekerho.backend.services;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.Category;

import org.sql2o.*;

import java.util.List;
import java.util.Optional;


public class CategoryService {
    
    private final Sql2o sql2o;

    
    public CategoryService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }
    
    
    // Returns a category from the database by category_id.
    // If the category is found, returns Optional<Category> with the category object.
    // Otherwise, returns an empty Optional<Caetgory>
    public Optional<Category> getCategoryById(int categoryId) {
        String sql
                = "SELECT *"
                + "FROM category "
                + "WHERE id = :id";

        try (Connection con = sql2o.open()) {
            List<Category> categories = con.createQuery(sql)
                    .addParameter("id", categoryId)
                    .executeAndFetch(Category.class);
            if (categories.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(categories.get(0));
            }
        }
    }
    
    // Saves the category to the database.
    public void saveCategory(Category category) {
        String sql
                = "INSERT INTO category(iconName, iconAnimatedName, BGName, name, loaded)"
                + "VALUES (:iconName, :iconAnimatedName, :BGName, :name, :loaded)";

        try (Connection con = sql2o.open()) {
            con.createQuery(sql).bind(category).executeUpdate();
        }
    }
    
    //TODO: Add tasks to the category's list of tasks'.

    // Lists all categories from the dabase.
    public List<Category> getAllcategories() {
        String sql
                = "SELECT *"
                + "FROM category";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql).executeAndFetch(Category.class);
        }
    }
    
}