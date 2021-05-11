package com.forge.messageservice.entities

import javax.persistence.*

@Entity
@Table(name = "users")
class User {
    /**
     * User's one bank id. Alphanumeric
     */
    @Id
    @Column(name = "username", length = 24, nullable = false)
    var username: String = ""

    @Column(name = "name")
    var name: String? = null

    @OneToMany
    @JoinColumn(name = "username")
    var onboardings : List<Onboarding>? = null

    @OneToMany
    @JoinColumn(name = "username")
    var tenantUsers : List<TenantUser> = emptyList()
}