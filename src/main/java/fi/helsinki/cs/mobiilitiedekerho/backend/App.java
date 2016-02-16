package fi.helsinki.cs.mobiilitiedekerho.backend;

import fi.helsinki.cs.mobiilitiedekerho.backend.services.*;

import org.sql2o.Sql2o;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import spark.Spark;
import java.security.Key;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class App {

    // Backend initialization.
    public static void main(String[] args) {
        // Sql2o object for the services
        Sql2o sql2o = new Sql2o(App.configureHikariConnectionPool());
        
        // Generates a secret key for JSON Web Token signatures.
        Key key = MacProvider.generateKey();

        SparkConfiguration();
                
        TaskService taskService = new TaskService(sql2o);
        AnswerService answerService = new AnswerService(sql2o);
        UserService userService = new UserService(sql2o, key);

        TaskResource taskResource = new TaskResource(userService, taskService);
        AnswerResource answerResource = new AnswerResource(userService, answerService);
        UserResource userResource = new UserResource(userService);

    }

    // Configures Hikari Connection Pool.
    private static HikariDataSource configureHikariConnectionPool() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(DbConfiguration.DB_JDBC_URL);
        config.setUsername(DbConfiguration.DB_USERNAME);
        config.setPassword(DbConfiguration.DB_PASSWORD);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        return new HikariDataSource(config);
    }
    
    // Sets response type to application/json.
    private static void SparkConfiguration() {
        Spark.before((req, res) -> {
            res.type("application/json");
        });
    }
}
