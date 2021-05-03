package com.forge.messageservice.instrumentations

import graphql.ExecutionResult
import graphql.execution.instrumentation.InstrumentationContext
import graphql.execution.instrumentation.SimpleInstrumentation
import graphql.execution.instrumentation.SimpleInstrumentationContext
import graphql.execution.instrumentation.nextgen.InstrumentationExecutionParameters
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.Duration
import java.time.Instant

@Component
class RequestLoggingInstrumentation(private val clock: Clock) : SimpleInstrumentation() {

    private val logger = LoggerFactory.getLogger(RequestLoggingInstrumentation::class.java)

    fun beginExecution(parameters: InstrumentationExecutionParameters): InstrumentationContext<ExecutionResult>? {
        val start = Instant.now(clock)
        val executionId = parameters.executionInput.executionId
        logger.info("${executionId}: query: ${parameters.query} with variables: ${parameters.variables}")

        return SimpleInstrumentationContext.whenCompleted { _: ExecutionResult?, throwable: Throwable? ->
            val duration = Duration.between(start, Instant.now(clock))
            if (throwable == null) {
                logger.info("${executionId}: completed successfully in: $duration")
            } else {
                logger.warn("${executionId}: failed in: $duration with throwable $throwable")
            }
        }
    }

}