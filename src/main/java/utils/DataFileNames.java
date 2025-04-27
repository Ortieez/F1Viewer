package utils;

public enum DataFileNames {
    DRIVERS("formula_one_driver.csv"),
    DRIVERS_CHAMPIONSHIP("formula_one_driverchampionship.csv"),
    CIRCUITS("formula_one_circuit.csv"),
    ROUNDS("formula_one_round.csv"),
    ROUNDS_ENTRIES("formula_one_roundentry.csv"),
    SESSIONS("formula_one_session.csv"),
    SESSIONS_ENTRIES("formula_one_sessionentry.csv"),
    TEAMS("formula_one_team.csv"),
    TEAMS_DRIVERS("formula_one_teamdriver.csv"),
    TEAMS_CHAMPIONSHIP("formula_one_teamchampionship.csv"),
    PITSTOPS("formula_one_pitstop.csv"),
    LAP_TIMES("formula_one_lap.csv"),
    PENALTIES("formula_one_penalty.csv"),
    SEASONS("formula_one_season.csv");

    private final String fileName;

    DataFileNames(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
