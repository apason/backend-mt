package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;

import java.util.Date;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import org.sql2o.*;

import java.util.List;

public class AnswerService {

    private final Sql2o sql2o;

    public AnswerService(Sql2o sql2o) {
	this.sql2o = sql2o;
    }

    public Answer getAnswerById(int answerId) {
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
                return null;
            } else {
                return answers.get(0);
            }
	}
    }
    
    public Answer setInitialAnswer(Integer userId, Integer taskId){
	Date date = new Date();
	SimpleDateFormat sdf = new SimpleDateFormat("MM+dd+yyyy+h+mm+ss+a");

	List<Answer> answer;

	int addedKey = -1;
	
	String sql =
	    "INSERT INTO answer " +
	    "(issued, enabled, task_id, user_id, uri) " +
	    "VALUES " +
	    "(NOW(), false, :task_id, :user_id, :uri)";

	try(Connection con = sql2o.open()) {
	
	    addedKey  = con.createQuery(sql, true)
		.addParameter("task_id", taskId)
		.addParameter("user_id", userId)
		.addParameter("uri", userId + "+" + taskId + "+" + sdf.format(date) + ".mp4")
		.executeUpdate()
		.getKey(Integer.class);
	}

	sql =
	    "SELECT * " +
	    "FROM answer " +
	    "WHERE id = :id";

	try(Connection con = sql2o.open()){
	    answer = con.createQuery(sql)
		.throwOnMappingFailure(false)
		.addParameter("id", addedKey)
		.executeAndFetch(Answer.class);
	}
	
        if (answer.isEmpty()) {
            return null;
        } else {
            return answer.get(0);
        }
    }

    public String enableAnswer(int answerId, int userId){
	
	String sql =
	    "SELECT * " +
	    "FROM answer "+
	    "WHERE id = :aid " +
	    "AND user_id = :uid";

	try(Connection con = sql2o.open()){
	    List<Answer> answers = con.createQuery(sql)
		.throwOnMappingFailure(false)
		.addParameter("aid", answerId)
		.addParameter("uid", userId)
		.executeAndFetch(Answer.class);

	    if(answers.size() != 1)
		return "InvalidPermissions";
	    else{
		sql =
		    "UPDATE answer SET " +
		    "loaded = NOW(), " +
		    "enabled = true " +
		    "WHERE id = :id";
		
		con.createQuery(sql)
		    .addParameter("id", answerId)
		    .executeUpdate();

		return "Success";
	    }
	}
    }


    public String deleteAnswer(int answerId, int userId){
	String sql =
	    "SELECT * " +
	    "FROM answer "+
	    "WHERE id = :aid" +
	    "AND user_id :uid";

	try(Connection con = sql2o.open()){
	    List<Answer> answers = con.createQuery(sql)
		.addParameter("aid", answerId)
		.addParameter("uid", userId)
		.executeAndFetch(Answer.class);

	    if(answers.size() != 1)
		return "InvalidPermissions";
	    else{
		sql =
		    "DELETE FROM answer " +
		    "WHERE id = :d";

		con.createQuery(sql)
		    .addParameter("id", answerId)
		    .executeUpdate();

		return "Success";
	    }
	}
    }
}

