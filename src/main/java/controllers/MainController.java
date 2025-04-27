package controllers;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {
    private static final List<String> menuItems = new ArrayList<>();
    private static int selectedIndex = 0;

    static {
        menuItems.add("TEST 1");
        menuItems.add("TEST 2");
        menuItems.add("TEST 3");
        menuItems.add("TEST 4");
        menuItems.add("Exit");
    }

    public static void run() throws IOException {
        DefaultTerminalFactory factory = new DefaultTerminalFactory();
        factory.setTerminalEmulatorTitle("F1 Viewer");
        Terminal terminal = factory.createTerminal();
        Screen screen = new TerminalScreen(terminal);
        TextGraphics textGraphics = screen.newTextGraphics();



        screen.startScreen();
        screen.setCursorPosition(null);

        boolean running = true;
        while (running) {
            screen.clear();

            textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
            textGraphics.putString(10, 3, "MAIN MENU");

            drawMenu(textGraphics);

            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.putString(10, menuItems.size() + 7, "Use UP/DOWN arrows to navigate");
            textGraphics.putString(10, menuItems.size() + 8, "Press ENTER to select");
            textGraphics.putString(10, menuItems.size() + 9, "Press ESC to exit");

            screen.refresh();

            KeyStroke keyStroke = screen.readInput();

            if (keyStroke.getKeyType() == KeyType.ArrowDown) {
                selectedIndex = (selectedIndex + 1) % menuItems.size();
            } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
                selectedIndex = (selectedIndex - 1 + menuItems.size()) % menuItems.size();
            } else if (keyStroke.getKeyType() == KeyType.Enter) {
                handleMenuSelection(screen, textGraphics);
            } else if (keyStroke.getKeyType() == KeyType.Escape) {
                running = false;
            }
        }

        screen.stopScreen();
    }

    private static void drawMenu(TextGraphics textGraphics) {
        for (int i = 0; i < menuItems.size(); i++) {
            if (i == selectedIndex) {
                textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);
                textGraphics.putString(10, i + 5, "> " + menuItems.get(i) + " <");
                textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
            } else {
                textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                textGraphics.putString(10, i + 5, "  " + menuItems.get(i) + "  ");
            }
        }
    }

    private static void handleMenuSelection(Screen screen, TextGraphics textGraphics) throws IOException {
        screen.clear();

        textGraphics.setForegroundColor(TextColor.ANSI.GREEN);
        textGraphics.putString(10, 5, "You selected: " + menuItems.get(selectedIndex));

        if (selectedIndex == menuItems.size() - 1) {
            textGraphics.putString(10, 7, "Exiting program...");
            screen.refresh();
            sleep(1500);
            screen.stopScreen();
            System.exit(0);
        } else {
            textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
            textGraphics.putString(10, 7, "Press any key to return to menu");
            screen.refresh();
            screen.readInput();
        }
    }

    static void sleep(long millis) {
        try {
            java.lang.Thread.sleep(millis);
        } catch (InterruptedException e) {
            // Ignore
        }
    }
}