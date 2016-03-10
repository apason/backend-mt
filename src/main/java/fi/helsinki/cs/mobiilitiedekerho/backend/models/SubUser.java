package fi.helsinki.cs.mobiilitiedekerho.backend.models;

import java.util.Date;


public class SubUser {

    private int id;
    private int user_id;
    private String nick;
    //private boolean enabled; //Or is Subuser eliminated directly?
    private Date create_time;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

//     public boolean isEnabled() {
//         return enabled;
//     }
// 
//     public void setEnabled(boolean enabled) {
//         this.enabled = enabled;
//     }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }
}
