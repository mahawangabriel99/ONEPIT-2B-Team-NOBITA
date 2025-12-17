import java.sql.*;
import java.util.Properties;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/barangay_census";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Don't use static connection - create new connections each time
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties props = new Properties();
            props.setProperty("user", USER);
            props.setProperty("password", PASSWORD);
            props.setProperty("useSSL", "false");
            props.setProperty("autoReconnect", "true");
            props.setProperty("characterEncoding", "UTF-8");
            props.setProperty("useUnicode", "true");
            
            Connection conn = DriverManager.getConnection(URL, props);
            System.out.println("New database connection created");
            
            // Create tables if not exist (only once)
            createTablesIfNotExist(conn);
            
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
    
    private static void createTablesIfNotExist(Connection conn) throws SQLException {
        String[] createTables = {
            // Users table
            "CREATE TABLE IF NOT EXISTS users (" +
            "  user_id INT PRIMARY KEY AUTO_INCREMENT," +
            "  username VARCHAR(50) UNIQUE NOT NULL," +
            "  password VARCHAR(255) NOT NULL," +
            "  role VARCHAR(20) NOT NULL," +
            "  full_name VARCHAR(100) NOT NULL," +
            "  email VARCHAR(100)," +
            "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  last_login TIMESTAMP," +
            "  is_active BOOLEAN DEFAULT TRUE" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            
            // Households table
            "CREATE TABLE IF NOT EXISTS households (" +
            "  household_id VARCHAR(20) PRIMARY KEY," +
            "  head_first_name VARCHAR(50) NOT NULL," +
            "  head_last_name VARCHAR(50) NOT NULL," +
            "  head_middle_name VARCHAR(50)," +
            "  purok VARCHAR(20) NOT NULL," +
            "  house_number VARCHAR(20)," +
            "  contact_number VARCHAR(20)," +
            "  address TEXT," +
            "  members_count INT DEFAULT 0," +
            "  status VARCHAR(20) DEFAULT 'Active'," +
            "  registration_date DATE," +
            "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP," +
            "  created_by INT," +
            "  FOREIGN KEY (created_by) REFERENCES users(user_id)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            
            // Household members table
            "CREATE TABLE IF NOT EXISTS household_members (" +
            "  member_id INT PRIMARY KEY AUTO_INCREMENT," +
            "  household_id VARCHAR(20)," +
            "  first_name VARCHAR(50) NOT NULL," +
            "  last_name VARCHAR(50) NOT NULL," +
            "  middle_name VARCHAR(50)," +
            "  age INT," +
            "  gender VARCHAR(10)," +
            "  relationship VARCHAR(20)," +
            "  occupation VARCHAR(50)," +
            "  education_level VARCHAR(50)," +
            "  birth_date DATE," +
            "  is_head BOOLEAN DEFAULT FALSE," +
            "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  FOREIGN KEY (household_id) REFERENCES households(household_id) ON DELETE CASCADE" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            
            // Activity log table
            "CREATE TABLE IF NOT EXISTS activity_logs (" +
            "  log_id INT PRIMARY KEY AUTO_INCREMENT," +
            "  user_id INT," +
            "  action VARCHAR(100) NOT NULL," +
            "  details TEXT," +
            "  ip_address VARCHAR(45)," +
            "  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  FOREIGN KEY (user_id) REFERENCES users(user_id)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4",
            
            // Reports table
            "CREATE TABLE IF NOT EXISTS reports (" +
            "  report_id INT PRIMARY KEY AUTO_INCREMENT," +
            "  report_type VARCHAR(50) NOT NULL," +
            "  report_name VARCHAR(100) NOT NULL," +
            "  generated_by INT," +
            "  parameters TEXT," +
            "  file_path VARCHAR(255)," +
            "  generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "  FOREIGN KEY (generated_by) REFERENCES users(user_id)" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4"
        };
        
        try (Statement stmt = conn.createStatement()) {
            for (String sql : createTables) {
                stmt.execute(sql);
            }
            System.out.println("Database tables checked/created successfully.");
            
            // Insert default users if not exist
            insertDefaultUsers(conn);
        }
    }
    
    private static void insertDefaultUsers(Connection conn) throws SQLException {
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        String insertSql = "INSERT INTO users (username, password, role, full_name, email) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            
            // Check and insert admin user
            checkStmt.setString(1, "admin");
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                insertStmt.setString(1, "admin");
                insertStmt.setString(2, "admin123");  // Plain text
                insertStmt.setString(3, "Administrator");
                insertStmt.setString(4, "System Administrator");
                insertStmt.setString(5, "admin@barangay.gov");
                insertStmt.executeUpdate();
                System.out.println("Created admin user");
            }
            
            // Check and insert enumerator user
            checkStmt.setString(1, "enumerator1");
            rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                insertStmt.setString(1, "enumerator1");
                insertStmt.setString(2, "enum123");  // Plain text
                insertStmt.setString(3, "Enumerator");
                insertStmt.setString(4, "Enumerator One");
                insertStmt.setString(5, "enumerator@barangay.gov");
                insertStmt.executeUpdate();
                System.out.println("Created enumerator user");
            }
            
            // Check and insert viewer user
            checkStmt.setString(1, "viewer1");
            rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                insertStmt.setString(1, "viewer1");
                insertStmt.setString(2, "view123");  // Plain text
                insertStmt.setString(3, "Viewer");
                insertStmt.setString(4, "Viewer One");
                insertStmt.setString(5, "viewer@barangay.gov");
                insertStmt.executeUpdate();
                System.out.println("Created viewer user");
            }
        }
    }

    public static boolean testConnection(String host, String port, String name, String user2, String pass) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'testConnection'");
    }
}