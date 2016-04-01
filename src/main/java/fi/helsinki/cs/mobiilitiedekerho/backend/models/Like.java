package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Like {

    private int id;
    private int subuser_id;
    private int liked_id;
    private Date loaded;
    private String likeType; //E.G "task" for a task-like and "answer" for a answer-like. If only those two then a boolean would be enought (though there may be more).

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public int getSubUserId() {
        return subuser_id;
    }

    public void setSubUserId(int subuser_id) {
        this.subuser_id = subuser_id;
    }
    

    public int getLiked_id() {
        return liked_id;
    }

    public void setLike_id(int liked_id) {
        this.liked_id = liked_id;
    }
    
    
    public Date getLoaded() {
        return loaded;
    }

    public void setLoaded(Date loaded) {
        this.loaded = loaded;
    }
    
    
    public String getlikeType() {
        return likeType;
    }

    public void setlikeType(String likeType) {
        this.likeType = likeType;
    }

}
