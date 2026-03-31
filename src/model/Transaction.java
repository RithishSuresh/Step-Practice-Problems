package model;

/**
 * Represents a financial transaction.
 */
public class Transaction {
    private int id;
    private double fee;
    private long timestamp;

    public Transaction(int id, double fee, long timestamp) {
        this.id = id;
        this.fee = fee;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public double getFee() { return fee; }
    public long getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", fee=" + fee +
                ", timestamp=" + timestamp +
                '}';
    }
}
