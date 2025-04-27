package ui;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import interfaces.IDataProvider;
import ui.screens.MainMenuScreen;
import ui.screens.UIScreen;

import java.awt.*;
import java.io.IOException;

/**
 * F1 UI Application - Main entry point for the UI framework
 * Acts as the coordinator between different screens
 */
public class MainUI {
    private Screen screen;
    private UIScreen currentScreen;
    private boolean running = true;
    private final IDataProvider dataProvider;
    private static final String APP_NAME = "F1 Viewer";
    private static final String APP_VERSION = "v0.3";
    private static final TerminalSize INITIAL_SIZE = new TerminalSize(90, 40);

    public MainUI(IDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public IDataProvider getDataProvider() {
        return dataProvider;
    }

    public void init() throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        factory.setTerminalEmulatorTitle(APP_NAME + " " + APP_VERSION);
        factory.setInitialTerminalSize(INITIAL_SIZE);

        Terminal terminal = factory.createTerminal();
        screen = new TerminalScreen(terminal);

        currentScreen = new MainMenuScreen(this);
    }

    /**
     * Start the application main loop
     */
    public void run() throws IOException {
        screen.startScreen();
        screen.setCursorPosition(null);

        while (running) {
            screen.clear();
            currentScreen.render(screen);
            screen.refresh();

            currentScreen.handleInput(screen.readInput());
        }

        screen.stopScreen();
    }

    /**
     * Change to a different screen
     */
    public void switchScreen(UIScreen newScreen) {
        currentScreen = newScreen;
    }

    /**
     * Exit the application
     */
    public void exit() {
        running = false;
    }

    /**
     * Get the current screen
     */
    public Screen getScreen() {
        return screen;
    }

    /**
     * Sleep helper method
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}