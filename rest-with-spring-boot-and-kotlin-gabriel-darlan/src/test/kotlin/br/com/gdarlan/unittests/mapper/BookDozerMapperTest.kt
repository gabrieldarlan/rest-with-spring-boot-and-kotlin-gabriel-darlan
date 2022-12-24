package br.com.gdarlan.unittests.mapper

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import br.com.gdarlan.data.vo.v1.BookVO
import br.com.gdarlan.mapper.DozerMapper
import br.com.gdarlan.model.Book
import br.com.gdarlan.unittests.mocks.MockBook

class BookDozerMapperTest {

    var inputObject: MockBook? = null

    @BeforeEach
    fun setUp() {
        inputObject = MockBook()
    }

    @Test
    fun parseEntityToVOTest() {
        val output: BookVO = DozerMapper.parseObject(inputObject!!.mockEntity(), BookVO::class.java)
        assertEquals(0, output.key)
        assertEquals("Author Test0", output.author)
//        assertEquals("0/10/2022", output.launchDate)
        assertEquals(0.0, output.price)
        assertEquals("Title Test0", output.title)


    }

    @Test
    fun parseEntityListToVOListTest() {
        val outputList: ArrayList<BookVO> =
            DozerMapper.parseListObjects(inputObject!!.mockEntityList(), BookVO::class.java)

        val outputZero: BookVO = outputList[0]

        assertEquals(0, outputZero.key)
        assertEquals("Author Test0", outputZero.author)
        assertEquals(0.0, outputZero.price)
        assertEquals("Title Test0", outputZero.title)

        val outputSeven: BookVO = outputList[7]
        assertEquals(7.toLong(), outputSeven.key)
        assertEquals("Author Test7", outputSeven.author)
        assertEquals(7.0, outputSeven.price)
        assertEquals("Title Test7", outputSeven.title)
    }

    @Test
    fun parseVOToEntityTest() {

        val output: Book = DozerMapper.parseObject(inputObject!!.mockVO(), Book::class.java)

        assertEquals(0, output.id)

        assertEquals("Author Test0", output.author)
        assertEquals(0.0, output.price)
        assertEquals("Title Test0", output.title)
//        assertEquals("0/10/2022", output.launchDate)
    }

    @Test
    fun parserVOListToEntityListTest() {

        val outputList: ArrayList<Book> =
            DozerMapper.parseListObjects(inputObject!!.mockVOList(), Book::class.java)

        val outputZero: Book = outputList[0]
        assertEquals(0, outputZero.id)

        assertEquals("Author Test0", outputZero.author)
        assertEquals(0.0, outputZero.price)
        assertEquals("Title Test0", outputZero.title)
//        assertEquals("0/10/2022", output.launchDate)

        val outputSeven: Book = outputList[7]
        assertEquals(7, outputSeven.id)

        assertEquals("Author Test7", outputSeven.author)
        assertEquals(7.0, outputSeven.price)
        assertEquals("Title Test7", outputSeven.title)
//        assertEquals("0/10/2022", output.launchDate)

        val outputTwelve: Book = outputList[12]
        assertEquals(12, outputTwelve.id)

        assertEquals("Author Test12", outputTwelve.author)
        assertEquals(12.0, outputTwelve.price)
        assertEquals("Title Test12", outputTwelve.title)
//        assertEquals("0/10/2022", output.launchDate)

    }
}