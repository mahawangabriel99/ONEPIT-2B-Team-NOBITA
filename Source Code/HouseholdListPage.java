import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.FileWriter;

public class HouseholdListPage extends JFrame {
    private String[] menuItems = {"Dashboard", "Household List", "Add Household", "Census Reports", "Settings"};
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JTable table;
    private DefaultTableModel tableModel;
    
    private final Color PRIMARY_COLOR = new Color(67, 97, 238);
    private final Color SECONDARY_COLOR = new Color(255, 107, 107);
    private final Color ACCENT_COLOR = new Color(46, 196, 182);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 252);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private final Color BORDER_COLOR = new Color(230, 230, 230);
    private final Color SUCCESS_COLOR = new Color(39, 174, 96);
    private final Color WARNING_COLOR = new Color(243, 156, 18);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    
    private HouseholdService householdService;
    
    public HouseholdListPage() throws SQLException {
        setTitle("Barangay Census System - Household List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 850);
        setLocationRelativeTo(null);
        
        this.householdService = new HouseholdService();
        
        initUI();
        loadHouseholds();
    }
    
    private void loadHouseholds() {
        try {
            List<Household> households = householdService.getAllHouseholds();
            updateTable(households);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading households: " + e.getMessage(),
                "Load Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateTable(List<Household> households) {
        tableModel.setRowCount(0);
        for (Household household : households) {
            Object[] row = {
                household.getHouseholdId(),
                getFullName(household.getHeadFirstName(), household.getHeadMiddleName(), household.getHeadLastName()),
                household.getHeadAge() != null ? household.getHeadAge() : "N/A",
                household.getHeadGender() != null ? household.getHeadGender() : "N/A",
                household.getHeadEducationLevel() != null ? household.getHeadEducationLevel() : "N/A",
                household.getHeadOccupation() != null ? household.getHeadOccupation() : "N/A",
                household.getPurok(),
                household.getMembersCount(),
                household.getStatus(),
                "View/Edit"
            };
            tableModel.addRow(row);
        }
    }
    
    private String getFullName(String firstName, String middleName, String lastName) {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName.trim());
        }
        if (middleName != null && !middleName.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(middleName.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (fullName.length() > 0) fullName.append(" ");
            fullName.append(lastName.trim());
        }
        return fullName.toString().isEmpty() ? "N/A" : fullName.toString();
    }
    
    private void navigateToPage(String pageName) {
    dispose();
    try {
        switch (pageName) {
            case "Dashboard":
                new DashboardPage().setVisible(true);
                break;
            case "Household List":
                new HouseholdListPage().setVisible(true);
                break;
            case "Add Household":
                new AddHouseholdPage().setVisible(true);
                break;
            case "Census Reports": // Changed from "Reports" to match menu
                new CensusReportPage().setVisible(true); // Changed from ReportsPage
                break;
            case "Settings":
                new SettingsPage().setVisible(true);
                break;
            default:
                new DashboardPage().setVisible(true);
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
            "Error navigating to page: " + e.getMessage(),
            "Navigation Error",
            JOptionPane.ERROR_MESSAGE);
    }
}
    
    private void logout() throws SQLException {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            CurrentUser.logout();
            dispose();
            new LoginPage().setVisible(true);
        }
    }
    
    private void showHouseholdDialog(String householdId, String headName) {
        try {
            Household household = householdService.getHouseholdById(householdId);
            if (household == null) {
                JOptionPane.showMessageDialog(this, "Household not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            List<HouseholdMember> members = householdService.getHouseholdMembers(householdId);
            
            JDialog dialog = new JDialog(this, "Household Details", true);
            dialog.setSize(700, 550);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(PRIMARY_COLOR);
            headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
            
            JLabel titleLabel = new JLabel("🏠 Household Details");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            
            JLabel idLabel = new JLabel("ID: " + householdId);
            idLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            idLabel.setForeground(Color.WHITE);
            
            headerPanel.add(titleLabel, BorderLayout.WEST);
            headerPanel.add(idLabel, BorderLayout.EAST);
            
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(BACKGROUND_COLOR);
            
            JTabbedPane tabbedPane = new JTabbedPane();
            tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
            
            JPanel infoPanel = new JPanel(new GridLayout(0, 2, 15, 10));
            infoPanel.setBackground(CARD_COLOR);
            infoPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
            
            String fullHeadName = getFullName(household.getHeadFirstName(), household.getHeadMiddleName(), household.getHeadLastName());
            addInfoField(infoPanel, "Head Name:", fullHeadName);
            addInfoField(infoPanel, "Head Age:", household.getHeadAge() != null ? household.getHeadAge() + " years" : "N/A");
            addInfoField(infoPanel, "Head Gender:", household.getHeadGender() != null ? household.getHeadGender() : "N/A");
            addInfoField(infoPanel, "Head Education:", household.getHeadEducationLevel() != null ? household.getHeadEducationLevel() : "N/A");
            addInfoField(infoPanel, "Head Occupation:", household.getHeadOccupation() != null ? household.getHeadOccupation() : "N/A");
            addInfoField(infoPanel, "Purok:", household.getPurok());
            addInfoField(infoPanel, "House Number:", household.getHouseNumber() != null ? household.getHouseNumber() : "N/A");
            addInfoField(infoPanel, "Address:", household.getAddress());
            addInfoField(infoPanel, "Contact:", household.getContactNumber() != null ? household.getContactNumber() : "N/A");
            addInfoField(infoPanel, "Total Members:", String.valueOf(members.size()));
            addInfoField(infoPanel, "Status:", household.getStatus());
            
            String regDate = formatDate(household.getRegistrationDate());
            addInfoField(infoPanel, "Date Registered:", regDate);
            
            JScrollPane infoScroll = new JScrollPane(infoPanel);
            infoScroll.setBorder(null);
            
            String[] columns = {"Name", "Age", "Gender", "Relation", "Education", "Occupation"};
            DefaultTableModel membersModel = new DefaultTableModel(columns, 0);
            
            for (HouseholdMember member : members) {
                Object[] row = {
                    getFullName(member.getFirstName(), member.getMiddleName(), member.getLastName()),
                    member.getAge(),
                    member.getGender(),
                    member.getRelationship(),
                    member.getEducationLevel() != null ? member.getEducationLevel() : "N/A",
                    member.getOccupation() != null ? member.getOccupation() : "N/A"
                };
                membersModel.addRow(row);
            }
            
            JTable membersTable = new JTable(membersModel);
            membersTable.setRowHeight(30);
            membersTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            membersTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            for (int i = 0; i < membersTable.getColumnCount(); i++) {
                membersTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
            
            JScrollPane membersScroll = new JScrollPane(membersTable);
            
            tabbedPane.addTab("Household Info", infoScroll);
            tabbedPane.addTab("Household Members (" + members.size() + ")", membersScroll);
            
            contentPanel.add(tabbedPane, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.setBackground(BACKGROUND_COLOR);
            
            JButton editButton = createMediumButton("✏️ Edit", SUCCESS_COLOR);
            editButton.addActionListener(e -> {
                dialog.dispose();
                showEditHouseholdDialog(householdId);
            });
            
            JButton closeButton = createMediumButton("Close", PRIMARY_COLOR);
            closeButton.addActionListener(e -> dialog.dispose());
            
            buttonPanel.add(editButton);
            buttonPanel.add(closeButton);
            
            dialog.add(headerPanel, BorderLayout.NORTH);
            dialog.add(contentPanel, BorderLayout.CENTER);
            dialog.add(buttonPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading household details: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private String formatDate(Date date) {
        if (date == null) return "N/A";
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd, yyyy");
        return sdf.format(date);
    }
    
    private void showEditHouseholdDialog(String householdId) {
        try {
            Household household = householdService.getHouseholdById(householdId);
            if (household == null) {
                JOptionPane.showMessageDialog(this, "Household not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            JDialog editDialog = new JDialog(this, "Edit Household - " + householdId, true);
            editDialog.setSize(900, 700);
            editDialog.setLocationRelativeTo(this);
            editDialog.setLayout(new BorderLayout());
            editDialog.setResizable(true);
            
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBackground(PRIMARY_COLOR);
            headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
            
            JLabel titleLabel = new JLabel("✏️ Edit Household - " + householdId);
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            titleLabel.setForeground(Color.WHITE);
            
            headerPanel.add(titleLabel, BorderLayout.WEST);
            
            JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(BACKGROUND_COLOR);
            
            JPanel formPanel = new JPanel(new GridBagLayout());
            formPanel.setBackground(CARD_COLOR);
            formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(8, 8, 8, 8);
            gbc.weightx = 1.0;
            gbc.gridwidth = 1;
            
            int row = 0;
            
            // Row 1: Household ID (read-only)
            gbc.gridx = 0; gbc.gridy = row;
            JLabel idLabel = new JLabel("Household ID:");
            idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            idLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(idLabel, gbc);
            
            JTextField idField = new JTextField(household.getHouseholdId());
            idField.setEditable(false);
            styleTextField(idField);
            gbc.gridx = 1;
            formPanel.add(idField, gbc);
            
            row++;
            
            // Row 2: Head First Name
            gbc.gridx = 0; gbc.gridy = row;
            JLabel firstNameLabel = new JLabel("Head First Name:*");
            firstNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            firstNameLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(firstNameLabel, gbc);
            
            JTextField firstNameField = new JTextField(household.getHeadFirstName() != null ? household.getHeadFirstName() : "");
            styleTextField(firstNameField);
            gbc.gridx = 1;
            formPanel.add(firstNameField, gbc);
            
            row++;
            
            // Row 3: Head Middle Name
            gbc.gridx = 0; gbc.gridy = row;
            JLabel middleNameLabel = new JLabel("Head Middle Name:");
            middleNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            middleNameLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(middleNameLabel, gbc);
            
            JTextField middleNameField = new JTextField(household.getHeadMiddleName() != null ? household.getHeadMiddleName() : "");
            styleTextField(middleNameField);
            gbc.gridx = 1;
            formPanel.add(middleNameField, gbc);
            
            row++;
            
            // Row 4: Head Last Name
            gbc.gridx = 0; gbc.gridy = row;
            JLabel lastNameLabel = new JLabel("Head Last Name:*");
            lastNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lastNameLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(lastNameLabel, gbc);
            
            JTextField lastNameField = new JTextField(household.getHeadLastName() != null ? household.getHeadLastName() : "");
            styleTextField(lastNameField);
            gbc.gridx = 1;
            formPanel.add(lastNameField, gbc);
            
            row++;
            
            // Row 5: Head Age
            gbc.gridx = 0; gbc.gridy = row;
            JLabel ageLabel = new JLabel("Head Age:*");
            ageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            ageLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(ageLabel, gbc);
            
            Integer currentAge = household.getHeadAge();
            int initialAge = (currentAge != null && currentAge > 0) ? currentAge : 25;
            JSpinner ageSpinner = new JSpinner(new SpinnerNumberModel(initialAge, 1, 120, 1));
            styleSpinner(ageSpinner);
            gbc.gridx = 1;
            formPanel.add(ageSpinner, gbc);
            
            row++;
            
            // Row 6: Head Gender
            gbc.gridx = 0; gbc.gridy = row;
            JLabel genderLabel = new JLabel("Head Gender:*");
            genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            genderLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(genderLabel, gbc);
            
            String[] genderOptions = {"Male", "Female", "Other"};
            JComboBox<String> genderCombo = new JComboBox<>(genderOptions);
            String currentGender = household.getHeadGender();
            if (currentGender != null && !currentGender.trim().isEmpty()) {
                genderCombo.setSelectedItem(currentGender);
            } else {
                genderCombo.setSelectedItem("Male");
            }
            styleComboBox(genderCombo);
            gbc.gridx = 1;
            formPanel.add(genderCombo, gbc);
            
            row++;
            
            // Row 7: Head Education Level
            gbc.gridx = 0; gbc.gridy = row;
            JLabel educationLabel = new JLabel("Head Education Level:");
            educationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            educationLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(educationLabel, gbc);
            
            String[] educationOptions = {
                "No Formal Education",
                "Elementary Level",
                "Elementary Graduate",
                "High School Level",
                "High School Graduate",
                "Vocational",
                "College Level",
                "College Graduate",
                "Post Graduate"
            };
            JComboBox<String> educationCombo = new JComboBox<>(educationOptions);
            String currentEducation = household.getHeadEducationLevel();
            if (currentEducation != null && !currentEducation.trim().isEmpty()) {
                educationCombo.setSelectedItem(currentEducation);
            } else {
                educationCombo.setSelectedItem("Elementary Graduate");
            }
            styleComboBox(educationCombo);
            gbc.gridx = 1;
            formPanel.add(educationCombo, gbc);
            
            row++;
            
            // Row 8: Head Occupation
            gbc.gridx = 0; gbc.gridy = row;
            JLabel occupationLabel = new JLabel("Head Occupation:");
            occupationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            occupationLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(occupationLabel, gbc);
            
            JTextField occupationField = new JTextField(household.getHeadOccupation() != null ? household.getHeadOccupation() : "");
            styleTextField(occupationField);
            gbc.gridx = 1;
            formPanel.add(occupationField, gbc);
            
            row++;
            
            // Row 9: Purok
            gbc.gridx = 0; gbc.gridy = row;
            JLabel purokLabel = new JLabel("Purok:*");
            purokLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            purokLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(purokLabel, gbc);
            
            String[] purokOptions = {"Purok 1", "Purok 2", "Purok 3", "Purok 4", "Purok 5", "Purok 6", "Purok 7"};
            JComboBox<String> purokCombo = new JComboBox<>(purokOptions);
            String currentPurok = household.getPurok();
            if (currentPurok != null && !currentPurok.trim().isEmpty()) {
                purokCombo.setSelectedItem(currentPurok);
            } else {
                purokCombo.setSelectedItem("Purok 1");
            }
            styleComboBox(purokCombo);
            gbc.gridx = 1;
            formPanel.add(purokCombo, gbc);
            
            row++;
            
            // Row 10: House Number
            gbc.gridx = 0; gbc.gridy = row;
            JLabel houseNumberLabel = new JLabel("House Number:");
            houseNumberLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            houseNumberLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(houseNumberLabel, gbc);
            
            JTextField houseNumberField = new JTextField(household.getHouseNumber() != null ? household.getHouseNumber() : "");
            styleTextField(houseNumberField);
            gbc.gridx = 1;
            formPanel.add(houseNumberField, gbc);
            
            row++;
            
            // Row 11: Status
            gbc.gridx = 0; gbc.gridy = row;
            JLabel statusLabel = new JLabel("Status:*");
            statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            statusLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(statusLabel, gbc);
            
            String[] statusOptions = {"Active", "Inactive", "Pending"};
            JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
            String currentStatus = household.getStatus();
            if (currentStatus != null && !currentStatus.trim().isEmpty()) {
                statusCombo.setSelectedItem(currentStatus);
            } else {
                statusCombo.setSelectedItem("Active");
            }
            styleComboBox(statusCombo);
            gbc.gridx = 1;
            formPanel.add(statusCombo, gbc);
            
            row++;
            
            // Row 12: Contact Number
            gbc.gridx = 0; gbc.gridy = row;
            JLabel contactLabel = new JLabel("Contact Number:");
            contactLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            contactLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(contactLabel, gbc);
            
            JTextField contactField = new JTextField(household.getContactNumber() != null ? household.getContactNumber() : "");
            styleTextField(contactField);
            gbc.gridx = 1;
            formPanel.add(contactField, gbc);
            
            row++;
            
            // Row 13: Address
            gbc.gridx = 0; gbc.gridy = row;
            JLabel addressLabel = new JLabel("Address:");
            addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            addressLabel.setForeground(TEXT_PRIMARY);
            formPanel.add(addressLabel, gbc);
            
            JTextArea addressArea = new JTextArea(household.getAddress() != null ? household.getAddress() : "", 3, 30);
            addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            addressArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 10, 8, 10)
            ));
            addressArea.setBackground(new Color(250, 250, 250));
            addressArea.setLineWrap(true);
            addressArea.setWrapStyleWord(true);
            
            JScrollPane addressScroll = new JScrollPane(addressArea);
            addressScroll.setPreferredSize(new Dimension(200, 80));
            gbc.gridx = 1;
            formPanel.add(addressScroll, gbc);
            
            JScrollPane formScroll = new JScrollPane(formPanel);
            formScroll.setBorder(null);
            
            contentPanel.add(formScroll, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
            buttonPanel.setBackground(BACKGROUND_COLOR);
            
            JButton saveButton = createMediumButton("💾 Save Changes", SUCCESS_COLOR);
            saveButton.addActionListener(e -> {
                // Validate required fields
                String firstName = firstNameField.getText().trim();
                String lastName = lastNameField.getText().trim();
                
                if (firstName.isEmpty() || lastName.isEmpty()) {
                    JOptionPane.showMessageDialog(editDialog, 
                        "Head First Name and Last Name are required!", 
                        "Validation Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    // Update household object with new values
                    household.setHeadFirstName(firstName);
                    household.setHeadLastName(lastName);
                    household.setHeadMiddleName(middleNameField.getText().trim());
                    household.setHeadAge((Integer) ageSpinner.getValue());
                    household.setHeadGender((String) genderCombo.getSelectedItem());
                    household.setHeadEducationLevel((String) educationCombo.getSelectedItem());
                    household.setHeadOccupation(occupationField.getText().trim());
                    household.setPurok((String) purokCombo.getSelectedItem());
                    household.setHouseNumber(houseNumberField.getText().trim());
                    household.setStatus((String) statusCombo.getSelectedItem());
                    household.setContactNumber(contactField.getText().trim());
                    household.setAddress(addressArea.getText().trim());
                    
                    System.out.println("DEBUG - Attempting to save household: " + household.getHouseholdId());
                    System.out.println("Name: " + household.getHeadFirstName() + " " + household.getHeadLastName());
                    System.out.println("Age: " + household.getHeadAge());
                    System.out.println("Gender: " + household.getHeadGender());
                    
                    boolean success = householdService.updateHousehold(household);
                    
                    if (success) {
                        JOptionPane.showMessageDialog(editDialog,
                            "✅ Household information updated successfully!",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        editDialog.dispose();
                        loadHouseholds(); // Refresh the table
                    } else {
                        JOptionPane.showMessageDialog(editDialog,
                            "❌ Failed to update household information. Please check the database connection.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(editDialog,
                        "Error updating household: " + ex.getMessage(),
                        "System Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
            
            JButton cancelButton = createMediumButton("❌ Cancel", DANGER_COLOR);
            cancelButton.addActionListener(e -> editDialog.dispose());
            
            buttonPanel.add(saveButton);
            buttonPanel.add(cancelButton);
            
            editDialog.add(headerPanel, BorderLayout.NORTH);
            editDialog.add(contentPanel, BorderLayout.CENTER);
            editDialog.add(buttonPanel, BorderLayout.SOUTH);
            
            editDialog.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error loading edit dialog: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void addInfoField(JPanel panel, String label, String value) {
        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.BOLD, 12));
        labelComp.setForeground(TEXT_PRIMARY);
        
        JLabel valueComp = new JLabel(value != null ? value : "N/A");
        valueComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        valueComp.setForeground(TEXT_SECONDARY);
        
        panel.add(labelComp);
        panel.add(valueComp);
    }
    
    private void initUI() {
        getContentPane().setBackground(BACKGROUND_COLOR);
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftPanel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("🏘️");
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        
        JLabel title = new JLabel("Barangay Census System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        
        leftPanel.add(logoLabel);
        leftPanel.add(title);
        
        JLabel userLabel = new JLabel("👤 " + CurrentUser.getFullName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);

        header.add(leftPanel, BorderLayout.WEST);
        header.add(userLabel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BACKGROUND_COLOR);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                                            createSidebar("Household List"), 
                                            createListContent());
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        splitPane.setResizeWeight(0.0);
        
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createSidebar(String currentPage) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(250, 250, 252));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(0, 10, 20, 10));
        
        JLabel sidebarLogo = new JLabel("🏘️ Barangay Census");
        sidebarLogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sidebarLogo.setForeground(TEXT_PRIMARY);
        logoPanel.add(sidebarLogo);
        sidebar.add(logoPanel);
        
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(220, 220, 220));
        sidebar.add(separator);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        for (String item : menuItems) {
            JButton menuBtn = new JButton(item);
            menuBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            menuBtn.setMaximumSize(new Dimension(190, 45));
            menuBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            menuBtn.setForeground(Color.BLACK);
            menuBtn.setBackground(new Color(250, 250, 252));
            menuBtn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(250, 250, 252)),
                new EmptyBorder(12, 15, 12, 15)
            ));
            menuBtn.setHorizontalAlignment(SwingConstants.LEFT);
            menuBtn.setFocusPainted(false);
            menuBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            if (item.equals(currentPage)) {
                menuBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                menuBtn.setForeground(PRIMARY_COLOR);
                menuBtn.setBackground(new Color(236, 239, 255));
                menuBtn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 3, 0, 0, PRIMARY_COLOR),
                    new EmptyBorder(12, 15, 12, 15)
                ));
            }
            
            menuBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    if (!item.equals(currentPage)) {
                        menuBtn.setBackground(new Color(245, 245, 245));
                        menuBtn.setForeground(Color.BLACK);
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (!item.equals(currentPage)) {
                        menuBtn.setBackground(new Color(250, 250, 252));
                        menuBtn.setForeground(Color.BLACK);
                    }
                }
            });
            
            final String pageName = item;
            menuBtn.addActionListener(e -> {
                navigateToPage(pageName);
            });
            
            sidebar.add(menuBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        JSeparator bottomSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        bottomSeparator.setForeground(new Color(220, 220, 220));
        sidebar.add(bottomSeparator);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoutBtn.setMaximumSize(new Dimension(190, 45));
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        logoutBtn.setForeground(Color.BLACK);
        logoutBtn.setBackground(new Color(255, 245, 245));
        logoutBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 220, 220), 1),
            new EmptyBorder(12, 15, 12, 15)
        ));
        logoutBtn.setHorizontalAlignment(SwingConstants.LEFT);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        logoutBtn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutBtn.setBackground(new Color(255, 235, 235));
                logoutBtn.setForeground(Color.BLACK);
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(255, 245, 245));
                logoutBtn.setForeground(Color.BLACK);
            }
        });
        
        logoutBtn.addActionListener(e -> {
            try {
                logout();
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        
        sidebar.add(logoutBtn);
        
        return sidebar;
    }
    
    private JPanel createListContent() {
        JPanel mainContent = new JPanel(new BorderLayout(0, 15));
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.Y_AXIS));
        topSection.setBackground(BACKGROUND_COLOR);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("📋 Household List");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        
        JLabel subtitle = new JLabel("View and manage all registered households");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        
        JButton addButton = createLargeButton("➕ Add Household", SUCCESS_COLOR);
        addButton.addActionListener(e -> {
            dispose();
            try {
                new AddHouseholdPage().setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        JPanel titleLeft = new JPanel(new BorderLayout());
        titleLeft.setOpaque(false);
        titleLeft.add(title, BorderLayout.NORTH);
        titleLeft.add(subtitle, BorderLayout.SOUTH);
        
        titlePanel.add(titleLeft, BorderLayout.WEST);
        titlePanel.add(addButton, BorderLayout.EAST);
        titlePanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        topSection.add(titlePanel);
        
        mainContent.add(topSection, BorderLayout.NORTH);
        
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        tablePanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        tablePanel.add(createTablePanel(), BorderLayout.CENTER);
        
        mainContent.add(tablePanel, BorderLayout.CENTER);
        
        return mainContent;
    }
    
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BACKGROUND_COLOR);
        
        String[] columns = {"Household ID", "Head Name", "Age", "Gender", "Education", "Occupation", "Purok", "Members", "Status", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 2) return Integer.class;
                if (columnIndex == 7) return Integer.class;
                return String.class;
            }
        };
        
        table = new JTable(tableModel);
        table.setRowHeight(45);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(240, 242, 245));
        table.getTableHeader().setForeground(TEXT_PRIMARY);
        table.getTableHeader().setBorder(new EmptyBorder(5, 10, 5, 10));
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (i != 1) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }
        
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(60);
        table.getColumnModel().getColumn(3).setPreferredWidth(70);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        table.getColumnModel().getColumn(6).setPreferredWidth(80);
        table.getColumnModel().getColumn(7).setPreferredWidth(70);
        table.getColumnModel().getColumn(8).setPreferredWidth(90);
        table.getColumnModel().getColumn(9).setPreferredWidth(220);
        
        table.getColumnModel().getColumn(8).setCellRenderer(new StatusCellRenderer());
        table.getColumnModel().getColumn(9).setCellRenderer(new ActionButtonCellRenderer());
        table.getColumnModel().getColumn(9).setCellEditor(new ActionButtonCellEditor());
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        scrollPane.getViewport().setBackground(CARD_COLOR);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton refreshButton = createMediumButton("🔄 Refresh", PRIMARY_COLOR);
        refreshButton.addActionListener(e -> loadHouseholds());
        
        JButton exportButton = createMediumButton("📤 Export to Excel", ACCENT_COLOR);
        exportButton.addActionListener(e -> exportToExcel());
        
        JButton deleteButton = createMediumButton("🗑️ Delete Selected", DANGER_COLOR);
        deleteButton.addActionListener(e -> {
            try {
                deleteSelectedHouseholds();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        
        bottomPanel.add(refreshButton);
        bottomPanel.add(exportButton);
        bottomPanel.add(deleteButton);
        
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        return tablePanel;
    }
    
    private void exportToExcel() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "No data to export!",
                "Empty Table",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Excel File");
        fileChooser.setSelectedFile(new java.io.File("Household_List_" + 
            new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            
            String filePath = selectedFile.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".csv")) {
                selectedFile = new java.io.File(filePath + ".csv");
            }
            
            final java.io.File fileToSave = selectedFile;
            
            JDialog progressDialog = new JDialog(this, "Exporting...", true);
            progressDialog.setSize(300, 100);
            progressDialog.setLocationRelativeTo(this);
            progressDialog.setLayout(new BorderLayout());
            
            JLabel progressLabel = new JLabel("Exporting household data to CSV...", SwingConstants.CENTER);
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            
            progressDialog.add(progressLabel, BorderLayout.NORTH);
            progressDialog.add(progressBar, BorderLayout.CENTER);
            progressDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            
            Thread exportThread = new Thread(() -> {
                try {
                    SwingUtilities.invokeLater(() -> progressDialog.setVisible(true));
                    
                    try (FileWriter writer = new FileWriter(fileToSave)) {
                        writer.write("Barangay Census System - Household List\n");
                        writer.write("Generated Date: " + new Date() + "\n");
                        writer.write("Generated By: " + CurrentUser.getFullName() + "\n");
                        writer.write("Total Records: " + tableModel.getRowCount() + "\n\n");
                        
                        for (int col = 0; col < tableModel.getColumnCount() - 1; col++) {
                            writer.write("\"" + tableModel.getColumnName(col) + "\"");
                            if (col < tableModel.getColumnCount() - 2) {
                                writer.write(",");
                            }
                        }
                        writer.write("\n");
                        
                        for (int row = 0; row < tableModel.getRowCount(); row++) {
                            for (int col = 0; col < tableModel.getColumnCount() - 1; col++) {
                                Object value = tableModel.getValueAt(row, col);
                                if (value != null) {
                                    String cellValue = value.toString().replace("\"", "\"\"");
                                    writer.write("\"" + cellValue + "\"");
                                } else {
                                    writer.write("\"\"");
                                }
                                if (col < tableModel.getColumnCount() - 2) {
                                    writer.write(",");
                                }
                            }
                            writer.write("\n");
                        }
                    }
                    
                    SwingUtilities.invokeLater(() -> {
                        progressDialog.dispose();
                        JOptionPane.showMessageDialog(HouseholdListPage.this,
                            "✅ Household list exported to CSV successfully!\n\n" +
                            "File saved to:\n" + fileToSave.getAbsolutePath(),
                            "Export Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                    
                } catch (Exception e) {
                    SwingUtilities.invokeLater(() -> {
                        progressDialog.dispose();
                        JOptionPane.showMessageDialog(HouseholdListPage.this,
                            "❌ Error exporting to CSV: " + e.getMessage(),
                            "Export Error",
                            JOptionPane.ERROR_MESSAGE);
                    });
                }
            });
            
            exportThread.start();
        }
    }
    
    class ActionButtonCellRenderer extends DefaultTableCellRenderer {
        private JPanel buttonPanel;
        
        public ActionButtonCellRenderer() {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 3));
            buttonPanel.setOpaque(true);
            buttonPanel.setBackground(CARD_COLOR);
            
            JButton viewButton = createTableCellButton("👁️ View", PRIMARY_COLOR);
            JButton editButton = createTableCellButton("✏️ Edit", SUCCESS_COLOR);
            
            buttonPanel.add(viewButton);
            buttonPanel.add(editButton);
        }
        
        private JButton createTableCellButton(String text, Color baseColor) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setForeground(Color.BLACK);
            button.setBackground(baseColor == PRIMARY_COLOR ? 
                new Color(236, 239, 255) : new Color(235, 255, 240));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(baseColor, 1),
                new EmptyBorder(6, 12, 6, 12)
            ));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setPreferredSize(new Dimension(85, 35));
            
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(baseColor);
                    button.setForeground(Color.BLACK);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(baseColor.darker(), 1),
                        new EmptyBorder(6, 12, 6, 12)
                    ));
                }
                public void mouseExited(MouseEvent e) {
                    button.setBackground(baseColor == PRIMARY_COLOR ? 
                        new Color(236, 239, 255) : new Color(235, 255, 240));
                    button.setForeground(Color.BLACK);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(baseColor, 1),
                        new EmptyBorder(6, 12, 6, 12)
                    ));
                }
            });
            
            return button;
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            if (isSelected) {
                buttonPanel.setBackground(table.getSelectionBackground());
            } else {
                buttonPanel.setBackground(CARD_COLOR);
            }
            
            return buttonPanel;
        }
    }
    
    class ActionButtonCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel buttonPanel;
        private int currentRow;
        
        public ActionButtonCellEditor() {
            buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 3));
            buttonPanel.setOpaque(true);
            buttonPanel.setBackground(CARD_COLOR);
            
            JButton viewButton = createTableCellButton("👁️ View", PRIMARY_COLOR);
            JButton editButton = createTableCellButton("✏️ Edit", SUCCESS_COLOR);
            
            viewButton.addActionListener(e -> {
                String householdId = (String) table.getValueAt(currentRow, 0);
                String headName = (String) table.getValueAt(currentRow, 1);
                showHouseholdDialog(householdId, headName);
                fireEditingStopped();
            });
            
            editButton.addActionListener(e -> {
                String householdId = (String) table.getValueAt(currentRow, 0);
                showEditHouseholdDialog(householdId);
                fireEditingStopped();
            });
            
            buttonPanel.add(viewButton);
            buttonPanel.add(editButton);
        }
        
        private JButton createTableCellButton(String text, Color baseColor) {
            JButton button = new JButton(text);
            button.setFont(new Font("Segoe UI", Font.BOLD, 11));
            button.setForeground(Color.BLACK);
            button.setBackground(baseColor == PRIMARY_COLOR ? 
                new Color(236, 239, 255) : new Color(235, 255, 240));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(baseColor, 1),
                new EmptyBorder(6, 12, 6, 12)
            ));
            button.setFocusPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setPreferredSize(new Dimension(85, 35));
            
            button.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    button.setBackground(baseColor);
                    button.setForeground(Color.BLACK);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(baseColor.darker(), 1),
                        new EmptyBorder(6, 12, 6, 12)
                    ));
                }
                public void mouseExited(MouseEvent e) {
                    button.setBackground(baseColor == PRIMARY_COLOR ? 
                        new Color(236, 239, 255) : new Color(235, 255, 240));
                    button.setForeground(Color.BLACK);
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(baseColor, 1),
                        new EmptyBorder(6, 12, 6, 12)
                    ));
                }
            });
            
            return button;
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            currentRow = row;
            
            if (isSelected) {
                buttonPanel.setBackground(table.getSelectionBackground());
            } else {
                buttonPanel.setBackground(CARD_COLOR);
            }
            
            return buttonPanel;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "View/Edit";
        }
    }
    
    class StatusCellRenderer extends DefaultTableCellRenderer {
        public StatusCellRenderer() {
            setHorizontalAlignment(JLabel.CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            String status = (String) value;
            setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
            setOpaque(true);
            
            switch (status) {
                case "Active":
                    setForeground(Color.BLACK);
                    setBackground(SUCCESS_COLOR);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(SUCCESS_COLOR.darker(), 1),
                        new EmptyBorder(6, 10, 6, 10)
                    ));
                    break;
                case "Inactive":
                    setForeground(Color.BLACK);
                    setBackground(DANGER_COLOR);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(DANGER_COLOR.darker(), 1),
                        new EmptyBorder(6, 10, 6, 10)
                    ));
                    break;
                case "Pending":
                    setForeground(Color.BLACK);
                    setBackground(WARNING_COLOR);
                    setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(WARNING_COLOR.darker(), 1),
                        new EmptyBorder(6, 10, 6, 10)
                    ));
                    break;
                default:
                    setForeground(TEXT_PRIMARY);
                    setBackground(CARD_COLOR);
            }
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }
            
            return this;
        }
    }
    
    private JButton createLargeButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(12, 20, 12, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(180, 45));
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor.brighter().darker(), 1),
                    new EmptyBorder(12, 20, 12, 20)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor.darker(), 1),
                    new EmptyBorder(12, 20, 12, 20)
                ));
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(bgColor.darker());
            }
        });
        
        return button;
    }
    
    private JButton createMediumButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            new EmptyBorder(8, 15, 8, 15)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(140, 40));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor.brighter().darker(), 1),
                    new EmptyBorder(8, 15, 8, 15)
                ));
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor.darker(), 1),
                    new EmptyBorder(8, 15, 8, 15)
                ));
            }
        });
        
        return button;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(new Color(250, 250, 250));
        field.setForeground(TEXT_PRIMARY);
        field.setPreferredSize(new Dimension(200, 40));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(new Color(250, 250, 250));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        combo.setForeground(TEXT_PRIMARY);
        combo.setPreferredSize(new Dimension(200, 40));
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 12, 8, 12)
        ));
        spinner.setBackground(new Color(250, 250, 250));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(new Color(250, 250, 250));
        spinner.setPreferredSize(new Dimension(200, 40));
    }
    
    private void deleteSelectedHouseholds() throws SQLException {
        int[] selectedRows = table.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this,
                "Please select households to delete.",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete " + selectedRows.length + " household(s)?\nThis action cannot be undone.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            for (int i = selectedRows.length - 1; i >= 0; i--) {
                int row = selectedRows[i];
                String householdId = (String) table.getValueAt(row, 0);
                try {
                    householdService.deleteHousehold(householdId);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                        "Error deleting household " + householdId + ": " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            loadHouseholds();
            JOptionPane.showMessageDialog(this,
                "✅ Successfully deleted " + selectedRows.length + " household(s)",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new HouseholdListPage().setVisible(true);
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                    "Error starting application: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}