package com.forge.messageservice.graphql.extensions

import graphql.kickstart.tools.TypeDefinitionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CustomConfiguration {

    @Bean
    fun typeDefinitionFactories(): List<TypeDefinitionFactory> {
        return listOf(CustomConnectionFactory())
    }

}