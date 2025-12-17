import java.util.ArrayList;
import java.util.List;

public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAOImpl();
    }
    
    public User login(String username, String password) {
        try {
            User user = userDAO.authenticate(username, password);
            if (user != null) {
                logActivity(user.getUserId(), "Login", "User logged into the system");
            }
            return user;
        } catch (DAOException e) {
            System.err.println("Login error: " + e.getMessage());
            return null;
        }
    }
    
    public void logActivity(int userId, String action, String details) {
        try {
            userDAO.logActivity(userId, action, details);
        } catch (DAOException e) {
            System.err.println("Log activity error: " + e.getMessage());
        }
    }
    
    public List<User> getAllUsers() {
        try {
            return userDAO.getAllUsers();
        } catch (DAOException e) {
            System.err.println("Get all users error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public User getUserById(int userId) {
        try {
            return userDAO.getUserById(userId);
        } catch (DAOException e) {
            System.err.println("Get user by ID error: " + e.getMessage());
            return null;
        }
    }
    
    public List<ActivityLog> getRecentActivities(int limit) {
        try {
            return userDAO.getActivityLogs(limit);
        } catch (DAOException e) {
            System.err.println("Get activities error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public boolean addUser(User user) {
        try {
            return userDAO.addUser(user);
        } catch (DAOException e) {
            System.err.println("Add user error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateUser(User user) {
        try {
            return userDAO.updateUser(user);
        } catch (DAOException e) {
            System.err.println("Update user error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteUser(int userId) {
        try {
            return userDAO.deleteUser(userId);
        } catch (DAOException e) {
            System.err.println("Delete user error: " + e.getMessage());
            return false;
        }
    }
}