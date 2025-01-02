import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
public class Managerstate extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private static Managerstate instance;
  private JFrame frame;
  private static final int EXIT = 0;
  private static final int ADD_MEMBER = 1;
  private static final int ADD_BOOKS = 2;
  private static final int ISSUE_BOOKS = 3;
  private static final int RETURN_BOOKS = 4;
  private static final int REMOVE_BOOKS = 6;
  private static final int PLACE_HOLD = 7;
  private static final int REMOVE_HOLD = 8;
  private static final int PROCESS_HOLD = 9;
  private static final int GET_TRANSACTIONS = 10;
  private static final int USERMENU = 11;
  private static final int HELP = 13;
  private Managerstate() {
      super();
      warehouse = Warehouse.instance();
      //context = LibContext.instance();
  }
  public static Managerstate instance() {
    if (instance == null) {
      instance = new Managerstate();
    }
    return instance;
  }

  @Override
  public void run() {
    frame = WarehouseContext.instance().getFrame();
    frame.getContentPane().removeAll();

    JButton logoutButton = new JButton("Logout");
    logoutButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            WarehouseContext.instance().changeState(3); // LoginState index
        }
    });

    // Become Clerk Button
    JButton becomeClerkButton = new JButton("Become Clerk");
    becomeClerkButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            WarehouseContext.instance().changeState(0); // ClerkState index
        }
    });

    JButton addProductButton = new JButton("Add Product");
    addProductButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          addProduct();
        }
    });

    JButton receiveShipmentButton = new JButton("Receive Shipment");
    receiveShipmentButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          receiveShipment();
        }
    });

    JButton displayWaitlistButton = new JButton("Display Products Waitlist");
    displayWaitlistButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          displayProductWaitlist();
        }
    });

    frame.getContentPane().add(logoutButton);
    frame.getContentPane().add(becomeClerkButton);
    frame.getContentPane().add(addProductButton);
    frame.getContentPane().add(receiveShipmentButton);
    frame.getContentPane().add(displayWaitlistButton);
    frame.revalidate();
    frame.repaint();
 }


public void addProduct() {
    boolean moreProducts = true;
    while (moreProducts) {
        String name = JOptionPane.showInputDialog(frame, "Enter product name");
        int stock = Integer.parseInt(JOptionPane.showInputDialog(frame,"Enter stock amount"));
        double price = Double.parseDouble(JOptionPane.showInputDialog(frame,"Enter price"));

        Product result = warehouse.addProduct(name, stock, price);
        if (result == null) {
            JOptionPane.showMessageDialog(frame,"Could not add product");
        } else {
            JOptionPane.showMessageDialog(frame, "Product added: " + result);
        }

        // Ask the user if they want to add another product
        String more = JOptionPane.showInputDialog(frame,"Do you want to add another product? (Y|y for yes, any other key for no): ");
        if (!more.equalsIgnoreCase("y")) {
            moreProducts = false;  // Exit the loop if the user doesn't want to add more
        }
    }
}

public void receiveShipment() {
  String productName = JOptionPane.showInputDialog(frame, "Enter product name:");
  if (productName == null || productName.isEmpty()) {
      return;
  }

  String quantityStr = JOptionPane.showInputDialog(frame, "Enter quantity received:");
  if (quantityStr == null || quantityStr.isEmpty()) {
      return;
  }
  
  try {
      int quantity = Integer.parseInt(quantityStr); // Convert quantity to integer
      Product product = warehouse.searchProductName(productName);

      if (product != null) {
          // Update the stock
          product.increaseStock(quantity);

          JOptionPane.showMessageDialog(frame, "Shipment received. Updated stock for " 
                  + productName + ": " + product.getStock());

          warehouse.fulfillWaitlistedOrders();
          
      } else {
          JOptionPane.showMessageDialog(frame, "Product not found in catalog.");
      }
  } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(frame, "Invalid quantity. Please enter a numeric value.");
  }
}

public void displayProductWaitlist() {
  String productName = JOptionPane.showInputDialog(frame, "Enter product name:");
  if (productName == null || productName.isEmpty()) {
      return;
  }

  Product product = warehouse.searchProductName(productName);
  if (product == null) {
      JOptionPane.showMessageDialog(frame, "Product not found.");
      return;
  }

  Waitlist waitlist = product.getWaitlist();
  if (waitlist.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "No waitlist entries for this product.");
      return;
  }

  StringBuilder waitlistInfo = new StringBuilder("Waitlist for product: " + product.getName() + "\n\n");
  waitlistInfo.append(waitlist.toString());

  JOptionPane.showMessageDialog(frame, waitlistInfo.toString());
}



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
  public int getNumber(String prompt) {
    do {
      try {
        String item = getToken(prompt);
        Integer num = Integer.valueOf(item);
        return num.intValue();
      } catch (NumberFormatException nfe) {
        System.out.println("Please input a number ");
      }
    } while (true);
  }
  public Calendar getDate(String prompt) {
    do {
      try {
        Calendar date = new GregorianCalendar();
        String item = getToken(prompt);
        DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
        date.setTime(df.parse(item));
        return date;
      } catch (Exception fe) {
        System.out.println("Please input a date as mm/dd/yy");
      }
    } while (true);
  }
  public int getCommand() {
    do {
      try {
        int value = Integer.parseInt(getToken("Enter command:" + HELP + " for help"));
        if (value >= EXIT && value <= HELP) {
          return value;
        }
      } catch (NumberFormatException nfe) {
        System.out.println("Enter a number");
      }
    } while (true);
  }

  public void help() {
    System.out.println("Enter a number between 0 and 12 as explained below:");
    System.out.println(EXIT + " to Exit\n");
    System.out.println(ADD_MEMBER + " to add a member");
    System.out.println(ADD_BOOKS + " to  add books");
    System.out.println(RETURN_BOOKS + " to  return books ");
    System.out.println(REMOVE_BOOKS + " to  remove books");
    System.out.println(PROCESS_HOLD + " to  process holds");
    System.out.println(USERMENU + " to  switch to the user menu");
    System.out.println(HELP + " for help");
  }












  /*public boolean clientmenu()
  {
    String userID = getToken("Please input the user id: ");
    if (Warehouse.instance().searchMembership(clientID) != null){
      (WarehouseContext.instance()).setUser(clientID);      
      return true;
    }
    else 
      System.out.println("Invalid user id."); return false;
  }*/

  public void terminate(int exitcode)
  {
    (WarehouseContext.instance()).changeState(exitcode); // exit with a code 
  }
 

  public void process() {
    int command, exitcode = -1;
    help();
    boolean done = false;
    while (!done) {
      switch (getCommand()) {
        /*case ADD_MEMBER:        addMember();
                                break;
        case ADD_BOOKS:         addBooks();
                                break;
        case RETURN_BOOKS:      returnBooks();
                                break;
        case REMOVE_BOOKS:      removeBooks();
                                break;
        case PROCESS_HOLD:      processHolds();
                                break;
        case USERMENU:          if (usermenu())
                                  {exitcode = 1;
                                   done = true;}
                                break;
        case HELP:              help();
                                break;
        case EXIT:              exitcode = 0;
                                done = true; break;*/
      }
    }
    terminate(exitcode);
  }
  /*public void run() {
    process();
  }*/



}