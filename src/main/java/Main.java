import controllers.CsvProviderController;
import ui.MainUI;
import utils.DataPreloader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            CsvProviderController dataProvider = new CsvProviderController();
            DataPreloader.preloadAllData(dataProvider);

            MainUI app = new MainUI(dataProvider);
            app.init();
            app.run();
        } catch (IOException e) {
            System.err.println("Error running F1 Viewer: " + e.getMessage());
        }
    }
}