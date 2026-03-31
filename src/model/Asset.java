package model;

/**
 * Represents a financial asset in a portfolio.
 */
public class Asset {
    private String name;
    private double returnRate;
    private double volatility;

    public Asset(String name, double returnRate, double volatility) {
        this.name = name;
        this.returnRate = returnRate;
        this.volatility = volatility;
    }

    public String getName() { return name; }
    public double getReturnRate() { return returnRate; }
    public double getVolatility() { return volatility; }

    @Override
    public String toString() {
        return "Asset{" +
                "name='" + name + '\'' +
                ", returnRate=" + returnRate +
                ", volatility=" + volatility +
                '}';
    }
}
