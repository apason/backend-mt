package fi.helsinki.cs.mobiilitiedekerho.backend;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.*;
import spark.Spark;
import spark.Response;
import spark.Request;

public class App
{
    public static void main( String[] args )
    {
      defineRoutes();
    }

    private static void defineRoutes() {
      Spark.get("/DescribeTask", (req, res) -> DescribeTask(req, res));
    }

    private static String DescribeTask(Request req, Response res) {
      String task_id = req.queryParams("task_id");

      if (task_id == null) {
        return("Error: Task not found.");
      }

      return "task_id: " + task_id;
    }
}
