package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Answer;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;

import spark.Spark;
import spark.Response;
import spark.Request;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

import static java.lang.System.out;

import com.typesafe.config.Config;

public class AnswerResource extends Resource {

    private final AnswerService answerService;


    public AnswerResource(UserService userService, AnswerService answerService, Config appConfiguration) {
        super(userService, appConfiguration);
        this.answerService = answerService;

        defineRoutes();
    }

    // Defines routes for AnswerResource.
    private void defineRoutes() {
        Spark.get("/DescribeAnswer", (req, res) -> {
            requireAnonymousUser(req, res);
            return this.describeAnswer(req, res);
        });
        
        Spark.get("/DescribeTaskAnswers", (req, res) -> {
            requireAnonymousUser(req, res);
            return this.describeTaskAnswers(req, res);
        });
        
        Spark.get("/DescribeSubUserAnswers", (req, res) -> {
            requireAnonymousUser(req, res);
            return this.describeSubUserAnswers(req, res);
        });
        
        Spark.get("/StartAnswerUpload", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
            Subuser subUser = requireSubUser(req, res, u);
            return this.startAnswerUpload(req, res, subUser);
        });
        
        Spark.get("/EndAnswerUpload", (req, res) -> {
            User u = requireAuthenticatedUser(req, res);
            return this.endAnswerUpload(req, res, u);
        });
    }

    // Describes an answer indicated by answer_id.
    // If the answer is not found, returns status: AnswerNotFoundError.
    String describeAnswer(Request req, Response res) {
        String answerId = req.queryParams("answer_id");
        Integer answerIdInt;
        JsonResponse jsonResponse = new JsonResponse();

        ArrayList<Answer> answers = new ArrayList<Answer>();

        if (answerId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
        
        try {
           answerIdInt = Integer.parseInt(answerId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        Optional<Answer> answer = answerService.getAnswerById(answerIdInt);

        if (!answer.isPresent()) {
            jsonResponse.setStatus("AnswerNotFoundError");
            return jsonResponse.toJson();
        }

        
        //Check if asker has the privileges to get the answers.
        int priLv = getUserService().getSubUserPrivacyLevel(answer.get().getSubuser_id());
        
        if (priLv == -1)
        	return jsonResponse.setStatus("SubuserNotFoundError").toJson(); //TODO: ???
        else if (priLv == 2) { //At least authenticated user is needed.
            // Check user-type because privacy-level.
            String authToken = req.queryParams("auth_token");
            String userType = getUserType(authToken);
            if (!userType.equals("authenticated"))
                return jsonResponse.setStatus("InsufficientPrivileges").toJson();
        }
        else if (priLv == 1) { //Only to itself.
            // First Checks user-type before getting user, avoid spark.halt.
            String authToken = req.queryParams("auth_token");
            String userType = getUserType(authToken);
            if (!userType.equals("authenticated"))
                return jsonResponse.setStatus("InsufficientPrivileges").toJson();
            else {
                //Only to itself. Subuser must be a subuser of the calling user.
                User user = requireAuthenticatedUser(req, res);
                if (getUserService().requireSubUser(user, answer.get().getSubuser_id()) == null)
                    return jsonResponse.setStatus("InsufficientPrivileges").toJson();
            }
        }
        // END
        
        
        answers.add(modifyUriToSignedDownloadUrl(answer.get()));

        jsonResponse.setObject(answers);

        return jsonResponse.setStatus("Success").toJson();
    }

    // Describes all answers associated with the task indicated by task_id.
    // If no answers are found, returns status: AnswerNotFoundError.
    String describeTaskAnswers(Request req, Response res) {
        String taskId = req.queryParams("task_id");
        Integer taskIdInt;
        JsonResponse jsonResponse = new JsonResponse();

        ArrayList<Answer> answers = new ArrayList<Answer>();

        if (taskId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
        
        try {
           taskIdInt = Integer.parseInt(taskId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        answers = (ArrayList<Answer>) answerService.getAnswersByTask(taskIdInt);

        if (answers.isEmpty()) {
            jsonResponse.setStatus("AnswerNotFoundError");
            return jsonResponse.toJson();
        }
        
        //Removes the answers that the user has no right to see. //TODO: Or should it tell them as inaccessible, bah "no good" for that.
        Iterator<Answer> iterator = answers.iterator();
        while (iterator.hasNext()) {
            Answer answer = iterator.next();
            //Check if asker has the privileges to get the answer. Remove if hasn't.
            int priLv = getUserService().getSubUserPrivacyLevel(answer.getSubuser_id());
            
            if (priLv == -1)
            	iterator.remove(); //TODO: ???
            else if (priLv == 2) { //At least authenticated user is needed.
                // Check user-type because privacy-level.
                String authToken = req.queryParams("auth_token");
                String userType = getUserType(authToken);
                if (!userType.equals("authenticated"))
                	iterator.remove();
            }
            else if (priLv == 1) { //Only to itself.
                // First Checks user-type before getting user, avoid spark.halt.
                String authToken = req.queryParams("auth_token");
                String userType = getUserType(authToken);
                if (!userType.equals("authenticated"))
                    iterator.remove();
                else {
                    //Only to itself. Subuser must be a subuser of the calling user.
                    User user = requireAuthenticatedUser(req, res);
                    if (getUserService().requireSubUser(user, answer.getSubuser_id()) == null)
                        iterator.remove();
                }
            }
        }
        
        for (Answer a: answers) {
            a = modifyUriToSignedDownloadUrl(a);
        }

        jsonResponse.setObject(answers);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Describes all answers associated with the SubUser indicated by subuser_id.
    // If no answers are found, returns status: AnswerNotFoundError.
    String describeSubUserAnswers(Request req, Response res) {
        String subUserId = req.queryParams("subuser_id");
        Integer subUserIdInt;
        
        JsonResponse jsonResponse = new JsonResponse();
        
        if (subUserId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }
        
        try {
           subUserIdInt = Integer.parseInt(subUserId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }
        
        //Check if asker has the privileges to get the answers.
        int priLv = getUserService().getSubUserPrivacyLevel(subUserIdInt);
        
        if (priLv == -1)
        	return jsonResponse.setStatus("SubuserNotFoundError").toJson();
        else if (priLv == 2) { //At least authenticated user is needed.
            // Check user-type because privacy-level.
            String authToken = req.queryParams("auth_token");
            String userType = getUserType(authToken);
            if (!userType.equals("authenticated"))
            	return jsonResponse.setStatus("InsufficientPrivileges").toJson();
        }
        else if (priLv == 1) { //Only to itself.
            // First Checks user-type before getting user, avoid spark.halt.
            String authToken = req.queryParams("auth_token");
            String userType = getUserType(authToken);
            if (!userType.equals("authenticated"))
                return jsonResponse.setStatus("InsufficientPrivileges").toJson();
            else {
                //Only to itself. Subuser must be a subuser of the calling user.
                User user = requireAuthenticatedUser(req, res);
                if (getUserService().requireSubUser(user, subUserIdInt) == null)
                    return jsonResponse.setStatus("InsufficientPrivileges").toJson();
            }
        }
        // END
        
        ArrayList<Answer> answers = new ArrayList<Answer>();
        answers = (ArrayList<Answer>) answerService.getAnswersBySubUser(subUserIdInt);
        
        if (answers.isEmpty()) {
            jsonResponse.setStatus("AnswerNotFoundError");
            return jsonResponse.toJson();
        }
        
        for (Answer a: answers) {
            a = modifyUriToSignedDownloadUrl(a);
        }
        
        jsonResponse.setObject(answers);

        return jsonResponse.setStatus("Success").toJson();
    }
    
    // Starts answer upload.
    // Creates the answer in the database.
    // Returns the new answer id and an URI for the client to upload to.
    String startAnswerUpload(Request req, Response res, Subuser subUser) {
        String taskId = req.queryParams("task_id");
        String fileType = req.queryParams("file_type");
        int taskIdInt;

        JsonResponse jsonResponse = new JsonResponse();

        if (taskId == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
	}

        if (fileType == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
	}
        
        if ( !( fileType.equals("mp4") ||
                fileType.equals("webm") ||
                fileType.equals("mkv") ||
                fileType.equals("jpeg") ||
                fileType.equals("jpg") ||
                fileType.equals("png")
                )) {
            jsonResponse.setStatus("FileTypeError");
            return jsonResponse.toJson();
        }
        
        
        try {
           taskIdInt = Integer.parseInt(taskId);
        } catch (Exception e) {
            return jsonResponse.setStatus("ParameterError").toJson();
        }

        Optional<Answer> answer = answerService.setInitialAnswer(subUser, taskIdInt, fileType);

        if (!answer.isPresent()) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        return jsonResponse
	    .addPropery("task_id", taskId)
	    .addPropery("answer_id", "" + answer.get().getId())
	    .addPropery("answer_uri", this.getS3Helper().generateSignedUploadUrl(
                    this.getAppConfiguration().getString("app.answer_bucket"),
                    answer.get().getUri()))
	    .setStatus("Success")
	    .toJson();
    }
    
    // Ends the answer upload.
    // If upload was successful, updates the database.
    // If upload failed, removes the row from the database.
    String endAnswerUpload(Request req, Response res, User user) {
        String uploadStatus = req.queryParams("upload_status");
        String answerIdString = req.queryParams("answer_id");
        Integer answerId;

        JsonResponse jsonResponse = new JsonResponse();

        if (uploadStatus == null || answerIdString == null) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        try {
            answerId = Integer.parseInt(answerIdString);
        } catch (Exception e) {
            jsonResponse.setStatus("ParameterError");
            return jsonResponse.toJson();
        }

        if (uploadStatus.equals("success")) {
            jsonResponse
                .setStatus(answerService
                            .enableAnswer(answerId, user));
            
        } else if (uploadStatus.equals("failure")) {
            jsonResponse
                .setStatus(answerService
                            .deleteAnswer(answerId, user));
        } else {
            jsonResponse.setStatus("InvalidStatus");
        }

        return jsonResponse.toJson();

    }
    
    // Generate signed url for answer uri.
    Answer modifyUriToSignedDownloadUrl(Answer a) {
        String uri = this.getS3Helper().generateSignedDownloadUrl(
                this.getAppConfiguration().getString("app.answer_bucket"),
                a.getUri()
        );

        a.setUri(uri);

        return a;
    }
}
