import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {
    
    @Override
    public User authenticate(String username, String password) throws DAOException {
        System.out.println("AUTHENTICATE: Trying " + username);
        
        // Method 1: Direct SQL comparison
        String sql = "SELECT * FROM users WHERE username = ? AND password = ? AND is_active = TRUE";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("✓ Authentication successful for " + username);
                    User user = extractUserFromResultSet(rs);
                    
                    // Update last login
                    updateLastLogin(user.getUserId(), conn);
                    
                    return user;
                }
            }
            
            System.out.println("✗ No user found with those credentials");
            return null;
            
        } catch (SQLException e) {
            System.err.println("Authentication error for " + username + ": " + e.getMessage());
            throw new DAOException("Error authenticating user", e);
        }
    }
    
    private void updateLastLogin(int userId, Connection conn) throws SQLException {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE user_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.executeUpdate();
            System.out.println("✓ Updated last login for user ID: " + userId);
        }
    }
    
    @Override
    public User getUserById(int userId) throws DAOException {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractUserFromResultSet(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("Error getting user by ID", e);
        }
    }
    
    @Override
    public List<User> getAllUsers() throws DAOException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY created_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                users.add(extractUserFromResultSet(rs));
            }
            return users;
        } catch (SQLException e) {
            throw new DAOException("Error getting all users", e);
        }
    }
    
    @Override
    public boolean addUser(User user) throws DAOException {
        String sql = "INSERT INTO users (username, password, role, full_name, email) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getFullName());
            pstmt.setString(5, user.getEmail());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("Error adding user", e);
        }
    }
    
    @Override
    public boolean updateUser(User user) throws DAOException {
        String sql = "UPDATE users SET username = ?, role = ?, full_name = ?, email = ?, is_active = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getRole());
            pstmt.setString(3, user.getFullName());
            pstmt.setString(4, user.getEmail());
            pstmt.setBoolean(5, user.isActive());
            pstmt.setInt(6, user.getUserId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("Error updating user", e);
        }
    }
    
    @Override
    public boolean deleteUser(int userId) throws DAOException {
        String sql = "UPDATE users SET is_active = FALSE WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("Error deleting user", e);
        }
    }
    
    @Override
    public void logActivity(int userId, String action, String details) throws DAOException {
        String sql = "INSERT INTO activity_logs (user_id, action, details) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, userId);
            pstmt.setString(2, action);
            pstmt.setString(3, details);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error logging activity", e);
        }
    }
    
    @Override
    public List<ActivityLog> getActivityLogs(int limit) throws DAOException {
        List<ActivityLog> logs = new ArrayList<>();
        String sql = "SELECT al.*, u.full_name as user_name FROM activity_logs al " +
                    "LEFT JOIN users u ON al.user_id = u.user_id " +
                    "ORDER BY al.created_at DESC LIMIT ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, limit);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ActivityLog log = new ActivityLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setUserId(rs.getInt("user_id"));
                    log.setAction(rs.getString("action"));
                    log.setDetails(rs.getString("details"));
                    log.setIpAddress(rs.getString("ip_address"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));
                    log.setUserName(rs.getString("user_name"));
                    logs.add(log);
                }
            }
            return logs;
        } catch (SQLException e) {
            throw new DAOException("Error getting activity logs", e);
        }
    }
    
    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setLastLogin(rs.getTimestamp("last_login"));
        user.setActive(rs.getBoolean("is_active"));
        return user;
    }
}