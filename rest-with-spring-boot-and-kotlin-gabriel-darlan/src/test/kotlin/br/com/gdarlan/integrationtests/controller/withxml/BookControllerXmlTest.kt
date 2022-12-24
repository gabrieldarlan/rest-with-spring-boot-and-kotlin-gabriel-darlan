package br.com.gdarlan.integrationtests.controller.withxml


import br.com.gdarlan.integrationtests.TestConfigs
import br.com.gdarlan.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.gdarlan.integrationtests.vo.AccountCredentialsVO
import br.com.gdarlan.integrationtests.vo.BookVO
import br.com.gdarlan.integrationtests.vo.TokenVO
import br.com.gdarlan.integrationtests.vo.wrappers.WrapperBookVO
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookControllerXmlTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var book: BookVO

    @BeforeAll
    fun setup() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        book = BookVO()
    }

    @Test
    @Order(1)
    fun authorization() {
        val user = AccountCredentialsVO()
        user.userName = "leandro"
        user.password = "admin123"
        val token = given()
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)
            .accessToken
        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/book/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(2)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testCreate() {
        mockBook()
        val content: String = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .body(book)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        book = objectMapper.readValue(content, BookVO::class.java)
        assertNotNull(book!!.id)
        assertNotNull(book.title)
        assertNotNull(book.author)
        assertNotNull(book.price)
        assertTrue(book.id > 0)
        assertEquals("Docker Deep Dive", book.title)
        assertEquals("Nigel Poulton", book.author)
        assertEquals(55.99, book.price)
    }

    @Test
    @Order(3)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testUpdate() {

        book.title = "Docker Deep Dive"

        val content: String = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .body(book)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val bookUpdated: BookVO = objectMapper.readValue(content, BookVO::class.java)
        assertNotNull(bookUpdated.id)
        assertNotNull(bookUpdated.title)
        assertNotNull(bookUpdated.author)
        assertNotNull(bookUpdated.price)
        assertEquals(bookUpdated.id, book.id)
        assertEquals("Docker Deep Dive", bookUpdated.title)
        assertEquals("Nigel Poulton", bookUpdated.author)
        assertEquals(55.99, bookUpdated.price)
    }

    @Test
    @Order(4)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindById() {
        val content: String = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .pathParam("id", book.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()
        val foundBook: BookVO = objectMapper.readValue(content, BookVO::class.java)
        assertNotNull(foundBook.id)
        assertNotNull(foundBook.title)
        assertNotNull(foundBook.author)
        assertNotNull(foundBook.price)
        assertEquals(foundBook.id, book.id)
        assertEquals("Docker Deep Dive", foundBook.title)
        assertEquals("Nigel Poulton", foundBook.author)
        assertEquals(55.99, foundBook.price)
    }

    @Test
    @Order(5)
    fun testDelete() {
        given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .pathParam("id", book.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)
    }

    @Test
    @Order(6)
    @Throws(JsonMappingException::class, JsonProcessingException::class)
    fun testFindAll() {
        val strContent = given().spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_XML)
            .queryParams(
                "page", 0,
                "size", 12,
                "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(strContent, WrapperBookVO::class.java)
        val content = wrapper.embedded!!.books

        val foundBookOne = content?.get(0)

        assertNotNull(foundBookOne!!.id)
        assertNotNull(foundBookOne.title)
        assertNotNull(foundBookOne.author)
        assertNotNull(foundBookOne.price)
        assertTrue(foundBookOne.id > 0)
        assertEquals("Implantando a governança de TI", foundBookOne.title)
        assertEquals("Aguinaldo Aragon Fernandes e Vladimir Ferraz de Abreu", foundBookOne.author)
        assertEquals(54.00, foundBookOne.price)


    }

    private fun mockBook() {
        book.title = "Docker Deep Dive"
        book.author = "Nigel Poulton"
        book.price = 55.99.toDouble()
        book.launchDate = Date()
    }
}