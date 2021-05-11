package com.forge.messageservice.lib.kafka

import com.alphamail.plugin.api.PluginConfiguration

class KafkaPluginConfiguration(
    val topic: String,
    val brokerAddress: String
) : PluginConfiguration