package com.forge.messageservice.lib.kafka

import com.alphamail.plugin.api.PluginConfiguration

class KafkaConfiguration : PluginConfiguration {
    var bootStrapAddress: String = ""
    var kafkaTopic: String = ""

    var kafkaGroupId: String? = null
    var kafkaSslKeyPassword: String? = null
    var kafkaSslEnable: Boolean? = null
}
