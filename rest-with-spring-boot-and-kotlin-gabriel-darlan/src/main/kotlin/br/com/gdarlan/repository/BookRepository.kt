package br.com.gdarlan.repository

import br.com.gdarlan.model.Book
import br.com.gdarlan.model.Person
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<Book, Long?> {
}