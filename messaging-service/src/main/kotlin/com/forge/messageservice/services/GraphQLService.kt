package com.forge.messageservice.services

import com.forge.messageservice.services.datafetcher.AllTemplateDataFetcher
import com.forge.messageservice.services.datafetcher.AllTemplateVersionDataFetcher
import com.forge.messageservice.services.datafetcher.TemplateDataFetcher
import com.forge.messageservice.services.datafetcher.TemplateVersionDataFetcher
import graphql.GraphQL
import graphql.schema.GraphQLSchema
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import javax.annotation.PostConstruct
import kotlin.jvm.Throws

@Service
class GraphQLService(
    private val allTemplateDataFetcher: AllTemplateDataFetcher,
    private val allTemplateVersionDataFetcher: AllTemplateVersionDataFetcher,
    private val templateDataFetcher: TemplateDataFetcher,
    private val templateVersionDataFetcher: TemplateVersionDataFetcher
) {

    @Value("classpath:message.graphql")
    var resource: Resource? = null

    private var graphQL: GraphQL? = null

    // load schema at application start up
    @PostConstruct
    @Throws(IOException::class)
    private fun loadSchema() {

        // get the schema
        val schemaFile: File = resource!!.file
        // parse schema
        val typeRegistry: TypeDefinitionRegistry = SchemaParser().parse(schemaFile)
        val wiring: RuntimeWiring? = buildRuntimeWiring()
        val schema: GraphQLSchema = SchemaGenerator().makeExecutableSchema(typeRegistry, wiring)
        graphQL = GraphQL.newGraphQL(schema).build()
    }

    private fun buildRuntimeWiring(): RuntimeWiring? {
        return RuntimeWiring.newRuntimeWiring()
            .type("Query") { typeWiring ->
                typeWiring
                    .dataFetcher("allTemplates", allTemplateDataFetcher)
                    .dataFetcher("template", templateDataFetcher)
                    .dataFetcher("allTemplateVersions", allTemplateVersionDataFetcher)
                    .dataFetcher("templateVersion", templateVersionDataFetcher)
            }
            .build()
    }


    fun getGraphQL(): GraphQL? {
        return graphQL
    }
}