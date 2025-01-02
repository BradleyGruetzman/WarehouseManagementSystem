import java.io.*;
import java.util.*;

public class Client implements Serializable {
    private String id;
    private String name;
    private String address;
    private String phone;
    private Wishlist wishlist;
    private List<Invoice> invoiceList;
    private Double balance;
    private Double credit;
    private static final String CLIENT_STRING = "C";
    private static final long serialVersionUID = 1L;

    public Client(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.invoiceList = new LinkedList<>();
        id = CLIENT_STRING + ClientIdServer.instance().getId();
        wishlist = new Wishlist();
        balance = 0.0;
        credit = 0.0;
    }

    public String getName(){ 
        return name;
    }
    public String getPhone(){
        return phone; 
    }
    public String getAddress(){ 
        return address; 
    }
    public String getId() {
        return id; 
    }
    public Wishlist getWishlist(){
        return wishlist; 
    }

    public Double getBalance(){
        return balance;
    }
    public boolean addProductToWishlist(Product product, int quantity) {
        return wishlist.addProduct(product, quantity);
    }

    public void displayWishlist() {
        wishlist.displayWishlist();
    }

    public Iterator<Invoice> getInvoiceIterator() {
        return invoiceList.iterator();
    }

    public void addInvoice(Invoice invoice){
        invoiceList.add(invoice);
        balance += invoice.getTotalCost();
    }

    public void payBalance(Double amount){
        balance -= amount;
        if(balance < 0){
            credit = balance * - 1;
            balance = 0.0;
        }
    }
    @Override
    public String toString() {
        return "Client Name: " + name + " Id: " + id + " Address: " + address + " Phone: " + phone + " Balance: " + balance + " Credit: " + credit;
    }
}
