package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Category {

    private int id;
    private String name;
    private Date loaded;
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    public Date getLoaded() {
        return loaded;
    }

    public void setLoaded(Date loaded) {
        this.loaded = loaded;
    }
}
