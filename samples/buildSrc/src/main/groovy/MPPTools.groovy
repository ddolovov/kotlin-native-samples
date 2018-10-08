/*
 * This file includes short-cuts that may potentially be implemented in Kotlin MPP Gradle plugin in the future.
 */

// Short-cuts for detecting the host OS.
def static isMacos() { System.getProperty('os.name') == 'Mac OS X' }
def static isLinux() { System.getProperty('os.name') == 'Linux' }
def static isWindows() { System.getProperty('os.name').startsWith('Windows') }

// A short-cut for evaluation of the default host Kotlin/Native preset.
def static defaultHostPreset(
        def subproject,
        def whitelist = [subproject.kotlin.presets.macosX64, subproject.kotlin.presets.linuxX64, subproject.kotlin.presets.mingwX64]) {

    if (whitelist.isEmpty()) {
        throw new Exception("Preset whitelist must not be empty in Kotlin/Native ${subproject.displayName}.")
    }

    def hostOs = System.getProperty('os.name')
    def preset = null

    if (isMacos()) {
        preset = subproject.kotlin.presets.macosX64
    } else if (isLinux()) {
        preset = subproject.kotlin.presets.linuxX64
    } else if (isWindows()) {
        preset = subproject.kotlin.presets.mingwX64
    }

    if (preset != null && !whitelist.contains(preset)) {
        preset = null
    }

    if (preset == null) {
        throw new Exception("Host OS '$hostOs' is not supported in Kotlin/Native ${subproject.displayName}.")
    }

    subproject.ext.hostPreset = preset

    return preset
}

// A short-cut to add a Kotlin/Native run task.
def static createRunTask(def subproject, String name, def target, Closure configureClosure = {}) {
    def task = subproject.tasks.create(name, RunKotlinNativeTask, subproject, target)
    task.configure(configureClosure)
    return task
}
