package dateGenie.server.models;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.JsonObject;

public class User {
    private String username; 
    private String email; 
    private String password;

    public static User createUserFromSql(SqlRowSet rs) {
		User user = new User(); 
		user.setUsername(rs.getString("username"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		return user;
	}
    
    public static User createUserFromClientPayload(JsonObject doc) {
        User user = new User(); 
        
        user.setUsername(doc.getString("username"));
        user.setEmail(doc.getString("email"));
        user.setPassword(doc.getString("password"));

        return user; 
    }    
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    } 
}
