package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Task {
    
    private int id;
    private String uri;
    private Date loaded;
    private boolean enabled;
    private String info;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getLoaded() {
        return loaded;
    }

    public void setLoaded(Date loaded) {
        this.loaded = loaded;
    }

    public void enable(){
	enabled = true;
    }

    public void disable(){
	enabled = false;
    }

    public boolean isEnabled(){
	return enabled;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    public String getInfo() {
        return info;
    }
}
