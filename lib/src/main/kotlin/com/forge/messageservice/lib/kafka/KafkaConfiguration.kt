package com.forge.messageservice.lib.kafka

import com.alphamail.plugin.api.PluginConfiguration

class KafkaConfiguration : PluginConfiguration {
    var bootStrapAddress: String = ""
    var kafkaTopic: String = ""
}