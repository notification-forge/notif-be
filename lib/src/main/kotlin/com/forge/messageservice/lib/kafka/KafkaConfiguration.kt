package com.forge.messageservice.lib.kafka

import com.alphamail.plugin.api.PluginConfiguration

class KafkaConfiguration : PluginConfiguration {
    var kafkaServer: String? = null
    var kafkaGroupId: String? = null
    var kafkaSslKeyPassword: String? = null
    var kafkaSslEnable: Boolean? = null
    var kafkaTopic: String? = null
}
