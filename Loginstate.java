import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class Loginstate extends WarehouseState implements ActionListener{
  private static final int CLERK_LOGIN = 0;
  private static final int CLIENT_LOGIN = 1;
  private static final int MANAGER_LOGIN = 2;
  private static final int EXIT = 3;
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));  
  private WarehouseContext context;
  private JFrame frame;
  private static Loginstate instance;
  private AbstractButton clientButton, logoutButton, clerkButton, managerButton;
  //private ClerkButton clerkButton;
  private Loginstate() {
      super();
      /*userButton = new JButton("user");
      clerkButton =  new JButton("clerk");
      logoutButton = new JButton("logout");
      userButton.addActionListener(this);
      logoutButton.addActionListener(this);
      clerkButton.addActionListener(this); */
 //     ((ClerkButton)clerkButton).setListener();
  }

  public static Loginstate instance() {
    if (instance == null) {
      instance = new Loginstate();
    }
    return instance;
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource().equals(this.clientButton)) 
       {      
         this.client();}
     if (event.getSource().equals(this.logoutButton)) 
       (WarehouseContext.instance()).changeState(3);
    else if (event.getSource().equals(this.clerkButton)) 
       this.clerk();
    else if (event.getSource().equals(this.managerButton))
       this.manager();

  } 

 

  public void clear() { //clean up stuff
    frame.getContentPane().removeAll();
    frame.paint(frame.getGraphics());   
  }  

  private void clerk() {
     System.out.println("clerk called");
    /*(WarehouseContext.instance()).setLogin(WarehouseContext.IsClerk);
     clear();
    (WarehouseContext.instance()).changeState(0);*/
  } 

  private void manager() {
    System.out.println("manager called");
    /*(WarehouseContext.instance()).setLogin(WarehouseContext.IsManager);
    clear();
    (WarehouseContext.instance()).changeState(2);*/
  }
  
  private void client(){
   System.out.println("client called");
  } 


  public void run() {
   
   frame = WarehouseContext.instance().getFrame();
   frame.getContentPane().removeAll();
   frame.getContentPane().setLayout(new FlowLayout());
      clientButton = new ClientButton();
      clerkButton =  new ClerkButton();
      managerButton = new ManagerButton();
      logoutButton = new JButton("logout");  
      
      logoutButton.addActionListener(this);
           
   frame.getContentPane().add(this.clientButton);
   frame.getContentPane().add(this.clerkButton);
   frame.getContentPane().add(this.managerButton);
   frame.getContentPane().add(this.logoutButton);
   frame.setVisible(true);
   frame.paint(frame.getGraphics()); 
   //frame.repaint();
   frame.toFront();
   frame.requestFocus();
   
  }
}