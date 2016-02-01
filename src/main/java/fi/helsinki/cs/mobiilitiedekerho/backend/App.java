package fi.helsinki.cs.mobiilitiedekerho.backend;

import static spark.Spark.*;

public class App
{
    public static void main( String[] args )
    {
      defineRoutes();
    }

    private static void defineRoutes() {
      get("/hello", (req, res) -> "Hello World");
    }
}
