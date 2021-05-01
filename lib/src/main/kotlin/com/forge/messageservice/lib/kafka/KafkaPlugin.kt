package com.forge.messageservice.lib.kafka

import com.alphamail.plugin.api.AlphamailPlugin

/**
 * Kafka plugin drops a message to a kafka topic indicating that a message has been
 * sent.
 *
 * Notifications from this plugin can be used by tenants to update the status of their
 * email dispatches.
 */
class KafkaPlugin(private val cfg: KafkaConfiguration) : AlphamailPlugin {

    override fun execute(): Any? {
        TODO("Not yet implemented")
    }

    override fun type(): AlphamailPlugin.PluginType {
        return AlphamailPlugin.PluginType.AfterSend
    }
}