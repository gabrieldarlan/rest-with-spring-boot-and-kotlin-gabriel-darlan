package br.com.gdarlan.repository

import br.com.gdarlan.model.Person
import br.com.gdarlan.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PersonRepository : JpaRepository<Person, Long?> {

    @Modifying
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id = :id")
    fun disabledPerson(@Param("id") id: Long?)
}