package org.beldr.model;

import com.tinify.Tinify;

public class ApiKey {

    private String key;
    private String usage;
    private boolean valid;

    public ApiKey(String key) {
        this.key = key;
        Tinify.setKey(key);
        if (Tinify.validate()) {
            this.usage = Integer.toString(Tinify.compressionCount());
            this.valid = true;
        } else {
            this.usage = "ERROR";
        }
    }

    public String getKey() {
        return key;
    }

    public String getUsage() {
        return usage;
    }

    public boolean isValid() {
        return valid;
    }

    public void updateUsage() {
        if (valid) {
            Tinify.setKey(key);
            if (Tinify.validate()) {
                this.usage = Integer.toString(Tinify.compressionCount());
            } else {
                this.usage = "ERROR";
                this.valid = false;
            }
        }
    }
}
