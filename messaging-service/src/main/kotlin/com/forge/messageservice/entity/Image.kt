package com.forge.messageservice.entity

import Auditable
import javax.persistence.*

@Entity
@Table(name = "images")
class Image : Auditable() {

    @Id
    @Column(name = "image_id", length = 255, nullable = false)
    var id: String? = null

    @Column(name = "app_code", length = 255, nullable = false)
    var appCode: String? = null

    @ManyToOne
    @JoinColumn(columnDefinition = "app_code", insertable = false, updatable = false)
    var tenant: Tenant? = null

    @Column(name = "file_name", length = 255, nullable = false)
    var fileName: String? = null

    /**
     * A base64 representation of the image
     */
    @Lob
    @Column(name = "image_data", nullable = false)
    var imageData: String? = null
}