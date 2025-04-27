package ui.screens;

import interfaces.IDataProvider;
import models.Round;
import models.Season;
import ui.MainUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Screen for displaying details of a specific F1 season
 */
public class SeasonDetailScreen extends BaseMenuScreen {
    private final Season season;
    private List<Round> rounds;

    public SeasonDetailScreen(MainUI app, Season season) {
        super(app, season.getYear() + " FORMULA ONE SEASON");
        this.season = season;

        loadRounds();
    }

    private void loadRounds() {
        IDataProvider dataProvider = app.getDataProvider();
        rounds = dataProvider.getRoundsForSeason(season.getId());

        menuItems = rounds.stream()
                .map(round -> "Round " + round.getId() + ": " + round.getName())
                .collect(Collectors.toCollection(ArrayList::new));

        menuItems.add("Season Standings");
        menuItems.add("Back to Seasons Browser");
    }

    @Override
    protected void handleMenuSelection() {
        if (selectedIndex == menuItems.size() - 1) {
            app.switchScreen(new SeasonsBrowserScreen(app));
        } else if (selectedIndex == menuItems.size() - 2) {
            showSeasonStandings();
        } else {
            Round selectedRound = rounds.get(selectedIndex);
            showRoundDetail(selectedRound);
        }
    }

    private void showSeasonStandings() {
        app.switchScreen(new SeasonStandingsScreen(app, season));
    }

    private void showRoundDetail(Round round) {
        app.switchScreen(new RoundDetailScreen(app, season, round));
    }

    @Override
    protected void handleEscape() {
        app.switchScreen(new SeasonsBrowserScreen(app));
    }
}