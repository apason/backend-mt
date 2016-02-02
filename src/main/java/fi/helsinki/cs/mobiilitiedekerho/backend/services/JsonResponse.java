package fi.helsinki.cs.mobiilitiedekerho.backend.services;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class JsonResponse {

    private String status;
    
    private Object object;
    
    public JsonResponse() {
        this.object = null;
        this.status = "";
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
    
    public String toJson() {
        Gson gson = new Gson();
        JsonElement jsonElement = new JsonObject();
        if (this.object != null) {
            jsonElement = gson.toJsonTree(this.object);
        }
        jsonElement.getAsJsonObject().addProperty("status", this.status);
        return gson.toJson(jsonElement);
    }
}
