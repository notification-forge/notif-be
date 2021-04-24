package com.forge.messageservice.services

import com.forge.messageservice.graphql.models.inputs.PaginationInput
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

// TODO: Assess whether this needs to be as a service or
//  better be as a `helper class` instead
@Service
class PaginationService {

    fun pageRequest(paginationInput: PaginationInput): Pageable {
        return PageRequest.of(
            paginationInput.pageNumber,
            paginationInput.rowPerPage,
            paginationInput.sortDirection,
            paginationInput.sortField
        )
    }
}