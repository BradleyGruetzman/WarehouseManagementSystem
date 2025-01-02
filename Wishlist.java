import java.io.*;
import java.util.*;

public class Wishlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private Map<Product, Integer> wishlistItems;

    public Wishlist() {
        wishlistItems = new HashMap<>();
    }

    // Add a product to the wishlist or update quantity if it already exists
    public boolean addProduct(Product product, int quantity) {
        wishlistItems.put(product, wishlistItems.getOrDefault(product, 0) + quantity);
        return true;
    }

    // Display the wishlist items with their quantities
    /*public void displayWishlist() {
        if (wishlistItems.isEmpty()) {
            System.out.println("Wishlist is empty.");
        } 
        else {
            for (Map.Entry<Product, Integer> entry : wishlistItems.entrySet()) {
                Product product = entry.getKey();
                int quantity = entry.getValue();
                double totalPrice = quantity * product.getPrice();
                System.out.println("Product Name: " + product.getName() + 
                                   " | ID: " + product.getID() + 
                                   " | Quantity: " + quantity + 
                                   " | Total Price: $" + totalPrice);
            }
        }
    } */

    public String displayWishlist() {
        if (wishlistItems.isEmpty()) {
            return "Wishlist is empty.";
        } 
    
        StringBuilder details = new StringBuilder();
        for (Map.Entry<Product, Integer> entry : wishlistItems.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();
            double totalPrice = quantity * product.getPrice();
    
            details.append("Product Name: ").append(product.getName())
                   .append(" | ID: ").append(product.getID())
                   .append(" | Quantity: ").append(quantity)
                   .append(" | Total Price: $").append(totalPrice)
                   .append("\n");
        }
        return details.toString();
    }

    public boolean updateProductQuantity(Product product, int newQuantity) {
        if (newQuantity <= 0){
            wishlistItems.remove(product);
            return true;
        } else {
            wishlistItems.put(product, newQuantity);
            return true;
        }
    }

    public boolean removeProduct(Product product){
        return wishlistItems.remove(product) != null;
    }

    // Get all products and their quantities
    public Map<Product, Integer> getWishlistItems() {
        return wishlistItems;
    }

    public boolean isEmpty() {
        return wishlistItems.isEmpty();
    }
}