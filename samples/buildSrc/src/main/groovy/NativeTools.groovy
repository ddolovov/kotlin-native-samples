/*
 * This file includes Kotlin/Native specific auxiliary methods that are not subject for adding to
 * Kotlin MPP Gradle plugin.
 */

def static localTestMavenRepoUrl() {
    "file://${new File(System.properties['user.home'] as String)}/.m2-kotlin-native-samples"
}
