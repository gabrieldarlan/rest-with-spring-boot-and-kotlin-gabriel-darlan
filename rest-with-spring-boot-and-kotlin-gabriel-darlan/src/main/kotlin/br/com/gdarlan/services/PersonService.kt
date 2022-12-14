package br.com.gdarlan.services

import br.com.gdarlan.controllers.PersonController
import br.com.gdarlan.data.vo.v1.PersonVO
import br.com.gdarlan.exceptions.RequiredObjectIsNullException
import br.com.gdarlan.exceptions.ResourceNotFoundException
import br.com.gdarlan.mapper.DozerMapper
import br.com.gdarlan.mapper.custom.PersonMapper
import br.com.gdarlan.model.Person
import br.com.gdarlan.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.logging.Logger

@Service
class PersonService {
    @Autowired
    private lateinit var repository: PersonRepository

    @Autowired
    private lateinit var mapper: PersonMapper

    @Autowired
    private lateinit var pagedResourcesAssembler: PagedResourcesAssembler<PersonVO>


    private val logger = Logger.getLogger(PersonService::class.java.name)


    fun findAll(pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Finding all people!")
        val persons = repository.findAll(pageable)
        val vos = persons.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
            .map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        return pagedResourcesAssembler.toModel(vos)
    }

    fun findPersonByName(firstName: String, pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Finding a people!")
        val persons = repository.findPersonByName(firstName, pageable)
        val vos = persons.map { p -> DozerMapper.parseObject(p, PersonVO::class.java) }
            .map { p -> p.add(linkTo(PersonController::class.java).slash(p.key).withSelfRel()) }
        return pagedResourcesAssembler.toModel(vos)
    }


    fun findById(id: Long): PersonVO {
        logger.info("Finding one person with id $id!")
        val person =
            repository.findById(id).orElseThrow { ResourceNotFoundException("No records found for this id") }
        val personVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO

    }

    fun create(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()
        logger.info("Creating one person with name ${person.firstName}")
        val entity: Person = DozerMapper.parseObject(person, Person::class.java)
        val personVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO

    }

    fun update(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()
        logger.info("Updating one person with id ${person.key}")
        val entity = repository.findById(person.key)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        entity.let { p ->
            p.firstName = person.firstName
            p.lastName = person.lastName
            p.address = person.address
            p.gender = person.gender
        }
        val personVO = DozerMapper.parseObject(repository.save(entity), PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO
    }

    @Transactional
    fun disabledPerson(id: Long): PersonVO {
        logger.info("Disabling one person with id $id!")
        repository.disabledPerson(id)
        val person =
            repository.findById(id).orElseThrow { ResourceNotFoundException("No records found for this id") }
        val personVO = DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java).slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)
        return personVO

    }

    fun delete(id: Long) {
        logger.info("Deleting one person with id $id")
        val entity = repository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }


}