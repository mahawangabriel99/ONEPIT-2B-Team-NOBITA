public class CurrentUser {
    private static User currentUser;
    
    public static void setUser(User user) {
        currentUser = user;
    }
    
    public static User getUser() {
        return currentUser;
    }
    
    public static int getUserId() {
        return currentUser != null ? currentUser.getUserId() : 1; // Default to admin if null
    }
    
    public static String getFullName() {
        return currentUser != null ? currentUser.getFullName() : "Admin User";
    }

    public static void logout() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }
}