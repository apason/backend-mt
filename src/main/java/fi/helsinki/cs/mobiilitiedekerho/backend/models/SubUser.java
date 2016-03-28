package fi.helsinki.cs.mobiilitiedekerho.backend.models;

public class Subuser {

    private int id;
    private String nick;
    private int user_id;
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNick(){
	return this.nick;
    }

    public void setNick(String nick){
	this.nick = nick;
    }

    public int getUserId(){
	return user_id;
    }

    public void setUserId(int user_id){
	this.user_id = user_id;
    }
}
