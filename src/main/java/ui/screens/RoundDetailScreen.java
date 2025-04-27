package ui.screens;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import models.Round;
import models.Season;
import ui.MainUI;

/**
 * Screen for displaying details of a specific F1 round
 */
public class RoundDetailScreen implements UIScreen {
    private final MainUI app;
    private final Season season;
    private final Round round;

    public RoundDetailScreen(MainUI app, Season season, Round round) {
        this.app = app;
        this.season = season;
        this.round = round;
    }

    @Override
    public void render(Screen screen) {
        TextGraphics textGraphics = screen.newTextGraphics();

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        textGraphics.putString(10, 3, "ROUND " + round.getId() + ": " + round.getName().toUpperCase());

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 5, "Season: " + season.getYear());
        textGraphics.putString(10, 6, "Circuit: " + round.getCircuit().getName());
        textGraphics.putString(10, 7, "Date: " + round.getDate());

        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        textGraphics.putString(10, 9, "Sessions:");
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(12, 10, "• Practice 1");
        textGraphics.putString(12, 11, "• Practice 2");
        textGraphics.putString(12, 12, "• Practice 3");
        textGraphics.putString(12, 13, "• Qualifying");
        textGraphics.putString(12, 14, "• Race");

        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        textGraphics.putString(10, 16, "Race Results (placeholder data):");
        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(12, 17, "1. Driver A (Team A)");
        textGraphics.putString(12, 18, "2. Driver B (Team B)");
        textGraphics.putString(12, 19, "3. Driver C (Team C)");

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 22, "Press ESC to go back");
    }

    @Override
    public void handleInput(KeyStroke keyStroke) {
        if (keyStroke.getKeyType() == KeyType.Escape) {
            app.switchScreen(new SeasonDetailScreen(app, season));
        }
    }
}