package models;

import utils.SessionType;

public class Session {
    private final int id;
    private final SessionType type;
    private final String date;
    private final String time;
    private final int roundId;

    public Session(int id, SessionType type, String date, String time, int roundId) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.time = time;
        this.roundId = roundId;
    }

    public int getId() {
        return id;
    }

    public SessionType getType() {
        return type;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getRoundId() {
        return roundId;
    }
}