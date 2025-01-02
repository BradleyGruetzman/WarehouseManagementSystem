import javax.swing.*;
import java.awt.*; 
import java.awt.event.*;

public class ManagerButton extends JButton implements ActionListener {

    public ManagerButton() {
        super("Manager");
        this.setListener();
    }

    public void setListener() {
        this.addActionListener(this);
    }

    public void actionPerformed(ActionEvent event) {
        if (verifyPassword("manager", "manager")) { // Verify credentials for manager
            (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
            Loginstate.instance().clear();
            (WarehouseContext.instance()).changeState(2); // Transition to ManagerState
        }
    }

    private boolean verifyPassword(String expectedUsername, String expectedPassword) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 5, 5)); 

        JLabel userLabel = new JLabel("Username:");
        JTextField userField = new JTextField(10);

        JLabel passLabel = new JLabel("Password:");
        JPasswordField passField = new JPasswordField(10);

        panel.add(userLabel);
        panel.add(userField);
        panel.add(passLabel);
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(
            null,
            panel,
            "Manager Login",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String enteredUsername = userField.getText();
            String enteredPassword = new String(passField.getPassword());

            if (enteredUsername.equals(expectedUsername) && enteredPassword.equals(expectedPassword)) {
                return true;
            } else {
                JOptionPane.showMessageDialog(
                    null,
                    "Invalid username or password.",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        return false; 
    }
}