package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Task;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;

import fi.helsinki.cs.mobiilitiedekerho.backend.services.UserService;

import java.util.Date;

import java.text.SimpleDateFormat;

import org.sql2o.*;

import java.util.List;
import java.util.Optional;

import static java.lang.System.out;

public class AnswerService {

    private final Sql2o sql2o;
    private UserService userService;

    public AnswerService(Sql2o sql2o, UserService userService) {
	this.sql2o = sql2o;
	this.userService = userService;
    }

    // Returns an answer from the database.
    // If the answer is found, returns Optional<Answer> with the answer object.
    // Otherwise returns an empty Optional<Answer>.
    public Optional<Answer> getAnswerById(int answerId) {
	String sql =
	    "SELECT *" +
	    "FROM answer " +
	    "WHERE id = :id";

	try(Connection con = sql2o.open()) {
	    List<Answer> answers = con.createQuery(sql)
		.throwOnMappingFailure(false)
		.addParameter("id", answerId)
		.executeAndFetch(Answer.class);
            
            if (answers.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(answers.get(0));
            }
	}
    }

    //checks if the answer with given id exists and is enabled
    private boolean taskExists(int taskId){
	String sql =
	    "SELECT * FROM task " +
	    "WHERE id = :task_id";

	try(Connection con = sql2o.open()){
	    List<Task> task = con.createQuery(sql)
		.throwOnMappingFailure(false)
		.addParameter("task_id", taskId)
		.executeAndFetch(Task.class);
	    
	    if(!task.isEmpty() &&
	       task.get(0).isEnabled()) return true;
	    else                        return false;
	}
	catch(Exception e){
	    return false;
	}
    }

    //checks if the answer is owned by given user
    private boolean answerExists(int answerId, User user){

	try(Connection con = sql2o.open()){

	    String sql =
		"SELECT * " +
		"FROM answer "+
		"WHERE id = :aid";
	    
	    List<Answer> answer = con.createQuery(sql)
		.throwOnMappingFailure(false)
		.addParameter("aid", answerId)
		.executeAndFetch(Answer.class);

	    if(answer.size() != 1)
		return false;

	    return userService.requireSubUser(user, answer.get(0).getSubuser_id()) != null;
	}
	catch(Exception e){
	    return false;
	}
    }
    
    // handles StartAnswerUpload api-call.
    public Optional<Answer> setInitialAnswer(Subuser subUser, Integer taskId, String fileType){
	Date date = new Date();

	List<Answer> answer;

	//first we need to check that the task exists and is enabled
	if(!taskExists(taskId))
	    return Optional.empty();

	int newAnswerId = -1;

	//create new disabled answer instance to be uploaded
	String sql =
	    "INSERT INTO answer " +
	    "(task_id, subuser_id, created) " +
	    "VALUES " +
	    "(:task_id, :subuser_id, NOW())";

	try(Connection con = sql2o.open()) {
	
	    newAnswerId  = con.createQuery(sql, true)
		.addParameter("task_id", taskId)
		.addParameter("subuser_id", subUser.getId())
		.executeUpdate()
		.getKey(Integer.class);
	}
        
        // Generate URI for the answer and save it to database.
        String uri = "answer_suid_" + Integer.toString(subUser.getId()) +
                "_id_" + newAnswerId + "." + fileType;
	
        sql = "UPDATE answer SET uri = :uri WHERE id = :id";
        
        try(Connection con = sql2o.open()){
	    con.createQuery(sql)
		.addParameter("uri", uri)
                .addParameter("id", newAnswerId)
		.executeUpdate();
	}
        
        // Get the newly created answer and return it.
	sql =
	    "SELECT * " +
	    "FROM answer " +
	    "WHERE id = :id";

	try(Connection con = sql2o.open()){
	    answer = con.createQuery(sql)
		.throwOnMappingFailure(false)
		.addParameter("id", newAnswerId)
		.executeAndFetch(Answer.class);
	}

        if (answer.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(answer.get(0));
        }
    }

    //this method is used when user calls EndAnswerUpload
    //to inform the end of his upload.
    public String enableAnswer(int answerId, User user){
	if(!answerExists(answerId, user))
	   return "InvalidPermissions";
	else{
	    String sql =
		"UPDATE answer SET " +
		"loaded = NOW(), " +
		"enabled = true " +
		"WHERE id = :id";
		
	    try(Connection con = sql2o.open()){
		con.createQuery(sql)
		.addParameter("id", answerId)
		.executeUpdate();

		return "Success";
	    }
	    catch(Exception e){
		return "DatabaseError";
	    }
	}
    }

    // Deletes an answer from the database.
    public String deleteAnswer(int answerId, User user){
	if(!answerExists(answerId, user))
	    return "InvalidPermissions";
	else{
	    String sql =
		"DELETE FROM answer " +
		"WHERE id = :id";

	    try(Connection con = sql2o.open()){		
		con.createQuery(sql)
		    .addParameter("id", answerId)
		    .executeUpdate();

		return "Success";
	    }
	    catch(Exception e){
		return "DatabaseError";
	    }
	}
    }
    
    // Returns a list of answers to a task.
    public List<Answer> getAnswersByTask(int taskId) {
        String sql
                = "SELECT *"
                + "FROM answer "
                + "WHERE task_id = :task_id";

        try (Connection con = sql2o.open()) {
            List<Answer> answers = con.createQuery(sql)
                    .addParameter("task_id", taskId)
                    .executeAndFetch(Answer.class);
            return answers;
        }        
    }
    
    // Returns a list of the answers of the SubUser as a parameter.
    public List<Answer> getAnswersBySubUser(int subUserId) {
        String sql
                = "SELECT *"
                + "FROM answer "
                + "WHERE subuser_id = :subuser_id";

        try (Connection con = sql2o.open()) {
            List<Answer> answers = con.createQuery(sql)
                    .addParameter("subuser_id", subUserId)
                    .executeAndFetch(Answer.class);
            return answers;
        }        
    }
    
}
