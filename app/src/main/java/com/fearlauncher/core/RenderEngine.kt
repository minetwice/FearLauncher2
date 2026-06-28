package com.fearlauncher.core

import java.io.File

object RenderEngine {
    enum class Renderer(val id: String, val displayName: String) {
        HOLLY("holly", "Holly Renderer"),
        ZINK("zink", "Zink (Mesa)"),
        LTW("ltw", "LTW Renderer"),
        GL4ES("gl4es", "GL4ES 1.1.4")
    }

    fun getEnvironmentVariables(renderer: Renderer, gameDir: File): Map<String, String> {
        val env = mutableMapOf<String, String>()

        when (renderer) {
            Renderer.HOLLY -> {
                env["GALLIUM_DRIVER"] = "zink"
                env["MESA_LOADER_DRIVER_OVERRIDE"] = "zink"
                env["ZINK_DESCRIPTORS"] = "lazy"
            }
            Renderer.ZINK -> {
                env["GALLIUM_DRIVER"] = "zink"
                env["TU_DEBUG"] = "sysmem"
            }
            Renderer.LTW -> {
                env["LIBGL_USEVBO"] = "1"
                env["MESA_EXTENSION_MAX_YEAR"] = "2003"
            }
            Renderer.GL4ES -> {
                env["LIBGL_ES"] = "2"
                env["LIBGL_GL"] = "21"
            }
        }

        // General performance tweaks
        env["MESA_GL_VERSION_OVERRIDE"] = "4.6"
        env["MESA_GLSL_VERSION_OVERRIDE"] = "460"

        return env
    }

    fun setupCore(context: android.content.Context) {
        // Initialize native libraries or assets for renderers
        val coreDir = File(context.filesDir, "core")
        if (!coreDir.exists()) coreDir.mkdirs()

        // In a real scenario, we would extract .so files for Mesa/Zink here
    }
}
