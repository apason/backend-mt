package fi.helsinki.cs.mobiilitiedekerho.backend.services;


import fi.helsinki.cs.mobiilitiedekerho.backend.models.Category;

import org.sql2o.*;

import java.util.List;
import java.util.ArrayList;
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
    
    // Lists all categories from the database.
    public List<Category> getAllCategories() {
        String sql
                = "SELECT * "
                + "FROM category";
        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                .throwOnMappingFailure(false)
                .executeAndFetch(Category.class);
        }
    }


//     // Saves the category to the database.
//     public void saveCategory(Category category) {
//         String sql
//                 = "INSERT INTO category(iconName, iconAnimatedName, BGName, name, loaded, tasks)"
//                 + "VALUES (:iconName, :iconAnimatedName, :BGName, :name, :loaded, :tasks)";
// 
//         try (Connection con = sql2o.open()) {
//             con.createQuery(sql).bind(category).executeUpdate();
//         }
//     }
//     
//     //Add a task to the category's list of tasks'.
//     public String addTask(int categoryId, Task task) {
//     
//         Connection con = null;
//         Get the tasks' list:
//         ArrayList<Tasks> tasks = null;
//         String sql
//                 = "SELECT tasks"
//                 + "FROM category "
//                 + "WHERE id = :id";
// 
//         try (con = sql2o.open()) {
//             tasks = con.createQuery(sql)
//                 .addParameter("id", categoryId)
//                 .executeAndFetch(Category.class);
//             if (tasks.isEmpty()) return "Cannot find category";
//         }
//     
//     
//         tasks.add(task);
//         String sql
//                 = "UPDATE category"
//                 + "SET tasks = :tasks"
//                 + "WHERE id = :id";
//                 
//         con.createQuery(sql)
//             .addParameter("id", categoryId)
//             .executeUpdate();
// 
//         return "Success";
//     }
    
}
