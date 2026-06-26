package com.fearlauncher.utils

import android.os.Build

object SystemUtils {
    fun getArchitecture(): String {
        return if (Build.SUPPORTED_ABIS.isNotEmpty()) {
            Build.SUPPORTED_ABIS[0]
        } else {
            System.getProperty("os.arch") ?: "unknown"
        }
    }

    fun isArm64(): Boolean {
        val arch = getArchitecture().lowercase()
        return arch.contains("arm64") || arch.contains("aarch64")
    }

    fun isX86_64(): Boolean {
        val arch = getArchitecture().lowercase()
        return arch.contains("x86_64") || arch.contains("amd64")
    }
}
