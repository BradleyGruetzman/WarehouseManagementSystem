import java.io.*;
import java.util.*;

public class Waitlist implements Serializable {
    private static final long serialVersionUID = 1L;
    private LinkedList<WaitlistEntry> waitlistEntries;

    public Waitlist() {
        this.waitlistEntries = new LinkedList<>();
    }

    // Add a client and requested quantity to the waitlist
    public void add(Client client, int quantity) {
        waitlistEntries.add(new WaitlistEntry(client, quantity));
    }

    // Remove a client from the waitlist
    public boolean remove(Client client) {
        Iterator<WaitlistEntry> iterator = waitlistEntries.iterator();
        while (iterator.hasNext()) {
            WaitlistEntry entry = iterator.next();
            if (entry.getClient().equals(client)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    // Check if the waitlist is empty
    public boolean isEmpty() {
        return waitlistEntries.isEmpty();
    }

    // Get the current waitlist entries
    public List<WaitlistEntry> getEntries() {
        return new ArrayList<>(waitlistEntries);
    }

    // Display the waitlist
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Waitlist:\n");
        for (WaitlistEntry entry : waitlistEntries) {
            Client client = entry.getClient();
            int quantity = entry.getQuantity();
            sb.append("Client: ").append(client.getName()).append(" | Requested Quantity: ").append(quantity).append("\n");
        }
        return sb.toString();
    }
}
