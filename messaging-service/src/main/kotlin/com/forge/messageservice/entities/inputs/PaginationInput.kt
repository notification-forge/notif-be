package com.forge.messageservice.entities.inputs

import org.springframework.data.domain.Sort

data class PaginationInput(
    val pageNumber: Int,
    val rowPerPage: Int,
    val sortDirection: Sort.Direction,
    val sortField: String
)