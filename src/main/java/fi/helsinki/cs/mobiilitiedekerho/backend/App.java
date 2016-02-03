package fi.helsinki.cs.mobiilitiedekerho.backend;

import fi.helsinki.cs.mobiilitiedekerho.backend.services.*;

import org.sql2o.Sql2o;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class App {

    public static void main(String[] args) {
        Sql2o sql2o = new Sql2o(App.configureHikariConnectionPool());

        TaskResource taskResource = new TaskResource(new TaskService(sql2o));

        AnswerResource answerResource = new AnswerResource(new AnswerService(sql2o));

        UserResource userResource = new UserResource(new UserService(sql2o));
    }

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
}
