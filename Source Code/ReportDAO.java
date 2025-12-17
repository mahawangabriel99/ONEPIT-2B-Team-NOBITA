import java.util.List;

public interface ReportDAO {
    List<Report> getAllReports() throws DAOException;
    Report getReportById(int reportId) throws DAOException;
    boolean saveReport(Report report) throws DAOException;
    boolean deleteReport(int reportId) throws DAOException;
    List<Report> getReportsByType(String reportType) throws DAOException;
}