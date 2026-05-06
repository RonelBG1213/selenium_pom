package com.automation.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;

public class TestDataLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private TestDataLoader() {}

    /**
     * Loads a JSON array from src/test/resources/testdata/<filename>
     * and deserializes each element into the given type.
     *
     * Usage: TestDataLoader.loadList("login_data.json", LoginData.class)
     */
    public static <T> List<T> loadList(String filename, Class<T> type) {
        String path = "testdata/" + filename;
        try (InputStream is = TestDataLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Test data file not found on classpath: " + path);
            }
            return MAPPER.readValue(is, MAPPER.getTypeFactory().constructCollectionType(List.class, type));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load test data from: " + path, e);
        }
    }

    /** Converts a JSON file directly into the Object[][] shape TestNG @DataProvider expects. */
    public static <T> Object[][] toDataProvider(String filename, Class<T> type) {
        List<T> records = loadList(filename, type);
        Object[][] data = new Object[records.size()][1];
        for (int i = 0; i < records.size(); i++) {
            data[i][0] = records.get(i);
        }
        return data;
    }
}
