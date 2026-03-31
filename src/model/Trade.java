package model;

/**
 * Represents a trade in the system.
 */
public class Trade {
    private String id;
    private double volume;

    public Trade(String id, double volume) {
        this.id = id;
        this.volume = volume;
    }

    public String getId() { return id; }
    public double getVolume() { return volume; }

    @Override
    public String toString() {
        return "Trade{" +
                "id='" + id + '\'' +
                ", volume=" + volume +
                '}';
    }
}
