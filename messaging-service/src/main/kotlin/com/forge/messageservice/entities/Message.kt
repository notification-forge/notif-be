package com.forge.messageservice.entities

import Auditable
import com.alphamail.plugin.api.MessageStatus
import com.alphamail.plugin.api.MessageType
import javax.persistence.*

@Entity
@Table(name = "messages")
class Message : Auditable() {

    @Id
    @Column(name = "message_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null

    @Column(name = "template_id")
    var templateId: Long? = null

    @Column(name = "template_version_id")
    var templateVersionId: Long? = null

    @Column(name = "app_code")
    var appCode: String? = null

    @Column(name = "content")
    var content: String = ""

    @Column(name = "message_settings")
    var settings: String = ""

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type")
    var type: MessageType = MessageType.EMAIL

    @Enumerated(EnumType.STRING)
    @Column(name = "message_status")
    var status: MessageStatus = MessageStatus.PENDING

    @Column(name = "reason", length = 2000)
    var reason: String? = null

    @Column(name = "times_triggered")
    var timesTriggered: Int = 0

    @ManyToOne
    @JoinColumn(name = "template_id", insertable = false, updatable = false)
    var template: Template? = null

    @ManyToOne
    @JoinColumn(name = "template_version_id", insertable = false, updatable = false)
    var templateVersion: TemplateVersion? = null
}