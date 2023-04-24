package dateGenie.server.models;

import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class ChatMessages {
    private List<ChatContent> chatContents = new LinkedList<>();

    public JsonObject toJson() {
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for(ChatContent c: chatContents) {
            arrayBuilder.add(c.toJson()); 
        }

        return Json.createObjectBuilder()
            .add("model", "gpt-3.5-turbo")
            .add("messages", arrayBuilder)
            .build();
    }

    public List<ChatContent> getChatContents() {
        return chatContents;
    }

    public void setChatContents(List<ChatContent> chatContents) {
        this.chatContents = chatContents;
    } 
}
