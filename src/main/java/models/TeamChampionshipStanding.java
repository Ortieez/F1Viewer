package models;

public class TeamChampionshipStanding {
    private int teamId;
    private int position;
    private double points;
    private int wins;

    public TeamChampionshipStanding() {
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
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

    @Override
    public String toString() {
        return "TeamChampionshipStanding{" +
                "teamId=" + teamId +
                ", position=" + position +
                ", points=" + points +
                ", wins=" + wins +
                '}';
    }
}