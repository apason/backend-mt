package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Like;
import org.sql2o.*;

import java.util.List;

public class LikeService {

    private Sql2o sql2o;


    public LikeService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }    

    // Get likes pointing to the given answer indicated by answerId.
    public List<Like> getLikesByAnswer(int answerId){
        String sql
            = "SELECT * FROM slaikka "
            + "WHERE answer_id = :aid";

        try (Connection con = sql2o.open()){
            List<Like> likes = con.createQuery(sql)
                .addParameter("aid", answerId)
                .executeAndFetch(Like.class);

            return likes;
        }
    }

    // Get all the likes to answer made by the SubUser as parameter.
    public List<Like> describeLikesToSubuser(Subuser subUser){
        String sql
            = "SELECT * FROM slaikka "
            + "WHERE answer_id IN "
                + "(SELECT id FROM answer "
                + "WHERE subuser_id = :suid)";

        try(Connection con = sql2o.open()){
            List<Like> likes = con.createQuery(sql)
                .addParameter("suid", subUser.getId())
                .executeAndFetch(Like.class);

            return likes;
        }
    }

    // Get all likes made by a SubUser.
    public List<Like> describeLikesFromSubuser(Subuser subUser){
        String sql
            = "SELECT * FROM slaikka "
            + "WHERE subuser_id = :suid";

        try(Connection con = sql2o.open()){
            List<Like> likes = con.createQuery(sql)
                .addParameter("suid", subUser.getId())
                .executeAndFetch(Like.class);
            
            return likes;
        }
    }


    // Like a answer pointed by answer_id and like by subUser.
    public int likeAnswer(int answerId, Subuser subUser){

        //check for duplicates
        if(likeExists(answerId, subUser))
            return -1;

        //add like
            String sql
            = "INSERT INTO slaikka "
            + "(subuser_id, answer_id, created) "
            + "VALUES "
            + "(:suid, :aid, NOW())";

            try(Connection con = sql2o.open()){
                Integer newLikeId = con.createQuery(sql, true)
                    .addParameter("suid", subUser.getId())
                    .addParameter("aid", answerId)
                    .executeUpdate()
                    .getKey(Integer.class);

                return newLikeId;
            }
    }

    // Check if a like exists to the given answer by the given SubUser.
    private boolean likeExists(int answerId, Subuser subUser){
        String sql
            = "SELECT * FROM slaikka "
            + "WHERE answer_id = :aid "
            + "AND subuser_id = :suid";

        try (Connection con = sql2o.open()){
            List<Like> likes = con.createQuery(sql)
                .addParameter("aid", answerId)
                .addParameter("suid", subUser.getId())
                .executeAndFetch(Like.class);

            return !likes.isEmpty();
        }
    }
}
