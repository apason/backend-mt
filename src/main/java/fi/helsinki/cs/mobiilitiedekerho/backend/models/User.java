package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class User {

    private int id;
    private Date created;
    private boolean enabled;
    private String email;
    private String password;
    private String pin;
    private int privacy_level; // 0, not setted, DEFAULT (which means: ???); 1, only to itself; 2 only to authenticated users; 2, everyone. TODO: Use Enum?

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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
    
    public int getPrivacyLevel() {
        return privacy_level;
    }

    public void setPrivacyLevel(int privacyLevel) {
        this.privacy_level = privacyLevel;
    }
    
}
