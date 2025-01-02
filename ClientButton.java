import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class ClientButton extends JButton implements ActionListener{ 
  //private static ClerkButton instance;
  //private JButton userButton;
  public ClientButton() {
      super("Client");
      this.setListener();
  }

/*  public static ClerkButton instance() {
    if (instance == null) {
      instance = new ClerkButton();
    }
    return instance;
  }*/

  public void setListener(){
    //System.out.println("In clerkButton setListener\n");
    this.addActionListener(this);
  }

  public void actionPerformed(ActionEvent event) {
    JFrame frame = WarehouseContext.instance().getFrame();

    String clientId = JOptionPane.showInputDialog(frame, "Please input the client id: ");

    if(Warehouse.instance().searchClientId(clientId)!= null){
      (WarehouseContext.instance()).setLogin(WarehouseContext.IsClient);
      (WarehouseContext.instance()).setClient(clientId);

      Loginstate.instance().clear();
      (WarehouseContext.instance()).changeState(1);
      
    }
    else{

      JOptionPane.showMessageDialog(null,"Invalid client id.");

    }
   
  }
}