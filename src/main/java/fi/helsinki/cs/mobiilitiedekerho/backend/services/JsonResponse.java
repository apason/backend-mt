package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonResponse {

    private String status;
    
    private Object object;
    
    private JsonElement jsonElement;
    
    public JsonResponse() {
        this.object = null;
        this.status = "";
        this.jsonElement = new JsonObject();
    }
    
    public JsonResponse(Object object) {
        this.object = object;
        this.status = "";
    }
    
    public JsonResponse setStatus(String status) {
        this.status = status;
        return this;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public JsonResponse setObject(Object object) {
        this.object = object;
        return this;
    }
    
    public Object getObject() {
        return this.object;
    }
    
    public JsonResponse addPropery(String propery, String value) {
        this.jsonElement.getAsJsonObject().addProperty(propery, value);
        return(this);
    }
    
    public String toJson() {
        Gson gson = new Gson();
        this.jsonElement.getAsJsonObject().addProperty("status", this.status);
        if (this.object != null) {
            this.jsonElement = gson.toJsonTree(this.object);
        }
        return gson.toJson(this.jsonElement);
    }
}
