package com.forge.messageservice.entities

import com.alphamail.plugin.api.MessageType
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "template_cfgs")
class TemplateCfg : Serializable {

    @Id
    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    var name: MessageType? = null

    @OneToMany(mappedBy = "templateCfg", fetch = FetchType.EAGER)
    var details: List<TemplateCfgDetail> = emptyList()
}