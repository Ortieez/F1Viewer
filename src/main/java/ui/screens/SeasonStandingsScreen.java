package ui.screens;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import interfaces.IDataProvider;
import models.DriverChampionshipStanding;
import models.Season;
import models.TeamChampionshipStanding;
import ui.MainUI;

import java.util.List;

/**
 * Screen for displaying season championship standings
 */
public class SeasonStandingsScreen implements UIScreen {
    private final MainUI app;
    private final Season season;
    private boolean showingDrivers = true; // Toggle between drivers and teams

    public SeasonStandingsScreen(MainUI app, Season season) {
        this.app = app;
        this.season = season;
    }

    @Override
    public void render(Screen screen) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        textGraphics.putString(10, 3, season.getYear() + " FORMULA ONE CHAMPIONSHIP");

        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        if (showingDrivers) {
            textGraphics.putString(10, 5, "DRIVERS' CHAMPIONSHIP STANDINGS");
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.putString(10, 6, "Press TAB to view Constructors' Championship");
        } else {
            textGraphics.putString(10, 5, "CONSTRUCTORS' CHAMPIONSHIP STANDINGS");
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.putString(10, 6, "Press TAB to view Drivers' Championship");
        }

        if (showingDrivers) {
            renderDriverStandings(textGraphics);
        } else {
            renderTeamStandings(textGraphics);
        }

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 22, "Press ESC to go back");
    }

    private void renderDriverStandings(TextGraphics textGraphics) {
        int terminalWidth = app.getScreen().getTerminalSize().getColumns();
        int terminalHeight = app.getScreen().getTerminalSize().getRows();

        int availableHeight = terminalHeight - 12; // Space for headers and footer

        int driverNameWidth = Math.max(15, Math.min(30, terminalWidth - 40));

        String formatStr = "%-4s %-" + driverNameWidth + "s %-10s %-5s";

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 8, String.format(formatStr, "Pos", "Driver", "Points", "Wins"));

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        String separatorLine = "─".repeat(Math.min(terminalWidth - 20, driverNameWidth + 30));
        textGraphics.putString(10, 9, separatorLine);

        List<DriverChampionshipStanding> standings = getDriverStandings();

        int totalRows = standings.size();
        int visibleRows = Math.min(availableHeight, totalRows);
        int scrollOffset = 0;

        if (standings.size() < 20) {
            for (int i = standings.size() + 1; i <= 20; i++) {
                DriverChampionshipStanding standing = new DriverChampionshipStanding();
                standing.setDriverId(i);
                standing.setPosition(i);
                standing.setPoints(100 - (i * 5) + (Math.random() * 10 - 5));
                standing.setWins(0);
                standings.add(standing);
            }
        }

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < visibleRows; i++) {
            int standingIdx = i + scrollOffset;
            if (standingIdx < standings.size()) {
                DriverChampionshipStanding standing = standings.get(standingIdx);
                String driverName = "Driver " + standing.getDriverId();

                if (driverName.length() > driverNameWidth) {
                    driverName = driverName.substring(0, driverNameWidth - 3) + "...";
                }

                if (i % 2 == 1) {
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                }

                textGraphics.putString(10, i + 10, String.format("%-4d %-" + driverNameWidth + "s %-10.1f %-5d",
                        standing.getPosition(),
                        driverName,
                        standing.getPoints(),
                        standing.getWins()));
            }
        }

        if (totalRows > visibleRows) {
            textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
            textGraphics.putString(5, 10, "↑");
            textGraphics.putString(5, 10 + visibleRows - 1, "↓");
            textGraphics.putString(10, 10 + visibleRows, "Use PAGE UP/DOWN to scroll");
        }
    }

    private void renderTeamStandings(TextGraphics textGraphics) {
        int terminalWidth = app.getScreen().getTerminalSize().getColumns();
        int terminalHeight = app.getScreen().getTerminalSize().getRows();

        int availableHeight = terminalHeight - 12;

        int teamNameWidth = Math.max(15, Math.min(30, terminalWidth - 40));

        String formatStr = "%-4s %-" + teamNameWidth + "s %-10s %-5s";

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 8, String.format(formatStr, "Pos", "Team", "Points", "Wins"));

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        String separatorLine = "─".repeat(Math.min(terminalWidth - 20, teamNameWidth + 30));
        textGraphics.putString(10, 9, separatorLine);

        List<TeamChampionshipStanding> standings = getTeamStandings();

        int totalRows = standings.size();
        int visibleRows = Math.min(availableHeight, totalRows);
        int scrollOffset = 0;

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        for (int i = 0; i < visibleRows; i++) {
            int standingIdx = i + scrollOffset;
            if (standingIdx < standings.size()) {
                TeamChampionshipStanding standing = standings.get(standingIdx);

                String teamName = getTeamName(standing, teamNameWidth);

                if (i % 2 == 1) {
                    textGraphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                }

                textGraphics.putString(10, i + 10, String.format("%-4d %-" + teamNameWidth + "s %-10.1f %-5d",
                        standing.getPosition(),
                        teamName,
                        standing.getPoints(),
                        standing.getWins()));
            }
        }

        if (totalRows > visibleRows) {
            textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
            textGraphics.putString(5, 10, "↑");
            textGraphics.putString(5, 10 + visibleRows - 1, "↓");
            textGraphics.putString(10, 10 + visibleRows, "Use PAGE UP/DOWN to scroll");
        }
    }

    private static String getTeamName(TeamChampionshipStanding standing, int teamNameWidth) {
        String teamName = "Team " + standing.getTeamId();

        if (teamName.length() > teamNameWidth) {
            teamName = teamName.substring(0, teamNameWidth - 3) + "...";
        }
        return teamName;
    }

    private List<DriverChampionshipStanding> getDriverStandings() {
        IDataProvider dataProvider = app.getDataProvider();
        return dataProvider.getDriverChampionshipStandings(season.getYear(), -1);
    }

    private List<TeamChampionshipStanding> getTeamStandings() {
        IDataProvider dataProvider = app.getDataProvider();
        return dataProvider.getTeamChampionshipStandings(season.getYear(), -1);
    }

    private int scrollOffset = 0;

    @Override
    public void handleInput(KeyStroke keyStroke)  {
        if (keyStroke.getKeyType() == KeyType.Escape) {
            app.switchScreen(new SeasonDetailScreen(app, season));
        } else if (keyStroke.getKeyType() == KeyType.Tab) {
            showingDrivers = !showingDrivers;
            scrollOffset = 0;
        } else if (keyStroke.getKeyType() == KeyType.PageDown) {
            int availableHeight = app.getScreen().getTerminalSize().getRows() - 12;
            int totalRows = showingDrivers ? getDriverStandings().size() : getTeamStandings().size();
            int scrollMax = Math.max(0, totalRows - availableHeight);
            scrollOffset = Math.min(scrollOffset + 5, scrollMax);
        } else if (keyStroke.getKeyType() == KeyType.PageUp) {
            scrollOffset = Math.max(0, scrollOffset - 5);
        }
    }
}