package com.forge.messageservice.entities

import Auditable
import java.time.LocalDateTime
import javax.persistence.*

/**
 * This entity represents a tenant in Alphamail. A tenant is identified by its application code (referred to as appCode).
 * A tenant may have submodules within it.
 */
@Entity
@Table(
    name = "tenants", indexes = [
        Index(name = "IDX_APPCODE_MODULE", columnList = "app_code, module")
    ]
)
class Tenant : Auditable() {

    @Id
    @Column(name = "app_code", length = 24, nullable = false)
    var appCode: String = ""

    @Column(name = "module", length = 24)
    var module: String? = null

    @Column(name = "display_name", length = 56, nullable = false)
    var displayName: String = ""

    @Column(name = "api_token")
    var apiToken: String? = null

    @Column(name = "justification")
    var justification: String? = null

    @Column(name = "description")
    var description: String? = null

    @Column(name = "primary_owner_name", length = 256, nullable = true)
    var primaryOwnerName: String? = null

    @Column(name = "primary_owner_id", length = 24, nullable = true)
    var primaryOwnerId: String? = null

    @Column(name = "secondary_owner_name", length = 256, nullable = true)
    var secondaryOwnerName: String? = null

    @Column(name = "secondary_owner_id", length = 24, nullable = true)
    var secondaryOwnerId: String? = null

    /**
     * A key used to decrypt properties in the appSettings denoted by `[enc]`
     */
    @Column(name = "encryption_key", length = 512, nullable = false)
    var encryptionKey: String? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 24, nullable = false)
    var status: AppStatus =
        AppStatus.PENDING_OWNER_APPROVAL

    enum class AppStatus {
        ACTIVE, INACTIVE, PENDING_OWNER_APPROVAL, REJECTED
    }

    /**
     * A YML text representing the application's global settings e.g.
     *
     * app:
     *  plugin:
     *      saveToOutlook:
     *          gaasProxyUsername: username
     *          gaasProxyPassword: password
     *  email:
     *      defaultSender: myapp@helloworld.com
     *      footerMessage: |
     *          This is an automated message, please do not reply.
     * ...
     *
     * This can then be used to configure plugins and/or value to replace template placeholders as:
     * {{ app.plugin.saveToOutlook.gaasProxyUsername }}
     *
     * Email templates can reference these global variables as:
     *
     * ```
     * <html>
     *     <h1>My First Email Template</h1>
     *
     *     <div>
     *         {{ app.email.footerMessage }}
     *     </div>
     * </html>
     * ```
     *
     */
    @Lob
    @Column(name = "app_settings", nullable = true, columnDefinition = "TEXT")
    val appSettings: String? = null

    @Column(name = "approved_timestamp")
    var approvedDate: LocalDateTime? = null

    @Column(name = "approved_by")
    var approvedBy: String = ""

    @Column(name = "rejected_timestamp")
    var rejectedDate: LocalDateTime? = null

    @Column(name = "rejected_by")
    var rejectedBy: String? = null

    @Column(name = "rejected_reason")
    var rejectedReason: String? = null

    @OneToMany(mappedBy = "appCode", cascade = [CascadeType.ALL])
    var tenantUsers: MutableSet<TenantUser> = mutableSetOf()
}