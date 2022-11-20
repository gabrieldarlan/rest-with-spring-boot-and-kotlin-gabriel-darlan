package br.com.gdarlan.unittests.mocks

import br.com.gdarlan.data.vo.v1.BookVO
import br.com.gdarlan.model.Book
import java.text.DateFormat
import java.util.*

class MockBook {
    fun mockEntity(): Book {
        return mockEntity(0)
    }

    fun mockVO(): BookVO {
        return mockVO(0)
    }

    fun mockEntityList(): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        for (i in 0..13) {
            books.add(mockEntity(i))
        }
        return books
    }

    fun mockVOList(): ArrayList<BookVO> {
        val books: ArrayList<BookVO> = ArrayList()
        for (i in 0..13) {
            books.add(mockVO(i))
        }
        return books
    }

    fun mockEntity(number: Int): Book {
        val book = Book()
        book.id = number.toLong()
        book.author = "Author Test$number"
        val c = Calendar.getInstance()
        val f = DateFormat.getDateInstance()
        val date = f.parse("$number/10/2022")
        book.launchDate = date
        book.price = 0.0 + number
        book.title = "Title Test$number"


        return book
    }

    fun mockVO(number: Int): BookVO {
        val book = BookVO()
        book.key = number.toLong()
        book.author = "Author Test$number"
//        val c = Calendar.getInstance()
//        val f = DateFormat.getDateInstance()
//        val date = f.parse("$number/10/2022")
//        book.launchDate = date
        book.price = 0.0 + number
        book.title = "Title Test$number"

        return book
    }
}