import java.nio.file.Paths

/*
 * This file includes short-cuts that may potentially be implemented in Kotlin MPP Gradle plugin in the future.
 */

// Short-cuts for detecting the host OS.
def static isMacos() { System.getProperty('os.name') == 'Mac OS X' }
def static isWindows() { System.getProperty('os.name').startsWith('Windows') }
def static isLinux() { isAnyLinux() && !isPiLinux() }
def static isRaspberrypi() { isAnyLinux() && isPiLinux() }

private def static isAnyLinux() { System.getProperty('os.name') == 'Linux' }

private def static isPiLinux() {
    def file = new File('/etc/os-release')
    if (!file.isFile()) return false

    file.eachLine { line ->
        def lowerLine = line.toLowerCase()
        if ('raspbian' in lowerLine && 'name' in lowerLine) return true
    }

    return false
}

// Short-cuts for mostly used paths.
def static mingwPath() {
    System.getenv('MINGW64_DIR') ?: 'c:/msys64/mingw64'
}

def static kotlinNativeDataPath() {
    System.getenv('KONAN_DATA_DIR') ?: Paths.get(System.getProperty('user.home'), '.konan').toString()
}

def static systemFrameworksPath() { '/Library/Frameworks' }
def static localFrameworksPath() { Paths.get(System.getProperty('user.home'), 'Library/Frameworks').toString() }

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
    } else if (isRaspberrypi()) {
        preset = subproject.kotlin.presets.linuxArm32Hfp
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
