package com.forge.messageservice.common.engines

import com.mitchellbosecke.pebble.PebbleEngine
import org.springframework.stereotype.Component
import java.io.StringWriter

@Component
class TemplatingEngine {

    private val engine = PebbleEngine.Builder().build()

    fun parseTemplate(template: String, context: Map<String, Any>) : String{
        val writer = StringWriter()
        val compiledTemplate = engine.getLiteralTemplate(template)
        compiledTemplate.evaluate(writer, context)
        return writer.toString()
    }
}