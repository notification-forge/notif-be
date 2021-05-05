package com.forge.messageservice.lib.kafka

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class SpringBeanUtil : ApplicationContextAware {

    private val logger = LoggerFactory.getLogger(SpringBeanUtil::class.java)

    companion object {
        private lateinit var applicationContext: ApplicationContext
        fun getApplicationContext() =  applicationContext
        fun getBean(name: String) : Any = applicationContext.getBean(name)
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        SpringBeanUtil.applicationContext = applicationContext
    }
}