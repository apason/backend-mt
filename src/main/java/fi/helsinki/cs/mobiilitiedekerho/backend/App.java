package fi.helsinki.cs.mobiilitiedekerho.backend;

import fi.helsinki.cs.mobiilitiedekerho.backend.services.*;

import org.sql2o.Sql2o;

public class App
{
    public static void main( String[] args )
    {
      Sql2o sql2o = new Sql2o(
              DbConfiguration.DB_JDBC_URL,
              DbConfiguration.DB_USERNAME,
              DbConfiguration.DB_PASSWORD);
      
      TaskResource taskResource = new TaskResource(new TaskService(sql2o));
      
      AnswerResource answerResource = new AnswerResource(new AnswerService(sql2o));
      
      UserResource userResource = new UserResource(new UserService(sql2o));
    }
}
