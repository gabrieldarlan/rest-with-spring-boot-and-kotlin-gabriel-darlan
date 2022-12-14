package br.com.gdarlan.integrationtests.controller.withjson

import br.com.gdarlan.integrationtests.TestConfigs
import br.com.gdarlan.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.gdarlan.integrationtests.vo.AccountCredentialsVO
import br.com.gdarlan.integrationtests.vo.PersonVO
import br.com.gdarlan.integrationtests.vo.TokenVO
import br.com.gdarlan.integrationtests.vo.wrappers.WrapperPersonVO
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerJsonTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO


    @BeforeAll
    fun setupTests() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(userName = "leandro", password = "admin123")
        val token = RestAssured.given()
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
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
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

    }

    @Test
    @Order(1)
    fun testCreate() {
        mockPerson()
        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertTrue(item.id > 0)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)

        assertEquals("Richard", item.firstName)
        assertEquals("Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(true, item.enabled)

    }

    @Test
    @Order(2)
    fun testUpdate() {

        person.lastName = "Mathew Stallman"

        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals(person.id, item.id)

        assertEquals("Richard", item.firstName)
        assertEquals("Mathew Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)

    }

    @Test
    @Order(3)
    fun testDisablePersonById() {

        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()
            .patch("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals(person.id, item.id)

        assertEquals("Richard", item.firstName)
        assertEquals("Mathew Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(false, item.enabled)

    }

    @Test
    @Order(4)
    fun testFindById() {

        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val item = objectMapper.readValue(content, PersonVO::class.java)
        person = item

        assertNotNull(item.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals(person.id, item.id)

        assertEquals("Richard", item.firstName)
        assertEquals("Mathew Stallman", item.lastName)
        assertEquals("New York City, New York, US", item.address)
        assertEquals("Male", item.gender)
        assertEquals(false, item.enabled)

    }

    @Test
    @Order(5)
    fun testDelete() {

        given()
            .spec(specification)
            .pathParam("id", person.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)


    }


    @Test
    @Order(6)
    fun testFindAll() {

        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .queryParam("page", 3, "size", 12, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.persons

        val item = people?.get(0)

        assertNotNull(item!!.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals("Allin", item.firstName)
        assertEquals("Emmot", item.lastName)
        assertEquals("7913 Lindbergh Way", item.address)
        assertEquals("Male", item.gender)
        assertEquals(false, item.enabled)
        val item2 = people[3]

        assertNotNull(item2.id)
        assertNotNull(item2.firstName)
        assertNotNull(item2.lastName)
        assertNotNull(item2.address)
        assertNotNull(item2.gender)
        assertEquals("Almeria", item2.firstName)
        assertEquals("Curm", item2.lastName)
        assertEquals("34 Burrows Point", item2.address)
        assertEquals("Female", item2.gender)
        assertEquals(false, item2.enabled)

    }

    @Test
    @Order(6)
    fun testFindByName() {

        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("firstName", "ayr")
            .queryParam("page", 0, "size", 12, "direction", "asc")
            .`when`()["findPersonByName/{firstName}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val wrapper = objectMapper.readValue(content, WrapperPersonVO::class.java)
        val people = wrapper.embedded!!.persons

        val item = people?.get(0)

        assertNotNull(item!!.id)
        assertNotNull(item.firstName)
        assertNotNull(item.lastName)
        assertNotNull(item.address)
        assertNotNull(item.gender)
        assertEquals("Ayrton", item.firstName)
        assertEquals("Senna", item.lastName)
        assertEquals("S??o Paulo", item.address)
        assertEquals("Male", item.gender)
    }

    @Test
    @Order(8)
    fun testFindAllWithoutToken() {

        val specificationWithoutToken: RequestSpecification = RequestSpecBuilder()
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        given()
            .spec(specificationWithoutToken)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .`when`()
            .get()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()


    }

    @Test
    @Order(9)
    fun testHATEOS() {

        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .queryParam("page", 3, "size", 12, "direction", "asc")
            .`when`()
            .get()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/796"}}}"""))
        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/198"}}}"""))
        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/685"}}}"""))
        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/187"}}}"""))

        assertTrue(content.contains("""{"first":{"href":"http://localhost:8888/api/person/v1?page=0&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","prev":{"href":"http://localhost:8888/api/person/v1?page=2&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","self":{"href":"http://localhost:8888/api/person/v1?page=3&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","next":{"href":"http://localhost:8888/api/person/v1?page=4&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","last":{"href":"http://localhost:8888/api/person/v1?page=83&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","page":{"size":12,"totalElements":1006,"totalPages":84,"number":3}"""))

    }

    private fun mockPerson() {
        with(person) {
            firstName = "Richard"
            lastName = "Stallman"
            address = "New York City, New York, US"
            gender = "Male"
        }
    }

}