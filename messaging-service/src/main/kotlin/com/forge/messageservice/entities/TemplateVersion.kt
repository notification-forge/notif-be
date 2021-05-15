package com.forge.messageservice.entities

import Auditable
import org.apache.commons.codec.digest.DigestUtils
import javax.persistence.*

@Entity
@Table(name = "template_versions")
class TemplateVersion : Auditable() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_version_id")
    var id: Long? = null

    @Column(name = "template_version_name")
    var name: String? = null

    /**
     * A user defined id of the template. Alphanumeric, dashes and dots only
     * Foreign key to the template
     */
    @Column(name = "template_id")
    var templateId: Long? = null

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
    var settings: String = ""
        set(value) {
            field = value
            isDirty = true
        }

    /**
     * Since the field `version` can be used for optimistic locking and to identify this entity's version, we use
     * templateHash here instead.
     *
     * `TemplateHash` is the SHA256 of the template body.
     */
    @Column(name = "template_hash", nullable = false)
    var templateHash: String? = null
        private set
        get() {
            generateHashWhenHashIsEmpty()
            return field
        }

    /**
     * The body of the template. Can
     */
    @Column(name = "body", columnDefinition = "MEDIUMTEXT", nullable = false)
    var body: String = ""
        set(value) {
            field = value
            isDirty = true
        }

    @Column(name = "version")
    var version: Long? = 0

    enum class TemplateStatus {
        DRAFT, PUBLISHED
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "template_status", length = 24, nullable = false)
    var status: TemplateStatus = TemplateStatus.DRAFT

    @ManyToOne
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    var template: Template? = null

    @Transient
    var isDirty: Boolean = false

    @PrePersist
    @PreUpdate
    fun generateHashWhenHashIsEmpty(){
        if (isDirty) {
            this.templateHash = DigestUtils.sha256Hex(settings + body)
        }
    }
}