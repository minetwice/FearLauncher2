package com.fearlauncher.models

data class JavaRuntime(
    val name: String,
    val majorVersion: Int,
    val isInstalled: Boolean = false,
    val downloadUrl: String
)
