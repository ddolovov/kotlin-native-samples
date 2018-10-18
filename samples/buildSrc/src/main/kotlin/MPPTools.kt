@file:JvmName("MPPTools")

import groovy.lang.Closure
import org.gradle.api.Project
import org.gradle.api.Task
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import org.jetbrains.kotlin.gradle.plugin.KotlinTargetPreset
import java.io.File
import java.nio.file.Paths

/*
 * This file includes short-cuts that may potentially be implemented in Kotlin MPP Gradle plugin in the future.
 */

// Short-cuts for detecting the host OS.
@get:JvmName("isMacos") val isMacos by lazy { hostOs == "Mac OS X" }
@get:JvmName("isWindows") val isWindows by lazy { hostOs.startsWith("Windows") }
@get:JvmName("isLinux") val isLinux by lazy { isAnyLinux && !isPiLinux }
@get:JvmName("isRaspberrypi") val isRaspberrypi by lazy { isAnyLinux && isPiLinux }

private val isAnyLinux by lazy { hostOs == "Linux" }

private val isPiLinux by lazy {
    val file = File("/etc/os-release")
    if (!file.isFile)
        false
    else
        file.useLines { lines ->
            lines.map { it.toLowerCase() }.any { "raspbian" in it && "name" in it }
        }
}

// Short-cuts for mostly used paths.
@get:JvmName("mingwPath")
val mingwPath by lazy { System.getenv("MINGW64_DIR") ?: "c:/msys64/mingw64" }

@get:JvmName("kotlinNativeDataPath")
val kotlinNativeDataPath by lazy {
    System.getenv("KONAN_DATA_DIR") ?: Paths.get(userHome, ".konan").toString()
}

@get:JvmName("systemFrameworksPath")
val systemFrameworksPath = "/Library/Frameworks"

@get:JvmName("localFrameworksPath")
val localFrameworksPath by lazy { Paths.get(userHome, "Library/Frameworks").toString() }

// A short-cut for evaluation of the default host Kotlin/Native preset.
@JvmOverloads
fun defaultHostPreset(
    subproject: Project,
    whitelist: List<KotlinTargetPreset<*>> = listOf(subproject.kotlin.presets.macosX64, subproject.kotlin.presets.linuxX64, subproject.kotlin.presets.mingwX64)
): KotlinTargetPreset<*> {

    if (whitelist.isEmpty())
        throw Exception("Preset whitelist must not be empty in Kotlin/Native ${subproject.displayName}.")

    val presetCandidate = when {
        isMacos -> subproject.kotlin.presets.macosX64
        isLinux -> subproject.kotlin.presets.linuxX64
        isWindows -> subproject.kotlin.presets.mingwX64
        isRaspberrypi -> subproject.kotlin.presets.linuxArm32Hfp
        else -> null
    }

    val preset = if (presetCandidate != null && presetCandidate in whitelist)
        presetCandidate
    else
        throw Exception("Host OS '$hostOs' is not supported in Kotlin/Native ${subproject.displayName}.")

    subproject.ext.set("hostPreset", preset)

    return preset
}

// A short-cut to add a Kotlin/Native run task.
@JvmOverloads
fun createRunTask(
        subproject: Project,
        name: String,
        target: KotlinTarget,
        configureClosure: Closure<Any>? = null
): Task {
    val task = subproject.tasks.create(name, RunKotlinNativeTask::class.java, subproject, target)
    task.configure(configureClosure ?: task.emptyConfigureClosure())
    return task
}
