package com.forge.messageservice.lib.kafka

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback

class KafkaMessageProducer(
    private val cfg: KafkaPluginConfiguration,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val cbHandler: ListenableFutureCallback<SendResult<String, String>>
) {

    internal fun send(data: String) {
        val future = kafkaTemplate.send(cfg.topic, data)
        future.addCallback(cbHandler)
    }

}