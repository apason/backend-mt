package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import fi.helsinki.cs.mobiilitiedekerho.backend.models.User;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Subuser;
import fi.helsinki.cs.mobiilitiedekerho.backend.models.Like;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.JwtException;
import java.security.Key;

import org.sql2o.*;

import java.util.List;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Optional;

public class LikeService {

    private Sql2o sql2o;

    
    public LikeService(Sql2o sql2o) {
        this.sql2o = sql2o;
    }    

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

    //TODO: Check for duplicates.
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
