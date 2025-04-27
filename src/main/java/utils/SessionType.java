package utils;

public enum SessionType {
    SPRINT_QUALI("SQ1", "SQ2", "SQ3"),
    PRACTICE("FP1", "FP2", "FP3"),
    QUALI("Q1", "Q2", "Q3"),
    RACE("R"),
    SPRINT_RACE("SR"),
    OLD_PREQUALI("QB"),
    OLD_QUALI("QA"),
    UNKNOWN("QO", "QA");

    private final String[] codes;

    SessionType(String... codes) {
        this.codes = codes;
    }

    public static SessionType fromCode(String code) {
        for (SessionType type : values()) {
            for (String c : type.codes) {
                if (c.equals(code)) {
                    return type;
                }
            }
        }
        return UNKNOWN;
    }
}