package com.forge.messageservice.entity

import javax.persistence.*
import javax.validation.constraints.Pattern

@Entity
@Table(name = "template_versions")
class TemplateVersion{

    /**
     * A user defined id of the template. Alphanumeric, dashes and dots only
     */
    @Id
    @Column(name = "template_version_id", length = 56, nullable = false)
    @Pattern(regexp = "[A-Za-z0-9\\-.]")
    var templateVersionId: String? = null

    @Column(name = "template_version_name")
    var templateVersionName: String? = null


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

    @Column(name = "version")
    var version: String? = null

    enum class TemplateStatus {
        DRAFT, PUBLISHED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "template_status", length = 24, nullable = false)
    var templateStatus: TemplateStatus = TemplateStatus.DRAFT

    @Column(name = "teams_webhook_id")
    var teamsWebhookId: Long? = null
}