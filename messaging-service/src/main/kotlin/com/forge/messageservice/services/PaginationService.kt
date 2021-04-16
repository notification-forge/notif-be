package com.forge.messageservice.services

import com.forge.messageservice.entities.Template
import com.forge.messageservice.entities.pages.Sort
import com.forge.messageservice.entities.pages.TemplatePages
import org.springframework.data.domain.Page
import org.springframework.stereotype.Service

@Service
class PaginationService {

    fun templatePagination(templates: Page<Template>): TemplatePages{
        val templatePagination = TemplatePages()
        paginationUpdate(templates, templatePagination)
        templatePagination.content = templates.content.toList()
        return templatePagination
    }

    private fun <T> paginationUpdate(pages: Page<T>, page: com.forge.messageservice.entities.pages.Page){
        page.apply {
            totalElements = pages.totalElements.toInt()
            totalPages = pages.totalPages
            isEmpty = pages.isEmpty
            isFirst = pages.isLast
            isLast = pages.isLast
            number = pages.number
            numberOfElements = pages.numberOfElements
            size = pages.size
            sort = Sort().apply {
                isSorted = pages.sort.isSorted
                isUnsorted = pages.sort.isUnsorted
                isEmpty = pages.sort.isEmpty
            }
        }
    }
}