package com.forge.messageservice.configurations

import graphql.Assert
import graphql.language.*
import graphql.schema.*
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer
import kotlin.jvm.Throws

@Component
class ObjectScalar internal constructor(name: String, description: String?) :
    GraphQLScalarType(name, description, object : Coercing<Any?, Any> {
        @Throws(CoercingSerializeException::class)
        override fun serialize(input: Any): Any {
            return input
        }

        @Throws(CoercingParseValueException::class)
        override fun parseValue(input: Any): Any? {
            return input
        }

        @Throws(CoercingParseLiteralException::class)
        override fun parseLiteral(input: Any): Any? {
            return this.parseLiteral(input, Collections.emptyMap())
        }

        @Throws(CoercingParseLiteralException::class)
        override fun parseLiteral(
            input: Any,
            variables: Map<String, Any>
        ): Any? {
            return when (input) {
                is NullValue -> {
                    null
                }
                is FloatValue -> {
                    (input as FloatValue).value
                }
                is StringValue -> {
                    (input as StringValue).value
                }
                is IntValue -> {
                    (input as IntValue).value
                }
                is BooleanValue -> {
                    (input as BooleanValue).isValue
                }
                is EnumValue -> {
                    (input as EnumValue).name
                }
                is VariableReference -> {
                    val varName: String = (input as VariableReference).name
                    variables[varName]
                }
                else -> {
                    val values: List<*>
                    when (input) {
                        is ArrayValue -> {
                            values = (input as ArrayValue).values
                            values.map { v ->
                                { this.parseLiteral(v, variables) }
                            }
                        }
                        is ObjectValue -> {
                            values = (input as ObjectValue).objectFields
                            val parsedValues: MutableMap<String?, Any?> = mutableMapOf()
                            values.forEach(Consumer { fld: Any ->
                                val parsedValue = this.parseLiteral(
                                    (fld as ObjectField).value,
                                    variables
                                )
                                parsedValues[fld.name] = parsedValue
                            })
                            parsedValues
                        }
                        else -> {
                            Assert.assertShouldNeverHappen(
                                "We have covered all Value types",
                                arrayOfNulls<Any>(0)
                            )
                        }
                    }
                }
            }
        }
    }) {
    constructor() : this("Object", "An object scalar") {}
}