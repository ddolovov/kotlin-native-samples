import groovy.lang.Closure
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction
import org.jetbrains.kotlin.gradle.plugin.KotlinTarget
import javax.inject.Inject

open class RunKotlinNativeTask @Inject constructor(
        private val myProject: Project,
        private val myTarget: KotlinTarget
): DefaultTask() {

    var buildType = "release"
    private var myArgs: List<String> = emptyList()

    fun args(vararg args: Any) {
        this.myArgs = args.map { it.toString() }
    }

    override fun configure(configureClosure: Closure<Any>): Task {
        val task = super.configure(configureClosure)
        this.dependsOn += myTarget.compilations.main.linkTaskName("executable", buildType)
        return task
    }

    @TaskAction
    fun run() {
        myProject.exec {
            it.executable = myTarget.compilations.main.getBinary("executable", buildType).toString()
            it.args = myArgs
        }
    }

    internal fun emptyConfigureClosure() = object : Closure<Any>(this) {
        override fun call(): RunKotlinNativeTask {
            return this@RunKotlinNativeTask
        }
    }
}
