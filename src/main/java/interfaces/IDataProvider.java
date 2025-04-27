package interfaces;

import models.*;
import utils.DataFileNames;

import java.util.List;
import java.util.Optional;

public interface IDataProvider {
    List<Season> getAllSeasons();

    Optional<Season> getSeasonByYear(int year);

    List<Round> getRoundsForSeason(int seasonId);

    Optional<Round> getRoundById(int roundId);

    List<Driver> getAllDrivers();

    Optional<Driver> getDriverById(int driverId);

    List<Team> getAllTeams();

    Optional<Team> getTeamById(int teamId);

    List<DriverChampionshipStanding> getDriverChampionshipStandings(int year, int round);

    List<TeamChampionshipStanding> getTeamChampionshipStandings(int year, int round);

    void loadFromCsv(DataFileNames fileName);
}