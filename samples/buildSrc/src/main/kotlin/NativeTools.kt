@file:JvmName("NativeTools")

import java.nio.file.Paths

/*
 * This file includes short-cuts needed only for Kotlin/Native samples.
 */

fun testMavenRepoUrl() = "file://" + Paths.get(userHome, ".m2-kotlin-native-samples")
