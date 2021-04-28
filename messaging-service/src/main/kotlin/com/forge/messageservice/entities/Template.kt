package com.forge.messageservice.entities

import Auditable
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "templates")
class Template : Auditable() {

    /**
     * Auto generated sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    var id: Long? = null

    @Column(name = "template_name")
    var name: String? = null

    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = "template_UUID")
    var uuid: UUID? = null

    enum class AlertType {
        TEAMS, EMAIL
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 24, nullable = false)
    var alertType: AlertType = AlertType.EMAIL

    @Column(name = "app_code")
    var appCode: String? = null
}