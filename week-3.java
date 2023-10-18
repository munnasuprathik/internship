import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.LineBorder;

public class PasswordStrengthIndicator {
    private JLabel messageLabel;
    private JPasswordField passwordField;
    private JCheckBox showPasswordCheckbox;
    private JProgressBar strengthBar;
    private JButton enterButton;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Password Strength Indicator");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 200);
            frame.setResizable(false); // Disable resizing

            JPanel panel = new JPanel();
            panel.setLayout(new GridLayout(6, 1, 5, 5)); // Improved layout

            PasswordStrengthIndicator app = new PasswordStrengthIndicator();
            app.messageLabel = new JLabel();
            app.passwordField = new JPasswordField(20);
            app.showPasswordCheckbox = new JCheckBox("Show Password");
            app.enterButton = new JButton("Enter");
            app.strengthBar = new JProgressBar();

            // Styling improvements
            app.strengthBar.setStringPainted(true);
            app.strengthBar.setBorderPainted(false);
            app.strengthBar.setForeground(Color.GRAY);
            app.strengthBar.setBackground(Color.WHITE);
            app.strengthBar.setString("");
            app.passwordField.setBorder(new LineBorder(Color.BLACK, 1)); // Add border
            app.enterButton.setBackground(new Color(46, 139, 87)); // Green background
            app.enterButton.setForeground(Color.WHITE);
            app.enterButton.setFocusPainted(false);

            panel.add(new JLabel("Enter Password: "));
            panel.add(app.passwordField);
            panel.add(app.strengthBar);
            panel.add(app.messageLabel);
            panel.add(app.showPasswordCheckbox);
            panel.add(app.enterButton);

            frame.add(panel);

            app.passwordField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateStrength();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateStrength();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateStrength();
                }

                private void updateStrength() {
                    String password = new String(app.passwordField.getPassword());
                    String strength = checkPasswordStrength(password);

                    if (strength.equals("Strong")) {
                        app.strengthBar.setValue(100);
                        app.strengthBar.setForeground(Color.GREEN);
                    } else if (strength.equals("Medium")) {
                        app.strengthBar.setValue(50);
                        app.strengthBar.setForeground(Color.ORANGE);
                    } else {
                        app.strengthBar.setValue(20);
                        app.strengthBar.setForeground(Color.RED);
                    }
                    app.strengthBar.setString(strength);
                    app.setStrengthMessages(password);

                    if (strength.equals("Strong")) {
                        app.enterButton.setEnabled(true);
                    } else {
                        app.enterButton.setEnabled(false);
                    }
                }

                private String checkPasswordStrength(String password) {
                    int length = password.length();
                    boolean hasUpperCase = !password.equals(password.toLowerCase());
                    boolean hasLowerCase = !password.equals(password.toUpperCase());
                    boolean hasDigit = password.matches(".*\\d.*");
                    boolean hasSpecialChar = !password.matches("[A-Za-z0-9]*");

                    if (length >= 8 && hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar) {
                        return "Strong";
                    } else if (length >= 6) {
                        return "Medium";
                    } else {
                        return "Weak";
                    }
                }
            });

            app.showPasswordCheckbox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JCheckBox checkBox = (JCheckBox) e.getSource();
                    app.passwordField.setEchoChar(checkBox.isSelected() ? '\0' : '*');
                }
            });

            app.enterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String password = new String(app.passwordField.getPassword());
                    if (password.isEmpty()) {
                        app.messageLabel.setText("Please enter a password.");
                    } else {
                        JOptionPane.showMessageDialog(null, "Your password is strong.", "Password Strength",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            });

            app.enterButton.setEnabled(false); // Disable the "Enter" button initially

            frame.setVisible(true);
        });
    }

    private void setStrengthMessages(String password) {
        // Your existing code for strength messages
    }
}
