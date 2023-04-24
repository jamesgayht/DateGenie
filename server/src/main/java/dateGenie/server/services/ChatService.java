package dateGenie.server.services;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import dateGenie.server.models.ChatContent;
import dateGenie.server.models.ChatMessages;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

@Service
public class ChatService {

    public static final String URL_CHAT = "https://api.openai.com/v1/chat/completions";

    /*
     * required json {
     * "model": "gpt-3.5-turbo",
     * "messages": [{
     * "role": "author of this message",
     * "content": "contents of the message",
     * "name": "name of the author" (optional)
     * }]
     * }
     */

    @Value("${CHAT_API_KEY}")
    private String apiKey;

    public String postUserInput(String message) {
        
        String bearer = "Bearer %s".formatted(apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", bearer);

        List<ChatContent> content = new LinkedList<>(); 
        ChatContent chatContentSystem = new ChatContent(); 
        ChatContent chatContentUser = new ChatContent(); 
        ChatMessages chatMessages = new ChatMessages(); 

        chatContentSystem.setRole("system");
        chatContentSystem.setContent("You are a itinerary and date planner in Singapore, start the response with Here is a suggested and keep activities to within the same district while keeping answers concise.");
        
        chatContentUser.setRole("user");
        chatContentUser.setContent(message);

        content.add(chatContentSystem);
        content.add(chatContentUser);
        chatMessages.setChatContents(content);
   
        JsonObject reqBody = chatMessages.toJson();

        RestTemplate template = new RestTemplate();

        System.out.println("req body >>> " + reqBody);
        
        HttpEntity<String> request = new HttpEntity<>(reqBody.toString(), headers);
        ResponseEntity<String> response = template.postForEntity(URL_CHAT, request, String.class);
    
        String payload = response.getBody();

        JsonReader reader = Json.createReader(new StringReader(payload));
        JsonObject result = reader.readObject();
        JsonArray choices = result.getJsonArray("choices");

        JsonObject choicesObj = choices.getJsonObject(0);
        JsonObject responseMessage = choicesObj.getJsonObject("message");
        String responseContent = responseMessage.getString("content");

        if(responseContent.trim() == "") responseContent = "No itinerary available"; 

        return responseContent;

    }
}
