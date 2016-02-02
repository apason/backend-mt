package fi.helsinki.cs.mobiilitiedekerho.backend;

import fi.helsinki.cs.mobiilitiedekerho.backend.services.*;

import org.sql2o.Sql2o;

public class App
{
    public static void main( String[] args )
    {
      Sql2o sql2o = new Sql2o(
              Configuration.DB_JDBC_URL,
              Configuration.DB_USERNAME,
              Configuration.DB_PASSWORD);
      
      TaskResource taskResource = new TaskResource(new TaskService(sql2o));
      
      AnswerResource answerResource = new AnswerResource(new AnswerService(sql2o));
    }
}
