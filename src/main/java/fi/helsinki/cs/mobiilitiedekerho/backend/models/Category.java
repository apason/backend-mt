package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Category {

    private int id;
    private String iconUri;
    private String iconAnimatedUri;
    private String BGUri;
    private String name;
    private Date loaded;
    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getIconUri() {
        return iconUri;
    }

    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }
    
    
    public String getIconAnimatedUri() {
        return iconAnimatedUri;
    }

    public void seticonAnimatedUri(String iconAnimatedUri) {
        this.iconAnimatedUri = iconAnimatedUri;
    }
    
    
    public String getBGUri() {
        return BGUri;
    }

    public void setBGUri(String BGUri) {
        this.BGUri = BGUri;
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
