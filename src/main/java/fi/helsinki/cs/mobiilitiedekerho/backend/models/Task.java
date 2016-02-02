package fi.helsinki.cs.mobiilitiedekerho.backend.models;
import java.util.Date;

public class Task {
  private int id;
  private String uri;
  private Date loaded;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Date getLoaded() {
        return loaded;
    }

    public void setLoaded(Date loaded) {
        this.loaded = loaded;
    }
}
