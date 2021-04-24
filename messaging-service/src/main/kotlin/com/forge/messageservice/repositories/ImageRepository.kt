package com.forge.messageservice.repositories

import com.forge.messageservice.entities.Image
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<Image, Long>, ImageRepositoryCustom {

    @Query("SELECT i FROM Image i WHERE i.fileName LIKE %:name% AND i.appCode in :appCodes")
    fun findWithNamesLike(
        @Param("appCodes") appCodes: List<String>,
        @Param("name") name: String,
        pageable: Pageable
    ): Page<Image>


}