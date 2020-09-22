import com.typesafe.config.ConfigFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.hocon.Hocon

//import kotlinx.serialization.config.ConfigParser

/**
 * package/name {
 *     ClassName {
 *         regex = true
 *         methods  = ["^method1()V$", "method2"]
 *         fields = [...]
 *     }
 * }
 *
 * if methods is null then all are accepted; if fields is null then all are accepted
 * regex defaults to false and affects the class name, method names and field names
 */

@Serializable
data class AbstractionSelection(val value: Map<String, AbstractedPackage>) {
    companion object {
        fun fromHocon(hoconStr: String)  = Hocon.decodeFromConfig(
                serializer(),
                ConfigFactory.empty()
                        .withValue("value", ConfigFactory.parseString(hoconStr).root()))
    }
}

typealias AbstractedPackage = Map<String, AbstractedClass>

@Serializable
data class AbstractedClass(
    val fields: List<String>? = null,
    val methods: List<String>? = null,
    val regex: Boolean = false
)