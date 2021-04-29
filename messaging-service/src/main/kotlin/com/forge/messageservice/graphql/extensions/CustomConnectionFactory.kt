package com.forge.messageservice.graphql.extensions

import graphql.kickstart.tools.TypeDefinitionFactory
import graphql.language.*

/**
 * A re-implementation of [RelayConnectionFactory](graphql.kickstart.tools.relay.RelayConnectionFactory) to add `totalCount` to the generated `*Connection` schema.
 *
 * This class is purposely declared open to allow subclasses to override schema generation behavior.
 */
@Suppress("MemberVisibilityCanBePrivate")
open class CustomConnectionFactory : TypeDefinitionFactory {

    override fun create(existing: MutableList<Definition<*>>): List<Definition<*>> {
        val connectionDirectives = findConnectionDirectives(existing)
        if (connectionDirectives.isEmpty()) {
            // do not add Relay definitions unless needed
            return emptyList()
        }

        val definitions = mutableListOf<Definition<*>>()
        val definitionsByName = existing.filterIsInstance<TypeDefinition<*>>()
            .associateBy { it.name }
            .toMutableMap()

        connectionDirectives
            .flatMap { createDefinitions(it) }
            .forEach {
                // Overwrite definitions created by `RelayConnectionFactory`
                definitionsByName[it.name] = it
                definitions.add(it)
            }

        if (!definitionsByName.containsKey("PageInfo")) {
            definitions.add(createPageInfo())
        }

        return definitions
    }

    protected fun findConnectionDirectives(definitions: List<Definition<*>>): List<DirectiveWithField> {
        return definitions.filterIsInstance<ObjectTypeDefinition>()
            .flatMap { it.fieldDefinitions }
            .flatMap { it.directivesWithField() }
            .filter { it.name == "connection" }
    }

    protected fun createDefinitions(directive: DirectiveWithField): List<ObjectTypeDefinition> {
        val definitions = mutableListOf<ObjectTypeDefinition>()
        definitions.add(createConnectionDefinition(directive.getTypeName()))
        definitions.add(createEdgeDefinition(directive.getTypeName(), directive.forTypeName()))
        return definitions.toList()
    }

    protected fun createConnectionDefinition(type: String): ObjectTypeDefinition =
        ObjectTypeDefinition.newObjectTypeDefinition()
            .name(type)
            .fieldDefinition(FieldDefinition("totalCount", TypeName("Int")))
            .fieldDefinition(FieldDefinition("edges", ListType(TypeName(type + "Edge"))))
            .fieldDefinition(FieldDefinition("pageInfo", TypeName("PageInfo")))
            .build()

    protected fun createEdgeDefinition(connectionType: String, nodeType: String): ObjectTypeDefinition =
        ObjectTypeDefinition.newObjectTypeDefinition()
            .name(connectionType + "Edge")
            .fieldDefinition(FieldDefinition("cursor", TypeName("String")))
            .fieldDefinition(FieldDefinition("node", TypeName(nodeType)))
            .build()

    protected fun createPageInfo(): ObjectTypeDefinition =
        ObjectTypeDefinition.newObjectTypeDefinition()
            .name("PageInfo")
            .fieldDefinition(FieldDefinition("hasPreviousPage", NonNullType(TypeName("Boolean"))))
            .fieldDefinition(FieldDefinition("hasNextPage", NonNullType(TypeName("Boolean"))))
            .fieldDefinition(FieldDefinition("startCursor", TypeName("String")))
            .fieldDefinition(FieldDefinition("endCursor", TypeName("String")))
            .build()

    protected fun Directive.forTypeName(): String {
        return (this.getArgument("for").value as StringValue).value
    }

    protected fun Directive.withField(field: FieldDefinition): DirectiveWithField {
        return DirectiveWithField(field, this.name, this.arguments, this.sourceLocation, this.comments)
    }

    protected fun FieldDefinition.directivesWithField(): List<DirectiveWithField> {
        return this.directives.map { it.withField(this) }
    }

    class DirectiveWithField(val field: FieldDefinition, name: String, arguments: List<Argument>, sourceLocation: SourceLocation, comments: List<Comment>) : Directive(name, arguments, sourceLocation, comments, IgnoredChars.EMPTY, emptyMap()) {
        fun getTypeName(): String {
            val type = field.type
            if (type is NonNullType) {
                return (type.type as TypeName).name
            }
            return (field.type as TypeName).name
        }
    }

}