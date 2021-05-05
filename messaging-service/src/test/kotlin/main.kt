import com.alphamail.plugin.api.PluginConfiguration
import com.forge.messageservice.lib.kafka.KafkaPluginConfiguration
import java.net.URL
import java.net.URLClassLoader
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass

class Loader {

    fun load(uri: String): Any? {
        val child = URLClassLoader(arrayOf(URL(uri)), this.javaClass.classLoader)
        val classToLoad = Class.forName("com.forge.messageservice.lib.kafka.KafkaPluginConfiguration", true, child)
        val kClass = Reflection.createKotlinClass(classToLoad)
        val instance = kClass.createInstance("bal") as KafkaPluginConfiguration
        println(instance.topic)
//        println(instance.beforeSend())
        return instance
    }
}

private fun <T : Any> KClass<T>.createInstance(kafkaServer: String): T {
//    Can use filter here if you have more than 1 constructor
    val noArgsConstructor = constructors.first()
//    If decided every plugin doesn't have other param, then this is good enough
    val params = noArgsConstructor.parameters.associateWith { kafkaServer }
//    Invoke constructor with param
    return noArgsConstructor.callBy(params)
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