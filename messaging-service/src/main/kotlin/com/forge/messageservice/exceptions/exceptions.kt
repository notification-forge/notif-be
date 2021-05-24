package com.forge.messageservice.exceptions


class TemplateExistedException(message: String) : BusinessException(message)
class TemplateDoesNotExistException(message: String) : BusinessException(message)
class TemplateVersionDoesNotExistException(message: String) : BusinessException(message)
class InvalidTemplateSettingFormatException(message: String) : BusinessException(message)

class TenantExistedException(message: String) : BusinessException(message)
class TenantDoesNotExistException(message: String) : BusinessException(message)
class OwnersMissingException(message: String) : BusinessException(message)

class UserHaveYetToOnboardException(message: String) : BusinessException(message)

class PluginMissingException(message: String) : BusinessException(message)
class TemplatePluginMissingException(message: String) : BusinessException(message)

class FieldValidationException(message: String) : BusinessException(message)

open class BusinessException(msg: String) : GraphQLException(msg)
class TenantNotFoundException(msg: String) : BusinessException(msg)

class MessageDoesNotExistException(message: String) : BusinessException(message)
class InvalidMessageSettingException(message: String) : BusinessException(message)
class MessageRetryLimitException(message: String) : BusinessException(message)