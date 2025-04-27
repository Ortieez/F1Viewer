package ui.screens;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import ui.MainUI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Main menu screen of the F1 Viewer application
 */
public class MainMenuScreen extends BaseMenuScreen {

    public MainMenuScreen(MainUI app) {
        super(app, "F1 VIEWER - MAIN MENU");
        this.menuItems = new ArrayList<>(Arrays.asList(
                "Seasons Browser",
                "Driver Statistics",
                "Team Statistics",
                "Circuit Information",
                "Exit"
        ));
    }

    @Override
    protected void handleMenuSelection() throws IOException {
        switch (selectedIndex) {
            case 0: // Seasons Browser
                app.switchScreen(new SeasonsBrowserScreen(app));
                break;
            case 1: // Driver Statistics
                // TODO: Implement this screen
                showNotImplementedMessage();
                break;
            case 2: // Team Statistics
                // TODO: Implement this screen
                showNotImplementedMessage();
                break;
            case 3: // Circuit Information
                // TODO: Implement this screen
                showNotImplementedMessage();
                break;
            case 4: // Exit
                TextGraphics textGraphics = app.getScreen().newTextGraphics();
                app.getScreen().clear();

                textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
                textGraphics.putString(10, 5, "Exiting program...");

                app.getScreen().refresh();
                MainUI.sleep(1500);
                app.exit();
                break;
        }
    }

    private void showNotImplementedMessage() throws IOException {
        Screen screen = app.getScreen();
        TextGraphics textGraphics = screen.newTextGraphics();

        screen.clear();

        textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
        textGraphics.putString(10, 5, "This feature is not yet implemented");

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        textGraphics.putString(10, 7, "Press any key to return to menu");

        screen.refresh();
        screen.readInput();
    }
}