public class WaitlistEntry {
    private Client client;
    private int quantity;

    public WaitlistEntry(Client client, int quantity) {
        this.client = client;
        this.quantity = quantity;
    }

    public Client getClient() {
        return client;
    }

    public int getQuantity() {
        return quantity;
    }
}
