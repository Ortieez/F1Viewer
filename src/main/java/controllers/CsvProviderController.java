package controllers;

import interfaces.IDataProvider;
import models.*;
import utils.DataFileNames;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import utils.SessionType;
import utils.Utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CsvProviderController implements IDataProvider {
    private final String csvPath;

    private final Map<DataFileNames, List<CSVRecord>> dataCache = new HashMap<>();

    private List<Season> seasons;
    private List<Driver> drivers;
    private List<Team> teams;
    private List<Round> rounds;
    private List<Circuit> circuits;
    private List<Session> sessions;
    private final Map<Integer, List<Session>> sessionsByRoundCache;
    private final Map<Integer, Map<Integer, List<DriverChampionshipStanding>>> driverStandingsCache;
    private final Map<Integer, Map<Integer, List<TeamChampionshipStanding>>> teamStandingsCache;

    public CsvProviderController() {
        this.csvPath = new ConfigController().readDataFolder();
        this.driverStandingsCache = new HashMap<>();
        this.teamStandingsCache = new HashMap<>();
        this.sessionsByRoundCache = new HashMap<>();
    }

    @Override
    public List<Season> getAllSeasons() {
        if (seasons == null) {
            loadSeasons();
        }
        return seasons;
    }

    @Override
    public Optional<Season> getSeasonByYear(int year) {
        if (seasons == null) {
            loadSeasons();
        }
        return seasons.stream()
                .filter(season -> season.getYear() == year)
                .findFirst();
    }

    @Override
    public List<Round> getRoundsForSeason(int seasonId) {
        if (rounds == null) {
            loadRounds();
        }
        return rounds.stream()
                .filter(round -> round.getSeasonId() == seasonId)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Round> getRoundById(int roundId) {
        if (rounds == null) {
            loadRounds();
        }
        return rounds.stream()
                .filter(round -> round.getId() == roundId)
                .findFirst();
    }

    @Override
    public List<Driver> getAllDrivers() {
        if (drivers == null) {
            loadDrivers();
        }
        return drivers;
    }

    @Override
    public Optional<Driver> getDriverById(int driverId) {
        if (drivers == null) {
            loadDrivers();
        }
        return drivers.stream()
                .filter(driver -> driver.getId() == driverId)
                .findFirst();
    }

    @Override
    public List<Team> getAllTeams() {
        if (teams == null) {
            loadTeams();
        }
        return teams;
    }

    @Override
    public Optional<Team> getTeamById(int teamId) {
        if (teams == null) {
            loadTeams();
        }
        return teams.stream()
                .filter(team -> team.getId() == teamId)
                .findFirst();
    }

    @Override
    public List<DriverChampionshipStanding> getDriverChampionshipStandings(int year, int round) {
        if (driverStandingsCache.containsKey(year) && driverStandingsCache.get(year).containsKey(round)) {
            return driverStandingsCache.get(year).get(round);
        }

        List<DriverChampionshipStanding> standings = loadDriverChampionshipStandings(year, round);

        if (!driverStandingsCache.containsKey(year)) {
            driverStandingsCache.put(year, new HashMap<>());
        }
        driverStandingsCache.get(year).put(round, standings);

        return standings;
    }

    @Override
    public List<TeamChampionshipStanding> getTeamChampionshipStandings(int year, int round) {
        if (teamStandingsCache.containsKey(year) && teamStandingsCache.get(year).containsKey(round)) {
            return teamStandingsCache.get(year).get(round);
        }

        List<TeamChampionshipStanding> standings = loadTeamChampionshipStandings(year, round);


        if (!teamStandingsCache.containsKey(year)) {
            teamStandingsCache.put(year, new HashMap<>());
        }
        teamStandingsCache.get(year).put(round, standings);

        return standings;
    }

    @Override
    public void loadFromCsv(DataFileNames fileName) {
        try {
            getDataFromCsv(fileName);
        } catch (IOException e) {
            System.err.println("Failed to load " + fileName + ": " + e.getMessage());
        }
    }

    @Override
    public List<Session> getSessionsForRound(int roundId) {
        if (sessionsByRoundCache.containsKey(roundId)) {
            return sessionsByRoundCache.get(roundId);
        }

        if (sessions == null) {
            loadSessions();
        }

        List<Session> roundSessions = sessions.stream()
                .filter(session -> session.getRoundId() == roundId)
                .collect(Collectors.toList());

        sessionsByRoundCache.put(roundId, roundSessions);
        return roundSessions;
    }

    private List<CSVRecord> getDataFromCsv(DataFileNames fileName) throws IOException {
        if (dataCache.containsKey(fileName)) {
            return dataCache.get(fileName);
        }

        List<CSVRecord> records = new ArrayList<>();

        try (Reader reader = new InputStreamReader(
                new FileInputStream(csvPath + fileName.getFileName()),
                StandardCharsets.UTF_8)) {

            CSVParser csvParser = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .parse(reader);

            for (CSVRecord csvRecord : csvParser) {
                records.add(csvRecord);
            }

            dataCache.put(fileName, records);
        }

        return records;
    }

    private void loadSeasons() {
        seasons = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.SEASONS);
            for (CSVRecord record : records) {
                seasons.add(convertToSeason(record));
            }
        } catch (IOException e) {
            System.err.println("Failed to load seasons: " + e.getMessage());
        }
    }

    private void loadDrivers() {
        drivers = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.DRIVERS);
            for (CSVRecord record : records) {
                drivers.add(convertToDriver(record));
            }
        } catch (IOException e) {
            System.err.println("Failed to load drivers: " + e.getMessage());
        }
    }

    private void loadTeams() {
        teams = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.TEAMS);
            for (CSVRecord record : records) {
                teams.add(convertToTeam(record));
            }
        } catch (IOException e) {
            System.err.println("Failed to load teams: " + e.getMessage());
        }
    }

    private void loadRounds() {
        rounds = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.ROUNDS);
            for (CSVRecord record : records) {
                rounds.add(convertToRound(record));
            }

            rounds.sort(Comparator.comparing(Round::getId));
        } catch (IOException e) {
            System.err.println("Failed to load rounds: " + e.getMessage());
        }
    }

    private void loadCircuits() {
        circuits = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.CIRCUITS);
            for (CSVRecord record : records) {
                circuits.add(convertToCircuit(record));
            }
        } catch (IOException e) {
            System.err.println("Failed to load circuits: " + e.getMessage());
        }
    }

    private Circuit getCircuitById(int circuitId) {
        if (circuits == null) {
            loadCircuits();
        }
        return circuits.stream()
                .filter(circuit -> circuit.getId() == circuitId)
                .findFirst()
                .orElse(null);
    }

    private void loadSessions() {
        sessions = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.SESSIONS);
            for (CSVRecord record : records) {
                sessions.add(convertToSession(record));
            }

            sessions.sort((s1, s2) -> {
                int dateComparison = Utilities.compareNullableStrings(s1.getDate(), s2.getDate());
                if (dateComparison != 0) {
                    return dateComparison;
                }

                return Utilities.compareNullableStrings(s1.getTime(), s2.getTime());
            });
        } catch (IOException e) {
            System.err.println("Failed to load sessions: " + e.getMessage());
        }
    }

    private List<DriverChampionshipStanding> loadDriverChampionshipStandings(int year, int round) {
        List<DriverChampionshipStanding> standings = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.DRIVERS_CHAMPIONSHIP);
            for (CSVRecord record : records) {
                int recordYear = Integer.parseInt(record.get("year"));
                int recordRound = Integer.parseInt(record.get("round_number"));
                if (recordYear == year && recordRound == round) {
                    standings.add(convertToDriverStanding(record));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load driver standings: " + e.getMessage());
        }
        return standings;
    }

    private List<TeamChampionshipStanding> loadTeamChampionshipStandings(int year, int round) {
        List<TeamChampionshipStanding> standings = new ArrayList<>();
        try {
            List<CSVRecord> records = getDataFromCsv(DataFileNames.TEAMS_CHAMPIONSHIP);
            for (CSVRecord record : records) {
                int recordYear = Integer.parseInt(record.get("year"));
                int recordRound = Integer.parseInt(record.get("round_number"));
                if (recordYear == year && recordRound == round) {
                    standings.add(convertToTeamStanding(record));
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load team standings: " + e.getMessage());
        }
        return standings;
    }

    private Season convertToSeason(CSVRecord record) {
        Season season = new Season();
        season.setId(Integer.parseInt(record.get("id")));
        season.setYear(Integer.parseInt(record.get("year")));
        season.setName(record.get("year") + " Formula One Season");
        return season;
    }

    private Driver convertToDriver(CSVRecord record) {
        Driver driver = new Driver();
        driver.setId(Integer.parseInt(record.get("id")));
        driver.setFirstName(record.get("forename"));
        driver.setLastName(record.get("surname"));
        driver.setNationality(record.get("nationality"));
        return driver;
    }

    private Team convertToTeam(CSVRecord record) {
        Team team = new Team();
        team.setId(Integer.parseInt(record.get("id")));
        team.setName(record.get("name"));
        team.setNationality(record.get("nationality"));
        return team;
    }

    private Round convertToRound(CSVRecord record) {
        Round round = new Round();
        round.setId(Integer.parseInt(record.get("id")));
        round.setSeasonId(Integer.parseInt(record.get("season_id")));
        round.setName(record.get("name"));

        int circuitId = Integer.parseInt(record.get("circuit_id"));
        Circuit circuit = getCircuitById(circuitId);
        round.setCircuit(circuit);

        round.setDate(record.get("date"));
        return round;
    }

    private Circuit convertToCircuit(CSVRecord record) {
        Circuit circuit = new Circuit();
        circuit.setId(Integer.parseInt(record.get("id")));
        circuit.setReference(record.get("reference"));
        circuit.setName(record.get("name"));
        circuit.setLocality(record.get("locality"));
        circuit.setCountry(record.get("country"));
        circuit.setAltitude(Integer.parseInt(record.get("altitude")));
        circuit.setWikipedia(record.get("wikipedia"));
        circuit.setLatitude(Double.parseDouble(record.get("latitude")));
        circuit.setLongitude(Double.parseDouble(record.get("longitude")));
        return circuit;
    }

    private DriverChampionshipStanding convertToDriverStanding(CSVRecord record) {
        DriverChampionshipStanding standing = new DriverChampionshipStanding();
        standing.setDriverId(Integer.parseInt(record.get("driver_id")));

        standing.setDriver(getDriverById(standing.getDriverId()).orElse(null));

        String positionStr = record.get("position");
        if (positionStr != null && !positionStr.isEmpty()) {
            standing.setPosition((int)Float.parseFloat(positionStr));
        } else {
            standing.setPosition(0);
        }

        standing.setPoints(Double.parseDouble(record.get("points")));
        standing.setWins(Integer.parseInt(record.get("win_count")));
        return standing;
    }

    private TeamChampionshipStanding convertToTeamStanding(CSVRecord record) {
        TeamChampionshipStanding standing = new TeamChampionshipStanding();
        standing.setTeamId(Integer.parseInt(record.get("team_id")));

        String positionStr = record.get("position");
        if (positionStr != null && !positionStr.isEmpty()) {
            standing.setPosition((int)Float.parseFloat(positionStr));
        } else {
            standing.setPosition(0);
        }

        standing.setPoints(Double.parseDouble(record.get("points")));
        standing.setWins(Integer.parseInt(record.get("win_count")));
        return standing;
    }

    private Session convertToSession(CSVRecord record) {
        Session session = new Session();
        session.setId(Integer.parseInt(record.get("id")));

        String numberStr = record.get("number");
        if (numberStr != null && !numberStr.isEmpty()) {
            session.setNumber(Integer.parseInt(numberStr));
        }

        session.setType(SessionType.fromCode(record.get("type")));
        session.setDate(record.get("date"));
        session.setTime(record.get("time"));

        String lapsStr = record.get("scheduled_laps");
        if (lapsStr != null && !lapsStr.isEmpty()) {
            session.setScheduledLaps(Integer.parseInt(lapsStr));
        }

        String cancelledStr = record.get("is_cancelled");
        session.setCancelled(cancelledStr != null && cancelledStr.equalsIgnoreCase("1"));

        String pointSystemStr = record.get("point_system_id");
        if (pointSystemStr != null && !pointSystemStr.isEmpty()) {
            session.setPointSystemId(Integer.parseInt(pointSystemStr));
        }

        session.setRoundId(Integer.parseInt(record.get("round_id")));
        return session;
    }
}