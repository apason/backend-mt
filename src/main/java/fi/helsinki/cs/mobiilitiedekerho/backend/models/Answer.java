package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Answer {

    private int id;
    private int task_id;
    private int subuser_id;
    private Date created;
    private boolean uploaded;
    private boolean enabled;
    private String type;
    private String uri;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public int getSubuser_id() {
        return subuser_id;
    }

    public void setSubuser_id(int subuser_id) {
        this.subuser_id = subuser_id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
