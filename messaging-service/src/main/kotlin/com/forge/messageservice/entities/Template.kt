package com.forge.messageservice.entities

import Auditable
import com.alphamail.plugin.api.MessageType
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "templates")
class Template : Auditable() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_id")
    var id: Long? = null

    @Column(name = "template_name")
    var name: String? = null

    @org.hibernate.annotations.Type(type = "uuid-char")
    @Column(name = "template_UUID")
    var uuid: UUID? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", length = 24, nullable = false)
    var type: MessageType = MessageType.EMAIL

    @Column(name = "app_code")
    var appCode: String? = null
}