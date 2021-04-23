package com.forge.messageservice.common.mapper

import com.alphamail.plugin.api.FieldConfiguration
import com.alphamail.plugin.api.FieldType
import com.forge.messageservice.exceptions.FieldValidationException
import org.springframework.stereotype.Component

@Component
class PluginSettingToConfigurationEntityMapper {

    fun <T> settingToConfigurationObject(
        configurationMap: Map<String, String>,
        fieldConfigurationMap: Map<String, FieldConfiguration>,
        targetClass: Class<T>
    ): T {
//        val cons: Constructor<*> = targetClass.getConstructor(KafkaConfiguration::class.java)
//        val entityInstance = cons.newInstance()
        val entityInstance = targetClass.getDeclaredConstructor().newInstance()

        val fields = targetClass.declaredFields

        for (field in fields) {

            val value = configurationMap[field.name]
            field.isAccessible = true

            val fieldConfiguration = fieldConfigurationMap.getOrElse(field.name) {
                throw FieldValidationException("${field.name}.key does not exist")
            }

            when (fieldConfiguration.fieldType) {
                FieldType.STRING -> {
                    field.set(entityInstance, value)
                }
                FieldType.INT -> {
                    field.set(entityInstance, value?.toIntOrNull())
                }
                FieldType.LONG -> {
                    field.set(entityInstance, value?.toLongOrNull())
                }
                FieldType.BIG_INTEGER -> {
                    field.set(entityInstance, value?.toBigIntegerOrNull())
                }
                FieldType.BIG_DECIMAL -> {
                    field.set(entityInstance, value?.toBigDecimalOrNull())
                }
                FieldType.BOOLEAN -> {
                    field.set(entityInstance, value?.toBoolean())
                }
                FieldType.ENUM -> {
                    for (enumConstant in field.type.enumConstants) {
                        if ((enumConstant as Enum<*>).name == value) {
                            field.set(entityInstance, enumConstant)
                        }
                    }
                }
            }
        }
        return entityInstance
    }
}