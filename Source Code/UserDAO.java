import java.util.List;

public interface UserDAO {
    User authenticate(String username, String password) throws DAOException;
    User getUserById(int userId) throws DAOException;
    List<User> getAllUsers() throws DAOException;
    boolean addUser(User user) throws DAOException;
    boolean updateUser(User user) throws DAOException;
    boolean deleteUser(int userId) throws DAOException;
    void logActivity(int userId, String action, String details) throws DAOException;
    List<ActivityLog> getActivityLogs(int limit) throws DAOException;
}