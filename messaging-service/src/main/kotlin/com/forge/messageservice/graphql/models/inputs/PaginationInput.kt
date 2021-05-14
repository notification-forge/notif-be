package com.forge.messageservice.graphql.models.inputs

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class PaginationInput {

    var pageNumber: Int = 1
    var rowPerPage: Int = 20
    var sortDirection: Sort.Direction = Sort.Direction.ASC
    var sortField: String? = null

    constructor(pageNumber: Int, rowPerPage: Int, sortDirection: Sort.Direction, sortField: String?) {
        this.pageNumber = pageNumber
        this.rowPerPage = rowPerPage
        this.sortDirection = sortDirection
        this.sortField = sortField
    }

    fun asPageRequest(): PageRequest = PageRequest.of(
        pageNumber, rowPerPage, sortDirection, sortField
    )

}
