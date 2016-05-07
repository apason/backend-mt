package fi.helsinki.cs.mobiilitiedekerho.backend;


import fi.helsinki.cs.mobiilitiedekerho.backend.services.*;

import org.sql2o.Sql2o;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import spark.Spark;
import java.security.Key;
import io.jsonwebtoken.impl.crypto.MacProvider;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;


public class App {

    private static Config appConfiguration;

    // Backend initialization.
    public static void main(String[] args) {
        SparkConfiguration();
        
        // Load application-wide confguration from application.conf 
        appConfiguration = ConfigFactory.load();

        // Sql2o object for the services
        Sql2o sql2o = new Sql2o(App.configureHikariConnectionPool());

        // Generates a secret key for JSON Web Token signatures.
        Key key = MacProvider.generateKey();


        /* Creates (starts) all Services and Resources that are part of the server. */

        // Create and starts the services' instances.
        UserService userService = new UserService(sql2o, key);        
        TaskService taskService = new TaskService(sql2o);
        AnswerService answerService = new AnswerService(sql2o, userService);
        CategoryService categoryService = new CategoryService(sql2o);
        LikeService likeService = new LikeService(sql2o);

        // This class is 'both' resource and service. 
        Misc misc = new Misc(userService, sql2o, appConfiguration);

        // Create and starts the resources' instances.
        LikeResource likeResource = new LikeResource(likeService, userService, answerService, appConfiguration);
        TaskResource taskResource = new TaskResource(userService, taskService, appConfiguration);
        AnswerResource answerResource = new AnswerResource(userService, answerService, appConfiguration);
        UserResource userResource = new UserResource(userService, appConfiguration);
        CategoryResource categoryResource = new CategoryResource(userService, categoryService, appConfiguration);
    }

    // Configures Hikari Connection Pool.
    private static HikariDataSource configureHikariConnectionPool() {        
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(appConfiguration.getString("db.jdbc_"));
        hikariConfig.setUsername(appConfiguration.getString("db.username"));
        hikariConfig.setPassword(appConfiguration.getString("db.password"));
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(hikariConfig);
    }

    // Sets response type to application/json.
    // And sets explicitely the charset to utf-8.
    private static void SparkConfiguration() {
        Spark.before((req, res) -> {
            res.type("application/json;charset=utf-8");
        });
    }
}
