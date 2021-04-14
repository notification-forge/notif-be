package com.forge.messageservice.configurations

import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock


@Configuration
open class ScalarConfiguration {

    @Bean
    open fun dateTime(): GraphQLScalarType {
        return ExtendedScalars.DateTime
    }

    @Bean
    open fun date(): GraphQLScalarType {
        return ExtendedScalars.Date
    }

    @Bean
    open fun clock(): Clock {
        return Clock.systemUTC()
    }
}

