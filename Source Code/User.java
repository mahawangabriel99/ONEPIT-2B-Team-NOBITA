import java.sql.Timestamp;

public class User {
    private int userId;
    private String username;
    private String password;
    private String role;
    private String fullName;
    private String email;
    private Timestamp createdAt;
    private Timestamp lastLogin;
    private boolean isActive;
    
    public User() {}
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    
    public Timestamp getLastLogin() { return lastLogin; }
    public void setLastLogin(Timestamp lastLogin) { this.lastLogin = lastLogin; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    // FIX 1: Change this from throwing exception to returning userId
    public int getId() {
        return this.userId;
    }

    // FIX 2: Add setId method
    public void setId(int id) {
        this.userId = id;
    }

    // FIX 3: Remove the exception from setCreatedBy or implement it properly
    public void setCreatedBy(int userId2) {
        // If you need this method but don't have a createdBy field,
        // you can either remove it or leave it empty
        // this.createdBy = userId2; // If you add a createdBy field
    }
}