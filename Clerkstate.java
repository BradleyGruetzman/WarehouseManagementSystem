import java.util.*;
import java.text.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
public class Clerkstate extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private static Clerkstate instance;
  private JFrame frame;
  private JList list;
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
  private Clerkstate() {
      super();
      warehouse = Warehouse.instance();
      //context = LibContext.instance();
  }
  public static Clerkstate instance() {
    if (instance == null) {
      instance = new Clerkstate();
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
            handleLogout();
        }
    });

    // Become Client Button
    JButton becomeClientButton = new JButton("Become Client");
    becomeClientButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            becomeClient();
             // ClientState index
        }
    });

    //Add a Client Button
    JButton addClientButton = new JButton("Add Client");
    addClientButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
         addClient();
        }
    });

    JButton showProductsButton = new JButton("Show Products");
    showProductsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showProducts();
        }
    });

    JButton showClientsButton = new JButton("Show Clients");
    showClientsButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showClients();
 
        }
    });

    JButton showClientsWithBalanceButton = new JButton("Show Clients with Balance");
    showClientsWithBalanceButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            showClientsWithBalance();
 
        }
    });

    JButton receivePaymentButton = new JButton("Receive a Payment");
    receivePaymentButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            receivePayment();
 
        }
    });
    frame.getContentPane().add(logoutButton);
    frame.getContentPane().add(becomeClientButton);
    frame.getContentPane().add(addClientButton);
    frame.getContentPane().add(showProductsButton);
    frame.getContentPane().add(showClientsButton);
    frame.getContentPane().add(showClientsWithBalanceButton);
    frame.getContentPane().add(receivePaymentButton);
    frame.revalidate();
    frame.repaint();
 }

 private void handleLogout() {
        WarehouseContext context = WarehouseContext.instance();
        int initialLogin = context.getLogin();

        // Determine next state based on initial login
        if (initialLogin == WarehouseContext.IsManager) {
            context.changeState(2); // Transition back to ManagerState
        } else {
            context.changeState(3); // Transition back to LoginState
        }
  }

  private void becomeClient(){
    String clientId = JOptionPane.showInputDialog(frame, "Please input the client id: ");

    if(Warehouse.instance().searchClientId(clientId)!= null){
      (WarehouseContext.instance()).setClient(clientId);

      Loginstate.instance().clear();
      (WarehouseContext.instance()).changeState(1);
      
    }
    else{

      JOptionPane.showMessageDialog(null,"Invalid client id.");

    }
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

  public void receivePayment(){
    if(list != null){
      frame.getContentPane().remove(list);
    }
    frame.revalidate();
    frame.repaint();
    String clientId = JOptionPane.showInputDialog(frame, "Enter client ID");
    Client client = warehouse.searchClientId(clientId);
    if (client == null) return;

    JOptionPane.showMessageDialog(frame, "Payment Due: " + client.getBalance());
    String clientPayment = JOptionPane.showInputDialog(frame, "Enter card number");
    Double paymentAmount = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter payment amount"));

    client.payBalance(paymentAmount);

    JOptionPane.showMessageDialog(frame, "Payment received, Current balance is " + client.getBalance());
  }

  public void showProducts() {
    if(list != null){
      frame.getContentPane().remove(list);
    }
    Iterator<Product> allProducts = warehouse.getProducts(); 
    DefaultListModel<String> productData = new DefaultListModel<String>();
    while (allProducts.hasNext()) {
        Product product = allProducts.next();
        productData.addElement(product.toString() + "\n");
    }
    list = new JList(productData);
    frame.getContentPane().add(list);
    frame.revalidate();
    frame.repaint();
}


  public void addClient(){
    String name = JOptionPane.showInputDialog(frame, "Enter client name");
    String address = JOptionPane.showInputDialog(frame, "Enter address");
    String phone = JOptionPane.showInputDialog(frame, "Enter phone");
    Client result = warehouse.addClient(name, address, phone);
    if(result == null){
      JOptionPane.showMessageDialog(frame, "Could not add client");
    }
    else{
      JOptionPane.showMessageDialog(frame, "Client added successfully");
    }
 
  
  }

  public void showClients() {
    if(list != null){
      frame.getContentPane().remove(list);
    }
    Iterator<Client> allClients = warehouse.getClients(); 
    DefaultListModel<String> clientData = new DefaultListModel<String>();
    while (allClients.hasNext()) {
        Client client = allClients.next();
        clientData.addElement(client.toString() + "\n");
    }
    if(!clientData.isEmpty()){
      list = new JList(clientData);
      frame.getContentPane().add(list);
      
    }
    frame.revalidate();
    frame.repaint();
  }

  public void showClientsWithBalance() {
    if(list != null){
      frame.getContentPane().remove(list);
    }
    Iterator<Client> allClients = warehouse.getClients(); 
    DefaultListModel<String> clientData = new DefaultListModel<String>();
    while (allClients.hasNext()) {
        Client client = allClients.next();
        if(client.getBalance() > 0){
          clientData.addElement(client.toString() + "\n");
        }
    }
    if(!clientData.isEmpty()){
      list = new JList(clientData);
      frame.getContentPane().add(list);
      
    }
    frame.revalidate();
    frame.repaint();
  }
  public boolean clientmenu()
  {
    String clientID = getToken("Please input the client id: ");
    if (Warehouse.instance().searchClientId(clientID) != null){
      (WarehouseContext.instance()).setClient(clientID);      
      return true;
    }
    else 
      System.out.println("Invalid client id."); return false;
  }

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