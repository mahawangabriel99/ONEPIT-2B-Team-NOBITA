import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DebugLoginTest {
    public static void main(String[] args) {
        System.out.println("=== BARANGAY CENSUS LOGIN DEBUG ===\n");
        
        // Test 1: Check database connection
        System.out.println("1. Testing Database Connection...");
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connected successfully");
                
                // Test 2: Check if users table exists and has data
                System.out.println("\n2. Checking users table...");
                String sql = "SELECT COUNT(*) as count FROM users";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    int count = rs.getInt("count");
                    System.out.println("✓ Users table exists with " + count + " records");
                }
                
                // Test 3: Show all users and their passwords
                System.out.println("\n3. Listing all users:");
                sql = "SELECT user_id, username, password, role, is_active FROM users";
                rs = stmt.executeQuery(sql);
                boolean hasUsers = false;
                while (rs.next()) {
                    hasUsers = true;
                    System.out.println("   ID: " + rs.getInt("user_id"));
                    System.out.println("   Username: " + rs.getString("username"));
                    System.out.println("   Password: " + rs.getString("password"));
                    System.out.println("   Role: " + rs.getString("role"));
                    System.out.println("   Active: " + rs.getBoolean("is_active"));
                    System.out.println("   ---");
                }
                
                if (!hasUsers) {
                    System.out.println("   ✗ NO USERS FOUND IN DATABASE!");
                    System.out.println("\n   Creating demo users...");
                    
                    // Insert plain text passwords directly
                    String insertSQL = "INSERT INTO users (username, password, role, full_name, email, is_active) VALUES " +
                            "('admin', 'admin123', 'Administrator', 'System Admin', 'admin@test.com', 1), " +
                            "('enumerator1', 'enum123', 'Enumerator', 'Enumerator One', 'enum@test.com', 1), " +
                            "('viewer1', 'view123', 'Viewer', 'Viewer One', 'viewer@test.com', 1)";
                    
                    int rows = stmt.executeUpdate(insertSQL);
                    System.out.println("   ✓ Created " + rows + " demo users");
                }
                
                // Test 4: Test authentication directly
                System.out.println("\n4. Testing authentication...");
                
                // Test admin
                System.out.println("   Testing admin/admin123...");
                sql = "SELECT * FROM users WHERE username = 'admin' AND password = 'admin123' AND is_active = 1";
                rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    System.out.println("   ✓ SQL query found admin with password 'admin123'");
                } else {
                    System.out.println("   ✗ SQL query DID NOT find admin with password 'admin123'");
                    
                    // Check what password is actually stored
                    sql = "SELECT password FROM users WHERE username = 'admin'";
                    rs = stmt.executeQuery(sql);
                    if (rs.next()) {
                        String actualPassword = rs.getString("password");
                        System.out.println("   Actual stored password for admin: '" + actualPassword + "'");
                        System.out.println("   Length: " + actualPassword.length() + " characters");
                        
                        // Try to match with trimmed
                        if ("admin123".equals(actualPassword.trim())) {
                            System.out.println("   ✓ Passwords match after trimming!");
                        }
                    }
                }
                
                rs.close();
                stmt.close();
                conn.close();
                
            } else {
                System.out.println("✗ Database connection FAILED!");
            }
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Test 5: Test UserDAO authentication
        System.out.println("\n5. Testing UserDAO authentication...");
        try {
            UserDAOImpl dao = new UserDAOImpl();
            
            System.out.println("   Testing admin/admin123...");
            User user = dao.authenticate("admin", "admin123");
            if (user != null) {
                System.out.println("   ✓ UserDAO.authenticate() returned user: " + user.getFullName());
            } else {
                System.out.println("   ✗ UserDAO.authenticate() returned NULL");
            }
            
            System.out.println("\n   Testing with wrong password...");
            user = dao.authenticate("admin", "wrongpass");
            if (user != null) {
                System.out.println("   ✗ ERROR: Should NOT authenticate with wrong password!");
            } else {
                System.out.println("   ✓ Correctly rejected wrong password");
            }
            
        } catch (Exception e) {
            System.out.println("   ✗ Error in UserDAO: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("\n=== DEBUG COMPLETE ===");
    }
}