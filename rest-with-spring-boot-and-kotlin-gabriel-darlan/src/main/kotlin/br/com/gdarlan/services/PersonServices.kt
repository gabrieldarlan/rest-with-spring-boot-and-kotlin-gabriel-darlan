package br.com.gdarlan.services

import br.com.gdarlan.model.Person
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicLong
import java.util.logging.Logger

@Service
class PersonServices {
    private val couter: AtomicLong = AtomicLong()

    private val logger = Logger.getLogger(PersonServices::class.java.name)

    fun findAll(): List<Person> {
        logger.info("Finding all people!")

        val persons: MutableList<Person> = ArrayList()
        for (i in 0..7) {
            val person = mockPerson(i)
            persons.add(person)
        }
        return persons

    }

    fun create(person: Person) = person
    fun update(person: Person) = person
    fun delete(od:Long) {}



    fun findById(id: Long): Person {
        logger.info("Finding one person!")

        return Person(
            id = couter.incrementAndGet(),
            firstName = "Maria",
            lasttName = "Joaquina",
            address = "Brazil",
            gender = "Trangender"
        )
    }


    private fun mockPerson(i: Int): Person {
        return Person(
            id = couter.incrementAndGet(),
            firstName = "Maria $i",
            lasttName = "Joaquina $i",
            address = "Brazil $i",
            gender = "Trangender $i"
        )
    }
}