package com.forge.messageservice.entities

import Auditable
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Suppress("unused")
@Entity
@Table(name = "api_clients")
class ApiClient: Auditable() {

    @Id
    @Column(name = "api_client_id", length = 55, nullable = false)
    var id: String = UUID.randomUUID().toString()

    @Column(name = "name", length= 55, nullable = false)
    var name: String = ""

    @Column(name = "access_key", length = 512, nullable = false)
    var accessKey: String = ""
}