package com.forge.messageservice.repositories

import com.forge.messageservice.entity.Image
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>, ImageRepositoryCustom {
    fun findAllByAppCode(appCode: String): List<Image>

    @Query("SELECT i FROM Image i WHERE i.appCode = :appCode AND i.id > :id")
    fun findAllByAppCodeAfterImageId(appCode: String, id: Long): List<Image>

}