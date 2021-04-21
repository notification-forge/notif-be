package com.forge.messageservice.entities


import Auditable
import com.forge.messageservice.common.extensions.toHexString
import java.security.MessageDigest
import javax.persistence.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(
    name = "images", indexes = [
        Index(name = "IDX_APPCODE_FILENAME", columnList = "app_code, file_name")
    ]
)
@NamedQueries(
    NamedQuery(
        name = "Image.findWithSimilarNames",
        query = "select i from Image i where i.appCode = :appCode and (i.fileName = :rawFilename or i.fileName like :filenameWildcard)"
    )
)
class Image : Auditable() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    var id: Long = 0

    @Column(name = "app_code", length = 255, nullable = false)
    @NotEmpty
    @Size(max = 24, message = "image.appCode.maxSize")
    var appCode: String = ""

    @Column(name = "content_type", length = 255, nullable = false)
    @NotEmpty
    @Size(max = 255, message = "image.contentType.maxSize")
    var contentType: String = ""

    @ManyToOne
    @JoinColumn(name = "app_code", insertable = false, updatable = false)
    var tenant: Tenant? = null

    @Column(name = "file_name", length = 255, nullable = false)
    @NotNull
    @NotEmpty
    @Size(max = 255, message = "image.filename.maxSize")
    var fileName: String = ""

    /**
     * An image SHA256
     */
    @Column(name = "file_signature", length = 512, nullable = false)
    @NotNull
    @Size(max = 512, message = "image.fileSignature.maxSize")
    var fileSignature: String = ""
        private set

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @NotNull
    var status: ImageStatus = ImageStatus.ACTIVE

    enum class ImageStatus {
        ACTIVE, DELETED
    }

    @Lob
    @Column(name = "image_data", nullable = false)
    @NotNull
    var imageData: ByteArray? = null

    @PrePersist
    @PreUpdate
    fun fingerprintImage() {
        val md = MessageDigest.getInstance("SHA-256")
        this.fileSignature = md.digest(this.imageData)?.toHexString().toString()
    }

}

