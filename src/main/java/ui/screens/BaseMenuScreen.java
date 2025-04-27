package ui.screens;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import ui.MainUI;

import java.io.IOException;
import java.util.List;

/**
 * Base class for menu screens that provides common menu functionality
 */
public abstract class BaseMenuScreen implements UIScreen {
    protected MainUI app;
    protected List<String> menuItems;
    protected int selectedIndex = 0;
    protected String title;

    public BaseMenuScreen(MainUI app, String title) {
        this.app = app;
        this.title = title;
    }

    @Override
    public void render(Screen screen) {
        TextGraphics textGraphics = screen.newTextGraphics();
        int terminalWidth = screen.getTerminalSize().getColumns();
        int terminalHeight = screen.getTerminalSize().getRows();

        if (terminalWidth >= 40 && terminalHeight >= 15) {
            drawBorder(textGraphics, terminalWidth, terminalHeight);
        }

        textGraphics.setForegroundColor(TextColor.ANSI.CYAN);
        int titleX = Math.max(10, (terminalWidth - title.length()) / 2);
        textGraphics.putString(titleX, 3, title);

        drawMenu(textGraphics);

        textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
        int instructionsY = Math.max(15, terminalHeight - 4);
        textGraphics.putString(10, instructionsY, "Use UP/DOWN arrows to navigate");
        textGraphics.putString(10, instructionsY + 1, "Press ENTER to select");
        textGraphics.putString(10, instructionsY + 2, "Press ESC to return or exit");
    }

    /**
     * Draw a border around the screen content
     */
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
    public void handleInput(KeyStroke keyStroke) throws IOException {
        if (keyStroke.getKeyType() == KeyType.ArrowDown) {
            selectedIndex = (selectedIndex + 1) % menuItems.size();
        } else if (keyStroke.getKeyType() == KeyType.ArrowUp) {
            selectedIndex = (selectedIndex - 1 + menuItems.size()) % menuItems.size();
        } else if (keyStroke.getKeyType() == KeyType.Enter) {
            handleMenuSelection();
        } else if (keyStroke.getKeyType() == KeyType.Escape) {
            handleEscape();
        }
    }

    /**
     * Draw the menu items
     */
    protected void drawMenu(TextGraphics textGraphics) {
        int terminalWidth = app.getScreen().getTerminalSize().getColumns();
        int terminalHeight = app.getScreen().getTerminalSize().getRows();

        int visibleItems = Math.min(menuItems.size(), terminalHeight - 10);

        int startIdx = 0;
        if (selectedIndex >= visibleItems) {
            startIdx = Math.min(selectedIndex - visibleItems + 1, menuItems.size() - visibleItems);
        }

        if (menuItems.size() > visibleItems) {
            textGraphics.setForegroundColor(TextColor.ANSI.YELLOW);
            if (startIdx > 0) {
                textGraphics.putString(5, 5, "↑");
            }
            if (startIdx + visibleItems < menuItems.size()) {
                textGraphics.putString(5, 5 + visibleItems - 1, "↓");
            }
        }

        int menuWidth = Math.max(30, terminalWidth - 20);

        for (int i = 0; i < visibleItems; i++) {
            int itemIdx = i + startIdx;
            if (itemIdx < menuItems.size()) {
                String menuItem = menuItems.get(itemIdx);

                if (menuItem.length() > menuWidth - 6) {
                    menuItem = menuItem.substring(0, menuWidth - 9) + "...";
                }

                if (itemIdx == selectedIndex) {
                    textGraphics.setForegroundColor(TextColor.ANSI.BLACK);
                    textGraphics.setBackgroundColor(TextColor.ANSI.WHITE);

                    String paddedItem = String.format("> %-" + (menuWidth - 4) + "s <", menuItem);
                    textGraphics.putString(10, i + 5, paddedItem);

                    textGraphics.setBackgroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    textGraphics.setForegroundColor(TextColor.ANSI.WHITE);
                    String paddedItem = String.format("  %-" + (menuWidth - 4) + "s  ", menuItem);
                    textGraphics.putString(10, i + 5, paddedItem);
                }
            }
        }
    }

    /**
     * Handle menu selection
     */
    protected abstract void handleMenuSelection() throws IOException;

    /**
     * Handle escape key press
     */
    protected void handleEscape() {
        app.exit();
    }
}