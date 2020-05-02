package org.beldr.util;

import com.tinify.*;
import org.beldr.model.ApiKey;
import org.beldr.ui.MainController;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TinyPNGer {
    private final static int KEY_SIZE = 500;

    public static void tinifyGroup(List<File> files) {
        int minUsage = KEY_SIZE - files.size();
        final ApiKey apiKey = MainController.apiKeys.getItems().stream().filter(e -> e.isValid() && Integer.parseInt(e.getUsage()) <= minUsage )
                .findFirst().orElse(null);
        if (apiKey == null) {
            return;
        }

        Tinify.setKey(apiKey.getKey());
        String filePath;
        for (File file : files) {
            try {
                filePath = file.getAbsolutePath();
                TinyPNGer.tinify(filePath, filePath);
            } catch (IOException ignored) {
            }
        }

        apiKey.updateUsage();
        MainController.apiKeys.refresh();
    }

    public static void tinify(String pathFrom, String pathTo) throws IOException {
        Source source = Tinify.fromFile(pathFrom);
        source.toFile(pathTo);
    }
}
