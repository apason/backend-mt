package fi.helsinki.cs.mobiilitiedekerho.backend.services;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

// JsonResponse class is used the construct JSON responses.
// The response always has the field status.
// Fields can be set with addProperty(field_name, field_value) method.
public class JsonResponse {

    private String status;

    private Object object;

    private ArrayList<String> properties;

    private ArrayList<String> values;

    public JsonResponse() {
        this.object = null;
        this.status = "";
        this.properties = new ArrayList<String>();
        this.values = new ArrayList<String>();
    }

    public JsonResponse(Object object) {
        this.object = object;
        this.status = "";
        this.properties = new ArrayList<String>();
        this.values = new ArrayList<String>();
    }
    
    // Sets the status of this response.
    public JsonResponse setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getStatus() {
        return this.status;
    }

    // Sets the object of this response.
    // This will be returned in the objects field in the generated JSON.
    public JsonResponse setObject(Object object) {
        this.object = object;
        return this;
    }

    public Object getObject() {
        return this.object;
    }

    // Adds a field to the JSON response.
    public JsonResponse addPropery(String property, String value) {
        this.properties.add(property);
        this.values.add(value);
        return this;
    }

    // Generates the JSON response.
    public String toJson() {
        Gson gson = new Gson();
        JsonObject jsonElement = new JsonObject();
        if (this.object != null) {
            jsonElement.add("objects", gson.toJsonTree(this.object));
        }
        for (int i = 0; i < this.properties.size(); i++) {
            jsonElement.getAsJsonObject().addProperty(this.properties.get(i), this.values.get(i));
        }
        jsonElement.getAsJsonObject().addProperty("status", this.status);
        return gson.toJson(jsonElement);
    }
}
