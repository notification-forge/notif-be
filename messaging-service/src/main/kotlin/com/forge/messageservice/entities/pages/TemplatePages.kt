package com.forge.messageservice.entities.pages

import com.forge.messageservice.entities.Template

class TemplatePages : Page() {
    var content: List<Template>? = null
}

class Sort {
    var isSorted: Boolean? = null
    var isUnsorted: Boolean? = null
    var isEmpty: Boolean? = null
}