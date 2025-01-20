package com.github.jonkke9.musicbot;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Config {

    public final static Logger LOGGER = LoggerFactory.getLogger(Config.class);

    private static Config INSTANCE;

    private final Properties config;

    private Config() {
        final Properties defaultConfig = new Properties();
        config = new Properties(defaultConfig);

        try (final InputStream defaultConfigStream = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            if (defaultConfigStream != null) {
                defaultConfig.load(defaultConfigStream);
            } else {
                LOGGER.error("Default config.properties not found in resources.");
            }
        } catch (IOException e) {
            LOGGER.error("Error loading default config.properties from resources.", e);
        }

        final File externalConfigFile = new File("config.properties");

        if (externalConfigFile.exists()) {
            try (FileInputStream input = new FileInputStream(externalConfigFile)) {
                config.load(input);
            } catch (IOException e) {
                LOGGER.error("Error loading external config.properties.", e);
            }
        } else {
            // Create external config file with default properties
            try (FileOutputStream output = new FileOutputStream(externalConfigFile)) {
                config.store(output, "Default configuration");
            } catch (IOException e) {
                LOGGER.error("Error creating external config.properties.", e);
            }
        }
    }

    public static Config getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }


    public @NotNull String get(final String key) {

        if (key == null) {
           return " ";
        }

        final String value = config.getProperty(key);

        if (value == null) {
            return key;
        }

        return value;
    }

    public String get(final String key, final String @NotNull ...args) {
        String message = get(key);

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                message = message.replaceFirst("%arg" + i + "%", args[i]);
            }
        }
        return message;
    }

    public boolean getBoolean(final String key) {
        return Boolean.parseBoolean(get(key));
    }
}
