package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Answer {

    private int id;
    private Date issued;
    private Date loaded;
    private boolean enabled;
    private int task_id;
    private int subuser_id;
    private String uri;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getIssued() {
        return issued;
    }

    public void setIssued(Date issued) {
        this.issued = issued;
    }    

    public Date getLoaded() {
        return loaded;
    }

    public void setLoaded(Date loaded) {
        this.loaded = loaded;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getSubUser_id() {
        return subuser_id;
    }

    public void setSubUser_id(int user_id) {
        this.subuser_id = subuser_id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
