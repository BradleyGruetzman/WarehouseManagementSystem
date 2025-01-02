import java.util.*;
import java.text.*;
import java.io.*;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginState extends WarehouseState{
    private static final int CLERK_LOGIN = 1;
    private static final int MANAGER_LOGIN = 0;
    private static final int USER_LOGIN = 2;
    private static final int EXIT = 3;
    private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private WarehouseContext context;
    private static LoginState instance;
    private JFrame frame;
    private JPanel panel;
    private JButton clientButton;
    private JButton salesclerkButton;
    private JButton managerButton;
    private JButton exitButton;

    private LoginState() {
        super();
        // context = LibContext.instance();
    }

    public static LoginState instance() {
        if (instance == null) {
            instance = new LoginState();
        }
        return instance;
    }

    private boolean verifyPassword(String user, String pass, String role){
        if("clerk".equals(role)) {
            if ("salesclerk".equals(user) && "salesclerk".equals(pass)) {
                return true;
            } else {
                //System.out.println(user + pass);
                return false;
            }
        }
        if("manager".equals(role)) {
            if ("manager".equals(user) && "manager".equals(pass)) {
                return true;
            } else {
                //System.out.println(user + pass);
                return false;
            }
        }
        else{
            return false;
        }
    }

    private void clerk() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Enter User ID:", usernameField,
                "Enter password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Enter User ID", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String userId = usernameField.getText();
            char[] passChars = passwordField.getPassword();
            String passId = new String(passChars);

            if (userId.isEmpty() || passId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (verifyPassword(userId, passId, "clerk")) {
                    (WarehouseContext.instance()).setLogin(WarehouseContext.IsClerk);
                    (WarehouseContext.instance()).changeState(1);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }



    private void manager() {
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
                "Enter User ID:", usernameField,
                "Enter password:", passwordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Enter User ID", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String userId = usernameField.getText();
            char[] passChars = passwordField.getPassword();
            String passId = new String(passChars);

            if (userId.isEmpty() || passId.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                if (verifyPassword(userId, passId, "manager")) {
                    (WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
                    (WarehouseContext.instance()).changeState(0);
                    frame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Username or Password!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void user(){

        JTextField userIdField = new JTextField();
        Object[] message = {
                "Enter User ID:", userIdField
        };
        int option = JOptionPane.showConfirmDialog(null, message, "Enter User ID", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String userId = userIdField.getText();
            if (Warehouse.instance().searchMembership(userId) != null){
                (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
                (WarehouseContext.instance()).setUser(userId);
                (WarehouseContext.instance()).changeState(2);
                frame.dispose();
            } else {
                // Handle empty user ID
                JOptionPane.showMessageDialog(null, "Invalid Client ID!", "Error", JOptionPane.ERROR_MESSAGE);

            }
        }


    }

    public void process(){


        frame = new JFrame("Login");
        frame.setBounds(100, 100, 300, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        clientButton = new JButton("Client");
        salesclerkButton = new JButton("Salesclerk");
        managerButton = new JButton("Manager");
        exitButton = new JButton("Exit");

        clientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                user();

            }
        });

        salesclerkButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                clerk();

            }
        });

        managerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                manager();

            }
        });

        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle exit button click - transition to exit state

                (WarehouseContext.instance()).changeState(3); // Change to the exit state
                frame.dispose(); // Dispose the JFrame
            }
        });

        panel.add(clientButton);
        panel.add(salesclerkButton);
        panel.add(managerButton);
        panel.add(exitButton);

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void run() {
        process();
    }
}