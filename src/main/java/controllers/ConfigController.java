package controllers;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigController {
    private static final String CONFIG_FILE = "src/main/resources/config.properties";
    private final Properties properties;

    public ConfigController() {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        }
    }

    public String readDataFolder() {
        return properties.getProperty("data.folder", "src/main/resources/f1-data/");
    }
}