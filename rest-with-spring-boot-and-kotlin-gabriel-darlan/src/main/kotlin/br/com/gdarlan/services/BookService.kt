package br.com.gdarlan.services


import br.com.gdarlan.controllers.BookController
import br.com.gdarlan.data.vo.v1.BookVO
import br.com.gdarlan.exceptions.RequiredObjectIsNullException
import br.com.gdarlan.exceptions.ResourceNotFoundException
import br.com.gdarlan.mapper.DozerMapper
import br.com.gdarlan.model.Book
import br.com.gdarlan.repository.BookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class BookService {
    @Autowired
    private lateinit var repository: BookRepository

    @Autowired
    private lateinit var pagedResourcesAssembler: PagedResourcesAssembler<BookVO>
//    @Autowired
//    private lateinit var mapper: BookMapper usado para mapeamento estilizado

    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<BookVO>> {
        logger.info("Finding all books!")
        val books = repository.findAll(pageable)
        val vos = books.map { b -> DozerMapper.parseObject(b, BookVO::class.java) }
            .map { b -> b.add(linkTo(BookController::class.java).slash(b.key).withSelfRel()) }
        return pagedResourcesAssembler.toModel(vos)
    }

    fun findById(id: Long): BookVO {
        logger.info("Finding one book with id $id!")
        val book =
            repository.findById(id).orElseThrow { ResourceNotFoundException("No records found for this id") }
        val bookVO = DozerMapper.parseObject(book, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO

    }

//    fun createV2(person: PersonVOV2): PersonVOV2 {
//        logger.info("Creating one person with name ${person.firstName}")
//        val entity: Person = mapper.mapVOToEntity(person)
//        return mapper.mapEntityToVO(repository.save(entity))
//
//    }

    fun create(book: BookVO?): BookVO {
        if (book == null) throw RequiredObjectIsNullException()
        logger.info("Creating one book with title ${book.title}")
        val entity: Book = DozerMapper.parseObject(book, Book::class.java)
        val bookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO

    }

    fun update(book: BookVO?): BookVO {
        if (book == null) throw RequiredObjectIsNullException()
        logger.info("Updating one book with id ${book.key}")
        val entity = repository.findById(book.key)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        entity.let { b ->
            b.author = entity.author
            b.launchDate = entity.launchDate
            b.price = entity.price
            b.title = entity.title
        }
        val bookVO = DozerMapper.parseObject(repository.save(entity), BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java).slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)
        return bookVO
    }


    fun delete(id: Long) {
        logger.info("Deleting one book with id $id")
        val entity = repository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("No records found for this id") }
        repository.delete(entity)
    }


}