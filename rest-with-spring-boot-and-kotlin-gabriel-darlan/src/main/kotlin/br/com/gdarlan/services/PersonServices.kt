package br.com.gdarlan.services

import br.com.gdarlan.data.vo.v1.PersonVO
import br.com.gdarlan.data.vo.v2.PersonVO as PersonVOV2
import br.com.gdarlan.exceptions.handler.ResourceNotFoundException
import br.com.gdarlan.mapper.DozerMapper
import br.com.gdarlan.mapper.custom.PersonMapper
import br.com.gdarlan.model.Person
import br.com.gdarlan.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonServices {
    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

    private val logger = Logger.getLogger(PersonServices::class.java.name)

    fun findAll(): List<PersonVO> {
        logger.info("Finding all people!")
        val persons = repository.findAll()
        return DozerMapper.parseListObjects(persons, PersonVO::class.java)
    }

    fun findById(id: Long): PersonVO {
        logger.info("Finding one person!")
        val person =
            repository.findById(id).orElseThrow { ResourceNotFoundException("No records found for this id") }
        return DozerMapper.parseObject(person, PersonVO::class.java)

    }

    fun createV2(person: PersonVOV2): PersonVOV2 {
        logger.info("Creating one person with name ${person.firstName}")
        val entity: Person = mapper.mapVOToEntity(person)
        return mapper.mapEntityToVO(repository.save(entity))

    }
    fun create(person: PersonVO): PersonVO {
        logger.info("Creating one person with name ${person.firstName}")
        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)

    }

    fun update(person: PersonVO): PersonVO {
        logger.info("Updating one person with id ${person.id}")
        val entity = repository.findById(person.id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        entity.let { p ->
            p.firstName = person.firstName
            p.lastName = person.lastName
            p.address = person.address
            p.gender = person.gender
        }
        return DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
    }


    fun delete(id: Long) {
        logger.info("Deleting one person with id $id")
        val entity = repository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }




}