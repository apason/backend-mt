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
        Spark.get("/GetEULA", (req, res) -> {
            requireAnonymousUser(req, res);
            return getEULA();
        
        });

        Spark.get("/GetInstructions", (req, res) -> {
            requireAnonymousUser(req, res);
            return getInstructions();
        });

        Spark.get("/GetCategoryMenuBG", (req, res) -> {
            requireAnonymousUser(req, res);
            return getCategoryMenuBG();
        });
    }


    private String getEULA() {
        JsonResponse jsonResponse = new JsonResponse();

        String sql = "SELECT info_text FROM info WHERE id=:id";

        try(Connection con = sql2o.open()){
            List<String> eula = con.createQuery(sql)
            .addParameter("id", "eula")
            .executeAndFetch(String.class);

            return jsonResponse.addPropery("eula", eula.get(0))
                .setStatus("Success")
                .toJson();
        } catch (Exception e) {
            return jsonResponse.setStatus("InfoNotFound").toJson();
        }
    }

    private String getInstructions() {
        JsonResponse jsonResponse = new JsonResponse();

        String sql = "SELECT info_text FROM info WHERE id=:id";

        try(Connection con = sql2o.open()){
            List<String> instructions = con.createQuery(sql)
            .addParameter("id", "instructions")
            .executeAndFetch(String.class);

            return jsonResponse.addPropery("instructions", instructions.get(0))
                .setStatus("Success")
                .toJson();
        } catch (Exception e) {
            return jsonResponse.setStatus("InfoNotFound").toJson();
        }
    }

    private String getCategoryMenuBG() {
        JsonResponse jsonResponse = new JsonResponse();

        String sql = "SELECT info_text FROM info WHERE id=:id";

        try(Connection con = sql2o.open()){
            List<String> BG_uri = con.createQuery(sql)
            .addParameter("id", "category_menu_bg_uri")
            .executeAndFetch(String.class);

            return jsonResponse.addPropery("category_menu_bg_uri", this.getS3Helper().generateSignedDownloadUrl(this.getAppConfiguration().getString("app.graphics_bucket"), BG_uri.get(0)))
                .setStatus("Success")
                .toJson();
        } catch (Exception e) {
            return jsonResponse.setStatus("InfoNotFound").toJson();
        }
    }
}
