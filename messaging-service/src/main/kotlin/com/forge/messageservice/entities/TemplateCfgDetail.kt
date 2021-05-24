package com.forge.messageservice.entities

import Auditable
import com.alphamail.plugin.api.MessageType
import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
@Table(name = "template_cfg_details")
class TemplateCfgDetail : Auditable() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Column(name = "template_cfg_name", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    var templateCfgType: MessageType? = null

    @ManyToOne
    @JoinColumn(name = "template_cfg_name", insertable = false, updatable = false)
    var templateCfg: TemplateCfg? = null

    @Column(name = "field_name", nullable = false, length = 55)
    var fieldName: String = ""

    @Column(name = "is_mandatory", length = 1, nullable = false)
    var isMandatory: Boolean = false

    @Column(name = "format", length = 500, nullable = true)
    var format: String? = null
}