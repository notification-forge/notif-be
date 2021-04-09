package com.forge.messageservice.controllers.v1

import com.forge.messageservice.services.GraphQLService
import graphql.ExecutionResult
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/templates")
class TemplateController(
    private val graphQLService: GraphQLService
) {

    @GetMapping
    fun getTemplates(@RequestBody query: String): ResponseEntity<ExecutionResult> {
        return ok().body(graphQLService.getGraphQL()!!.execute(query))
    }
}