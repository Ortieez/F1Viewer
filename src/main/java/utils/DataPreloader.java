package utils;

import interfaces.IDataProvider;

public class DataPreloader {
    public static void preloadAllData(IDataProvider dataProvider) {
        System.out.println("Pre-loading all data...");

        for (DataFileNames fileName : DataFileNames.values()) {
            System.out.println("Loading " + fileName.getFileName() + "...");
            dataProvider.loadFromCsv(fileName);
        }

        System.out.println("All data pre-loaded.");
    }
}
