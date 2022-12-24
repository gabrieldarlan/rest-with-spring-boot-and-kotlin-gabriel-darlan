package br.com.gdarlan.integrationtests.controller.cors.withjson

import br.com.gdarlan.integrationtests.TestConfigs
import br.com.gdarlan.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.gdarlan.integrationtests.vo.AccountCredentialsVO
import br.com.gdarlan.integrationtests.vo.PersonVO
import br.com.gdarlan.integrationtests.vo.TokenVO
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.shaded.com.fasterxml.jackson.databind.DeserializationFeature
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerCorsWithJson() : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var person: PersonVO
    private lateinit var token: String

    @BeforeAll
    fun setupTests() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        person = PersonVO()
        token = ""
    }

    @Test
    @Order(1)
    fun authorization() {
        val user = AccountCredentialsVO(userName = "leandro", password = "admin123")
        token = given()
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
            .accessToken!!

    }


    @Test
    @Order(1)
    fun testeCreate() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


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

        val createPerson = objectMapper.readValue(
            content,
            PersonVO::class.java
        )
        person = createPerson
        assertNotNull(createPerson.id)
        assertNotNull(createPerson.firstName)
        assertNotNull(createPerson.lastName)
        assertNotNull(createPerson.address)
        assertNotNull(createPerson.gender)
        assertTrue(createPerson.id > 0)
        assertEquals("Nelson", createPerson.firstName)
        assertEquals("Piquet", createPerson.lastName)
        assertEquals("Brasília, DF, Brasil", createPerson.address)
        assertEquals("Male", createPerson.gender)
    }


    @Test
    @Order(2)
    fun testeCreateWithWrongOrigin() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(person)
            .`when`()
            .post()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()


        assertEquals("Invalid CORS request", content)
    }


    @Test
    @Order(3)
    fun testeFindByID() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_LOCALHOST)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()["{id}"]
            .then()
            .statusCode(200)
            .extract()
            .body()
            .asString()

        val createPerson = objectMapper.readValue(
            content,
            PersonVO::class.java
        )
        assertNotNull(createPerson.id)
        assertNotNull(createPerson.firstName)
        assertNotNull(createPerson.lastName)
        assertNotNull(createPerson.address)
        assertNotNull(createPerson.gender)

        assertTrue(createPerson.id > 0)

        assertEquals("Nelson", createPerson.firstName)
        assertEquals("Piquet", createPerson.lastName)
        assertEquals("Brasília, DF, Brasil", createPerson.address)
        assertEquals("Male", createPerson.gender)
    }

    @Test
    @Order(4)
    fun testeFindByIDWithWrongOrigin() {
        mockPerson()

        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()


        val content = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", person.id)
            .`when`()["{id}"]
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()


        assertEquals("Invalid CORS request", content)
    }

    private fun mockPerson() {
        person.firstName = "Nelson"
        person.lastName = "Piquet"
        person.address = "Brasília, DF, Brasil"
        person.gender = "Male"
    }

}