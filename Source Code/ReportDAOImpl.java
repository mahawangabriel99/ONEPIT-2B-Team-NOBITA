import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportDAOImpl implements ReportDAO {
    
    @Override
    public List<Report> getAllReports() throws DAOException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports ORDER BY generated_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Report report = new Report();
                report.setReportId(rs.getInt("report_id"));
                report.setReportType(rs.getString("report_type"));
                report.setReportName(rs.getString("report_name"));
                report.setGeneratedBy(rs.getInt("generated_by"));
                report.setParameters(rs.getString("parameters"));
                report.setGeneratedAt(rs.getTimestamp("generated_at"));
                reports.add(report);
            }
            return reports;
        } catch (SQLException e) {
            throw new DAOException("Error getting all reports", e);
        }
    }
    
    @Override
    public Report getReportById(int reportId) throws DAOException {
        String sql = "SELECT * FROM reports WHERE report_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reportId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Report report = new Report();
                    report.setReportId(rs.getInt("report_id"));
                    report.setReportType(rs.getString("report_type"));
                    report.setReportName(rs.getString("report_name"));
                    report.setGeneratedBy(rs.getInt("generated_by"));
                    report.setParameters(rs.getString("parameters"));
                    report.setGeneratedAt(rs.getTimestamp("generated_at"));
                    return report;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new DAOException("Error getting report by ID", e);
        }
    }
    
    @Override
    public boolean saveReport(Report report) throws DAOException {
        String sql = "INSERT INTO reports (report_type, report_name, generated_by, parameters) " +
                    "VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, report.getReportType());
            pstmt.setString(2, report.getReportName());
            pstmt.setInt(3, report.getGeneratedBy());
            pstmt.setString(4, report.getParameters());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error saving report", e);
        }
    }
    
    @Override
    public boolean deleteReport(int reportId) throws DAOException {
        String sql = "DELETE FROM reports WHERE report_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, reportId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DAOException("Error deleting report", e);
        }
    }
    
    @Override
    public List<Report> getReportsByType(String reportType) throws DAOException {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM reports WHERE report_type = ? ORDER BY generated_at DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, reportType);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Report report = new Report();
                    report.setReportId(rs.getInt("report_id"));
                    report.setReportType(rs.getString("report_type"));
                    report.setReportName(rs.getString("report_name"));
                    report.setGeneratedBy(rs.getInt("generated_by"));
                    report.setParameters(rs.getString("parameters"));
                    report.setGeneratedAt(rs.getTimestamp("generated_at"));
                    reports.add(report);
                }
            }
            return reports;
        } catch (SQLException e) {
            throw new DAOException("Error getting reports by type", e);
        }
    }
}