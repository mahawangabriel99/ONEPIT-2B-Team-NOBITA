import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.*;

public class DashboardPage extends JFrame {
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
    
    public DashboardPage() {
        setTitle("Barangay Census System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
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
                                            createSidebar("Dashboard"), 
                                            createDashboardContent());
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
            menuBtn.setForeground(TEXT_SECONDARY);
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
                        menuBtn.setForeground(TEXT_PRIMARY);
                    }
                }
                public void mouseExited(MouseEvent e) {
                    if (!item.equals(currentPage)) {
                        menuBtn.setBackground(new Color(250, 250, 252));
                        menuBtn.setForeground(TEXT_SECONDARY);
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
        logoutBtn.setForeground(DANGER_COLOR);
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
            }
            public void mouseExited(MouseEvent e) {
                logoutBtn.setBackground(new Color(255, 245, 245));
            }
        });
        
        logoutBtn.addActionListener(e -> logout());
        
        sidebar.add(logoutBtn);
        
        return sidebar;
    }
    
    private JPanel createDashboardContent() {
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setBackground(BACKGROUND_COLOR);
        mainContent.setBorder(new EmptyBorder(25, 30, 25, 30));
        
        // Title section
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("Dashboard");
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(TEXT_PRIMARY);
        
        JLabel subtitle = new JLabel("System Overview and Statistics");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        
        titlePanel.add(title, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        titlePanel.setBorder(new EmptyBorder(0, 0, 25, 0));
        
        mainContent.add(titlePanel, BorderLayout.NORTH);
        
        // Statistics cards
        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        statsPanel.setBackground(BACKGROUND_COLOR);
        
        ApplicationService appService = null;
        try {
            appService = new ApplicationService();
        } catch (SQLException ex) {
            System.getLogger(DashboardPage.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        Map<String, Object> stats = appService.getDashboardStats();
        
        String[] statTitles = {"Total Households", "Total Population", "Active Records", "Pending Updates"};
        Object[] statValues = {
            stats.get("totalHouseholds"),
            stats.get("totalPopulation"),
            stats.get("activeHouseholds"),
            stats.get("pendingHouseholds")
        };
        Color[] statColors = {PRIMARY_COLOR, SUCCESS_COLOR, WARNING_COLOR, DANGER_COLOR};
        
        for (int i = 0; i < statTitles.length; i++) {
            statsPanel.add(createStatCard(statTitles[i], statValues[i], statColors[i]));
        }
        
        mainContent.add(statsPanel, BorderLayout.CENTER);
        
        // Bottom panels
        JPanel bottomPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        bottomPanel.setBackground(BACKGROUND_COLOR);
        bottomPanel.setBorder(new EmptyBorder(20, 0, 0, 0));
        
        bottomPanel.add(createActivityPanel());
        bottomPanel.add(createAlertsPanel());
        
        mainContent.add(bottomPanel, BorderLayout.SOUTH);
        
        return mainContent;
    }
    
    private JPanel createStatCard(String title, Object value, Color color) {
        JPanel card = new JPanel(new BorderLayout(0, 15));
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(2, 2, 2, 2),
                new EmptyBorder(23, 23, 23, 23)
            )
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        titleLabel.setForeground(TEXT_SECONDARY);
        
        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        valueLabel.setForeground(color);
        
        JPanel trendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        trendPanel.setOpaque(false);
        
        JLabel trendLabel = new JLabel("↑ 12.5%");
        trendLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        trendLabel.setForeground(SUCCESS_COLOR);
        
        JLabel periodLabel = new JLabel("from last month");
        periodLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        periodLabel.setForeground(TEXT_SECONDARY);
        
        trendPanel.add(trendLabel);
        trendPanel.add(periodLabel);
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        card.add(trendPanel, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("📋 Recent Activity");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        panel.add(title, BorderLayout.NORTH);
        
        ApplicationService appService = null;
        try {
            appService = new ApplicationService();
        } catch (SQLException ex) {
            System.getLogger(DashboardPage.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        
        // Get real activities from database - FIXED: Cast to raw List
        java.util.List<ActivityLog> activities = appService.getRecentActivities();
        if (activities != null && !activities.isEmpty()) {
            for (ActivityLog activity : activities) {
                String action = activity.getAction();
                String user = activity.getUserName() != null ? activity.getUserName() : "User";
                String time = " (" + getTimeAgo(activity.getCreatedAt()) + ")";
                listModel.addElement("• " + user + ": " + action + time);
            }
        } else {
            // Fallback sample data
            listModel.addElement("• Household 'Dela Cruz' added by Admin (2 hours ago)");
            listModel.addElement("• Census report generated for November (4 hours ago)");
            listModel.addElement("• 5 new households registered (1 day ago)");
            listModel.addElement("• System maintenance completed (2 days ago)");
            listModel.addElement("• User permissions updated (3 days ago)");
        }
        
        JList<String> activityList = new JList<String>(listModel);
        activityList.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        activityList.setBackground(CARD_COLOR);
        activityList.setSelectionBackground(new Color(240, 240, 240));
        activityList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(new EmptyBorder(8, 5, 8, 5));
                setForeground(TEXT_PRIMARY);
                return this;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(activityList);
        scrollPane.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createAlertsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel title = new JLabel("⚠️ System Alerts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(TEXT_PRIMARY);
        
        JButton viewAllBtn = createSmallButton("View All", PRIMARY_COLOR);
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(viewAllBtn, BorderLayout.EAST);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        
        JPanel alertsPanel = new JPanel();
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
        alertsPanel.setBackground(CARD_COLOR);
        
        ApplicationService appService = null;
        try {
            appService = new ApplicationService();
        } catch (SQLException ex) {
            System.getLogger(DashboardPage.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        // FIXED: Cast to raw List
        java.util.List<String> alerts = appService.getSystemAlerts();
        
        Color[] alertColors = {WARNING_COLOR, WARNING_COLOR, DANGER_COLOR, PRIMARY_COLOR, SUCCESS_COLOR};
        
        for (int i = 0; i < alerts.size(); i++) {
            JPanel alertItem = new JPanel(new BorderLayout());
            alertItem.setBackground(CARD_COLOR);
            alertItem.setBorder(new EmptyBorder(8, 0, 8, 0));
            
            JLabel alertLabel = new JLabel("• " + alerts.get(i));
            alertLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            alertLabel.setForeground(i < alertColors.length ? alertColors[i] : TEXT_SECONDARY);
            
            JLabel timeLabel = new JLabel("2h ago");
            timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            timeLabel.setForeground(TEXT_SECONDARY);
            
            alertItem.add(alertLabel, BorderLayout.WEST);
            alertItem.add(timeLabel, BorderLayout.EAST);
            alertsPanel.add(alertItem);
            
            if (i < alerts.size() - 1) {
                alertsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
            }
        }
        
        JScrollPane scrollPane = new JScrollPane(alertsPanel);
        scrollPane.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createSmallButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private JButton createStyledButton(String text, Color bgColor, boolean hasShadow) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
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
                if (hasShadow) {
                    button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bgColor.brighter().darker(), 1),
                        BorderFactory.createEmptyBorder(12, 24, 12, 24)
                    ));
                }
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
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
    
    private String getTimeAgo(java.sql.Timestamp timestamp) {
        if (timestamp == null) return "recently";
        
        long diff = System.currentTimeMillis() - timestamp.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        
        if (days > 0) return days + " day" + (days > 1 ? "s" : "") + " ago";
        if (hours > 0) return hours + " hour" + (hours > 1 ? "s" : "") + " ago";
        if (minutes > 0) return minutes + " minute" + (minutes > 1 ? "s" : "") + " ago";
        return "just now";
    }
    
    private void navigateToPage(String pageName) {
        dispose();
        switch (pageName) {
            case "Dashboard":
                new DashboardPage().setVisible(true);
                break;
            case "Household List":
            {
                try {
                    new HouseholdListPage().setVisible(true);
                } catch (SQLException ex) {
                    System.getLogger(DashboardPage.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
                break;

            case "Add Household":
            {
                try {
                    new AddHouseholdPage().setVisible(true);
                } catch (SQLException ex) {
                    System.getLogger(DashboardPage.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
            }
                break;

            case "Census Reports":
            {
                try {
                    new CensusReportPage().setVisible(true);
                } catch (SQLException ex) {
                    System.getLogger(DashboardPage.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
                }
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