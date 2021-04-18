package com.forge.messageservice.configurations

import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock


@Configuration
open class ScalarConfiguration {

    @Bean
    open fun dateTime(): GraphQLScalarType = ExtendedScalars.DateTime

    @Bean
    open fun date(): GraphQLScalarType = ExtendedScalars.Date

    @Bean
    open fun uploadScalar(): GraphQLScalarType = ApolloScalars.Upload

    @Bean
    open fun clock(): Clock = Clock.systemUTC()
}

