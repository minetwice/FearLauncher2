package com.fearlauncher.core.java;

import java.io.File;
import java.util.Map;

public class NativeLauncher {
    static {
        System.loadLibrary("launcher-core");
    }

    public native String getRendererName(int rendererId);
    public native void setEnv(String key, String value);
    public native long getTotalRam();

    public static void setupEnvironment(Map<String, String> env) {
        NativeLauncher launcher = new NativeLauncher();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            launcher.setEnv(entry.getKey(), entry.getValue());
        }
    }

    public static String buildJavaCommand(String jrePath, int maxMemory, String jvmArgs) {
        return jrePath + " -Xmx" + maxMemory + "M " + jvmArgs;
    }
}
