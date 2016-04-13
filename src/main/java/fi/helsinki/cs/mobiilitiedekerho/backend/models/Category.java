package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Category {

    private int id;
    private Date created;
    private boolean uploaded;
    private boolean enabled;
    private String name;
    private int cordinate_x;
    private int cordinate_y;
    private String bg_uri;
    private String icon_uri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getCordinateX() {
        return cordinate_x;
    }

    public void setCordinateX(int cordinate_x) {
        this.cordinate_x = cordinate_x;
    }
    
    public int getCordinateY() {
        return cordinate_y;
    }

    public void setCordinateY(int cordinate_y) {
        this.cordinate_y = cordinate_y;
    }

    public String getBg_uri() {
        return bg_uri;
    }

    public void setBg_uri(String bg_uri) {
        this.bg_uri = bg_uri;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public void setIcon_uri(String icon_uri) {
        this.icon_uri = icon_uri;
    }
}
