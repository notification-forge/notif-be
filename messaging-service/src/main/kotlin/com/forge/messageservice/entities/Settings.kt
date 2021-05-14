package com.forge.messageservice.entities

import javax.persistence.Column

abstract class Settings{}

class MailSettings : Settings() {

    @Column(name = "importance")
    val importance: String = ""

    @Column(name = "subject")
    val subject: String = ""

    @Column(name = "sender")
    val sender: String = ""

    @Column(name = "recipients")
    val recipients: String = ""

    @Column(name = "cc_recipients")
    val ccRecipients: String = ""

    @Column(name = "bcc_recipients")
    val bccRecipients: String = ""

    @Column(name = "has_attachments")
    val hasAttachments: Boolean? = null
}

class TeamsSettings : Settings() {

    @Column(name = "teams_webhook_url")
    val teamsWebhookUrl: String? = null
}