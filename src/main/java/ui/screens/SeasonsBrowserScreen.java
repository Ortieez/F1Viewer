package ui.screens;

import interfaces.IDataProvider;
import models.Season;
import ui.MainUI;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Screen for browsing F1 seasons
 */
public class SeasonsBrowserScreen extends BaseMenuScreen {
    private List<Season> seasons;

    public SeasonsBrowserScreen(MainUI app) {
        super(app, "F1 SEASONS BROWSER");

        loadSeasons();
    }

    private void loadSeasons() {
        IDataProvider dataProvider = app.getDataProvider();
        seasons = dataProvider.getAllSeasons();

        menuItems = seasons.stream()
                .map(season -> season.getYear() + " Formula One Season")
                .collect(Collectors.toCollection(ArrayList::new));

        menuItems.add("Back to Main Menu");
    }

    @Override
    protected void handleMenuSelection() {
        if (selectedIndex == menuItems.size() - 1) {
            app.switchScreen(new MainMenuScreen(app));
        } else {
            Season selectedSeason = seasons.get(selectedIndex);
            app.switchScreen(new SeasonDetailScreen(app, selectedSeason));
        }
    }

    @Override
    protected void handleEscape() {
        app.switchScreen(new MainMenuScreen(app));
    }
}