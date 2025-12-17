import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class HouseholdService {
    private HouseholdDAO householdDAO;
    private MemberDAO memberDAO;
    
    public HouseholdService() throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        this.householdDAO = new HouseholdDAOImpl(connection);
        this.memberDAO = new MemberDAOImpl();
    }
    
    public boolean addHousehold(Household household) {
        try {
            boolean success = householdDAO.addHousehold(household);
            if (success) {
                // Log the activity
                UserService userService = new UserService();
                userService.logActivity(household.getCreatedBy(), "Add Household", 
                           "Added household " + household.getHouseholdId());
            }
            return success;
        } catch (DAOException e) {
            System.err.println("Add household error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateHousehold(Household household) {
        try {
            return householdDAO.updateHousehold(household);
        } catch (DAOException e) {
            System.err.println("Update household error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteHousehold(String householdId) {
        try {
            return householdDAO.deleteHousehold(householdId);
        } catch (DAOException e) {
            System.err.println("Delete household error: " + e.getMessage());
            return false;
        }
    }
    
    public List<Household> getAllHouseholds() {
        try {
            return householdDAO.getAllHouseholds();
        } catch (DAOException e) {
            System.err.println("Get all households error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public List<Household> searchHouseholds(String searchTerm, String purok, String status) {
        try {
            return householdDAO.searchHouseholds(searchTerm, purok, status);
        } catch (DAOException e) {
            System.err.println("Search households error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Household getHouseholdDetails(String householdId) {
        try {
            return householdDAO.getHouseholdById(householdId);
        } catch (DAOException e) {
            System.err.println("Get household details error: " + e.getMessage());
            return null;
        }
    }
    
    // FIXED: Now properly implemented
    public Household getHouseholdById(String householdId) {
        return getHouseholdDetails(householdId);
    }
    
    public int getTotalHouseholds() {
        try {
            return householdDAO.getHouseholdCount();
        } catch (DAOException e) {
            System.err.println("Get total households error: " + e.getMessage());
            return 0;
        }
    }
    
    public int getTotalPopulation() {
        try {
            return householdDAO.getPopulationCount();
        } catch (DAOException e) {
            System.err.println("Get total population error: " + e.getMessage());
            return 0;
        }
    }
    
    public List<String> getAllPuroks() {
        try {
            return householdDAO.getAllPuroks();
        } catch (DAOException e) {
            System.err.println("Get puroks error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public Map<String, Integer> getHouseholdStatsByPurok() {
        try {
            return householdDAO.getHouseholdStatsByPurok();
        } catch (DAOException e) {
            System.err.println("Get household stats error: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    public Map<String, Integer> getAgeDistribution() {
        try {
            return memberDAO.getAgeDistribution();
        } catch (DAOException e) {
            System.err.println("Get age distribution error: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    public Map<String, Integer> getGenderDistribution() {
        try {
            return memberDAO.getGenderDistribution();
        } catch (DAOException e) {
            System.err.println("Get gender distribution error: " + e.getMessage());
            return new HashMap<>();
        }
    }
    
    public int getActiveHouseholdCount() {
        try {
            List<Household> households = householdDAO.searchHouseholds("", "", "Active");
            return households.size();
        } catch (DAOException e) {
            System.err.println("Get active households error: " + e.getMessage());
            return 0;
        }
    }
    
    public int getPendingHouseholdCount() {
        try {
            List<Household> households = householdDAO.searchHouseholds("", "", "Pending");
            return households.size();
        } catch (DAOException e) {
            System.err.println("Get pending households error: " + e.getMessage());
            return 0;
        }
    }
    
    public List<HouseholdMember> getHouseholdMembers(String householdId) {
        try {
            return memberDAO.getMembersByHousehold(householdId);
        } catch (DAOException e) {
            System.err.println("Get household members error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    public boolean addMember(HouseholdMember member) {
        try {
            return memberDAO.addMember(member);
        } catch (DAOException e) {
            System.err.println("Add member error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateMember(HouseholdMember member) {
        try {
            return memberDAO.updateMember(member);
        } catch (DAOException e) {
            System.err.println("Update member error: " + e.getMessage());
            return false;
        }
    }
    
    public boolean deleteMember(int memberId) {
        try {
            return memberDAO.deleteMember(memberId);
        } catch (DAOException e) {
            System.err.println("Delete member error: " + e.getMessage());
            return false;
        }
    }

    // FIXED: Now properly implemented
    public List<Household> getHouseholdsByPurok(String selectedPurok) {
        try {
            return householdDAO.searchHouseholds("", selectedPurok, "");
        } catch (DAOException e) {
            System.err.println("Get households by purok error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // FIXED: Now properly implemented
    public List<Household> searchHouseholdsByPurok(String searchTerm, String selectedPurok) {
        try {
            return householdDAO.searchHouseholds(searchTerm, selectedPurok, "");
        } catch (DAOException e) {
            System.err.println("Search households by purok error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    
    // New method for simple search
    public List<Household> searchHouseholds(String searchTerm) {
        try {
            return householdDAO.searchHouseholds(searchTerm, "", "");
        } catch (DAOException e) {
            System.err.println("Simple search error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}