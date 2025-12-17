import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private JButton loginButton;
    
    private final Color PRIMARY_COLOR = new Color(67, 97, 238);
    private final Color ACCENT_COLOR = new Color(46, 196, 182);
    private final Color BACKGROUND_COLOR = new Color(248, 249, 252);
    private final Color CARD_COLOR = Color.WHITE;
    
    public LoginPage() {
        setTitle("Barangay Census System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initUI();
        setupEnterKeyListeners();
    }
    
    private void initUI() {
        getContentPane().setBackground(BACKGROUND_COLOR);
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainPanel.add(createContentPanel(), BorderLayout.CENTER);
        setContentPane(mainPanel);
    }
    
    private void setupEnterKeyListeners() {
        // Add Enter key listener to username field
        usernameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Move focus to password field when Enter is pressed
                    passwordField.requestFocus();
                }
            }
        });
        
        // Add Enter key listener to password field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Move focus to role combo when Enter is pressed
                    roleCombo.requestFocus();
                }
            }
        });
        
        // Add Enter key listener to role combo
        roleCombo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Trigger login when Enter is pressed on role combo
                    loginButton.doClick();
                }
            }
        });
        
        // Also add Enter key listener to the entire frame for convenience
        getRootPane().setDefaultButton(loginButton);
        
        // Add global Enter key listener to the form panel
        Action loginAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton.doClick();
            }
        };
        
        // Bind Enter key to login action
        loginButton.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                  .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "login");
        loginButton.getActionMap().put("login", loginAction);
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
        
        header.add(leftPanel, BorderLayout.WEST);
        
        return header;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BACKGROUND_COLOR);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 10, 5, 10);
        
        // Logo
        JLabel logoLabel = new JLabel("🏘️", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 72));
        logoLabel.setForeground(PRIMARY_COLOR);
        
        // Title
        JLabel titleLabel = new JLabel("Barangay Census System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.BLACK);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Secure Login Portal", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.DARK_GRAY);
        
        // Add spacing
        panel.add(Box.createVerticalStrut(20), gbc);
        panel.add(logoLabel, gbc);
        panel.add(titleLabel, gbc);
        panel.add(subtitleLabel, gbc);
        panel.add(Box.createVerticalStrut(20), gbc);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_COLOR);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        formPanel.setPreferredSize(new Dimension(380, 400));
        
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.insets = new Insets(8, 8, 8, 8);
        
        // Form title
        JLabel formTitle = new JLabel("Login to System", SwingConstants.CENTER);
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        formTitle.setForeground(Color.BLACK);
        fgbc.gridx = 0; fgbc.gridy = 0;
        fgbc.gridwidth = 2;
        formPanel.add(formTitle, fgbc);
        
        // Add spacing
        fgbc.gridy = 1;
        formPanel.add(Box.createVerticalStrut(10), fgbc);
        
        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        userLabel.setForeground(Color.BLACK);
        fgbc.gridy = 2;
        fgbc.gridwidth = 1;
        formPanel.add(userLabel, fgbc);
        
        usernameField = new JTextField(20);
        styleTextField(usernameField);
        fgbc.gridy = 3;
        fgbc.gridx = 0;
        fgbc.gridwidth = 2;
        formPanel.add(usernameField, fgbc);
        
        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        passLabel.setForeground(Color.BLACK);
        fgbc.gridy = 4;
        fgbc.gridwidth = 1;
        formPanel.add(passLabel, fgbc);
        
        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        fgbc.gridy = 5;
        fgbc.gridwidth = 2;
        formPanel.add(passwordField, fgbc);
        
        // Role
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        roleLabel.setForeground(Color.BLACK);
        fgbc.gridy = 6;
        fgbc.gridwidth = 1;
        formPanel.add(roleLabel, fgbc);
        
        roleCombo = new JComboBox<>(new String[]{"Administrator", "Enumerator", "Viewer"});
        styleComboBox(roleCombo);
        fgbc.gridy = 7;
        fgbc.gridwidth = 2;
        formPanel.add(roleCombo, fgbc);
        
        // Add spacing before button
        fgbc.gridy = 8;
        fgbc.insets = new Insets(15, 8, 8, 8);
        formPanel.add(Box.createVerticalStrut(10), fgbc);
        
        // Login button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginButton.setForeground(Color.BLACK);
        loginButton.setBackground(ACCENT_COLOR);
        loginButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 50), 1),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(150, 35));
        loginButton.setToolTipText("Press Enter key to login");
        
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(ACCENT_COLOR.brighter());
                loginButton.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(ACCENT_COLOR);
                loginButton.setForeground(Color.BLACK);
            }
        });
        
        loginButton.addActionListener(e -> login());
        fgbc.gridy = 9;
        fgbc.gridwidth = 2;
        fgbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, fgbc);
        
        // Add keyboard shortcut hint
        JLabel shortcutHint = new JLabel("Tip: Use TAB to navigate, ENTER to submit", SwingConstants.CENTER);
        shortcutHint.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        shortcutHint.setForeground(Color.GRAY);
        fgbc.gridy = 10;
        fgbc.insets = new Insets(10, 8, 0, 8);
        formPanel.add(shortcutHint, fgbc);
        
        // Add form to main panel
        panel.add(formPanel, gbc);
        
        return panel;
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        field.setBackground(new Color(250, 250, 250));
        field.setForeground(Color.BLACK);
        field.setPreferredSize(new Dimension(250, 35));
    }
    
    private void styleComboBox(JComboBox<String> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        combo.setBackground(new Color(250, 250, 250));
        combo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        combo.setForeground(Color.BLACK);
        combo.setPreferredSize(new Dimension(250, 35));
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter both username and password!", 
                "Login Error", 
                JOptionPane.ERROR_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        try {
            UserService userService = new UserService();
            User user = userService.login(username, password);
            
            if (user != null && user.getRole().equals(role)) {
                JOptionPane.showMessageDialog(this,
                    "Login successful! Welcome, " + user.getFullName() + " (" + user.getRole() + ")",
                    "Access Granted",
                    JOptionPane.INFORMATION_MESSAGE);
                
                CurrentUser.setUser(user);
                
                dispose();
                new DashboardPage().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Invalid username, password, or role!\n\n" +
                    "Try these demo accounts:\n" +
                    "• admin / admin123 (Administrator)\n" +
                    "• enumerator1 / enum123 (Enumerator)\n" +
                    "• viewer1 / view123 (Viewer)",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
                passwordField.requestFocus();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Database connection error: " + e.getMessage() + "\n\n" +
                "Make sure:\n1. MySQL is running\n2. Database is created\n3. Driver is in classpath",
                "System Error",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginPage().setVisible(true);
        });
    }
}