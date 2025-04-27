package ui.screens;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import models.Round;
import models.Season;
import models.Session;
import ui.MainUI;

/**
 * Screen for displaying race results for a specific round
 */
public class RaceResultsScreen implements UIScreen {
    private final MainUI app;
    private final Season season;
    private final Round round;
    private final Session raceSession;

    public RaceResultsScreen(MainUI app, Season season, Round round, Session raceSession) {
        this.app = app;
        this.season = season;
        this.round = round;
        this.raceSession = raceSession;
    }

    @Override
    public void render(Screen screen) {
        TextGraphics textGraphics = screen.newTextGraphics();
        int terminalWidth = screen.getTerminalSize().getColumns();

        if (terminalWidth >= 60) {
            drawBorder(textGraphics, terminalWidth, screen.getTerminalSize().getRows());
        }

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        String title = round.getName().toUpperCase() + " - RACE RESULTS";
        int titleX = Math.max(10, (terminalWidth - title.length()) / 2);
        textGraphics.putString(titleX, 3, title);

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 5, "Season: " + season.getYear());
        textGraphics.putString(10, 6, "Date: " + raceSession.getDate());

        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        textGraphics.putString(10, 8, "Race Results:");

        // TODO: Implement actual race results loading
        showPlaceholderResults(textGraphics);

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 22, "Press ESC to go back to round details");
    }

    private void showPlaceholderResults(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

        String header = String.format("%-4s %-20s %-15s %-10s %-10s", "Pos", "Driver", "Team", "Time", "Points");
        textGraphics.putString(10, 10, header);

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        textGraphics.putString(10, 11, "─".repeat(60));

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 12, String.format("%-4s %-20s %-15s %-10s %-10s", "1", "Driver A", "Team A", "1:30:45.123", "25"));
        textGraphics.putString(10, 13, String.format("%-4s %-20s %-15s %-10s %-10s", "2", "Driver B", "Team B", "+5.213s", "18"));
        textGraphics.putString(10, 14, String.format("%-4s %-20s %-15s %-10s %-10s", "3", "Driver C", "Team C", "+10.546s", "15"));
        textGraphics.putString(10, 15, String.format("%-4s %-20s %-15s %-10s %-10s", "4", "Driver D", "Team D", "+15.897s", "12"));
        textGraphics.putString(10, 16, String.format("%-4s %-20s %-15s %-10s %-10s", "5", "Driver E", "Team E", "+25.213s", "10"));

        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        textGraphics.putString(10, 18, "Note: This is placeholder data. Race results feature is under development.");
    }

    private void drawBorder(TextGraphics textGraphics, int width, int height) {
        textGraphics.setForegroundColor(TextColor.ANSI.BLUE);

        for (int x = 5; x < width - 5; x++) {
            textGraphics.putString(x, 1, "─");
            textGraphics.putString(x, height - 2, "─");
        }

        for (int y = 2; y < height - 2; y++) {
            textGraphics.putString(5, y, "│");
            textGraphics.putString(width - 6, y, "│");
        }

        textGraphics.putString(5, 1, "┌");
        textGraphics.putString(width - 6, 1, "┐");
        textGraphics.putString(5, height - 2, "└");
        textGraphics.putString(width - 6, height - 2, "┘");
    }

    @Override
    public void handleInput(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Escape) {
            app.switchScreen(new RoundDetailScreen(app, season, round));
        }
    }
}