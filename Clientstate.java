import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

public class Clientstate extends WarehouseState {
  private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
  private static Warehouse warehouse;
  private WarehouseContext context;
  private static Clientstate instance;
  private JFrame frame;
  private JTextArea text;
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
  private Clientstate() {
      super();
      warehouse = Warehouse.instance();
      //context = LibContext.instance();
  }
  public static Clientstate instance() {
    if (instance == null) {
      instance = new Clientstate();
    }
    return instance;
  }
    @Override
  public void run() {
  frame = WarehouseContext.instance().getFrame();
  frame.getContentPane().removeAll();
  JButton showClientDetailsButton = new JButton("Show Client Details");
  JButton logoutButton = new JButton("Logout");
  JButton addProductToWishlistButton = new JButton("Add Product to Wishlist");
  JButton displayClientsWishlistButton = new JButton("Display Client's Wishlist");
  JButton placeOrderButton = new JButton("Place an Order");
  JButton showProductsButton = new JButton("Show Products");

  addProductToWishlistButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        if(text != null){
          frame.getContentPane().remove(text);
          frame.revalidate();
          frame.repaint();
        }
        addProductToClientsWishlist();

    }
});

  displayClientsWishlistButton.addActionListener(new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent e) {
          displayClientsWishlist(); 
    }
  });

  showClientDetailsButton.addActionListener(new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent e) {
          if(text != null){
            frame.getContentPane().remove(text);
            frame.revalidate();
            frame.repaint();
          }
          
          handleShowClientDetails(); 

    }
  });
  logoutButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          if(text != null){
            frame.getContentPane().remove(text);
            frame.revalidate();
            frame.repaint();
          }
          
          handleLogout(); // LoginState index

      }
  });

  placeOrderButton.addActionListener(new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent e) {
      if(text != null){
        frame.getContentPane().remove(text);
        frame.revalidate();
        frame.repaint();
      }
            handlePlaceOrder();

    }
  });

  showProductsButton.addActionListener(new ActionListener(){
    @Override
    public void actionPerformed(ActionEvent e) {
      if(text != null){
        frame.getContentPane().remove(text);
        frame.revalidate();
        frame.repaint();
      }
            showProducts();

    }
  });

  frame.getContentPane().add(showClientDetailsButton);
  frame.getContentPane().add(addProductToWishlistButton);
  frame.getContentPane().add(displayClientsWishlistButton);
  frame.getContentPane().add(placeOrderButton);
  frame.getContentPane().add(logoutButton);
  frame.getContentPane().add(showProductsButton);
  

  frame.revalidate();
  frame.repaint();
 }

  public void addProductToClientsWishlist() {
    WarehouseContext context = WarehouseContext.instance();
    String clientId = context.getClient();
    Client client = warehouse.searchClientId(clientId);

    if(!isValidClient(client)) return;

    // Validate the client
    if (client == null) {
        JOptionPane.showMessageDialog(frame, "Client not found.");
        return;
    }

    boolean moreProducts = true;
    while (moreProducts) {
        String productName = JOptionPane.showInputDialog(frame, "Enter product name:");
        if (productName == null || productName.isEmpty()) {
            break; // Break if the user cancels or enters an empty string
        }

        Product product = warehouse.searchProductName(productName);

        if (product != null) {
            try {
                String quantityStr = JOptionPane.showInputDialog(frame, "Enter quantity:");
                if (quantityStr == null || quantityStr.isEmpty()) {
                    break;
                }

                int quantity = Integer.parseInt(quantityStr);
                boolean added = warehouse.addToWishlist(client, product, quantity);

                if (added) {
                    JOptionPane.showMessageDialog(frame, "Product added to wishlist: " + product.getName() + " (Quantity: " + quantity + ")");
                } else {
                    JOptionPane.showMessageDialog(frame, "Failed to add product to wishlist.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Invalid quantity. Please enter a numeric value.");
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Product does not exist.");
        }
        int confirm = JOptionPane.showConfirmDialog(frame, "Do you want to add another product to the client's wishlist?", 
                                                    "Add Another Product", JOptionPane.YES_NO_OPTION);
        moreProducts = (confirm == JOptionPane.YES_OPTION);
    }
}

public void showProducts() {
  Iterator<Product> allProducts = warehouse.getProducts(); 
  DefaultListModel<String> productData = new DefaultListModel<String>();
  while (allProducts.hasNext()) {
      Product product = allProducts.next();
      productData.addElement("Name: " + product.getName() + " | Price: $" + product.getPrice());
  }
  JList productList = new JList(productData);
  frame.getContentPane().add(productList);
  frame.revalidate();
  frame.repaint();
}

  public void displayClientsWishlist() {
    WarehouseContext context = WarehouseContext.instance();
    String clientId = context.getClient();
    Client client = warehouse.searchClientId(clientId);

    if(!isValidClient(client)) return;
    if (clientId == null || clientId.isEmpty()) {
      return;
    }

    if (!isValidClient(client)) {
      JOptionPane.showMessageDialog(frame, "Client not found.");
      return;
  }

    Wishlist wishlist = client.getWishlist();

    if (isWishlistEmpty(wishlist)) {
      JOptionPane.showMessageDialog(frame, "The client's wishlist is empty.");
      return;
   }

    String wishlistDetails = client.displayWishlist();
    JOptionPane.showMessageDialog(frame, wishlistDetails);
  }


  private void handleLogout() {
        WarehouseContext context = WarehouseContext.instance();
        int initialLogin = context.getLogin();

        // Determine next state based on initial login
        if (initialLogin == WarehouseContext.IsClerk || initialLogin == WarehouseContext.IsManager) {
            context.changeState(0); // Transition back to ClerkState
        } else {
            context.changeState(3); // Transition back to LoginState
        }
  }

  private void handleShowClientDetails() {
    WarehouseContext context = WarehouseContext.instance();
    JOptionPane.showMessageDialog(frame, warehouse.searchClientId(context.getClient()));
  }

  private boolean isValidClient(Client client) {
    if (client == null) {
        JOptionPane.showMessageDialog(frame, "Client not found.", "Error", JOptionPane.ERROR_MESSAGE);
        return false;
    }
    return true;
}

  private boolean isWishlistEmpty(Wishlist wishlist) {
    if (wishlist.isEmpty()) {
      JOptionPane.showMessageDialog(frame, "The wishlist is empty.", "Wishlist", JOptionPane.INFORMATION_MESSAGE);
      return true;
    }
    return false;
}

  private void handlePlaceOrder(){
   
    WarehouseContext context = WarehouseContext.instance();
    String clientId = context.getClient();
    Client client = warehouse.searchClientId(clientId);

    if(!isValidClient(client)) return;

    Wishlist wishlist = client.getWishlist();
    if(isWishlistEmpty(wishlist)) return;

    Map<Product, Integer> orderItems = new HashMap<>();

    Map<Product, Integer> items = wishlist.getWishlistItems();
    for (Map.Entry<Product, Integer> entry : items.entrySet()) {
        Product product = entry.getKey();
        int requestedQuantity = entry.getValue();
        int availableStock = product.getStock();
        String options[] = {"Change quantity", "Remove item", "Leave as is"};
        JOptionPane.showMessageDialog(frame, "Product: " + product.getName() + " | Requested: " + requestedQuantity + " | Available: " + availableStock);
        int userChoice = JOptionPane.showOptionDialog(frame, "Wishlist", "Options", 0, 1, null, options, null );
        // Handle user choice
        if (userChoice == 0) {
            // Update quantity
            int newQuantity = Integer.parseInt(JOptionPane.showInputDialog(frame,"Enter new quantity: "));
            if (newQuantity <= 0) {
                wishlist.removeProduct(product);
                JOptionPane.showMessageDialog(frame, "Product removed from wishlist.");
                continue; // Skip further processing for this product
            } else {
                wishlist.updateProductQuantity(product, newQuantity); // Update the quantity in the wishlist
                requestedQuantity = newQuantity; // Update requestedQuantity to reflect the new value
            }
        } else if (userChoice == 1) {
            wishlist.removeProduct(product);
            JOptionPane.showMessageDialog(frame, "Product removed from wishlist.");
            continue; // Skip further processing for this product
        }

        //Check if the requested quantity exceeds available stock
        if (requestedQuantity > availableStock) {
            int fulfilledQuantity = availableStock;
            int waitlistedQuantity = requestedQuantity - availableStock;

            // Update the stock and fulfill only what is available
            product.reduceStock(fulfilledQuantity);
            orderItems.put(product, fulfilledQuantity);

            // Add the remaining quantity to the waitlist
            product.addToWaitlist(client, waitlistedQuantity);
            JOptionPane.showMessageDialog(frame, "Only " + fulfilledQuantity + " of " + requestedQuantity + " could be fulfilled. Remaining " +
                                waitlistedQuantity + " has been added to the waitlist.");
        } else {
            // Fulfill the entire order if stock is sufficient
            product.reduceStock(requestedQuantity);
            orderItems.put(product, requestedQuantity);
        }
    }

    if (!orderItems.isEmpty()) {
        createInvoiceForClient(client, orderItems);
        wishlist.getWishlistItems().clear();
        JOptionPane.showMessageDialog(frame, "Wishlist has been cleared. Order has been placed.");
    } else {
        JOptionPane.showMessageDialog(frame, "No items left in the wishlist. No order created.");
    }
  }


 private void createInvoiceForClient(Client client, Map<Product, Integer> orderItems) {
    Invoice invoice = new Invoice(client, orderItems);
    client.addInvoice(invoice);
    if(text != null){
      frame.getContentPane().remove(text);
    }
    text = new JTextArea(40, 40);
    text.append("Genereated Invoice: \n" + invoice.toString());
    frame.getContentPane().add(text);
    frame.revalidate();
    frame.repaint();
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




  /*public boolean usermenu()
  {
    String userID = getToken("Please input the user id: ");
    if (Warehouse.instance().searchMembership(userID) != null){
      (WarehouseContext.instance()).setUser(userID);      
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