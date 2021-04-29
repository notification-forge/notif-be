package com.forge.messageservice.entities

import Auditable
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 * This entity represents the mapping between a tenant and its users. The same user can belong to multiple tenants.
 */
@Entity
@Table(name = "tenant_users")
class TenantUser : Auditable() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tenant_user_id", nullable = false)
    var id: Long = 0

    @Column(name = "app_code", length = 24, nullable = false, unique = true)
    var appCode: String = ""

    @ManyToOne
    @JoinColumn(name = "app_code", insertable = false, updatable = false)
    var tenant: Tenant? = null

    @Column(name = "username", length = 24, nullable = false, unique = true)
    var username: String = ""

    @ManyToOne
    @JoinColumn(name = "username", insertable = false, updatable = false)
    var user: User? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    var status: Status = Status.ACTIVE

    enum class Status {
        ACTIVE, INACTIVE
    }
}