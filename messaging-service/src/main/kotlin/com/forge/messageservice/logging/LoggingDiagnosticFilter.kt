package com.forge.messageservice.logging

import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.web.filter.OncePerRequestFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingDiagnosticFilter : OncePerRequestFilter(){

    private val LOG = LoggerFactory.getLogger(LoggingDiagnosticFilter::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        MDC.clear()

        val startTime = System.currentTimeMillis()
        try{
            MDC.putCloseable("requestId", getRequestId(request))
            LOG.info("Starting request ${request.requestURI}")

            filterChain.doFilter(request, response)
        } finally {
            val endTime = System.currentTimeMillis()

            MDC.putCloseable("responseCode", response.status.toString())
            MDC.putCloseable("responseTimeMillis", (endTime - startTime).toString())
            LOG.info("Complete request ${request.requestURI}")
        }
    }

    private fun getRequestId(request: HttpServletRequest): String{
        return request.getHeader("X-B3-TraceId") ?: UUID.randomUUID().toString()
    }
}