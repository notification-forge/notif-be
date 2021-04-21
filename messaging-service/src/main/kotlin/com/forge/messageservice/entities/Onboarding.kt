package com.forge.messageservice.entities

import Auditable
import javax.persistence.*

@Entity
@Table(name = "onboarding")
class Onboarding : Auditable(){
    /**
     * A user defined id of the onboarding. Auto generated sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onboarding_id")
    var id: Long? = null

    @Column(name = "username", nullable = false)
    var username: String? = null

    @Column(name = "app_code", length = 55, nullable = false)
    var appCode: String? = null
}