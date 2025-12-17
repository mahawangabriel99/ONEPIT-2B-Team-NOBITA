import java.sql.SQLException;
import java.util.*;

public class ApplicationService {
    private HouseholdService householdService;
    private UserService userService;
    private ReportService reportService;
    
    public ApplicationService() throws SQLException {
        this.householdService = new HouseholdService();
        this.userService = new UserService();
        this.reportService = new ReportService();
    }
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<String, Object>();
        
        try {
            stats.put("totalHouseholds", householdService.getTotalHouseholds());
            stats.put("totalPopulation", householdService.getTotalPopulation());
            stats.put("activeHouseholds", householdService.getActiveHouseholdCount());
            stats.put("pendingHouseholds", householdService.getPendingHouseholdCount());
            stats.put("averageHouseholdSize", householdService.getTotalHouseholds() > 0 ? 
                     String.format("%.1f", (double) householdService.getTotalPopulation() / householdService.getTotalHouseholds()) : "0.0");
            
            return stats;
        } catch (Exception e) {
            System.err.println("Get dashboard stats error: " + e.getMessage());
            stats.put("totalHouseholds", 0);
            stats.put("totalPopulation", 0);
            stats.put("activeHouseholds", 0);
            stats.put("pendingHouseholds", 0);
            stats.put("averageHouseholdSize", "0.0");
            return stats;
        }
    }
    
    public Map<String, Integer> getPurokStats() {
        return householdService.getHouseholdStatsByPurok();
    }
    
    public java.util.List<ActivityLog> getRecentActivities() {
        return userService.getRecentActivities(10);
    }
    
    public java.util.List<String> getSystemAlerts() {
        java.util.List<String> alerts = new ArrayList<String>();
        
        try {
            int pendingCount = householdService.getPendingHouseholdCount();
            if (pendingCount > 0) {
                alerts.add(pendingCount + " households pending verification");
            }
            
            int totalHouseholds = householdService.getTotalHouseholds();
            int totalMembers = householdService.getTotalPopulation();
            if (totalHouseholds > 0 && totalMembers > 0) {
                double avg = (double) totalMembers / totalHouseholds;
                if (avg > 10) {
                    alerts.add("High average household size detected (" + String.format("%.1f", avg) + ")");
                }
            }
            
            alerts.add("Database connection: Active");
            alerts.add("System status: Operational");
            
        } catch (Exception e) {
            alerts.add("Error checking system alerts: " + e.getMessage());
        }
        
        return alerts;
    }
}