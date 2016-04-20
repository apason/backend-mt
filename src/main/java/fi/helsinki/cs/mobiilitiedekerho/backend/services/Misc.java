package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import org.sql2o.*;
import spark.Spark;
import spark.Request;
import spark.Response;

import java.util.List;

import com.typesafe.config.Config;

public class Misc extends Resource {

    private final  Sql2o sql2o;

    public Misc(UserService userService, Sql2o sql2o, Config appConfiguration) {
        super(userService, appConfiguration);

        this.sql2o = sql2o;
        defineRoutes();
    }

    private void defineRoutes() {
        Spark.get("/GetBuckets", (req, res) -> {
            requireAnonymousUser(req, res);
            return getBuckets();
        });
        Spark.get("/GetEULA", (req, res) -> {
            requireAnonymousUser(req, res);
            return getEULA();
        
        });
        Spark.get("/GetInstructions", (req, res) -> {
            requireAnonymousUser(req, res);
            return getInstructions();
        });
    }

    String getBuckets() {
        JsonResponse jsonResponse = new JsonResponse();

        String sql;
        List<String> res;

        try(Connection con = sql2o.open()){
        
            sql = "SELECT s3_location FROM info";
            res = con.createQuery(sql).executeAndFetch(String.class);
            jsonResponse.addPropery("s3_location", res.get(0));
            
            sql = "SELECT tasks_bucket FROM info";
            res = con.createQuery(sql).executeAndFetch(String.class);
            jsonResponse.addPropery("tasks_bucket", res.get(0));

            sql = "SELECT answers_bucket FROM info";
            res = con.createQuery(sql).executeAndFetch(String.class);
            jsonResponse.addPropery("answers_bucket", res.get(0));

            sql = "SELECT graphics_bucket FROM info";
            res = con.createQuery(sql).executeAndFetch(String.class);
            jsonResponse.addPropery("graphics_bucket", res.get(0));

            
            return jsonResponse.setStatus("Success").toJson();
        } catch (Exception e) {
            return jsonResponse.setStatus("InfoNotFound").toJson();
        }
    }

    String getEULA() {
        JsonResponse jsonResponse = new JsonResponse();

        String sql = "SELECT eula FROM info ";

        try(Connection con = sql2o.open()){
            List<String> eula = con.createQuery(sql)
            .executeAndFetch(String.class);

            return jsonResponse.addPropery("eula", eula.get(0))
                .setStatus("Success")
                .toJson();
        } catch (Exception e) {
            return jsonResponse.setStatus("InfoNotFound").toJson();
        }
    }
    
    String getInstructions() {
        JsonResponse jsonResponse = new JsonResponse();

        String sql = "SELECT instructions FROM info ";

        try(Connection con = sql2o.open()){
            List<String> instructions = con.createQuery(sql)
            .executeAndFetch(String.class);

            return jsonResponse.addPropery("instructions", instructions.get(0))
                .setStatus("Success")
                .toJson();
        } catch (Exception e) {
            return jsonResponse.setStatus("InfoNotFound").toJson();
        }
    }
    
}
