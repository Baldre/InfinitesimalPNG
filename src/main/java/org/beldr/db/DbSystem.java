package org.beldr.db;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DbSystem {

    private static final String PATH = "keys.txt";
    private static List<String> keys;

    public static List<String> getKeys() {
        if (keys == null) {
            initialize();
        }
        return keys;
    }

    public static void initialize() {
        try {
            keys = Files.readAllLines(Paths.get(PATH), Charset.defaultCharset());
        } catch (IOException e) {
            System.out.println("file not found");
        }
    }

    public static void removeKey(String key) {
        keys.remove(key);
        save();
    }

    public static void addKey(String key) {
        keys.add(key);
        save();
    }

    private static void save() {
        try {
            FileWriter writer = new FileWriter(PATH, false);
            writer.write(String.join(System.lineSeparator(), keys));
            writer.close();
        } catch (IOException e) {
            System.out.println("Error saving");
        }
    }
}
