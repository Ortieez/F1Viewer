package utils;

public enum SessionType {
    PRACTICE_1("FP1", "P1", "Practice 1"),
    PRACTICE_2("FP2", "P2", "Practice 2"),
    PRACTICE_3("FP3", "P3", "Practice 3"),
    QUALIFYING_1("Q1", "Qualifying 1"),
    QUALIFYING_2("Q2", "Qualifying 2"),
    QUALIFYING_3("Q3", "Qualifying 3"),
    QUALIFYING("Q", "Qualifying"),
    SPRINT_QUALIFYING_1("SQ1", "Sprint Qualifying 1"),
    SPRINT_QUALIFYING_2("SQ2", "Sprint Qualifying 2"),
    SPRINT_QUALIFYING_3("SQ3", "Sprint Qualifying 3"),
    SPRINT_QUALIFYING("SQ", "Sprint Qualifying"),
    SPRINT_RACE("SR", "Sprint Race"),
    RACE("R", "Race"),
    UNKNOWN("UNK", "Unknown");

    private final String[] codes;
    private final String displayName;

    SessionType(String code, String displayName) {
        this.codes = new String[]{code};
        this.displayName = displayName;
    }

    SessionType(String code1, String code2, String displayName) {
        this.codes = new String[]{code1, code2};
        this.displayName = displayName;
    }

    SessionType(String... codes) {
        this.codes = codes;
        this.displayName = name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }

    public String getDisplayName() {
        return displayName;
    }

    public static SessionType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            return UNKNOWN;
        }

        for (SessionType type : values()) {
            for (String c : type.codes) {
                if (c.equalsIgnoreCase(code)) {
                    return type;
                }
            }
        }
        return UNKNOWN;
    }
}