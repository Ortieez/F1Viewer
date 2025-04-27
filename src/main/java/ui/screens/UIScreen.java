package ui.screens;

import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.screen.Screen;

import java.io.IOException;

/**
 * Base interface for all UI screens
 */
public interface UIScreen {
    /**
     * Render the screen
     * @param screen The lanterna screen to render to
     */
    void render(Screen screen) throws IOException;

    /**
     * Handle user input
     * @param keyStroke The key pressed by the user
     */
    void handleInput(KeyStroke keyStroke) throws IOException;
}