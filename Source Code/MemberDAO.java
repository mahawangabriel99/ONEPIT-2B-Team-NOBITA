import java.util.List;
import java.util.Map;

public interface MemberDAO {
    List<HouseholdMember> getMembersByHousehold(String householdId) throws DAOException;
    boolean addMember(HouseholdMember member) throws DAOException;
    boolean updateMember(HouseholdMember member) throws DAOException;
    boolean deleteMember(int memberId) throws DAOException;
    List<HouseholdMember> getMembersByCriteria(String criteria) throws DAOException;
    Map<String, Integer> getAgeDistribution() throws DAOException;
    Map<String, Integer> getGenderDistribution() throws DAOException;
}