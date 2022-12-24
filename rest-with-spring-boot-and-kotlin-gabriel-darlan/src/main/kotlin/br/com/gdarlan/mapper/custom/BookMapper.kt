package br.com.gdarlan.mapper.custom

import br.com.gdarlan.data.vo.v1.BookVO
import br.com.gdarlan.model.Book
import org.springframework.stereotype.Service

@Service
class BookMapper {

    fun mapEntityToVO(book: Book): BookVO {
        return BookVO(
            key = book.id,
            author = book.author,
            launchDate = book.launchDate,
            price = book.price,
            title = book.title
        )
    }

    fun mapVOToEntity(bookVO: BookVO): Book {
        return Book(
            id = bookVO.key,
            author = bookVO.author,
            launchDate = bookVO.launchDate,
            price = bookVO.price,
            title = bookVO.title
        )
    }


}