import com.alphamail.plugin.api.AlphamailPlugin
import com.alphamail.plugin.api.PluginConfiguration
import com.forge.messageservice.lib.kafka.KafkaConfiguration
import java.net.URL
import java.net.URLClassLoader
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass

class Loader {

    fun load(uri: String): Any? {
        val child = URLClassLoader(arrayOf(URL(uri)), this.javaClass.classLoader)
        val classToLoad = Class.forName("com.forge.messageservice.lib.kafka.KafkaPlugin", true, child)
        val kClass = Reflection.createKotlinClass(classToLoad)
        val config = KafkaConfiguration().apply {
            kafkaServer = "bla blo"
        }
        val instance = kClass.createInstance(config) as AlphamailPlugin
        println(instance.beforeSend())
        return instance
    }
}

//Decorator to call create plugin instance with config
fun <T : Any> KClass<T>.createInstance(config: PluginConfiguration): T {
//    Can use filter here if you have more than 1 constructor
    val noArgsConstructor = constructors.first()
//    If decided every plugin doesn't have other param, then this is good enough
    val params = noArgsConstructor.parameters.associateWith { config }
//    Invoke constructor with param
    return noArgsConstructor.callBy(params)
}

fun main(args: Array<String>) {
    val lib = "file:./lib/build/libs/lib-1.0.jar"
    val loader = Loader()
    println(loader.load(lib))
}