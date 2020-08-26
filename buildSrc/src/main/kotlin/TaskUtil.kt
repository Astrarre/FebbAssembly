import org.gradle.api.Task
import java.nio.file.Path
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class TaskDelegate(private val context: ProjectContext, private val configure: Task.() -> Unit) {
    private lateinit var task: Task
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Task) {}
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Task {
        if (::task.isInitialized) return task
        else {
            task = context.project.task(property.name) { configure(it) }
            return task
        }
    }
}
//TODO: try getting rid of properties or smthn like that mb

/** you MUST access the delegate at least once */
fun ProjectContext.task(vararg dependencies: Task, configure: Task.() -> Unit): TaskDelegate {
    return TaskDelegate(this
    ) {
        for (dependency in dependencies) dependsOn(dependency)
        configure()
    }
}

fun Task.setFileInputs(vararg inputs: Path) {
    this.inputs.files(inputs.map { it.toFile() })
}

fun Task.setDirInputs(vararg inputs: Path) {
    for(input in inputs) {
        this.inputs.dir(input)
    }
}

fun Task.setPropertyInputs(vararg inputs: Pair<String, Any?>) {
    this.inputs.properties(inputs.toMap())
}

fun Task.setFileOutputs(vararg outputs: Path) {
    this.outputs.files(outputs.map { it.toFile() })
}

fun Task.setDirOutputs(vararg outputs: Path) {
    this.outputs.dirs(outputs.map { it.toFile() })
}