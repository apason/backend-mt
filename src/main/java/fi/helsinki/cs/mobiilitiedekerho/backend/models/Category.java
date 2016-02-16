package fi.helsinki.cs.mobiilitiedekerho.backend.models;


import java.util.Date;



public class Category {

    private int id;
    private String iconName;
    private String iconAnimatedName;
    private String BGName;
    private Date loaded;

    
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    
    
    public String getIconAnimatedName() {
        return iconAnimatedName;
    }

    public void seticonAnimatedName(String iconAnimatedName) {
        this.iconAnimatedName = iconAnimatedName;
    }
    
    
    public String getBGName() {
        return BGName;
    }

    public void setBGName(String BGName) {
        this.BGName = BGName;
    }

    
    public Date getLoaded() {
        return loaded;
    }

    public void setLoaded(Date loaded) {
        this.loaded = loaded;
    }
    
}
