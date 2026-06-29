package com.fearlauncher.core.java;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class CoreConfig {
    private static final String CORE_PROPS = "core_settings.properties";

    public static void saveRamSettings(File filesDir, int ramMB) {
        Properties props = new Properties();
        props.setProperty("max_memory", String.valueOf(ramMB));
        saveProperties(filesDir, props);
    }

    public static void saveJreSettings(File filesDir, String jreId) {
        Properties props = new Properties();
        props.setProperty("selected_jre", jreId);
        saveProperties(filesDir, props);
    }

    private static void saveProperties(File filesDir, Properties props) {
        File coreFile = new File(filesDir, CORE_PROPS);
        try (FileOutputStream out = new FileOutputStream(coreFile)) {
            props.store(out, "Core Launcher Settings");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
