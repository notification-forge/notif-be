package com.forge.messageservice.entities

import Auditable
import java.time.OffsetDateTime
import javax.persistence.*

// The max size of a medium blob https://mariadb.com/kb/en/mediumblob/
const val PLUGIN_MAX_SIZE: Long = 16777215

/**
 * Plugins are jar files that implements `AlphamailPlugin` interface.
 *
 * Plugins are stored in the database a blobs (s3 next phase - due to environment constraints).
 */
@Entity
@Table(name = "plugins")
class Plugin : Auditable(){
    /**
     *  Auto generated sequence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plugin_id")
    var id: Long? = null

    @Column(name = "name", nullable = false)
    var name: String? = null

    @Column(name = "app_code", length = 32)
    var appCode: String? = null

    /**
     * Column that holds the jar file's binary data. Holds a max of 16MB.
     *
     * See https://mariadb.com/kb/en/mediumblob/
     */
    @Lob
    @Column(name = "jar", columnDefinition = "MEDIUMBLOB")
    var jar: ByteArray? = null

    @Column(name = "filename")
    var fileName: String? = null

    @Column(name = "plugin_class_name")
    var pluginClassName: String? = null

    @Column(name = "configuration_class_name")
    var configurationClassName: String? = null

    @Column(name = "configuration_descriptor", nullable = false, columnDefinition = "TEXT")
    var configurationDescriptors: String? = null

    @Column(name = "jar_url", nullable = false)
    var jarUrl: String = ""

}