package com.fearlauncher.logic

import android.content.Context
import java.io.File

object LauncherManager {

    fun launchGame(
        context: Context,
        versionId: String,
        username: String,
        maxMemory: Int,
        renderer: String,
        jvmArgs: String
    ): Process? {
        val gameDir = VersionManager.getGameDirectory(context)
        val versionDir = File(VersionManager.getVersionsDirectory(context), versionId)
        val jarFile = File(versionDir, "$versionId.jar")

        if (!jarFile.exists()) return null

        val jrePath = File(context.filesDir, "jre/21/bin/java") // Defaulting to JRE 21

        val command = mutableListOf<String>()
        command.add(jrePath.absolutePath)
        command.add("-Xmx${maxMemory}M")
        command.add("-Djava.library.path=${File(versionDir, "natives").absolutePath}")

        // Add custom JVM args
        jvmArgs.split(" ").filter { it.isNotBlank() }.forEach { command.add(it) }

        command.add("-cp")
        val classpath = buildClasspath(versionDir)
        command.add(classpath)

        command.add("net.minecraft.client.main.Main") // Default main class

        // Game Arguments
        command.add("--username")
        command.add(username)
        command.add("--version")
        command.add(versionId)
        command.add("--gameDir")
        command.add(gameDir.absolutePath)
        command.add("--assetsDir")
        command.add(File(gameDir, "assets").absolutePath)
        command.add("--assetIndex")
        command.add(versionId) // Simplified

        val processBuilder = ProcessBuilder(command)
        processBuilder.directory(gameDir)

        // Renderer Environment Variables
        val env = processBuilder.environment()
        when (renderer) {
            "Holly Renderer" -> env["GALLIUM_DRIVER"] = "zink"
            "Zink" -> env["GALLIUM_DRIVER"] = "zink"
            "GL4ES" -> env["LIBGL_ES"] = "2"
        }

        return try {
            processBuilder.start()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun buildClasspath(versionDir: File): String {
        val libsDir = File(versionDir, "libraries")
        val jars = libsDir.walk().filter { it.extension == "jar" }.map { it.absolutePath }.toList()
        val clientJar = File(versionDir, "${versionDir.name}.jar").absolutePath
        return (jars + clientJar).joinToString(File.pathSeparator)
    }
}
