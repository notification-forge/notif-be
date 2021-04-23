package com.forge.messageservice.lib.kafka

    import com.alphamail.plugin.api.AlphamailPlugin


class KafkaPlugin(private val cfg: KafkaConfiguration) : AlphamailPlugin {

    override fun beforeSend(): Any? {
        return "This is kafka plugin before sending, for ${cfg.kafkaServer}"
    }

    override fun afterSend(): Any? {
        return "This is kafka plugin after sending"
    }
}