package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Task {
    
    private int id;
    private Date loaded;
    private boolean enabled;
    private String info;
    private int category_id;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    
    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }
    
    public int getCategory_id() {
        return category_id;
    }
}
