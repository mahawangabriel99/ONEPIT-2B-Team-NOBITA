import java.util.List;
import java.util.Map;

public interface HouseholdDAO {
    Household getHouseholdById(String householdId) throws DAOException;
    List<Household> getAllHouseholds() throws DAOException;
    List<Household> searchHouseholds(String searchTerm, String purok, String status) throws DAOException;
    boolean addHousehold(Household household) throws DAOException;
    boolean updateHousehold(Household household) throws DAOException;
    boolean deleteHousehold(String householdId) throws DAOException;
    int getHouseholdCount() throws DAOException;
    int getPopulationCount() throws DAOException;
    List<String> getAllPuroks() throws DAOException;
    Map<String, Integer> getHouseholdStatsByPurok() throws DAOException;
    
    // Add this method to match the implementation
    boolean householdExists(String householdId) throws DAOException;
}