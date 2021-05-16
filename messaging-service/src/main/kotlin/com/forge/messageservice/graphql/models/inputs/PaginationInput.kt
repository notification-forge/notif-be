package com.forge.messageservice.graphql.models.inputs

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PaginationInput(
    var pageNumber: Int,
    var rowPerPage: Int,
    var sortDirection: Sort.Direction,
    var sortField: String?
) {

    fun asPageRequest(): PageRequest = PageRequest.of(
        pageNumber, rowPerPage, sortDirection, sortField
    )

}
