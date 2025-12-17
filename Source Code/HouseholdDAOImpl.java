import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HouseholdDAOImpl implements HouseholdDAO {
    private static final Logger logger = Logger.getLogger(HouseholdDAOImpl.class.getName());
    private final Connection connection;
    
    public HouseholdDAOImpl(Connection connection) {
        this.connection = connection;
    }
    
    @Override
    public Household getHouseholdById(String householdId) throws DAOException {
        String sql = "SELECT * FROM households WHERE household_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, householdId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToHousehold(rs);
                } else {
                    throw new DAOException("Household not found with ID: " + householdId);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving household by ID: " + householdId, e);
            throw new DAOException("Failed to retrieve household with ID: " + householdId, e);
        }
    }
    
    @Override
    public List<Household> getAllHouseholds() throws DAOException {
        List<Household> households = new ArrayList<>();
        String sql = "SELECT * FROM households ORDER BY household_id";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                households.add(mapResultSetToHousehold(rs));
            }
            
            return households;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error retrieving all households", e);
            throw new DAOException("Failed to retrieve all households", e);
        }
    }
    
    @Override
    public List<Household> searchHouseholds(String searchTerm, String purok, String status) throws DAOException {
        List<Household> households = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM households WHERE 1=1");
        List<Object> parameters = new ArrayList<>();
        
        // Dynamic query building
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            sql.append(" AND (head_first_name LIKE ? OR head_last_name LIKE ? OR household_id LIKE ? OR address LIKE ?)");
            String likeTerm = "%" + searchTerm + "%";
            parameters.add(likeTerm);
            parameters.add(likeTerm);
            parameters.add(likeTerm);
            parameters.add(likeTerm);
        }
        
        if (purok != null && !purok.trim().isEmpty() && !purok.equals("All Puroks")) {
            sql.append(" AND purok = ?");
            parameters.add(purok);
        }
        
        if (status != null && !status.trim().isEmpty() && !status.equals("All")) {
            sql.append(" AND status = ?");
            parameters.add(status);
        }
        
        sql.append(" ORDER BY household_id");
        
        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            // Set parameters
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    households.add(mapResultSetToHousehold(rs));
                }
            }
            
            return households;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error searching households", e);
            throw new DAOException("Failed to search households", e);
        }
    }
    
    @Override
    public boolean addHousehold(Household household) throws DAOException {
        String sql = "INSERT INTO households (household_id, head_first_name, head_last_name, head_middle_name, "
                   + "head_age, head_gender, head_education_level, head_occupation, "
                   + "purok, house_number, contact_number, address, members_count, status, "
                   + "registration_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, household.getHouseholdId());
            stmt.setString(2, household.getHeadFirstName());
            stmt.setString(3, household.getHeadLastName());
            stmt.setString(4, household.getHeadMiddleName());
            
            // Handle new fields with null checks
            if (household.getHeadAge() != null) {
                stmt.setInt(5, household.getHeadAge());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }
            
            stmt.setString(6, household.getHeadGender());
            stmt.setString(7, household.getHeadEducationLevel());
            stmt.setString(8, household.getHeadOccupation());
            stmt.setString(9, household.getPurok());
            stmt.setString(10, household.getHouseNumber());
            stmt.setString(11, household.getContactNumber());
            stmt.setString(12, household.getAddress());
            stmt.setInt(13, household.getMembersCount());
            stmt.setString(14, household.getStatus());
            stmt.setDate(15, new java.sql.Date(household.getRegistrationDate().getTime()));
            stmt.setInt(16, household.getCreatedBy());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding household: " + household.getHouseholdId(), e);
            throw new DAOException("Failed to add household: " + household.getHouseholdId(), e);
        }
    }
    
    @Override
    public boolean updateHousehold(Household household) throws DAOException {
        String sql = "UPDATE households SET head_first_name = ?, head_last_name = ?, head_middle_name = ?, "
                   + "head_age = ?, head_gender = ?, head_education_level = ?, head_occupation = ?, "
                   + "purok = ?, house_number = ?, contact_number = ?, address = ?, status = ?, "
                   + "updated_at = CURRENT_TIMESTAMP WHERE household_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, household.getHeadFirstName());
            stmt.setString(2, household.getHeadLastName());
            stmt.setString(3, household.getHeadMiddleName());
            
            // Handle nullable Integer for age
            if (household.getHeadAge() != null) {
                stmt.setInt(4, household.getHeadAge());
            } else {
                stmt.setNull(4, Types.INTEGER);
            }
            
            stmt.setString(5, household.getHeadGender());
            stmt.setString(6, household.getHeadEducationLevel());
            stmt.setString(7, household.getHeadOccupation());
            stmt.setString(8, household.getPurok());
            stmt.setString(9, household.getHouseNumber());
            stmt.setString(10, household.getContactNumber());
            stmt.setString(11, household.getAddress());
            stmt.setString(12, household.getStatus());
            stmt.setString(13, household.getHouseholdId());
            
            int rowsAffected = stmt.executeUpdate();
            logger.info("Updated household " + household.getHouseholdId() + " - Rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating household: " + household.getHouseholdId(), e);
            throw new DAOException("Failed to update household: " + household.getHouseholdId(), e);
        }
    }
    
    @Override
    public boolean deleteHousehold(String householdId) throws DAOException {
        String sql = "DELETE FROM households WHERE household_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, householdId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting household: " + householdId, e);
            throw new DAOException("Failed to delete household: " + householdId, e);
        }
    }
    
    @Override
    public int getHouseholdCount() throws DAOException {
        String sql = "SELECT COUNT(*) FROM households";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting household count", e);
            throw new DAOException("Failed to get household count", e);
        }
    }
    
    @Override
    public int getPopulationCount() throws DAOException {
        String sql = "SELECT SUM(members_count) FROM households";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting population count", e);
            throw new DAOException("Failed to get population count", e);
        }
    }
    
    @Override
    public List<String> getAllPuroks() throws DAOException {
        List<String> puroks = new ArrayList<>();
        String sql = "SELECT DISTINCT purok FROM households WHERE purok IS NOT NULL ORDER BY purok";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                puroks.add(rs.getString("purok"));
            }
            
            return puroks;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting all puroks", e);
            throw new DAOException("Failed to get all puroks", e);
        }
    }
    
    @Override
    public Map<String, Integer> getHouseholdStatsByPurok() throws DAOException {
        Map<String, Integer> stats = new LinkedHashMap<>();
        String sql = "SELECT purok, COUNT(*) as household_count FROM households "
                   + "WHERE purok IS NOT NULL GROUP BY purok ORDER BY purok";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                stats.put(rs.getString("purok"), rs.getInt("household_count"));
            }
            
            return stats;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting household stats by purok", e);
            throw new DAOException("Failed to get household statistics by purok", e);
        }
    }
    
    @Override
    public boolean householdExists(String householdId) throws DAOException {
        String sql = "SELECT COUNT(*) FROM households WHERE household_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, householdId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if household exists: " + householdId, e);
            throw new DAOException("Failed to check if household exists: " + householdId, e);
        }
    }
    
    // Helper method to map ResultSet to Household object
    private Household mapResultSetToHousehold(ResultSet rs) throws SQLException {
        Household household = new Household();
        household.setHouseholdId(rs.getString("household_id"));
        household.setHeadFirstName(rs.getString("head_first_name"));
        household.setHeadLastName(rs.getString("head_last_name"));
        household.setHeadMiddleName(rs.getString("head_middle_name"));
        
        // Get new fields - handle null values
        household.setHeadAge(rs.getInt("head_age"));
        if (rs.wasNull()) {
            household.setHeadAge(null);
        }
        
        household.setHeadGender(rs.getString("head_gender"));
        household.setHeadEducationLevel(rs.getString("head_education_level"));
        household.setHeadOccupation(rs.getString("head_occupation"));
        
        household.setPurok(rs.getString("purok"));
        household.setHouseNumber(rs.getString("house_number"));
        household.setContactNumber(rs.getString("contact_number"));
        household.setAddress(rs.getString("address"));
        household.setMembersCount(rs.getInt("members_count"));
        household.setStatus(rs.getString("status"));
        household.setRegistrationDate(rs.getDate("registration_date"));
        household.setCreatedBy(rs.getInt("created_by"));
        household.setCreatedAt(rs.getTimestamp("created_at"));
        household.setUpdatedAt(rs.getTimestamp("updated_at"));
        return household;
    }
}