package com.forge.messageservice.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "templates")
class Template {

    /**
     * A user defined id of the template. Alphanumeric, dashes and dots only
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    var templateId: Long? = null

    @Column(name = "template_name")
    var templateName: String? = null

    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = "template_UUID")
    var templateUUID: UUID? = null

    enum class AlertType {
        TEAMS, EMAIL
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 24, nullable = false)
    var alertType: AlertType = AlertType.EMAIL

    @Column(name = "app_code")
    var appCode: String? = null

}