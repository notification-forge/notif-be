package com.forge.messageservice.controllers.exceptions

open class BusinessException(msg: String) : RuntimeException(msg)
class TenantAlreadyExistException(msg: String) : BusinessException(msg)
class APIKeyNotFoundException(msg: String) : BusinessException(msg)