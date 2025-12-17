import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberDAOImpl implements MemberDAO {
    
    @Override
    public List<HouseholdMember> getMembersByHousehold(String householdId) throws DAOException {
        List<HouseholdMember> members = new ArrayList<HouseholdMember>();
        String sql = "SELECT * FROM household_members WHERE household_id = ? ORDER BY is_head DESC, member_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, householdId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(extractMemberFromResultSet(rs));
                }
            }
            return members;
        } catch (SQLException e) {
            throw new DAOException("Error getting members by household", e);
        }
    }
    
    @Override
    public boolean addMember(HouseholdMember member) throws DAOException {
        String sql = "INSERT INTO household_members (household_id, first_name, last_name, middle_name, "
                    + "age, gender, relationship, occupation, education_level, birth_date, is_head) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, member.getHouseholdId());
            pstmt.setString(2, member.getFirstName());
            pstmt.setString(3, member.getLastName());
            pstmt.setString(4, member.getMiddleName());
            pstmt.setInt(5, member.getAge());
            pstmt.setString(6, member.getGender());
            pstmt.setString(7, member.getRelationship());
            pstmt.setString(8, member.getOccupation());
            pstmt.setString(9, member.getEducationLevel());
            pstmt.setDate(10, member.getBirthDate());
            pstmt.setBoolean(11, member.isHead());
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Update household members count
            if (rowsAffected > 0) {
                updateHouseholdMemberCount(member.getHouseholdId());
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("Error adding member", e);
        }
    }
    
    @Override
    public boolean updateMember(HouseholdMember member) throws DAOException {
        String sql = "UPDATE household_members SET first_name = ?, last_name = ?, middle_name = ?, "
                    + "age = ?, gender = ?, relationship = ?, occupation = ?, education_level = ?, "
                    + "birth_date = ?, is_head = ? WHERE member_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, member.getFirstName());
            pstmt.setString(2, member.getLastName());
            pstmt.setString(3, member.getMiddleName());
            pstmt.setInt(4, member.getAge());
            pstmt.setString(5, member.getGender());
            pstmt.setString(6, member.getRelationship());
            pstmt.setString(7, member.getOccupation());
            pstmt.setString(8, member.getEducationLevel());
            pstmt.setDate(9, member.getBirthDate());
            pstmt.setBoolean(10, member.isHead());
            pstmt.setInt(11, member.getMemberId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("Error updating member", e);
        }
    }
    
    @Override
    public boolean deleteMember(int memberId) throws DAOException {
        String sql = "DELETE FROM household_members WHERE member_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Update household members count
            if (rowsAffected > 0) {
                // Get household ID before deleting
                String householdId = getHouseholdIdByMemberId(memberId);
                if (householdId != null) {
                    updateHouseholdMemberCount(householdId);
                }
            }
            
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new DAOException("Error deleting member", e);
        }
    }
    
    @Override
    public List<HouseholdMember> getMembersByCriteria(String criteria) throws DAOException {
        List<HouseholdMember> members = new ArrayList<HouseholdMember>();
        String sql = "SELECT * FROM household_members WHERE " + criteria + " ORDER BY member_id";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                members.add(extractMemberFromResultSet(rs));
            }
            return members;
        } catch (SQLException e) {
            throw new DAOException("Error getting members by criteria", e);
        }
    }
    
    @Override
    public Map<String, Integer> getAgeDistribution() throws DAOException {
        Map<String, Integer> distribution = new HashMap<String, Integer>();
        String sql = "SELECT " +
                    "CASE " +
                    "  WHEN age < 18 THEN '0-17' " +
                    "  WHEN age BETWEEN 18 AND 59 THEN '18-59' " +
                    "  ELSE '60+' " +
                    "END as age_group, " +
                    "COUNT(*) as count " +
                    "FROM household_members " +
                    "GROUP BY age_group";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                distribution.put(rs.getString("age_group"), rs.getInt("count"));
            }
            return distribution;
        } catch (SQLException e) {
            throw new DAOException("Error getting age distribution", e);
        }
    }
    
    @Override
    public Map<String, Integer> getGenderDistribution() throws DAOException {
        Map<String, Integer> distribution = new HashMap<String, Integer>();
        String sql = "SELECT gender, COUNT(*) as count FROM household_members GROUP BY gender";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                distribution.put(rs.getString("gender"), rs.getInt("count"));
            }
            return distribution;
        } catch (SQLException e) {
            throw new DAOException("Error getting gender distribution", e);
        }
    }
    
    private HouseholdMember extractMemberFromResultSet(ResultSet rs) throws SQLException {
        HouseholdMember member = new HouseholdMember();
        member.setMemberId(rs.getInt("member_id"));
        member.setHouseholdId(rs.getString("household_id"));
        member.setFirstName(rs.getString("first_name"));
        member.setLastName(rs.getString("last_name"));
        member.setMiddleName(rs.getString("middle_name"));
        member.setAge(rs.getInt("age"));
        member.setGender(rs.getString("gender"));
        member.setRelationship(rs.getString("relationship"));
        member.setOccupation(rs.getString("occupation"));
        member.setEducationLevel(rs.getString("education_level"));
        member.setBirthDate(rs.getDate("birth_date"));
        member.setHead(rs.getBoolean("is_head"));
        return member;
    }
    
    private void updateHouseholdMemberCount(String householdId) throws SQLException {
        String sql = "UPDATE households SET members_count = " +
                    "(SELECT COUNT(*) FROM household_members WHERE household_id = ?) " +
                    "WHERE household_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, householdId);
            pstmt.setString(2, householdId);
            pstmt.executeUpdate();
        }
    }
    
    private String getHouseholdIdByMemberId(int memberId) throws SQLException {
        String sql = "SELECT household_id FROM household_members WHERE member_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, memberId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("household_id");
                }
            }
            return null;
        }
    }
}