import javax.inject.Inject
import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.TaskAction

class RunKotlinNativeTask extends DefaultTask {
    private final def myProject
    private final def myTarget

    String buildType = 'release'
    private List<String> myArgs = []

    void args(Object... args) {
        this.myArgs = args.collect { it.toString() }
    }

    @Inject
    RunKotlinNativeTask(def project, def target) {
        this.myProject = project
        this.myTarget = target
    }

    Task configure(Closure configureClosure) {
        super.configure(configureClosure)
        this.dependsOn myTarget.compilations.main.linkTaskName('executable', buildType)
    }

    @TaskAction
    void run() {
        def programFile = myTarget.compilations.main.getBinary('executable', buildType)
        def arguments = myArgs
        myProject.exec {
            executable programFile
            args arguments
        }
    }
}
