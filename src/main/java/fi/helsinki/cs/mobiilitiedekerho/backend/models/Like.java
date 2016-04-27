package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Like {

    private int id;
    private int subuser_id;
    private int answer_id;
    private Date created;
    
    
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
    

    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }
    
    
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
    
}
