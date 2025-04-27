package ui.screens;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import interfaces.IDataProvider;
import models.Round;
import models.Season;
import models.Session;
import ui.MainUI;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Screen for displaying details of a specific F1 round
 */
public class RoundDetailScreen implements UIScreen {
    private final MainUI app;
    private final Season season;
    private final Round round;
    private List<Session> sessions;

    public RoundDetailScreen(MainUI app, Season season, Round round) {
        this.app = app;
        this.season = season;
        this.round = round;
        loadSessions();
    }

    private void loadSessions() {
        IDataProvider dataProvider = app.getDataProvider();
        sessions = dataProvider.getSessionsForRound(round.getId());
    }

    @Override
    public void render(Screen screen) {
        TextGraphics textGraphics = screen.newTextGraphics();
        int terminalWidth = screen.getTerminalSize().getColumns();

        if (terminalWidth >= 60) {
            drawBorder(textGraphics, screen.getTerminalSize().getColumns(), screen.getTerminalSize().getRows());
        }

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        String title = "ROUND " + round.getId() + ": " + round.getName().toUpperCase();
        int titleX = Math.max(10, (terminalWidth - title.length()) / 2);
        textGraphics.putString(titleX, 3, title);

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 5, "Season: " + season.getYear());
        if (round.getCircuit() != null) {
            textGraphics.putString(10, 6, "Circuit: " + round.getCircuit().getName());
            textGraphics.putString(10, 7, "Location: " + round.getCircuit().getLocality() + ", " + round.getCircuit().getCountry());
        }
        textGraphics.putString(10, 8, "Date: " + formatDate(round.getDate()));

        renderSessions(textGraphics);

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 22, "Press ESC to go back");

        if (findRaceSession() != null) {
            textGraphics.putString(10, 23, "Press R to view race results");
        }
    }

    private void renderSessions(TextGraphics textGraphics) {
        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        textGraphics.putString(10, 10, "Sessions:");

        if (sessions == null || sessions.isEmpty()) {
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.putString(12, 11, "No session data available");
            return;
        }

        int rowCounter = 11;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        for (Session session : sessions) {
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);

            String sessionDisplay = "• " + session.getType().getDisplayName();

            if (session.getDate() != null && !session.getDate().isEmpty()) {
                try {
                    LocalDate sessionDate = LocalDate.parse(session.getDate());
                    sessionDisplay += " - " + sessionDate.format(dateFormatter);

                    if (session.getTime() != null && !session.getTime().isEmpty()) {
                        sessionDisplay += " " + session.getTime();
                    }
                } catch (Exception e) {
                    sessionDisplay += " - " + session.getDate();
                    if (session.getTime() != null && !session.getTime().isEmpty()) {
                        sessionDisplay += " " + session.getTime();
                    }
                }
            }

            if (session.isCancelled()) {
                sessionDisplay += " (CANCELLED)";
                textGraphics.setForegroundColor(TextColor.ANSI.RED);
            }

            textGraphics.putString(12, rowCounter++, sessionDisplay);

            // Prevent overflow
            if (rowCounter > 20) {
                break;
            }
        }
    }

    private String formatDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return "Date not available";
        }

        try {
            LocalDate date = LocalDate.parse(dateStr);
            return date.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"));
        } catch (Exception e) {
            return dateStr;
        }
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
            app.switchScreen(new SeasonDetailScreen(app, season));
        } else if (keyStroke.getKeyType() == KeyType.Character) {
            if (keyStroke.getCharacter() == 'r' || keyStroke.getCharacter() == 'R') {
                // Check if there's a race session
                Session raceSession = findRaceSession();
                if (raceSession != null) {
                    app.switchScreen(new RaceResultsScreen(app, season, round, raceSession));
                }
            }
        }
    }

    private Session findRaceSession() {
        if (sessions == null || sessions.isEmpty()) {
            return null;
        }

        // Look for the race session
        return sessions.stream()
                .filter(session -> session.getType() != null &&
                        (session.getType().name().equals("RACE") ||
                                session.getType().getDisplayName().equalsIgnoreCase("Race")))
                .findFirst()
                .orElse(null);
    }
}