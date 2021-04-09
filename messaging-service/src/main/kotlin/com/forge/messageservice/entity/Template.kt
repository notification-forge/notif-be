package com.forge.messageservice.entity

import java.time.OffsetDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Pattern

@Entity
@Table(name = "templates")
class Template {

    /**
     * A user defined id of the template. Alphanumeric, dashes and dots only
     */
    @Id
    @Column(name = "template_id", length = 56, nullable = false)
    @Pattern(regexp = "[A-Za-z0-9\\-.]")
    var templateId: String? = null

    @Column(name = "template_name")
    var templateName: String? = null

    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = "template_UUID")
    var templateUUID: UUID? = null

    enum class AlertType {
        TEAMS_NOTIFICATION, EMAIL
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 24, nullable = false)
    var alertType: AlertType = AlertType.EMAIL

    @Column(name = "app_code")
    var appCode: String? = null

    @Column(name = "created_by", length = 24, nullable = false)
    val createdBy: String? = null

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false)
    val dateCreated: OffsetDateTime? = null

    @Column(name = "updated_by", length = 24)
    val updatedBy: String? = null

//    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated", nullable = false)
    val dateUpdated: OffsetDateTime? = null
}