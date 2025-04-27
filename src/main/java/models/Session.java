package models;

import utils.SessionType;

public class Session {
    private int id;
    private int number;
    private SessionType type;
    private String date;
    private String time;
    private int scheduledLaps;
    private boolean isCancelled;
    private int pointSystemId;
    private int roundId;

    public Session() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getScheduledLaps() {
        return scheduledLaps;
    }

    public void setScheduledLaps(int scheduledLaps) {
        this.scheduledLaps = scheduledLaps;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        isCancelled = cancelled;
    }

    public int getPointSystemId() {
        return pointSystemId;
    }

    public void setPointSystemId(int pointSystemId) {
        this.pointSystemId = pointSystemId;
    }

    public int getRoundId() {
        return roundId;
    }

    public void setRoundId(int roundId) {
        this.roundId = roundId;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", number=" + number +
                ", type=" + type +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", scheduledLaps=" + scheduledLaps +
                ", isCancelled=" + isCancelled +
                ", pointSystemId=" + pointSystemId +
                ", roundId=" + roundId +
                '}';
    }
}