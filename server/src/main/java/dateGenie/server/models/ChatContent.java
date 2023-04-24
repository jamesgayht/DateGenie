package dateGenie.server.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class ChatContent {
    private String role;
    private String content;

    public JsonObject toJson() {
        return Json.createObjectBuilder()
            .add("role", role)
            .add("content", content)
            .build();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
