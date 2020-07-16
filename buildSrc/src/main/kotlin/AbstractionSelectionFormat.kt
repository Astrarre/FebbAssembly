import com.typesafe.config.ConfigFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.config.ConfigParser

@Serializable
data class AbstractionSelection(val value: Map<String, AbstractedPackage>) {
    companion object {
        fun fromHocon(hoconStr: String) = ConfigParser.parse(
            ConfigFactory.empty()
                .withValue("value", ConfigFactory.parseString(hoconStr).root()), serializer()
        )
    }
}

typealias AbstractedPackage = Map<String, AbstractedClass>

@Serializable
data class AbstractedClass(
    val fields: List<String>? = null,
    val methods: List<String>? = null,
    val regex: Boolean = false
)