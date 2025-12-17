import java.sql.SQLException;
import java.util.*;

public class ReportService {
    private ReportDAO reportDAO;
    private HouseholdService householdService;
    
    public ReportService() throws SQLException {
        this.reportDAO = new ReportDAOImpl();
        this.householdService = new HouseholdService();
    }
    
    public Map<String, Object> generatePopulationReport() {
        Map<String, Object> reportData = new HashMap<>();
        
        try {
            int totalHouseholds = householdService.getTotalHouseholds();
            int totalPopulation = householdService.getTotalPopulation();
            Map<String, Integer> purokStats = householdService.getHouseholdStatsByPurok();
            Map<String, Integer> ageDistribution = householdService.getAgeDistribution();
            Map<String, Integer> genderDistribution = householdService.getGenderDistribution();
            
            reportData.put("totalHouseholds", totalHouseholds);
            reportData.put("totalPopulation", totalPopulation);
            reportData.put("averageHouseholdSize", totalHouseholds > 0 ? 
                          String.format("%.1f", (double) totalPopulation / totalHouseholds) : "0.0");
            reportData.put("purokDistribution", purokStats);
            reportData.put("ageDistribution", ageDistribution);
            reportData.put("genderDistribution", genderDistribution);
            reportData.put("reportDate", new Date());
            
            return reportData;
        } catch (Exception e) {
            System.err.println("Generate report error: " + e.getMessage());
            return reportData;
        }
    }
    
    public Map<String, Object> generateHouseholdReport(String purok) {
        Map<String, Object> reportData = new HashMap<>();
        
        try {
            List<Household> households;
            if (purok.equals("All Puroks")) {
                households = householdService.searchHouseholds("", "All Puroks", "All");
            } else {
                households = householdService.searchHouseholds("", purok, "All");
            }
            
            reportData.put("households", households);
            reportData.put("totalCount", households.size());
            reportData.put("generatedDate", new Date());
            reportData.put("purok", purok);
            
        } catch (Exception e) {
            System.err.println("Generate household report error: " + e.getMessage());
        }
        
        return reportData;
    }
    
    public List<Report> getAllReports() {
        try {
            return reportDAO.getAllReports();
        } catch (DAOException e) {
            System.err.println("Get all reports error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public boolean saveReport(Report report) {
        try {
            return reportDAO.saveReport(report);
        } catch (DAOException e) {
            System.err.println("Save report error: " + e.getMessage());
            return false;
        }
    }
}