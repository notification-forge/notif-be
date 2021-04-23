package com.forge.messageservice.entities

import Auditable
import javax.persistence.*

@Entity
@Table(name = "template_plugins")
class TemplatePlugin : Auditable() {

    /**
     * Auto generated sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "template_plugin_id")
    var id: Long? = null

    @Column(name = "template_version_id", nullable = false)
    var templateVersionId: Long? = null

    @Column(name = "plugin_id", nullable = false)
    var pluginId: Long? = null

    @Column(name = "configuration", nullable = false, columnDefinition = "TEXT")
    var configuration: String? = null
}
