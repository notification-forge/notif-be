package com.forge.messageservice.entity

import java.time.OffsetDateTime
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

    enum class AlertType {
        TEAMS_NOTIFICATION, EMAIL
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 24, nullable = false)
    var alertType: AlertType =
        AlertType.EMAIL

    /**
     * A JSON encoded field containing the configuration for the given alert. For emails typical settings may be as follows:
     * ```
     * {
     *  "to": ["receipient@company.com"],
     *  "cc": ["receipient@company.com"],
     *  "bcc": ["receipient@company.com"],
     *  "subject": "Bill Alert for the month of {{ month }} {{ year }}",
     *  "sender": ["{{ app.emails.defaultSender }}"],
     *  "format": "html | rich text"
     * }
     * ```
     *
     * In the example above, the defaults will be used unless they are overridden by the same key in the incoming api message.
     *
     * The format must be something that alert renderers should support. i.e. for format `html` there must be an AlertHtmlRenderer for it
     */
    @Column(name = "settings", columnDefinition = "MEDIUMTEXT", nullable = false)
    var settings: String? = null

    /**
     * Since the field `version` can be used for optimistic locking and to identify this entity's version, we use
     * templateHash here instead.
     *
     * `TemplateHash` is the SHA256 of the template body.
     */
    @Column(name = "template_hash", length = 128, nullable = false)
    var templateHash: String? = null

    /**
     * The body of the template. Can
     */
    @Column(name = "body", columnDefinition = "MEDIUMTEXT", nullable = false)
    var body: String? = null

    @Column(name = "app_code")
    var appCode: String? = null

    @Column(name = "version")
    var version: String? = null


    enum class TemplateStatus {
        DRAFT, PUBLISHED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "template_status", length = 24, nullable = false)
    var templateStatus: TemplateStatus =
        TemplateStatus.DRAFT

    @Column(name = "description")
    var description: String? = null

    @Column(name = "has_image")
    var hasImage: Boolean = false

    @Column(name = "created_by", length = 24, nullable = false)
    val createdBy: String? = null

    @Column(name = "date_created", nullable = false)
    val dateCreated: OffsetDateTime? = null

    @Column(name = "updated_by", length = 24)
    val updatedBy: String? = null

    @Column(name = "date_updated", nullable = false)
    val dateUpdated: OffsetDateTime? = null
}