package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class Task {


    private int id;
    private int category_id;
    private Date created;
    private boolean uploaded;
    private boolean enabled;
    private String name;
    private String info;
    private int coordinate_x;
    private int coordinate_y;
    private String uri;
    private String icon_uri;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    
    public int getCordinateX() {
        return coordinate_x;
    }

    public void setCordinateX(int cordinate_x) {
        this.coordinate_x = cordinate_x;
    }
    
    public int getCordinateY() {
        return coordinate_y;
    }

    public void setCordinateY(int cordinate_y) {
        this.coordinate_y = cordinate_y;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getIcon_uri() {
        return icon_uri;
    }

    public void setIcon_uri(String icon_uri) {
        this.icon_uri = icon_uri;
    }
}
