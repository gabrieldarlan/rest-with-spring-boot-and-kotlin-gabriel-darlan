package br.com.gdarlan.services

import br.com.gdarlan.exceptions.handler.ResourceNotFoundException
import br.com.gdarlan.model.Person
import br.com.gdarlan.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonServices {
    @Autowired
    private lateinit var repository: PersonRepository

    private val logger = Logger.getLogger(PersonServices::class.java.name)

    fun findAll(): List<Person> {
        logger.info("Finding all people!")
        return repository.findAll()
    }

    fun findById(id: Long): Person {
        logger.info("Finding one person!")
        return repository.findById(id).orElseThrow { ResourceNotFoundException("No records found for this id") }

    }

    fun create(person: Person): Person {
        logger.info("Creating one person with name ${person.firstName}")
        return repository.save(person)
    }

    fun update(person: Person): Person {
        logger.info("Updating one person with id ${person.id}")
        val entity = repository.findById(person.id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        entity.let { p ->
            p.firstName = person.firstName
            p.lastName = person.lastName
            p.address = person.address
            p.gender = person.gender
        }
        return repository.save(entity)
    }


    fun delete(id: Long) {
        logger.info("Deleting one person with id $id")
        val entity = repository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }


}