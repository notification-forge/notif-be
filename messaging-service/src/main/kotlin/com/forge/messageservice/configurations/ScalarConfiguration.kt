package com.forge.messageservice.configurations

import graphql.kickstart.servlet.apollo.ApolloScalars
import graphql.scalars.ExtendedScalars
import graphql.schema.GraphQLScalarType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock


@Configuration
class ScalarConfiguration {

    @Bean
    fun dateTime(): GraphQLScalarType = ExtendedScalars.DateTime

    @Bean
    fun date(): GraphQLScalarType = ExtendedScalars.Date

    @Bean
    fun uploadScalar(): GraphQLScalarType = ApolloScalars.Upload

    @Bean
    fun clock(): Clock = Clock.systemUTC()
}

