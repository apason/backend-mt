package fi.helsinki.cs.mobiilitiedekerho.backend.models;


import java.util.Date;
import java.util.ArrayList;



public class Category {

    private int id;
    private String iconName;
    private String iconAnimatedName;
    private String BGName;
    private String name;
    private Date loaded;
    private ArrayList<Task> tasks;

    
    
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
    
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    
    public void addTask(Task task) {
        tasks.add(task);
    }
    
    
}
