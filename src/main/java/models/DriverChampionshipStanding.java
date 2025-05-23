package models;

public class DriverChampionshipStanding {
    private int driverId;
    private int position;
    private double points;
    private int wins;
    private Driver driver;

    public DriverChampionshipStanding() {
    }

    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "DriverChampionshipStanding{" +
                "driverId=" + driverId +
                ", position=" + position +
                ", points=" + points +
                ", wins=" + wins +
                '}';
    }
}
