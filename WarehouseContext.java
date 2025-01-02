import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.*;
import java.io.*;
public class WarehouseContext {
  
  private static int currentState;
  private static Warehouse warehouse;
  private static WarehouseContext context;
  private int currentUser;
  private String clientID;
  private static JFrame WarehouseFrame; 
  private BufferedReader reader = new BufferedReader(new 
                                      InputStreamReader(System.in));
  public static final int IsClerk = 0;
  public static final int IsClient = 1;
  public static final int IsManager = 2;
  private static WarehouseState[] states;
  private int[][] nextState;

  public String getToken(String prompt) {
    do {
      try {
        System.out.println(prompt);
        String line = reader.readLine();
        StringTokenizer tokenizer = new StringTokenizer(line,"\n\r\f");
        if (tokenizer.hasMoreTokens()) {
          return tokenizer.nextToken();
        }
      } catch (IOException ioe) {
        System.exit(0);
      }
    } while (true);
  }
  
  private boolean yesOrNo(String prompt) {
    String more = getToken(prompt + " (Y|y)[es] or anything else for no");
    if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
      return false;
    }
    return true;
  }

  private void retrieve() {
    try {
        Warehouse tempWarehouse = Warehouse.retrieve();
      if (tempWarehouse != null) {
        System.out.println(" The Warehouse has been successfully retrieved from the file WarehouseData \n" );
        warehouse = tempWarehouse;
      } else {
        System.out.println("File doesnt exist; creating new warehouse" );
        warehouse = Warehouse.instance();
      }
    } catch(Exception cnfe) {
      cnfe.printStackTrace();
    }
  }

  public void setLogin(int code)
  {currentUser = code;}

  public void setClient(String cID)
  { clientID = cID;}

  public int getLogin()
  { return currentUser;}

  public String getClient()
  { return clientID;}

  public JFrame getFrame()
  { return WarehouseFrame;}

  private WarehouseContext() { //constructor
    System.out.println("In Warehousecontext constructor");
    if (yesOrNo("Look for saved data and  use it?")) {
      retrieve();
    } else {
      warehouse = Warehouse.instance();
    }
    // set up the FSM and transition table;
    states = new WarehouseState[4];
    states[0] = Clerkstate.instance();
    states[1] = Clientstate.instance(); 
    states[2] = Managerstate.instance();
    states[3]=  Loginstate.instance();
    nextState = new int[4][4];
    nextState[0][1] = 1;nextState[0][2] = 2;nextState[0][3] = 3;
    nextState[1][3] = 3;
    nextState[2][0] = 0;nextState[2][3] = 3;
    nextState[3][1] = 1;nextState[3][2] = 2;nextState[3][0] = 0;nextState[3][3] = -2;
    currentState = 3;
    JFrame.setDefaultLookAndFeelDecorated(true);
    WarehouseFrame = new JFrame("Warehouse GUI");
	WarehouseFrame.addWindowListener(new WindowAdapter()
       {public void windowClosing(WindowEvent e){System.exit(0);}});
    WarehouseFrame.setSize(600,600);
    WarehouseFrame.setLocation(600, 600);
    }

  public void changeState(int transition)
  {
    //System.out.println("current state " + currentState + " \n \n ");
    currentState = nextState[currentState][transition];
    if (currentState == -2) 
      {System.out.println("Error has occurred"); terminate();}
    if (currentState == -1) 
      { //System.out.println("current state " + currentState + " \n \n ");
      terminate();}
    states[currentState].run();
  }

  private void terminate() {
    int saveOption = JOptionPane.showConfirmDialog(
        null,
        "Would you like to save data?",
        "Save Confirmation",
        JOptionPane.YES_NO_OPTION
    );

    if (saveOption == JOptionPane.YES_OPTION) {
        if (warehouse.save()) {
            JOptionPane.showMessageDialog(
                null,
                "The warehouse has been successfully saved in the file WarehouseData.",
                "Save Successful",
                JOptionPane.INFORMATION_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(
                null,
                "There was an error saving the warehouse data.",
                "Save Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    JOptionPane.showMessageDialog(
        null,
        "Goodbye!",
        "Termination",
        JOptionPane.INFORMATION_MESSAGE
    );

    System.exit(0);
}

  public static WarehouseContext instance() {
    if (context == null) {
       System.out.println("calling constructor");
      context = new WarehouseContext();
    }
    return context;
  }

  public void process(){
    states[currentState].run();
  }
  
  public static void main (String[] args){
    WarehouseContext.instance().process(); 
    states[currentState].run();
  }


}
