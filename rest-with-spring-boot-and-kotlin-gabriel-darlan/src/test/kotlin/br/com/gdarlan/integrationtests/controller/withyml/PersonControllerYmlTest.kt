package br.com.gdarlan.integrationtests.controller.withyml

import br.com.gdarlan.integrationtests.TestConfigs
import br.com.gdarlan.integrationtests.controller.withyml.mapper.YMLMapper
import br.com.gdarlan.integrationtests.testcontainer.AbstractIntegrationTest
import br.com.gdarlan.integrationtests.vo.AccountCredentialsVO
import br.com.gdarlan.integrationtests.vo.PersonVO
import br.com.gdarlan.integrationtests.vo.TokenVO
import br.com.gdarlan.integrationtests.vo.wrappers.WrapperBookVO
import br.com.gdarlan.integrationtests.vo.wrappers.WrapperPersonVO
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.EncoderConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerYmlTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: YMLMapper
    private lateinit var person: PersonVO


    @BeforeAll
    fun setupTests() {
        objectMapper = YMLMapper()
        person = PersonVO()
    }

    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(userName = "leandro", password = "admin123")
        val token = RestAssured.given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(user, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java, objectMapper)
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
        val item = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

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

    }

    @Test
    @Order(2)
    fun testUpdate() {

        person.lastName = "Mathew Stallman"

        val item = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .body(person, objectMapper)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

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
    fun testFindById() {

        val item = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .pathParam("id", person.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java, objectMapper)

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
    @Order(4)
    fun testDelete() {

        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .pathParam("id", person.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)


    }


//    @Test
//    @Order(5)
//    fun testFindAll() {
//
//        val wrapper = given()
//            .config(
//                RestAssuredConfig
//                    .config()
//                    .encoderConfig(
//                        EncoderConfig.encoderConfig()
//                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
//                    )
//            )
//            .spec(specification)
//            .contentType(TestConfigs.CONTENT_TYPE_YML)
//            .queryParams(
//                "page", 0,
//                "size", 12,
//                "direction", "asc")
//            .`when`()
//            .get()
//            .then()
//            .statusCode(200)
//            .extract()
//            .body()
//            .`as`(WrapperBookVO::class.java, objectMapper)
//
//        val books = wrapper.embedded!!.books
//
//        val foundBookOne = books?.get(0)
//
//        assertNotNull(foundBookOne!!.id)
//        assertNotNull(foundBookOne.title)
//        assertNotNull(foundBookOne.author)
//        assertNotNull(foundBookOne.price)
//        assertTrue(foundBookOne.id > 0)
//        assertEquals("Big Data: como extrair volume, variedade, velocidade e valor da avalanche de informação cotidiana", foundBookOne.title)
//        assertEquals("Viktor Mayer-Schonberger e Kenneth Kukier", foundBookOne.author)
//        assertEquals(54.00, foundBookOne.price)
//
//
//
//    }

    @Test
    @Order(6)
    fun testFindAllWithoutToken() {

        val specificationWithoutToken: RequestSpecification = RequestSpecBuilder()
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
            .addFilter(RequestLoggingFilter(LogDetail.ALL))
            .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()

        given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specificationWithoutToken)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
            .`when`()
            .get()
            .then()
            .statusCode(403)
            .extract()
            .body()
            .asString()


    }

    @Test
    @Order(7)
    fun testHATEOS() {

        val content = given()
            .config(
                RestAssuredConfig
                    .config()
                    .encoderConfig(
                        EncoderConfig.encoderConfig()
                            .encodeContentTypeAs(TestConfigs.CONTENT_TYPE_YML, ContentType.TEXT)
                    )
            )
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_YML)
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

        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/700"}}}"""))
        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/729"}}}"""))
        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/379"}}}"""))
        assertTrue(content.contains("""_links":{"self":{"href":"http://localhost:8888/api/person/v1/191"}}}"""))

        assertTrue(content.contains("""{"first":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=0&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","self":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=0&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","next":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=1&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","last":{"href":"http://localhost:8888/api/person/v1?direction=asc&page=83&size=12&sort=firstName,asc"}"""))
        assertTrue(content.contains(""","page":{"size":12,"totalElements":1006,"totalPages":84,"number":0}"""))

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