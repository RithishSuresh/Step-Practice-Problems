package model;

/**
 * Represents a client in the financial system.
 */
public class Client {
    private String name;
    private int riskScore;
    private double accountBalance;

    public Client(String name, int riskScore, double accountBalance) {
        this.name = name;
        this.riskScore = riskScore;
        this.accountBalance = accountBalance;
    }

    public String getName() { return name; }
    public int getRiskScore() { return riskScore; }
    public double getAccountBalance() { return accountBalance; }

    @Override
    public String toString() {
        return "Client{" +
                "name='" + name + '\'' +
                ", riskScore=" + riskScore +
                ", balance=" + accountBalance +
                '}';
    }
}
