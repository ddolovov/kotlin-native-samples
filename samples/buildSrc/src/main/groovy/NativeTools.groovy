import java.nio.file.Paths

/*
 * This file includes Kotlin/Native specific auxiliary methods that are not subject for adding to
 * Kotlin MPP Gradle plugin.
 */

def static testMavenRepoUrl() {
    'file://' + Paths.get(System.getProperty('user.home'), '.m2-kotlin-native-samples')
}
