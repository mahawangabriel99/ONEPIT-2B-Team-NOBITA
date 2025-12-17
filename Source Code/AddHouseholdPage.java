import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;

public class AddHouseholdPage extends JFrame {
    private String[] menuItems = {"Dashboard", "Household List", "Add Household", "Census Reports", "Settings"};
    
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
    private final Color INFO_COLOR = new Color(52, 152, 219);
    private final Color CONFIRM_COLOR = new Color(255, 235, 59);
    
    private JTextField householdIdField;
    private JTextField headFirstNameField;
    private JTextField headLastNameField;
    private JTextField headMiddleNameField;
    private JComboBox<String> purokCombo;
    private JTextField houseNumberField;
    private JTextField contactField;
    private JTextArea addressArea;
    private JSpinner membersSpinner;
    private JComboBox<String> statusCombo;
    private JTextField registrationDateField;
    
    // New fields for head information
    private JSpinner headAgeSpinner;
    private JComboBox<String> headGenderCombo;
    private JComboBox<String> headRelationshipCombo;
    private JComboBox<String> headEducationCombo;
    private JTextField headOccupationField;
    
    private HouseholdService householdService;
    
    public AddHouseholdPage() throws SQLException {
        setTitle("Barangay Census System - Add Household");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 900);
        setLocationRelativeTo(null);
        
        this.householdService = new HouseholdService();
        
        initUI();
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
                                            createSidebar("Add Household"), 
                                            createFormContent());
        splitPane.setDividerLocation(220);
        splitPane.setDividerSize(1);
        splitPane.setBorder(null);
        
        panel.add(splitPane, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createSidebar(String currentPage) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(250, 250, 252));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));
        
        // Logo in sidebar
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        logoPanel.setOpaque(false);
        logoPanel.setBorder(new EmptyBorder(0, 10, 20, 10));
        
        JLabel sidebarLogo = new JLabel("🏘️ Barangay Census");
        sidebarLogo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sidebarLogo.setForeground(TEXT_PRIMARY);
        logoPanel.add(sidebarLogo);
        sidebar.add(logoPanel);
        
        // Divider
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
            menuBtn.addActionListener(e -> navigateToPage(pageName));
            
            sidebar.add(menuBtn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        // Divider before logout
        JSeparator bottomSeparator = new JSeparator(SwingConstants.HORIZONTAL);
        bottomSeparator.setForeground(new Color(220, 220, 220));
        sidebar.add(bottomSeparator);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Logout button
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
        
        logoutBtn.addActionListener(e -> logout());
        
        sidebar.add(logoutBtn);
        
        return sidebar;
    }
    
    private JPanel createFormContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("➕ Add New Household");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        
        JLabel subtitle = new JLabel("Register a new household in the barangay");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        titlePanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        mainContent.add(titlePanel, BorderLayout.NORTH);
        
        // Form panel with scroll pane for safety
        JPanel formPanel = createHouseholdForm();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        mainContent.add(scrollPane, BorderLayout.CENTER);
        
        return mainContent;
    }
    
    private JPanel createHouseholdForm() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 8, 6, 8);
        
        // Household ID
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel idLabel = new JLabel("Household ID:*");
        idLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        idLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(idLabel, gbc);
        
        householdIdField = new JTextField(15);
        styleTextField(householdIdField);
        gbc.gridx = 1;
        formPanel.add(householdIdField, gbc);
        
        // Head First Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel firstNameLabel = new JLabel("Head First Name:*");
        firstNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        firstNameLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(firstNameLabel, gbc);
        
        headFirstNameField = new JTextField(15);
        styleTextField(headFirstNameField);
        gbc.gridx = 1;
        formPanel.add(headFirstNameField, gbc);
        
        // Head Last Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lastNameLabel = new JLabel("Head Last Name:*");
        lastNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lastNameLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(lastNameLabel, gbc);
        
        headLastNameField = new JTextField(15);
        styleTextField(headLastNameField);
        gbc.gridx = 1;
        formPanel.add(headLastNameField, gbc);
        
        // Head Middle Name
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel middleNameLabel = new JLabel("Head Middle Name:");
        middleNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        middleNameLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(middleNameLabel, gbc);
        
        headMiddleNameField = new JTextField(15);
        styleTextField(headMiddleNameField);
        gbc.gridx = 1;
        formPanel.add(headMiddleNameField, gbc);
        
        // Head Age - CHANGED TO START AT 1 YEAR OLD
        gbc.gridx = 0;
        gbc.gridy = 4;
        JLabel ageLabel = new JLabel("Head Age:*");
        ageLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ageLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(ageLabel, gbc);
        
        headAgeSpinner = new JSpinner(new SpinnerNumberModel(25, 1, 120, 1)); // Starts at 1 year old
        styleSpinner(headAgeSpinner);
        gbc.gridx = 1;
        formPanel.add(headAgeSpinner, gbc);
        
        // Head Gender
        gbc.gridx = 0;
        gbc.gridy = 5;
        JLabel genderLabel = new JLabel("Head Gender:*");
        genderLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        genderLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(genderLabel, gbc);
        
        headGenderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        styleComboBox(headGenderCombo);
        gbc.gridx = 1;
        formPanel.add(headGenderCombo, gbc);
        
        // Relationship to Head (for head, this is always "Head")
        gbc.gridx = 0;
        gbc.gridy = 6;
        JLabel relationshipLabel = new JLabel("Relationship:");
        relationshipLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        relationshipLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(relationshipLabel, gbc);
        
        headRelationshipCombo = new JComboBox<>(new String[]{"Head", "Spouse", "Child", "Parent", "Sibling", "Other Relative", "Non-Relative"});
        headRelationshipCombo.setSelectedItem("Head");
        headRelationshipCombo.setEnabled(false); // Head is always "Head"
        styleComboBox(headRelationshipCombo);
        gbc.gridx = 1;
        formPanel.add(headRelationshipCombo, gbc);
        
        // Head Education Level
        gbc.gridx = 0;
        gbc.gridy = 7;
        JLabel educationLabel = new JLabel("Head Education Level:");
        educationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        educationLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(educationLabel, gbc);
        
        headEducationCombo = new JComboBox<>(new String[]{
            "No Formal Education",
            "Elementary Level",
            "Elementary Graduate",
            "High School Level",
            "High School Graduate",
            "Vocational",
            "College Level",
            "College Graduate",
            "Post Graduate"
        });
        styleComboBox(headEducationCombo);
        gbc.gridx = 1;
        formPanel.add(headEducationCombo, gbc);
        
        // Head Occupation
        gbc.gridx = 0;
        gbc.gridy = 8;
        JLabel occupationLabel = new JLabel("Head Occupation:");
        occupationLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        occupationLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(occupationLabel, gbc);
        
        headOccupationField = new JTextField(15);
        styleTextField(headOccupationField);
        gbc.gridx = 1;
        formPanel.add(headOccupationField, gbc);
        
        // Purok
        gbc.gridx = 0;
        gbc.gridy = 9;
        JLabel purokLabel = new JLabel("Purok:*");
        purokLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        purokLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(purokLabel, gbc);
        
        purokCombo = new JComboBox<>(new String[]{"Purok 1", "Purok 2", "Purok 3", "Purok 4", "Purok 5", "Purok 6", "Purok 7"});
        styleComboBox(purokCombo);
        gbc.gridx = 1;
        formPanel.add(purokCombo, gbc);
        
        // House Number
        gbc.gridx = 0;
        gbc.gridy = 10;
        JLabel houseNoLabel = new JLabel("House Number:");
        houseNoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        houseNoLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(houseNoLabel, gbc);
        
        houseNumberField = new JTextField(15);
        styleTextField(houseNumberField);
        gbc.gridx = 1;
        formPanel.add(houseNumberField, gbc);
        
        // Contact Number
        gbc.gridx = 0;
        gbc.gridy = 11;
        JLabel contactLabel = new JLabel("Contact Number:*");
        contactLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        contactLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(contactLabel, gbc);
        
        contactField = new JTextField(15);
        styleTextField(contactField);
        gbc.gridx = 1;
        formPanel.add(contactField, gbc);
        
        // Address
        gbc.gridx = 0;
        gbc.gridy = 12;
        JLabel addressLabel = new JLabel("Address:*");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addressLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(addressLabel, gbc);
        
        addressArea = new JTextArea(3, 15);
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        addressArea.setBackground(new Color(250, 250, 250));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setPreferredSize(new Dimension(180, 80));
        gbc.gridx = 1;
        formPanel.add(addressScroll, gbc);
        
        // Members Count
        gbc.gridx = 0;
        gbc.gridy = 13;
        JLabel membersLabel = new JLabel("Number of Members:*");
        membersLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        membersLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(membersLabel, gbc);
        
        membersSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        styleSpinner(membersSpinner);
        gbc.gridx = 1;
        formPanel.add(membersSpinner, gbc);
        
        // Status
        gbc.gridx = 0;
        gbc.gridy = 14;
        JLabel statusLabel = new JLabel("Status:*");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(statusLabel, gbc);
        
        statusCombo = new JComboBox<>(new String[]{"Active", "Inactive", "Pending"});
        styleComboBox(statusCombo);
        gbc.gridx = 1;
        formPanel.add(statusCombo, gbc);
        
        // Registration Date
        gbc.gridx = 0;
        gbc.gridy = 15;
        JLabel dateLabel = new JLabel("Registration Date:*");
        dateLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dateLabel.setForeground(TEXT_PRIMARY);
        formPanel.add(dateLabel, gbc);
        
        registrationDateField = new JTextField(15);
        styleTextField(registrationDateField);
        registrationDateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        gbc.gridx = 1;
        formPanel.add(registrationDateField, gbc);
        
        // Required fields note
        gbc.gridx = 0;
        gbc.gridy = 16;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 8, 10, 8);
        
        JLabel requiredNote = new JLabel("* Required fields");
        requiredNote.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        requiredNote.setForeground(DANGER_COLOR);
        formPanel.add(requiredNote, gbc);
        
        // Confirmation Button
        gbc.gridx = 0;
        gbc.gridy = 17;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 8, 12, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JButton confirmButton = createMediumButton("✅ Confirm Entries", CONFIRM_COLOR);
        confirmButton.addActionListener(e -> confirmAndSave());
        formPanel.add(confirmButton, gbc);
        
        // Separator
        gbc.gridy = 18;
        gbc.insets = new Insets(8, 8, 8, 8);
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setForeground(new Color(200, 200, 200));
        formPanel.add(separator, gbc);
        
        // Action buttons panel
        gbc.gridy = 19;
        gbc.insets = new Insets(10, 8, 5, 8);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
        
        // Preview Button
        JButton previewButton = createMediumButton("👁️ Preview", INFO_COLOR);
        previewButton.setPreferredSize(new Dimension(120, 40));
        previewButton.setToolTipText("Preview household information");
        previewButton.addActionListener(e -> showPreview());
        
        // Clear Button
        JButton clearButton = createMediumButton("🗑️ Clear", WARNING_COLOR);
        clearButton.setPreferredSize(new Dimension(120, 40));
        clearButton.setToolTipText("Clear all form fields");
        clearButton.addActionListener(e -> clearForm());
        
        // Cancel Button
        JButton cancelButton = createMediumButton("❌ Cancel", DANGER_COLOR);
        cancelButton.setPreferredSize(new Dimension(120, 40));
        cancelButton.setToolTipText("Cancel and return to household list");
        cancelButton.addActionListener(e -> {
            dispose();
            try {
                new HouseholdListPage().setVisible(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
        
        // Add buttons
        buttonPanel.add(previewButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(cancelButton);
        
        formPanel.add(buttonPanel, gbc);
        
        // Add some padding at the bottom
        gbc.gridy = 20;
        gbc.weighty = 1.0;
        formPanel.add(Box.createVerticalGlue(), gbc);
        
        return formPanel;
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
        button.setPreferredSize(new Dimension(180, 40));
        
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
    
    private void confirmAndSave() {
        if (!validateForm()) {
            return;
        }
        
        String message = "<html><div style='font-family: Segoe UI; font-size: 12px;'>" +
                        "<b>Please confirm the information:</b><br><br>" +
                        "<table style='border-collapse: collapse; width: 100%;'>" +
                        "<tr><td style='padding: 4px 0;'><b>Household ID:</b></td><td>" + householdIdField.getText().trim() + "</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Head Name:</b></td><td>" + 
                        headFirstNameField.getText().trim() + " " + 
                        (headMiddleNameField.getText().trim().isEmpty() ? "" : headMiddleNameField.getText().trim() + " ") + 
                        headLastNameField.getText().trim() + "</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Head Age:</b></td><td>" + headAgeSpinner.getValue() + " years old</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Head Gender:</b></td><td>" + headGenderCombo.getSelectedItem() + "</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Head Education:</b></td><td>" + headEducationCombo.getSelectedItem() + "</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Head Occupation:</b></td><td>" + headOccupationField.getText().trim() + "</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Purok:</b></td><td>" + purokCombo.getSelectedItem() + "</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Contact:</b></td><td>" + contactField.getText().trim() + "</td></tr>" +
                        "<tr><td style='padding: 4px 0;'><b>Members:</b></td><td>" + membersSpinner.getValue() + "</td></tr>" +
                        "</table><br>" +
                        "<i>Click OK to save.</i>" +
                        "</div></html>";
        
        int response = JOptionPane.showConfirmDialog(this,
            message,
            "Confirm Information",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (response == JOptionPane.OK_OPTION) {
            saveHousehold();
        }
    }
    
    private boolean validateForm() {
        StringBuilder errors = new StringBuilder();
        
        if (householdIdField.getText().trim().isEmpty()) {
            errors.append("• Household ID is required\n");
        }
        
        if (headFirstNameField.getText().trim().isEmpty()) {
            errors.append("• Head First Name is required\n");
        }
        
        if (headLastNameField.getText().trim().isEmpty()) {
            errors.append("• Head Last Name is required\n");
        }
        
        // CHANGED: Age validation now checks for minimum 1 year old
        if (headAgeSpinner.getValue() == null || ((Integer)headAgeSpinner.getValue()) < 1) {
            errors.append("• Head Age must be at least 1 year old\n");
        }
        
        if (contactField.getText().trim().isEmpty()) {
            errors.append("• Contact Number is required\n");
        }
        
        if (addressArea.getText().trim().isEmpty()) {
            errors.append("• Address is required\n");
        }
        
        if (errors.length() > 0) {
            JOptionPane.showMessageDialog(this,
                "Please fix the following errors:\n\n" + errors.toString(),
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(new Color(250, 250, 250));
        field.setForeground(TEXT_PRIMARY);
        field.setPreferredSize(new Dimension(180, 40));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        combo.setBackground(new Color(250, 250, 250));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        combo.setForeground(TEXT_PRIMARY);
        combo.setPreferredSize(new Dimension(180, 40));
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        spinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        spinner.setBackground(new Color(250, 250, 250));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(new Color(250, 250, 250));
        spinner.setPreferredSize(new Dimension(180, 40));
    }
    
    private void showPreview() {
        if (householdIdField.getText().trim().isEmpty() ||
            headFirstNameField.getText().trim().isEmpty() ||
            headLastNameField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields!", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        StringBuilder preview = new StringBuilder();
        preview.append("<html><body style='font-family: Segoe UI; font-size: 12px;'>");
        preview.append("<h3 style='color: #4361ee;'>Preview</h3>");
        preview.append("<table style='border-collapse: collapse; width: 100%;'>");
        
        addPreviewRow(preview, "Household ID:", householdIdField.getText().trim());
        addPreviewRow(preview, "Head Name:", 
            headFirstNameField.getText().trim() + " " + 
            (headMiddleNameField.getText().trim().isEmpty() ? "" : headMiddleNameField.getText().trim() + " ") + 
            headLastNameField.getText().trim());
        addPreviewRow(preview, "Head Age:", headAgeSpinner.getValue().toString() + " years");
        addPreviewRow(preview, "Head Gender:", (String) headGenderCombo.getSelectedItem());
        addPreviewRow(preview, "Relationship:", (String) headRelationshipCombo.getSelectedItem());
        addPreviewRow(preview, "Education Level:", (String) headEducationCombo.getSelectedItem());
        addPreviewRow(preview, "Occupation:", headOccupationField.getText().trim());
        addPreviewRow(preview, "Purok:", (String) purokCombo.getSelectedItem());
        addPreviewRow(preview, "House Number:", houseNumberField.getText().trim());
        addPreviewRow(preview, "Contact:", contactField.getText().trim());
        addPreviewRow(preview, "Address:", addressArea.getText().trim());
        addPreviewRow(preview, "Members:", membersSpinner.getValue().toString());
        addPreviewRow(preview, "Status:", (String) statusCombo.getSelectedItem());
        addPreviewRow(preview, "Reg. Date:", registrationDateField.getText().trim());
        
        preview.append("</table>");
        preview.append("</body></html>");
        
        JDialog previewDialog = new JDialog(this, "Preview", true);
        previewDialog.setSize(450, 450);
        previewDialog.setLocationRelativeTo(this);
        
        JLabel previewLabel = new JLabel(preview.toString());
        previewLabel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        JButton okButton = createMediumButton("OK", PRIMARY_COLOR);
        okButton.setPreferredSize(new Dimension(100, 35));
        okButton.addActionListener(e -> previewDialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.add(okButton);
        
        previewDialog.add(previewLabel, BorderLayout.CENTER);
        previewDialog.add(buttonPanel, BorderLayout.SOUTH);
        previewDialog.setVisible(true);
    }
    
    private void addPreviewRow(StringBuilder preview, String label, String value) {
        preview.append("<tr>");
        preview.append("<td style='padding: 4px 0; width: 120px; font-weight: bold; color: #555;'>");
        preview.append(label);
        preview.append("</td>");
        preview.append("<td style='padding: 4px 0; color: #222;'>");
        preview.append(value.isEmpty() ? "<i>Not specified</i>" : value);
        preview.append("</td>");
        preview.append("</tr>");
    }
    
    private void saveHousehold() {
        if (householdIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Household ID is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            householdIdField.requestFocus();
            return;
        }
        
        if (headFirstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Head First Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            headFirstNameField.requestFocus();
            return;
        }
        
        if (headLastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Head Last Name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            headLastNameField.requestFocus();
            return;
        }
        
        // CHANGED: Age validation now checks for minimum 1 year old
        if (((Integer)headAgeSpinner.getValue()) < 1) {
            JOptionPane.showMessageDialog(this, "Head Age must be at least 1 year old!", "Validation Error", JOptionPane.ERROR_MESSAGE);
            headAgeSpinner.requestFocus();
            return;
        }
        
        try {
            Household household = new Household();
            household.setHouseholdId(householdIdField.getText().trim());
            household.setHeadFirstName(headFirstNameField.getText().trim());
            household.setHeadLastName(headLastNameField.getText().trim());
            household.setHeadMiddleName(headMiddleNameField.getText().trim());
            
            // Set the new head information
            household.setHeadAge((Integer) headAgeSpinner.getValue());
            household.setHeadGender((String) headGenderCombo.getSelectedItem());
            household.setHeadEducationLevel((String) headEducationCombo.getSelectedItem());
            household.setHeadOccupation(headOccupationField.getText().trim());
            
            household.setPurok((String) purokCombo.getSelectedItem());
            household.setHouseNumber(houseNumberField.getText().trim());
            household.setContactNumber(contactField.getText().trim());
            household.setAddress(addressArea.getText().trim());
            household.setMembersCount((Integer) membersSpinner.getValue());
            household.setStatus((String) statusCombo.getSelectedItem());
            
            try {
                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
                household.setRegistrationDate(sdf.parse(registrationDateField.getText().trim()));
            } catch (Exception e) {
                household.setRegistrationDate(new Date());
            }
            
            household.setCreatedBy(CurrentUser.getUserId());
            
            boolean success = householdService.addHousehold(household);
            
            if (success) {
                JOptionPane.showMessageDialog(this,
                    "<html><body style='font-family: Segoe UI;'>" +
                    "<h3 style='color: #27ae60; text-align: center;'>✅ Household Registered Successfully!</h3>" +
                    "<div style='background: #f8f9fa; padding: 12px; border-radius: 8px; margin: 12px;'>" +
                    "<p><b>Household ID:</b> " + household.getHouseholdId() + "</p>" +
                    "<p><b>Head Name:</b> " + household.getHeadFullName() + "</p>" +
                    "<p><b>Head Age:</b> " + household.getHeadAge() + " years old</p>" +
                    "<p><b>Head Gender:</b> " + household.getHeadGender() + "</p>" +
                    "<p><b>Head Education:</b> " + household.getHeadEducationLevel() + "</p>" +
                    "<p><b>Head Occupation:</b> " + household.getHeadOccupation() + "</p>" +
                    "<p><b>Purok:</b> " + household.getPurok() + "</p>" +
                    "<p><b>Registration Date:</b> " + new java.text.SimpleDateFormat("yyyy-MM-dd").format(household.getRegistrationDate()) + "</p>" +
                    "</div>" +
                    "</body></html>",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                
                clearForm();
                
                int option = JOptionPane.showConfirmDialog(this,
                    "Do you want to add another household?",
                    "Continue",
                    JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.NO_OPTION) {
                    dispose();
                    try {
                        new HouseholdListPage().setVisible(true);
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to save household. Please check the data and try again.",
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                "Error saving household: " + e.getMessage(),
                "System Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearForm() {
        householdIdField.setText("");
        headFirstNameField.setText("");
        headLastNameField.setText("");
        headMiddleNameField.setText("");
        headAgeSpinner.setValue(25);
        headGenderCombo.setSelectedIndex(0);
        headRelationshipCombo.setSelectedItem("Head");
        headEducationCombo.setSelectedIndex(0);
        headOccupationField.setText("");
        purokCombo.setSelectedIndex(0);
        houseNumberField.setText("");
        contactField.setText("");
        addressArea.setText("");
        membersSpinner.setValue(1);
        statusCombo.setSelectedIndex(0);
        registrationDateField.setText(new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        householdIdField.requestFocus();
    }
    
    private void navigateToPage(String pageName) {
        dispose();
        switch (pageName) {
            case "Dashboard":
                new DashboardPage().setVisible(true);
                break;
            case "Household List":
                try {
                    new HouseholdListPage().setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Add Household":
                break;
            case "Census Reports":
                try {
                    new CensusReportPage().setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Settings":
                new SettingsPage().setVisible(true);
                break;
        }
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginPage().setVisible(true);
        }
    }
}