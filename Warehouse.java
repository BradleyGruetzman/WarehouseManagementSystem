import java.io.*;
import java.util.*;
import javax.swing.*;
public class Warehouse implements Serializable{
    private static final long serialVersionUID = 1L;
    private ClientList clientList;
    private Catalog catalog;
    public static Warehouse warehouse;
    private Warehouse(){
        clientList = ClientList.instance();
        catalog = Catalog.instance();
    }

    public static Warehouse instance() {
        if (warehouse == null) {
            ClientIdServer.instance();
            ProductIdServer.instance();
            return (warehouse = new Warehouse());
        } else {
            return warehouse;
        }
    }

    public Client addClient(String name, String address, String phone) {
        Client client = new Client(name, address, phone);
        if (clientList.insertClient(client)) {
            return client;
        }
        return null;
    }
    
    public Product addProduct(String name, int stock, double price){
        Product product = new Product(name, stock, price);
        if(catalog.insertProduct(product)){
            return product;
        }
        return null;
    }

    
    public Iterator<Client> getClients() {
        return clientList.getClients();
    }

   
    public Iterator<Product> getProducts() {
        return catalog.getProducts();
    }

    public boolean addToWishlist(Client client, Product product, int quantity){ return client.addProductToWishlist(product, quantity);}

    public Client searchClientId(String clientId){return clientList.search(clientId); }

    public Product searchProductName(String productName){return catalog.search(productName); }

    



    public static Warehouse retrieve() {
        try {

            FileInputStream file = new FileInputStream("WarehouseData");
            ObjectInputStream input = new ObjectInputStream(file);
            warehouse = (Warehouse) input.readObject();
            ClientIdServer.retrieve(input);
            return warehouse;

        } catch(IOException ioe) {
            ioe.printStackTrace();
            return null;
        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            return null;
        }
    }
    public static  boolean save() {
        try {
            FileOutputStream file = new FileOutputStream("WarehouseData");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(warehouse);
            output.writeObject(ClientIdServer.instance());
            //output.writeObject(ProductIdServer.instance());
            return true;
        } catch(IOException ioe) {
            ioe.printStackTrace();
            return false;
        }
    }
    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(warehouse);
        } catch(IOException ioe) {
            System.out.println(ioe);
        }
    }
    private void readObject(java.io.ObjectInputStream input) {
        try {
            input.defaultReadObject();
            if (warehouse == null) {
                warehouse = (Warehouse) input.readObject();
            } else {
                input.readObject();
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void fulfillWaitlistedOrders() {
        Iterator<Product> products = catalog.getProducts();
    
        while (products.hasNext()) {
            Product product = products.next();
            int currentStock = product.getStock();
    
            if (currentStock > 0 && !product.getWaitlist().isEmpty()) {
                // Get a copy of the waitlist entries to avoid ConcurrentModificationException
                List<WaitlistEntry> waitlistEntries = product.getWaitlist().getEntries();
    
                for (WaitlistEntry entry : waitlistEntries) {
                    Client client = entry.getClient();
                    int requestedQuantity = entry.getQuantity();
    
                    if (requestedQuantity <= currentStock) {
                        // Fulfill this client's waitlist request
                        Map<Product, Integer> orderedItems = new HashMap<>();
                        orderedItems.put(product, requestedQuantity);
    
                        Invoice invoice = new Invoice(client, orderedItems);
                        client.addInvoice(invoice);
    
                        // Update stock and remove client from the waitlist
                        product.reduceStock(requestedQuantity);
                        product.getWaitlist().remove(client);
    
                        // Print invoice or perform other actions (e.g., save or display)
                        JOptionPane.showMessageDialog(null, "Fulfilled waitlist for client: " + client.getName());
                        JOptionPane.showMessageDialog(null, invoice);
                    } else {
                        // If requested quantity can't be fulfilled, break to move to next product
                        break;
                    }
                    
                    // Update stock after each successful fulfillment
                    currentStock = product.getStock();
                    if (currentStock <= 0) break;
                }
            }
        }
    }
    
}