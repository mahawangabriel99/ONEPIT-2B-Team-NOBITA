import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SettingsPage extends JFrame {
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
    
    private UserService userService;
    private DataBackupService backupService;
    
    public SettingsPage() {
        setTitle("Barangay Census System - Settings");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        this.userService = new UserService();
        this.backupService = new DataBackupService();
        
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
                                            createSidebar("Settings"), 
                                            createSettingsContent());
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
    
    private JPanel createSettingsContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("⚙️ Settings");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        
        JLabel subtitle = new JLabel("System Configuration and Management");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        titlePanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        mainContent.add(titlePanel, BorderLayout.NORTH);
        
        // Settings cards
        JPanel settingsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        settingsPanel.setBackground(BACKGROUND_COLOR);
        
        settingsPanel.add(createSettingsCard("👥 User Management", "Manage system users and permissions", PRIMARY_COLOR, 
            e -> {
                try {
                    showUserManagementDialog();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error loading users: " + e1.getMessage(),
                                                "Error", JOptionPane.ERROR_MESSAGE);
                }
            }));
        settingsPanel.add(createSettingsCard("⚙️ System Configuration", "Configure system settings and preferences", SUCCESS_COLOR,
            e -> showSystemConfigDialog()));
        settingsPanel.add(createSettingsCard("🏢 Barangay Information", "Update barangay details and contact info", WARNING_COLOR,
            e -> showBarangayInfoDialog()));
        settingsPanel.add(createSettingsCard("💾 Data Management", "Backup, restore, and manage system data", DANGER_COLOR,
            e -> showDataManagementDialog()));
        
        mainContent.add(settingsPanel, BorderLayout.CENTER);
        
        // Database status panel
        JPanel statusPanel = createDatabaseStatusPanel();
        mainContent.add(statusPanel, BorderLayout.SOUTH);
        
        return mainContent;
    }
    
    private JPanel createSettingsCard(String title, String description, Color color, ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        
        JLabel descLabel = new JLabel("<html><div style='width:250px'>" + description + "</div></html>");
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        descLabel.setForeground(TEXT_SECONDARY);
        
        JButton manageBtn = createStyledButton("Manage", color, false);
        manageBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        manageBtn.setForeground(Color.BLACK);
        manageBtn.setBackground(color);
        manageBtn.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        manageBtn.addActionListener(action);
        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        
        content.add(titleLabel);
        content.add(Box.createRigidArea(new Dimension(0, 8)));
        content.add(descLabel);
        content.add(Box.createVerticalGlue());
        content.add(manageBtn);
        
        card.add(content, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createDatabaseStatusPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("📊 System Status");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        
        JPanel statusPanel = new JPanel(new GridLayout(2, 2, 15, 10));
        statusPanel.setOpaque(false);
        
        // Database Status
        JPanel dbPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        dbPanel.setOpaque(false);
        
        JLabel dbLabel = new JLabel("Database Connection:");
        dbLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        dbLabel.setForeground(TEXT_SECONDARY);
        
        JLabel dbStatus = new JLabel("🟢 Connected");
        dbStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        dbStatus.setForeground(SUCCESS_COLOR);
        
        dbPanel.add(dbLabel);
        dbPanel.add(dbStatus);
        
        // System Status
        JPanel sysPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        sysPanel.setOpaque(false);
        
        JLabel sysLabel = new JLabel("System Status:");
        sysLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sysLabel.setForeground(TEXT_SECONDARY);
        
        JLabel sysStatus = new JLabel("🟢 Operational");
        sysStatus.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sysStatus.setForeground(SUCCESS_COLOR);
        
        sysPanel.add(sysLabel);
        sysPanel.add(sysStatus);
        
        // Last Backup
        JPanel backupPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        backupPanel.setOpaque(false);
        
        JLabel backupLabel = new JLabel("Last Backup:");
        backupLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backupLabel.setForeground(TEXT_SECONDARY);
        
        String lastBackup = getLastBackupDate();
        JLabel backupDate = new JLabel(lastBackup);
        backupDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backupDate.setForeground(TEXT_PRIMARY);
        
        backupPanel.add(backupLabel);
        backupPanel.add(backupDate);
        
        // Users Online
        JPanel usersPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        usersPanel.setOpaque(false);
        
        JLabel usersLabel = new JLabel("Active Users:");
        usersLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usersLabel.setForeground(TEXT_SECONDARY);
        
        JLabel usersCount = new JLabel("1");
        usersCount.setFont(new Font("Segoe UI", Font.BOLD, 13));
        usersCount.setForeground(PRIMARY_COLOR);
        
        usersPanel.add(usersLabel);
        usersPanel.add(usersCount);
        
        statusPanel.add(dbPanel);
        statusPanel.add(sysPanel);
        statusPanel.add(backupPanel);
        statusPanel.add(usersPanel);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(statusPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private String getLastBackupDate() {
        File backupDir = new File("backups");
        if (!backupDir.exists()) {
            return "No backup found";
        }
        
        File[] backupFiles = backupDir.listFiles((dir, name) -> name.endsWith(".sql") || name.endsWith(".csv"));
        if (backupFiles == null || backupFiles.length == 0) {
            return "No backup found";
        }
        
        // Get the most recent backup
        File lastBackup = backupFiles[0];
        for (File file : backupFiles) {
            if (file.lastModified() > lastBackup.lastModified()) {
                lastBackup = file;
            }
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date(lastBackup.lastModified()));
    }
    
    private JButton createStyledButton(String text, Color bgColor, boolean hasShadow) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.BLACK);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (hasShadow) {
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(12, 24, 12, 24)
            ));
        }
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setForeground(Color.BLACK);
                if (hasShadow) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.brighter().darker(), 1),
                        BorderFactory.createEmptyBorder(12, 24, 12, 24)
                    ));
                }
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setForeground(Color.BLACK);
                if (hasShadow) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.darker(), 1),
                        BorderFactory.createEmptyBorder(12, 24, 12, 24)
                    ));
                }
            }
        });
        
        return button;
    }
    
    private void showUserManagementDialog() throws SQLException {
        JDialog dialog = new JDialog(this, "User Management", true);
        dialog.setSize(900, 700);  // Increased size
        dialog.setLocationRelativeTo(this);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        
        // Create main panel with scroll
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel title = new JLabel("👥 User Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        
        JLabel subtitle = new JLabel("Manage system users and permissions");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setBorder(new EmptyBorder(5, 0, 20, 0));
        
        // User table
        String[] columns = {"ID", "Username", "Full Name", "Email", "Role", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable userTable = new JTable(model);
        userTable.setRowHeight(35);
        userTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        userTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        userTable.getTableHeader().setBackground(PRIMARY_COLOR);
        userTable.getTableHeader().setForeground(Color.WHITE);
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Load users
        loadUsersIntoTable(model);
        
        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.setPreferredSize(new Dimension(800, 400));
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton addButton = createStyledButton("Add User", SUCCESS_COLOR, true);
        JButton editButton = createStyledButton("Edit User", PRIMARY_COLOR, true);
        JButton deleteButton = createStyledButton("Delete User", DANGER_COLOR, true);
        JButton closeButton = createStyledButton("Close", TEXT_SECONDARY, false);
        
        addButton.addActionListener(e -> {
            try {
                showAddUserDialog(dialog, model);
            } catch (SQLException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(dialog, "Error: " + e1.getMessage(), 
                                            "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        editButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idValue = model.getValueAt(selectedRow, 0);
                if (idValue != null) {
                    try {
                        int userId = Integer.parseInt(idValue.toString());
                        showEditUserDialog(dialog, userId, model);
                    } catch (NumberFormatException | SQLException ex) {
                        JOptionPane.showMessageDialog(dialog, "Invalid user ID format", 
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a user to edit.",
                                            "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        deleteButton.addActionListener(e -> {
            int selectedRow = userTable.getSelectedRow();
            if (selectedRow >= 0) {
                Object idValue = model.getValueAt(selectedRow, 0);
                String username = (String) model.getValueAt(selectedRow, 1);
                
                if (idValue != null) {
                    try {
                        int userId = Integer.parseInt(idValue.toString());
                        
                        int confirm = JOptionPane.showConfirmDialog(dialog,
                            "Are you sure you want to delete user: " + username + "?\n\n" +
                            "This action cannot be undone!",
                            "Confirm Delete",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            if (userService.deleteUser(userId)) {
                                model.removeRow(selectedRow);
                                JOptionPane.showMessageDialog(dialog, "User deleted successfully!",
                                                            "Success", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(dialog, "Failed to delete user.",
                                                            "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Invalid user ID format",
                                                    "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (HeadlessException e1) {
                        e1.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a user to delete.",
                                            "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        closeButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);
        
        // Add components to dialog
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to scroll pane
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        content.add(mainScroll, BorderLayout.CENTER);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
    
    private void loadUsersIntoTable(DefaultTableModel model) throws SQLException {
        model.setRowCount(0); // Clear existing rows
        
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            model.addRow(new Object[]{
                user.getUserId(),  // Use getUserId() instead of getId()
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole(),
                user.isActive() ? "Active" : "Inactive"
            });
        }
    }
    
    private void showAddUserDialog(JDialog parentDialog, DefaultTableModel userTableModel) throws SQLException {
        JDialog dialog = new JDialog(parentDialog, "Add New User", true);
        dialog.setSize(500, 600);  // Increased height
        dialog.setLocationRelativeTo(parentDialog);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        
        // Create scrollable main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel title = new JLabel("➕ Add New User");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Form panel with scroll
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Username
        JPanel usernamePanel = createFormField("Username:", "");
        
        // Password
        JPanel passwordPanel = createFormField("Password:", "", true);
        
        // Confirm Password
        JPanel confirmPanel = createFormField("Confirm Password:", "", true);
        
        // Full Name
        JPanel namePanel = createFormField("Full Name:", "");
        
        // Email
        JPanel emailPanel = createFormField("Email:", "");
        
        // Role
        JPanel rolePanel = new JPanel(new BorderLayout(10, 5));
        rolePanel.setBackground(CARD_COLOR);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Administrator", "Enumerator", "Viewer"});
        styleComboBox(roleCombo);
        rolePanel.add(roleLabel, BorderLayout.NORTH);
        rolePanel.add(roleCombo, BorderLayout.CENTER);
        
        // Status
        JPanel statusPanel = new JPanel(new BorderLayout(10, 5));
        statusPanel.setBackground(CARD_COLOR);
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
        styleComboBox(statusCombo);
        statusPanel.add(statusLabel, BorderLayout.NORTH);
        statusPanel.add(statusCombo, BorderLayout.CENTER);
        
        // Add all panels to form
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(confirmPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(namePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(rolePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(statusPanel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton saveButton = createStyledButton("Save User", SUCCESS_COLOR, true);
        JButton cancelButton = createStyledButton("Cancel", TEXT_SECONDARY, false);
        
        saveButton.addActionListener(e -> {
            String username = ((JTextField) usernamePanel.getComponent(1)).getText().trim();
            String password = ((JTextField) passwordPanel.getComponent(1)).getText();
            String confirm = ((JTextField) confirmPanel.getComponent(1)).getText();
            String fullName = ((JTextField) namePanel.getComponent(1)).getText().trim();
            String email = ((JTextField) emailPanel.getComponent(1)).getText().trim();
            
            // Validate input
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Password is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirm)) {
                JOptionPane.showMessageDialog(dialog, "Passwords do not match!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Full name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create user object
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setFullName(fullName);
            newUser.setEmail(email);
            newUser.setRole((String) roleCombo.getSelectedItem());
            newUser.setActive(((String) statusCombo.getSelectedItem()).equals("Active"));
            
            if (userService.addUser(newUser)) {
                JOptionPane.showMessageDialog(dialog, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Add to table
                userTableModel.addRow(new Object[]{
                    newUser.getUserId(),
                    newUser.getUsername(),
                    newUser.getFullName(),
                    newUser.getEmail(),
                    newUser.getRole(),
                    newUser.isActive() ? "Active" : "Inactive"
                });
                
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add user. Username may already exist.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(title, BorderLayout.NORTH);
        
        // Wrap form panel in scroll pane
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(formScroll, BorderLayout.CENTER);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to scroll pane
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        
        content.add(mainScroll, BorderLayout.CENTER);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
    
    private void showEditUserDialog(JDialog parentDialog, int userId, DefaultTableModel userTableModel) throws SQLException {
        User user = userService.getUserById(userId);
        if (user == null) {
            JOptionPane.showMessageDialog(parentDialog, "User not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(parentDialog, "Edit User", true);
        dialog.setSize(500, 550);  // Increased height
        dialog.setLocationRelativeTo(parentDialog);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        
        // Create scrollable main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel title = new JLabel("✏️ Edit User: " + user.getUsername());
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Username (read-only)
        JPanel usernamePanel = createFormField("Username:", user.getUsername());
        ((JTextField) usernamePanel.getComponent(1)).setEditable(false);
        
        // Full Name
        JPanel namePanel = createFormField("Full Name:", user.getFullName());
        
        // Email
        JPanel emailPanel = createFormField("Email:", user.getEmail());
        
        // Role
        JPanel rolePanel = new JPanel(new BorderLayout(10, 5));
        rolePanel.setBackground(CARD_COLOR);
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JComboBox<String> roleCombo = new JComboBox<>(new String[]{"Administrator", "Enumerator", "Viewer"});
        roleCombo.setSelectedItem(user.getRole());
        styleComboBox(roleCombo);
        rolePanel.add(roleLabel, BorderLayout.NORTH);
        rolePanel.add(roleCombo, BorderLayout.CENTER);
        
        // Status
        JPanel statusPanel = new JPanel(new BorderLayout(10, 5));
        statusPanel.setBackground(CARD_COLOR);
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Inactive"});
        statusCombo.setSelectedItem(user.isActive() ? "Active" : "Inactive");
        styleComboBox(statusCombo);
        statusPanel.add(statusLabel, BorderLayout.NORTH);
        statusPanel.add(statusCombo, BorderLayout.CENTER);
        
        // Password (optional)
        JPanel passwordPanel = createFormField("New Password (leave blank to keep current):", "", true);
        
        // Add all panels to form
        formPanel.add(usernamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(namePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(rolePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(statusPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        formPanel.add(passwordPanel);
      
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton saveButton = createStyledButton("Save Changes", SUCCESS_COLOR, true);
        JButton cancelButton = createStyledButton("Cancel", TEXT_SECONDARY, false);
        
        saveButton.addActionListener(e -> {
            // Validate input
            String fullName = ((JTextField) namePanel.getComponent(1)).getText().trim();
            if (fullName.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Full name is required!", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Update user object
            user.setFullName(fullName);
            user.setEmail(((JTextField) emailPanel.getComponent(1)).getText().trim());
            user.setRole((String) roleCombo.getSelectedItem());
            user.setActive(((String) statusCombo.getSelectedItem()).equals("Active"));
            
            String newPassword = ((JTextField) passwordPanel.getComponent(1)).getText();
            if (!newPassword.isEmpty()) {
                user.setPassword(newPassword);
            }
            
            if (userService.updateUser(user)) {
                JOptionPane.showMessageDialog(dialog, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                
                // Update table
                for (int i = 0; i < userTableModel.getRowCount(); i++) {
                    Object idValue = userTableModel.getValueAt(i, 0);
                    if (idValue != null && idValue.toString().equals(String.valueOf(user.getUserId()))) {
                        userTableModel.setValueAt(user.getFullName(), i, 2);
                        userTableModel.setValueAt(user.getEmail(), i, 3);
                        userTableModel.setValueAt(user.getRole(), i, 4);
                        userTableModel.setValueAt(user.isActive() ? "Active" : "Inactive", i, 5);
                        break;
                    }
                }
                
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        mainPanel.add(title, BorderLayout.NORTH);
        
        // Wrap form panel in scroll pane
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(formScroll, BorderLayout.CENTER);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to scroll pane
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        
        content.add(mainScroll, BorderLayout.CENTER);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
    
    private void showSystemConfigDialog() {
        JDialog dialog = new JDialog(this, "System Configuration", true);
        dialog.setSize(700, 750);  // Increased size
        dialog.setLocationRelativeTo(this);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        
        // Create scrollable main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel title = new JLabel("⚙️ System Configuration");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        
        JLabel subtitle = new JLabel("Configure system settings and preferences");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setBorder(new EmptyBorder(5, 0, 20, 0));
        
        // Form panel with scroll
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Load current settings
        Properties settings = loadSystemSettings();
        
        // Database Settings
        JLabel dbTitle = new JLabel("Database Settings");
        dbTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dbTitle.setForeground(TEXT_PRIMARY);
        dbTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Database Host
        JPanel hostPanel = createFormField("Database Host:", 
            settings.getProperty("db.host", "localhost"));
        
        // Database Port
        JPanel portPanel = createFormField("Database Port:", 
            settings.getProperty("db.port", "3306"));
        
        // Database Name
        JPanel dbNamePanel = createFormField("Database Name:", 
            settings.getProperty("db.name", "barangay_census"));
        
        // Database Username
        JPanel dbUserPanel = createFormField("Database Username:", 
            settings.getProperty("db.username", "root"));
        
        // Database Password
        JPanel dbPassPanel = createFormField("Database Password:", 
            settings.getProperty("db.password", ""), true);
        
        // Application Settings
        JLabel appTitle = new JLabel("Application Settings");
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        appTitle.setForeground(TEXT_PRIMARY);
        appTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        // Auto Backup
        JPanel backupPanel = new JPanel(new BorderLayout(10, 5));
        backupPanel.setBackground(CARD_COLOR);
        JLabel backupLabel = new JLabel("Auto Backup:");
        backupLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JCheckBox autoBackupCheck = new JCheckBox("Enable automatic daily backup");
        autoBackupCheck.setSelected(Boolean.parseBoolean(settings.getProperty("auto.backup", "true")));
        autoBackupCheck.setBackground(CARD_COLOR);
        backupPanel.add(backupLabel, BorderLayout.NORTH);
        backupPanel.add(autoBackupCheck, BorderLayout.CENTER);
        
        // Backup Retention Days
        JPanel retentionPanel = createFormField("Backup Retention (days):", 
            settings.getProperty("backup.retention", "30"));
        
        // Session Timeout
        JPanel timeoutPanel = createFormField("Session Timeout (minutes):", 
            settings.getProperty("session.timeout", "30"));
        
        // Add all panels to form
        formPanel.add(dbTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(hostPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(portPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(dbNamePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(dbUserPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(dbPassPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(appTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(backupPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(retentionPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(timeoutPanel);
        formPanel.add(Box.createVerticalGlue());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton saveButton = createStyledButton("Save Settings", SUCCESS_COLOR, true);
        JButton testButton = createStyledButton("Test Connection", PRIMARY_COLOR, true);
        JButton cancelButton = createStyledButton("Cancel", TEXT_SECONDARY, false);
        
        testButton.addActionListener(e -> {
            String host = ((JTextField) hostPanel.getComponent(1)).getText();
            String port = ((JTextField) portPanel.getComponent(1)).getText();
            String name = ((JTextField) dbNamePanel.getComponent(1)).getText();
            String user = ((JTextField) dbUserPanel.getComponent(1)).getText();
            String pass = ((JTextField) dbPassPanel.getComponent(1)).getText();
            
            try {
                if (DatabaseConnection.testConnection(host, port, name, user, pass)) {
                    JOptionPane.showMessageDialog(dialog, 
                        "✅ Database connection successful!", 
                        "Connection Test", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(dialog, 
                        "❌ Failed to connect to database. Please check settings.", 
                        "Connection Test", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Connection error: " + ex.getMessage(), 
                    "Connection Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        saveButton.addActionListener(e -> {
            Properties newSettings = new Properties();
            
            // Get values from form fields
            newSettings.setProperty("db.host", ((JTextField) hostPanel.getComponent(1)).getText());
            newSettings.setProperty("db.port", ((JTextField) portPanel.getComponent(1)).getText());
            newSettings.setProperty("db.name", ((JTextField) dbNamePanel.getComponent(1)).getText());
            newSettings.setProperty("db.username", ((JTextField) dbUserPanel.getComponent(1)).getText());
            newSettings.setProperty("db.password", ((JTextField) dbPassPanel.getComponent(1)).getText());
            newSettings.setProperty("auto.backup", String.valueOf(autoBackupCheck.isSelected()));
            newSettings.setProperty("backup.retention", ((JTextField) retentionPanel.getComponent(1)).getText());
            newSettings.setProperty("session.timeout", ((JTextField) timeoutPanel.getComponent(1)).getText());
            
            try {
                saveSystemSettings(newSettings);
                JOptionPane.showMessageDialog(dialog, 
                    "Settings saved successfully!\n\n" +
                    "Some changes may require restarting the application.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, 
                    "Error saving settings: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(testButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add components to dialog
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Wrap form panel in scroll pane
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(formScroll, BorderLayout.CENTER);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to scroll pane
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        content.add(mainScroll, BorderLayout.CENTER);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
    
    private JPanel createFormField(String labelText, String defaultValue) {
        return createFormField(labelText, defaultValue, false);
    }
    
    private JPanel createFormField(String labelText, String defaultValue, boolean isPassword) {
        JPanel panel = new JPanel(new BorderLayout(10, 5));
        panel.setBackground(CARD_COLOR);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JTextField field;
        if (isPassword) {
            field = new JPasswordField(defaultValue);
        } else {
            field = new JTextField(defaultValue);
        }
        styleTextField(field);
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    private Properties loadSystemSettings() {
        Properties props = new Properties();
        File settingsFile = new File("config/system.properties");
        
        if (settingsFile.exists()) {
            try (FileInputStream fis = new FileInputStream(settingsFile)) {
                props.load(fis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Set defaults if not exists
        if (!props.containsKey("db.host")) props.setProperty("db.host", "localhost");
        if (!props.containsKey("db.port")) props.setProperty("db.port", "3306");
        if (!props.containsKey("db.name")) props.setProperty("db.name", "barangay_census");
        if (!props.containsKey("db.username")) props.setProperty("db.username", "root");
        if (!props.containsKey("db.password")) props.setProperty("db.password", "");
        if (!props.containsKey("auto.backup")) props.setProperty("auto.backup", "true");
        if (!props.containsKey("backup.retention")) props.setProperty("backup.retention", "30");
        if (!props.containsKey("session.timeout")) props.setProperty("session.timeout", "30");
        
        return props;
    }
    
    private void saveSystemSettings(Properties settings) throws IOException {
        File configDir = new File("config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
        File settingsFile = new File("config/system.properties");
        try (FileOutputStream fos = new FileOutputStream(settingsFile)) {
            settings.store(fos, "Barangay Census System Configuration");
        }
    }
    
    private void showBarangayInfoDialog() {
        JDialog dialog = new JDialog(this, "Barangay Information", true);
        dialog.setSize(700, 750);  // Increased size
        dialog.setLocationRelativeTo(this);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(BACKGROUND_COLOR);
        
        // Create scrollable main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        
        // Title
        JLabel title = new JLabel("🏢 Barangay Information");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(PRIMARY_COLOR);
        
        JLabel subtitle = new JLabel("Update barangay details and contact information");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setBorder(new EmptyBorder(5, 0, 20, 0));
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        // Load current barangay info
        Properties barangayInfo = loadBarangayInfo();
        
        // Barangay Details
        JLabel detailsTitle = new JLabel("Barangay Details");
        detailsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        detailsTitle.setForeground(TEXT_PRIMARY);
        detailsTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Barangay Name
        JPanel namePanel = createFormField("Barangay Name:", 
            barangayInfo.getProperty("name", "Barangay 123"));
        
        // Barangay Number
        JPanel numberPanel = createFormField("Barangay Number:", 
            barangayInfo.getProperty("number", "123"));
        
        // City/Municipality
        JPanel cityPanel = createFormField("City/Municipality:", 
            barangayInfo.getProperty("city", "Sample City"));
        
        // Province
        JPanel provincePanel = createFormField("Province:", 
            barangayInfo.getProperty("province", "Sample Province"));
        
        // Region
        JPanel regionPanel = createFormField("Region:", 
            barangayInfo.getProperty("region", "Region IV-A"));
        
        // Zip Code
        JPanel zipPanel = createFormField("Zip Code:", 
            barangayInfo.getProperty("zipcode", "1234"));
        
        // Contact Information
        JLabel contactTitle = new JLabel("Contact Information");
        contactTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        contactTitle.setForeground(TEXT_PRIMARY);
        contactTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        // Barangay Captain
        JPanel captainPanel = createFormField("Barangay Captain:", 
            barangayInfo.getProperty("captain", "Juan Dela Cruz"));
        
        // Contact Number
        JPanel contactPanel = createFormField("Contact Number:", 
            barangayInfo.getProperty("contact", "(02) 123-4567"));
        
        // Email Address
        JPanel emailPanel = createFormField("Email Address:", 
            barangayInfo.getProperty("email", "barangay123@sample.gov.ph"));
        
        // Website
        JPanel websitePanel = createFormField("Website:", 
            barangayInfo.getProperty("website", "www.samplebarangay.gov.ph"));
        
        // Address
        JPanel addressPanel = new JPanel(new BorderLayout(10, 5));
        addressPanel.setBackground(CARD_COLOR);
        JLabel addressLabel = new JLabel("Complete Address:");
        addressLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JTextArea addressArea = new JTextArea(barangayInfo.getProperty("address", "Sample Street, Sample City, Sample Province"), 4, 30);
        addressArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        JScrollPane addressScroll = new JScrollPane(addressArea);
        addressScroll.setPreferredSize(new Dimension(0, 100));
        addressPanel.add(addressLabel, BorderLayout.NORTH);
        addressPanel.add(addressScroll, BorderLayout.CENTER);
        
        // Add all panels to form
        formPanel.add(detailsTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(namePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(numberPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(cityPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(provincePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(regionPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(zipPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(contactTitle);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(captainPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(contactPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(emailPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(websitePanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(addressPanel);
        formPanel.add(Box.createVerticalGlue());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton saveButton = createStyledButton("Save Information", SUCCESS_COLOR, true);
        JButton cancelButton = createStyledButton("Cancel", TEXT_SECONDARY, false);
        
        saveButton.addActionListener(e -> {
            Properties newInfo = new Properties();
            
            // Get values from form fields
            newInfo.setProperty("name", ((JTextField) namePanel.getComponent(1)).getText());
            newInfo.setProperty("number", ((JTextField) numberPanel.getComponent(1)).getText());
            newInfo.setProperty("city", ((JTextField) cityPanel.getComponent(1)).getText());
            newInfo.setProperty("province", ((JTextField) provincePanel.getComponent(1)).getText());
            newInfo.setProperty("region", ((JTextField) regionPanel.getComponent(1)).getText());
            newInfo.setProperty("zipcode", ((JTextField) zipPanel.getComponent(1)).getText());
            newInfo.setProperty("captain", ((JTextField) captainPanel.getComponent(1)).getText());
            newInfo.setProperty("contact", ((JTextField) contactPanel.getComponent(1)).getText());
            newInfo.setProperty("email", ((JTextField) emailPanel.getComponent(1)).getText());
            newInfo.setProperty("website", ((JTextField) websitePanel.getComponent(1)).getText());
            newInfo.setProperty("address", addressArea.getText());
            
            try {
                saveBarangayInfo(newInfo);
                JOptionPane.showMessageDialog(dialog, 
                    "Barangay information saved successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(dialog, 
                    "Error saving information: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        // Add components to dialog
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        headerPanel.add(title, BorderLayout.NORTH);
        headerPanel.add(subtitle, BorderLayout.SOUTH);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Wrap form panel in scroll pane
        JScrollPane formScroll = new JScrollPane(formPanel);
        formScroll.setBorder(null);
        formScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(formScroll, BorderLayout.CENTER);
        
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to scroll pane
        JScrollPane mainScroll = new JScrollPane(mainPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        
        content.add(mainScroll, BorderLayout.CENTER);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
    
    private Properties loadBarangayInfo() {
        Properties props = new Properties();
        File infoFile = new File("config/barangay.properties");
        
        if (infoFile.exists()) {
            try (FileInputStream fis = new FileInputStream(infoFile)) {
                props.load(fis);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        // Set defaults if not exists
        if (!props.containsKey("name")) props.setProperty("name", "Barangay 123");
        if (!props.containsKey("number")) props.setProperty("number", "123");
        if (!props.containsKey("city")) props.setProperty("city", "Sample City");
        if (!props.containsKey("province")) props.setProperty("province", "Sample Province");
        if (!props.containsKey("region")) props.setProperty("region", "Region IV-A");
        if (!props.containsKey("zipcode")) props.setProperty("zipcode", "1234");
        if (!props.containsKey("captain")) props.setProperty("captain", "Juan Dela Cruz");
        if (!props.containsKey("contact")) props.setProperty("contact", "(02) 123-4567");
        if (!props.containsKey("email")) props.setProperty("email", "barangay123@sample.gov.ph");
        if (!props.containsKey("website")) props.setProperty("website", "www.samplebarangay.gov.ph");
        if (!props.containsKey("address")) props.setProperty("address", "Sample Street, Sample City, Sample Province");
        
        return props;
    }
    
    private void saveBarangayInfo(Properties info) throws IOException {
        File configDir = new File("config");
        if (!configDir.exists()) {
            configDir.mkdirs();
        }
        
        File infoFile = new File("config/barangay.properties");
        try (FileOutputStream fos = new FileOutputStream(infoFile)) {
            info.store(fos, "Barangay Information");
        }
    }
    
    private void showDataManagementDialog() {
        JDialog dialog = new JDialog(this, "Data Management", true);
        dialog.setSize(700, 600);  // Increased size
        dialog.setLocationRelativeTo(this);
        
        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("💾 Data Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(PRIMARY_COLOR);
        
        // Main panel with tabs
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Backup Tab
        JPanel backupPanel = createBackupPanel(dialog);
        tabbedPane.addTab("🔒 Backup", backupPanel);
        
        // Restore Tab
        JPanel restorePanel = createRestorePanel(dialog);
        tabbedPane.addTab("🔄 Restore", restorePanel);
        
        // Export Tab
        JPanel exportPanel = createExportPanel(dialog);
        tabbedPane.addTab("📤 Export", exportPanel);
        
        // Import Tab - Changed to Excel only
        JPanel importPanel = createImportPanel(dialog);
        tabbedPane.addTab("📥 Import", importPanel);
        
        JButton closeButton = createStyledButton("Close", TEXT_SECONDARY, false);
        closeButton.addActionListener(e -> dialog.dispose());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(closeButton);
        
        content.add(title, BorderLayout.NORTH);
        content.add(tabbedPane, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(content);
        dialog.setVisible(true);
    }
    
    private JPanel createBackupPanel(JDialog parentDialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CARD_COLOR);
        
        JLabel title = new JLabel("Database Backup");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoArea.setBackground(CARD_COLOR);
        infoArea.setText("Create a backup of the entire database.\n\n" +
                        "Backup includes:\n" +
                        "• Household records\n" +
                        "• Member information\n" +
                        "• User accounts\n" +
                        "• System settings\n\n" +
                        "Backup files are saved in the 'backups' folder.\n" +
                        "Format: backup_YYYYMMDD_HHMMSS.sql");
        infoArea.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        JButton backupButton = createStyledButton("Start Backup", SUCCESS_COLOR, true);
        backupButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        backupButton.setPreferredSize(new Dimension(200, 45));
        
        JProgressBar progressBar = new JProgressBar();
        progressBar.setVisible(false);
        
        backupButton.addActionListener(e -> {
            backupButton.setEnabled(false);
            progressBar.setVisible(true);
            progressBar.setIndeterminate(true);
            
            new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        return backupService.createDatabaseBackup();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        return false;
                    }
                }
                
                @Override
                protected void done() {
                    try {
                        boolean success = get();
                        progressBar.setVisible(false);
                        backupButton.setEnabled(true);
                        
                        if (success) {
                            String backupPath = backupService.getLastBackupPath();
                            JOptionPane.showMessageDialog(parentDialog,
                                "✅ Backup completed successfully!\n\n" +
                                "Backup saved to:\n" + backupPath + "\n\n" +
                                "Backup time: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
                                "Backup Successful",
                                JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(parentDialog,
                                "❌ Backup failed. Please check database connection.",
                                "Backup Failed",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(parentDialog,
                            "Error during backup: " + ex.getMessage(),
                            "Backup Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            }.execute();
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(backupButton);
        buttonPanel.add(progressBar);
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createRestorePanel(JDialog parentDialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CARD_COLOR);
        
        JLabel title = new JLabel("Database Restore");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoArea.setBackground(CARD_COLOR);
        infoArea.setText("Restore database from a backup file.\n\n" +
                        "⚠️ Warning: This will overwrite current data!\n" +
                        "Make sure to backup current data first.\n\n" +
                        "Supported formats:\n" +
                        "• SQL backup files (.sql)\n" +
                        "• CSV data files (.csv)");
        infoArea.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        JButton browseButton = createStyledButton("Browse Backup File", WARNING_COLOR, true);
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JTextField filePathField = new JTextField();
        filePathField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filePathField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        filePathField.setEditable(false);
        
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("backups"));
            fileChooser.setDialogTitle("Select Backup File");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || 
                           f.getName().toLowerCase().endsWith(".sql") ||
                           f.getName().toLowerCase().endsWith(".csv");
                }
                public String getDescription() {
                    return "Backup Files (*.sql, *.csv)";
                }
            });
            
            int result = fileChooser.showOpenDialog(parentDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        
        JButton restoreButton = createStyledButton("Restore Database", DANGER_COLOR, true);
        restoreButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        restoreButton.setPreferredSize(new Dimension(200, 45));
        restoreButton.setEnabled(false);
        
        filePathField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void insertUpdate(DocumentEvent e) { update(); }
            private void update() {
                restoreButton.setEnabled(!filePathField.getText().trim().isEmpty());
            }
        });
        
        restoreButton.addActionListener(e -> {
            String filePath = filePathField.getText();
            File backupFile = new File(filePath);
            
            if (!backupFile.exists()) {
                JOptionPane.showMessageDialog(parentDialog,
                    "Backup file not found: " + filePath,
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(parentDialog,
                "⚠️ WARNING: This will overwrite ALL current data!\n\n" +
                "Are you sure you want to restore from:\n" + backupFile.getName() + "?\n\n" +
                "This action cannot be undone!",
                "Confirm Database Restore",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                restoreButton.setEnabled(false);
                
                new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        try {
                            return backupService.restoreDatabase(backupFile);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return false;
                        }
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            boolean success = get();
                            restoreButton.setEnabled(true);
                            
                            if (success) {
                                JOptionPane.showMessageDialog(parentDialog,
                                    "✅ Database restored successfully!\n\n" +
                                    "The system will now restart to apply changes.",
                                    "Restore Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                                    
                                // Restart application
                                dispose();
                                new LoginPage().setVisible(true);
                            } else {
                                JOptionPane.showMessageDialog(parentDialog,
                                    "❌ Restore failed. Please check the backup file.",
                                    "Restore Failed",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parentDialog,
                                "Error during restore: " + ex.getMessage(),
                                "Restore Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });
        
        JPanel filePanel = new JPanel(new BorderLayout(10, 10));
        filePanel.setBackground(CARD_COLOR);
        filePanel.add(new JLabel("Backup File:"), BorderLayout.WEST);
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(restoreButton);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CARD_COLOR);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(new JScrollPane(infoArea));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(filePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createExportPanel(JDialog parentDialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CARD_COLOR);
        
        JLabel title = new JLabel("Export Data");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoArea.setBackground(CARD_COLOR);
        infoArea.setText("Export data to various formats for reporting or external use.\n\n" +
                        "Available export formats:\n" +
                        "• CSV (Comma Separated Values)\n" +
                        "• Excel Spreadsheet (.xlsx)\n" +
                        "• PDF Report\n" +
                        "• JSON Data\n\n" +
                        "Select data to export:\n");
        infoArea.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        // Data selection checkboxes
        JPanel dataPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        dataPanel.setBackground(CARD_COLOR);
        dataPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JCheckBox householdsCheck = new JCheckBox("Household Data", true);
        householdsCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        householdsCheck.setBackground(CARD_COLOR);
        
        JCheckBox membersCheck = new JCheckBox("Member Data", true);
        membersCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        membersCheck.setBackground(CARD_COLOR);
        
        JCheckBox usersCheck = new JCheckBox("User Data", false);
        usersCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        usersCheck.setBackground(CARD_COLOR);
        
        JCheckBox reportsCheck = new JCheckBox("Report Data", false);
        reportsCheck.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        reportsCheck.setBackground(CARD_COLOR);
        
        dataPanel.add(householdsCheck);
        dataPanel.add(membersCheck);
        dataPanel.add(usersCheck);
        dataPanel.add(reportsCheck);
        
        // Format selection
        JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        formatPanel.setBackground(CARD_COLOR);
        formatPanel.add(new JLabel("Export Format:"));
        
        JComboBox<String> formatCombo = new JComboBox<>(new String[]{"CSV", "Excel", "PDF", "JSON"});
        formatCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        formatPanel.add(formatCombo);
        
        JButton exportButton = createStyledButton("Export Data", PRIMARY_COLOR, true);
        exportButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        exportButton.setPreferredSize(new Dimension(200, 45));
        
        exportButton.addActionListener(e -> {
            String format = (String) formatCombo.getSelectedItem();
            boolean includeHouseholds = householdsCheck.isSelected();
            boolean includeMembers = membersCheck.isSelected();
            boolean includeUsers = usersCheck.isSelected();
            boolean includeReports = reportsCheck.isSelected();
            
            if (!includeHouseholds && !includeMembers && !includeUsers && !includeReports) {
                JOptionPane.showMessageDialog(parentDialog,
                    "Please select at least one data type to export.",
                    "No Data Selected",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("exports"));
            fileChooser.setDialogTitle("Save Export File");
            
            String extension = getExtensionForFormat(format);
            String fileName = "export_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + extension;
            fileChooser.setSelectedFile(new File(fileName));
            
            int result = fileChooser.showSaveDialog(parentDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File exportFile = fileChooser.getSelectedFile();
                exportButton.setEnabled(false);
                
                new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        try {
                            return backupService.exportData(exportFile, format, 
                                includeHouseholds, includeMembers, includeUsers, includeReports);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return false;
                        }
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            boolean success = get();
                            exportButton.setEnabled(true);
                            
                            if (success) {
                                JOptionPane.showMessageDialog(parentDialog,
                                    "✅ Data exported successfully!\n\n" +
                                    "File saved to:\n" + exportFile.getAbsolutePath() + "\n\n" +
                                    "Format: " + format,
                                    "Export Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(parentDialog,
                                    "❌ Export failed. Please try again.",
                                    "Export Failed",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parentDialog,
                                "Error during export: " + ex.getMessage(),
                                "Export Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(exportButton);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CARD_COLOR);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(new JScrollPane(infoArea));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(dataPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(formatPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createImportPanel(JDialog parentDialog) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(CARD_COLOR);
        
        JLabel title = new JLabel("Import Data");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(PRIMARY_COLOR);
        
        JTextArea infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        infoArea.setBackground(CARD_COLOR);
        infoArea.setText("Import data from external Excel files.\n\n" +
                        "Supported import formats:\n" +
                        "• Excel files (.xlsx, .xls) ONLY\n\n" +
                        "⚠️ Note: Data will be added to existing records.\n" +
                        "Duplicate records will be skipped.");
        infoArea.setBorder(new EmptyBorder(15, 0, 15, 0));
        
        JButton browseButton = createStyledButton("Browse Import File", ACCENT_COLOR, true);
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JTextField filePathField = new JTextField();
        filePathField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        filePathField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        filePathField.setEditable(false);
        
        // Changed to Excel only filter
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Select Excel File");
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || 
                           f.getName().toLowerCase().endsWith(".xlsx") ||
                           f.getName().toLowerCase().endsWith(".xls");
                }
                public String getDescription() {
                    return "Excel Files (*.xlsx, *.xls)";
                }
            });
            
            int result = fileChooser.showOpenDialog(parentDialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                filePathField.setText(selectedFile.getAbsolutePath());
            }
        });
        
        // Data type selection
        JPanel importTypePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        importTypePanel.setBackground(CARD_COLOR);
        importTypePanel.add(new JLabel("Import as:"));
        
        JComboBox<String> importTypeCombo = new JComboBox<>(new String[]{"Households", "Members", "Users"});
        importTypeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        importTypePanel.add(importTypeCombo);
        
        JButton importButton = createStyledButton("Import Data", SUCCESS_COLOR, true);
        importButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        importButton.setPreferredSize(new Dimension(200, 45));
        importButton.setEnabled(false);
        
        filePathField.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { update(); }
            public void removeUpdate(DocumentEvent e) { update(); }
            public void insertUpdate(DocumentEvent e) { update(); }
            private void update() {
                importButton.setEnabled(!filePathField.getText().trim().isEmpty());
            }
        });
        
        importButton.addActionListener(e -> {
            String filePath = filePathField.getText();
            File importFile = new File(filePath);
            String importType = (String) importTypeCombo.getSelectedItem();
            
            if (!importFile.exists()) {
                JOptionPane.showMessageDialog(parentDialog,
                    "Import file not found: " + filePath,
                    "File Not Found",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if file is Excel
            String fileName = importFile.getName().toLowerCase();
            if (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls")) {
                JOptionPane.showMessageDialog(parentDialog,
                    "Please select an Excel file (.xlsx or .xls) only!",
                    "Invalid File Format",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(parentDialog,
                "Import " + importType + " from:\n" + importFile.getName() + "\n\n" +
                "This will add new records to the database.\n" +
                "Duplicate records will be skipped.",
                "Confirm Import",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                importButton.setEnabled(false);
                
                new SwingWorker<Integer, Void>() {
                    @Override
                    protected Integer doInBackground() throws Exception {
                        try {
                            return backupService.importData(importFile, importType);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            return -1;
                        }
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            int recordsImported = get();
                            importButton.setEnabled(true);
                            
                            if (recordsImported > 0) {
                                JOptionPane.showMessageDialog(parentDialog,
                                    "✅ Import completed successfully!\n\n" +
                                    recordsImported + " " + importType.toLowerCase() + " imported.\n\n" +
                                    "The data is now available in the system.",
                                    "Import Successful",
                                    JOptionPane.INFORMATION_MESSAGE);
                            } else if (recordsImported == 0) {
                                JOptionPane.showMessageDialog(parentDialog,
                                    "⚠️ No new records imported.\n\n" +
                                    "All records may already exist in the database.",
                                    "Import Complete",
                                    JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(parentDialog,
                                    "❌ Import failed. Please check the file format.",
                                    "Import Failed",
                                    JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(parentDialog,
                                "Error during import: " + ex.getMessage(),
                                "Import Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }.execute();
            }
        });
        
        JPanel filePanel = new JPanel(new BorderLayout(10, 10));
        filePanel.setBackground(CARD_COLOR);
        filePanel.add(new JLabel("Import File:"), BorderLayout.WEST);
        filePanel.add(filePathField, BorderLayout.CENTER);
        filePanel.add(browseButton, BorderLayout.EAST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(CARD_COLOR);
        buttonPanel.add(importButton);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(CARD_COLOR);
        mainPanel.add(title);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(new JScrollPane(infoArea));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(filePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(importTypePanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(buttonPanel);
        
        panel.add(mainPanel, BorderLayout.CENTER);
        return panel;
    }
    
    private String getExtensionForFormat(String format) {
        switch (format) {
            case "CSV": return ".csv";
            case "Excel": return ".xlsx";
            case "PDF": return ".pdf";
            case "JSON": return ".json";
            default: return ".txt";
        }
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(new Color(250, 250, 250));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(new Color(250, 250, 250));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
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
                try {
                    new AddHouseholdPage().setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Census Reports":
                try {
                    new CensusReportPage().setVisible(true);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                break;
            case "Settings":
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