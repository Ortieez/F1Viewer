package models;

public class Round {
    private int id;
    private int seasonId;
    private String name;
    private Circuit circuit;
    private String date;

    public Round() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Circuit getCircuit() {
        return circuit;
    }

    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Round{" +
                "id=" + id +
                ", seasonId=" + seasonId +
                ", name='" + name + '\'' +
                ", circuit='" + circuit + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}